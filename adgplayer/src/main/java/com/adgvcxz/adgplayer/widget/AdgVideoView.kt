package com.adgvcxz.adgplayer.widget

import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.graphics.SurfaceTexture
import android.os.Build
import android.util.AttributeSet
import android.util.Log
import android.view.Surface
import android.view.TextureView
import android.widget.RelativeLayout
import com.adgvcxz.adgplayer.AdgMediaPlayerManager
import com.adgvcxz.adgplayer.PlayerStatus
import com.adgvcxz.adgplayer.ScreenOrientation
import com.adgvcxz.adgplayer.bean.VideoProgress
import com.adgvcxz.adgplayer.extensions.*
import com.adgvcxz.adgplayer.util.OrientationHelper
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import tv.danmaku.ijk.media.player.IMediaPlayer
import java.util.concurrent.TimeUnit

/**
 * zhaowei
 * Created by zhaowei on 2017/3/11.
 */

class AdgVideoView : RelativeLayout, TextureView.SurfaceTextureListener {

    private var textureView: AdgTextureView? = null

    private var currentPosition: Long = 0

    var duration: Long = 0
        get() = AdgMediaPlayerManager.instance.duration
        private set

    var volume: Float = 1.0f
        set (value) {
            if (value in 0..1) {
                field = value
                AdgMediaPlayerManager.instance.setVolume(value, value)
            }
        }

    var brightness: Float
        set(value) {
            if (context is Activity) {
                val lp = (context as Activity).window.attributes
                lp.screenBrightness = value
                if (lp.screenBrightness > 1) {
                    lp.screenBrightness = 1F
                } else if (lp.screenBrightness < 0.01) {
                    lp.screenBrightness = 0.01F
                }
                (context as Activity).window.attributes = lp
            }
        }
        get() {
            var value = 0.5F
            if (context is Activity) {
                value = (context as Activity).window.attributes.buttonBrightness
            }
            if (value < 0) {
                return 0.5F
            }
            return value
        }

    var status = PlayerStatus.Init
        private set(value) {
            field = value
            statusObservable.onNext(value)
            if (value == PlayerStatus.Playing) {
                progressObservable.subscribe { progressListener?.invoke(it) }
            }
        }

    val statusObservable = status.rx()


    var progressListener: ((VideoProgress) -> Unit)? = null
    var bufferListener: ((Int) -> Unit)? = null
    var orientationHelper: OrientationHelper? = null
    var screenOrientation: ScreenOrientation
        get() {
            if (orientationHelper != null) {
                return orientationHelper!!.screenOrientation
            }
            return (context as Activity).screenOrientation()
        }
        set(value) {
            if (orientationHelper == null) {
                orientationHelper = OrientationHelper(context as Activity)
            }
            orientationHelper?.rotateScreen(value)
        }

    private val progressObservable: Observable<VideoProgress> by lazy {
        Observable.interval(350, TimeUnit.MILLISECONDS)
                .takeWhile { status == PlayerStatus.Playing }
                .map { VideoProgress(AdgMediaPlayerManager.instance.currentPosition, duration) }
                .filter { currentPosition != it.progress }
                .takeWhile { it.duration > 0 && it.progress < it.duration }
                .doOnNext { currentPosition = it.progress }
                .observeOn(AndroidSchedulers.mainThread())
    }

    init {

        AdgMediaPlayerManager.instance.initPlayer()

        AdgMediaPlayerManager.instance.videoSizeChangeRx()
                .takeWhile { status != PlayerStatus.Destroy }
                .subscribe { textureView?.requestLayout() }

        AdgMediaPlayerManager.instance.videoPreparedRx()
                .takeWhile { status != PlayerStatus.Destroy }
                .subscribe {
                    status = PlayerStatus.Prepared
                    status = PlayerStatus.Playing
                }

        AdgMediaPlayerManager.instance.videoBufferRx()
                .takeWhile { status != PlayerStatus.Destroy }
                .subscribe {
                    Log.e("zhaow", "buffer   ${it}")
                    bufferListener?.invoke(it)
                }

        AdgMediaPlayerManager.instance.videoInfoRx()
                .takeWhile { status != PlayerStatus.Destroy }
                .subscribe {
                    Log.e("zhaow", "info    ${it.what}")
                    when (it.what) {
                        IMediaPlayer.MEDIA_INFO_BUFFERING_START -> status = PlayerStatus.Buffering
                        IMediaPlayer.MEDIA_INFO_BUFFERING_END -> status = PlayerStatus.Playing
                    }
                }

    }

    constructor(context: Context) : super(context) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init(context)
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init(context)
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        init(context)
    }

    private fun init(context: Context) {
        textureView = AdgTextureView(context)
        val lp = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        lp.addRule(RelativeLayout.CENTER_IN_PARENT)
        addView(textureView, lp)
        textureView?.surfaceTextureListener = this
        status = PlayerStatus.Init
    }

    fun start(url: String) {
        status = PlayerStatus.Preparing
        AdgMediaPlayerManager.instance.prepare(context, url)
    }


    override fun onSurfaceTextureSizeChanged(surface: SurfaceTexture?, width: Int, height: Int) {
    }

    override fun onSurfaceTextureUpdated(surface: SurfaceTexture?) {
    }

    override fun onSurfaceTextureDestroyed(surface: SurfaceTexture?): Boolean {
        AdgMediaPlayerManager.instance.setSurface(null)
        surface?.release()
        return true
    }

    @Suppress("NAME_SHADOWING")
    override fun onSurfaceTextureAvailable(surface: SurfaceTexture?, width: Int, height: Int) {
        val `surface` = Surface(surface)
        AdgMediaPlayerManager.instance.setSurface(surface)
        surface.release()
    }

    fun pause() {
        AdgMediaPlayerManager.instance.pause()
        status = PlayerStatus.Pause
    }

    fun start() {
        AdgMediaPlayerManager.instance.start()
        status = PlayerStatus.Playing
    }

    fun isPlaying(): Boolean = AdgMediaPlayerManager.instance.isPlaying

    fun seekTo(progress: Int) {
        //todo 会自动回退1~3秒 据说是因为缺少I帧导致的 估计是的
        AdgMediaPlayerManager.instance.seekTo(AdgMediaPlayerManager.instance.duration * progress / 100)
    }


    fun onDestroy() {
        status = PlayerStatus.Release
        AdgMediaPlayerManager.instance.release()
        status = PlayerStatus.Destroy
        statusObservable.onComplete()
        orientationHelper?.onDestroy()
    }
}
