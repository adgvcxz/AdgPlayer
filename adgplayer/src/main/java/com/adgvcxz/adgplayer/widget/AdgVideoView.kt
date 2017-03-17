package com.adgvcxz.adgplayer.widget

import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.graphics.SurfaceTexture
import android.os.Build
import android.util.AttributeSet
import android.view.Surface
import android.view.TextureView
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.RelativeLayout
import com.adgvcxz.adgplayer.AdgMediaPlayer
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
    private lateinit var originParent: ViewGroup
    private lateinit var originLayoutParams: ViewGroup.LayoutParams

    var duration: Long = 0
        get() = AdgMediaPlayer.instance.mediaPlayer.duration
        private set

    var volume: Float = 1.0f
        set (value) {
            if (value in 0..1) {
                field = value
                AdgMediaPlayer.instance.mediaPlayer.setVolume(value, value)
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
    var orientationEnable: Boolean = false
        set(value) {
            if (field != value) {
                field = value
                if (value) {
                    orientationHelper.enable()
                } else {
                    orientationHelper.disable()
                }
            }
        }
    var screenOrientation: ScreenOrientation
        get() {
            return orientationHelper.screenOrientation
        }
        set(value) {
            orientationHelper.rotateScreen(value)
            if (value != ScreenOrientation.Portrait) {
                fullScreen()
            } else {
                quitFullScreen()
            }
        }

    private val progressObservable: Observable<VideoProgress> by lazy {
        Observable.interval(350, TimeUnit.MILLISECONDS)
                .takeWhile { status == PlayerStatus.Playing }
                .map { VideoProgress(AdgMediaPlayer.instance.mediaPlayer.currentPosition, duration) }
                .filter { currentPosition != it.progress }
                .takeWhile { it.duration > 0 && it.progress < it.duration }
                .doOnNext { currentPosition = it.progress }
                .observeOn(AndroidSchedulers.mainThread())
    }

    private val orientationHelper: OrientationHelper by lazy { OrientationHelper(context as Activity) }

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
        AdgMediaPlayer.instance.prepare(context, url)
        initRx()
    }

    private fun initRx() {
        AdgMediaPlayer.instance.videoSizeChangeRx()
                .takeWhile { status != PlayerStatus.Destroy }
                .subscribe { textureView?.requestLayout() }

        AdgMediaPlayer.instance.videoPreparedRx()
                .takeWhile { status != PlayerStatus.Destroy }
                .subscribe {
                    status = PlayerStatus.Prepared
                    status = PlayerStatus.Playing
                }

        AdgMediaPlayer.instance.videoBufferRx()
                .takeWhile { status != PlayerStatus.Destroy }
                .map { if (it >= 94) 100 else it }
                .subscribe {
                    bufferListener?.invoke(it)
                }

        AdgMediaPlayer.instance.videoInfoRx()
                .takeWhile { status != PlayerStatus.Destroy }
                .subscribe {
                    when (it.what) {
                        IMediaPlayer.MEDIA_INFO_BUFFERING_START -> status = PlayerStatus.Buffering
                        IMediaPlayer.MEDIA_INFO_BUFFERING_END -> status = PlayerStatus.Playing
                    }
                }
    }


    override fun onSurfaceTextureSizeChanged(surface: SurfaceTexture?, width: Int, height: Int) {
    }

    override fun onSurfaceTextureUpdated(surface: SurfaceTexture?) {
    }

    override fun onSurfaceTextureDestroyed(surface: SurfaceTexture?): Boolean {
        AdgMediaPlayer.instance.mediaPlayer.setSurface(null)
        surface?.release()
        return true
    }

    @Suppress("NAME_SHADOWING")
    override fun onSurfaceTextureAvailable(surface: SurfaceTexture?, width: Int, height: Int) {
        val `surface` = Surface(surface)
        AdgMediaPlayer.instance.mediaPlayer.setSurface(surface)
        surface.release()
    }

    fun pause() {
        AdgMediaPlayer.instance.mediaPlayer.pause()
        status = PlayerStatus.Pause
    }

    fun start() {
        AdgMediaPlayer.instance.mediaPlayer.start()
        status = PlayerStatus.Playing
    }

    fun isPlaying(): Boolean = AdgMediaPlayer.instance.mediaPlayer.isPlaying

    fun seekTo(progress: Int) {
        if (status == PlayerStatus.Pause || status == PlayerStatus.Buffering || status == PlayerStatus.Playing
                || status == PlayerStatus.Completed) {
            if (status == PlayerStatus.Pause) {
                start()
            }
            //todo 会自动回退1~3秒 据说是因为缺少I帧导致的 估计是的
            AdgMediaPlayer.instance.mediaPlayer.seekTo(AdgMediaPlayer.instance.mediaPlayer.duration * progress / 100)
        }
    }

    fun fullScreen() {
//        (context as Activity).fullScreen()
        originLayoutParams = layoutParams
        originParent = parent as ViewGroup
        originParent.removeView(this)
        androidContentView().addView(this, FrameLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT))
    }

    fun quitFullScreen() {
        androidContentView().removeView(this)
        originParent.addView(this, originLayoutParams)
    }


    fun onDestroy() {
        status = PlayerStatus.Release
        AdgMediaPlayer.instance.mediaPlayer.release()
        status = PlayerStatus.Destroy
        statusObservable.onComplete()
        orientationHelper.disable()
    }
}
