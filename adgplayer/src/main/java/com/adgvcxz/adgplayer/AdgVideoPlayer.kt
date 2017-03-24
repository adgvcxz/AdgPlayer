package com.adgvcxz.adgplayer

import android.app.Activity
import android.graphics.SurfaceTexture
import android.media.AudioManager
import android.net.Uri
import android.os.Handler
import android.view.Surface
import android.view.TextureView
import com.adgvcxz.adgplayer.bean.VideoProgress
import com.adgvcxz.adgplayer.bean.VideoSize
import tv.danmaku.ijk.media.player.IMediaPlayer
import tv.danmaku.ijk.media.player.IjkMediaPlayer
import java.lang.ref.WeakReference
import java.util.*

/**
 * zhaowei
 * Created by zhaowei on 2017/3/10.
 */
class AdgVideoPlayer private constructor() : TextureView.SurfaceTextureListener {

    private object Holder {
        val Instance = AdgVideoPlayer()
    }

    companion object {
        val instance: AdgVideoPlayer by lazy {
            Holder.Instance
        }
    }

    internal lateinit var mainPlayer: IMediaPlayer
    private var adgVideoView: IAdgVideoView? = null
    private var activity: WeakReference<Activity>? = null
    internal var listeners: WeakReference<ArrayList<AdgVideoPlayerListener>> = WeakReference(ArrayList())
    private var progressTimer: Timer? = null
    private val handler: Handler = Handler()
    internal var current: Long = -1
    internal var isStart = false


    var status = PlayerStatus.Init
        internal set(value) {
            field = value
            listeners.get()?.map { it.onStatusChanged(value) }
            if (value == PlayerStatus.Playing) {
                startProgressTimer()
            } else if (value == PlayerStatus.Pause || value == PlayerStatus.Completed || value == PlayerStatus.Error
                    || value == PlayerStatus.Release || value == PlayerStatus.Destroy) {
                stopProgressTimer()
            }
        }

    var duration: Long = 0
        get() = mainPlayer.duration
        private set

    var volume: Float = 1.0f
        set (value) {
            if (value in 0..1) {
                field = value
                mainPlayer.setVolume(value, value)
            }
        }


    var brightness: Float
        set(value) {
            val lp = activity?.get()?.window?.attributes
            if (lp != null) {
                lp.screenBrightness = value
                if (lp.screenBrightness > 1) {
                    lp.screenBrightness = 1F
                } else if (lp.screenBrightness < 0.01) {
                    lp.screenBrightness = 0.01F
                }
                activity?.get()?.window?.attributes = lp
            }
        }
        get() {
            val value = activity?.get()?.window?.attributes?.buttonBrightness ?: 0.5F
            if (value < 0) {
                return 0.5F
            }
            return value
        }


    fun prepare(url: String) {
        if (activity?.get() != null) {
            if (isStart) {
                mainPlayer.release()
                initPlayer()
            }
            isStart = true
            status = PlayerStatus.Preparing
            setSurface(this.adgVideoView?.getTextureView()?.surfaceTexture)
            mainPlayer.setDataSource(activity!!.get(), Uri.parse(url))
            mainPlayer.prepareAsync()
            mainPlayer.start()
        }
    }

    fun initPlayer() {
        stopProgressTimer()
        mainPlayer = IjkMediaPlayer()
        initOptions(mainPlayer)
        initListener()
    }

    private fun initOptions(player: IMediaPlayer) {
        player.setAudioStreamType(AudioManager.STREAM_MUSIC)
        //播放时，屏幕保持常亮
        player.setScreenOnWhilePlaying(true)

        if (player is IjkMediaPlayer) {
            //启用硬编码
            player.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec", 1)
            player.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec-auto-rotate", 1)
            player.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec-handle-resolution-change", 1)
        }
    }

    private fun initListener() {
        mainPlayer.setOnPreparedListener {
            listeners.get()?.map { it.onVideoPrepared() }
            status = PlayerStatus.Playing
            if (current > 0) {
                mainPlayer.seekTo(current)
            }
        }

        mainPlayer.setOnInfoListener { _, what, _ ->
            when (what) {
                IMediaPlayer.MEDIA_INFO_BUFFERING_START -> status = PlayerStatus.Buffering
                IMediaPlayer.MEDIA_INFO_BUFFERING_END -> status = PlayerStatus.Playing
            }
            false
        }

        mainPlayer.setOnVideoSizeChangedListener { _, width, height, sarNum, sarDen ->
            listeners.get()?.map { it.onVideoSizeChanged(VideoSize(width, height, sarNum, sarDen)) }
        }

        mainPlayer.setOnBufferingUpdateListener { _, buffer ->
            if (buffer >= 94) {
                listeners.get()?.map { it.onVideoBufferChanged(100) }
            } else {
                listeners.get()?.map { it.onVideoBufferChanged(buffer) }
            }
        }

        mainPlayer.setOnCompletionListener { status = PlayerStatus.Completed }
    }

    fun init(activity: Activity) {
        this.activity = WeakReference(activity)
        status = PlayerStatus.Init
        initPlayer()
    }

    fun bindView(adgVideoView: IAdgVideoView) {
        this.adgVideoView = adgVideoView
        this.adgVideoView?.getTextureView()?.surfaceTextureListener = this
    }

    fun unBindView() {
        this.activity = null
        this.adgVideoView = null
    }

    fun changeQuality(url: String) {
        if (status == PlayerStatus.Pause || status == PlayerStatus.Playing || status == PlayerStatus.Buffering
                || status == PlayerStatus.Preparing) {
            mainPlayer.release()
            initPlayer()
//            this.adgVideoView?.generateTextureView()?.surfaceTextureListener = this
            setSurface(this.adgVideoView?.getTextureView()?.surfaceTexture)
            mainPlayer.setDataSource(activity!!.get(), Uri.parse(url))
            mainPlayer.prepareAsync()
            mainPlayer.start()
        }
    }

    fun startProgressTimer() {
        if (progressTimer != null) {
            stopProgressTimer()
        }
        progressTimer = Timer()
        progressTimer?.schedule(ProgressTimerTask(), 0, 350)

    }

    fun stopProgressTimer() {
        progressTimer?.cancel()
        progressTimer = null
    }

    override fun onSurfaceTextureSizeChanged(surface: SurfaceTexture?, width: Int, height: Int) {
    }

    override fun onSurfaceTextureUpdated(surface: SurfaceTexture?) {
    }

    override fun onSurfaceTextureDestroyed(surface: SurfaceTexture?): Boolean {
        mainPlayer.setSurface(null)
        surface?.release()
        return true
    }

    override fun onSurfaceTextureAvailable(surfaceTexture: SurfaceTexture?, width: Int, height: Int) {
        setSurface(surfaceTexture)
    }

    private fun setSurface(surfaceTexture: SurfaceTexture?) {
        if (surfaceTexture != null) {
            val surface = Surface(surfaceTexture)
            mainPlayer.setSurface(surface)
            surface.release()
        }
    }

    inner class ProgressTimerTask : TimerTask() {
        override fun run() {
            current = mainPlayer.currentPosition
            handler.post { listeners.get()?.map { it.onProgressChanged(VideoProgress(current, duration)) } }
        }
    }
}