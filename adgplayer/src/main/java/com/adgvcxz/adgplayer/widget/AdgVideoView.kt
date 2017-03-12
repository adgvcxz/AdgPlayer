package com.adgvcxz.adgplayer.widget

import android.annotation.TargetApi
import android.content.Context
import android.graphics.SurfaceTexture
import android.os.Build
import android.util.AttributeSet
import android.view.Surface
import android.view.TextureView
import android.widget.RelativeLayout
import com.adgvcxz.adgplayer.AdgMediaPlayerManager
import com.adgvcxz.adgplayer.PlayerStatus
import io.reactivex.Observable
import io.reactivex.disposables.CompositeDisposable
import tv.danmaku.ijk.media.player.IMediaPlayer
import java.lang.ref.WeakReference
import java.util.concurrent.TimeUnit

/**
 * zhaowei
 * Created by zhaowei on 2017/3/11.
 */

class AdgVideoView : RelativeLayout, TextureView.SurfaceTextureListener, IMediaPlayer.OnVideoSizeChangedListener {

    private var textureView: AdgTextureView? = null
    var currentPosition: Long = 0
    var status = PlayerStatus.Init

    private val disposables: CompositeDisposable by lazy {
        CompositeDisposable()
    }
    val progressObservable: Observable<Long> by lazy {
        Observable.interval(350, TimeUnit.MILLISECONDS)
                .filter { status == PlayerStatus.Playing }
                .map { AdgMediaPlayerManager.instance.currentPosition }
                .takeWhile {AdgMediaPlayerManager.instance.duration > 0 && it < AdgMediaPlayerManager.instance.duration}
                .filter { currentPosition != it }
                .doOnNext { currentPosition = it }
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
        AdgMediaPlayerManager.instance.onSizeChangeListener = WeakReference(this)
    }

    fun start(url: String) {
        status = PlayerStatus.Preparing
        AdgMediaPlayerManager.instance.start(context, url)
    }


    override fun onSurfaceTextureSizeChanged(surface: SurfaceTexture?, width: Int, height: Int) {
    }

    override fun onSurfaceTextureUpdated(surface: SurfaceTexture?) {
    }

    override fun onSurfaceTextureDestroyed(surface: SurfaceTexture?): Boolean {
        return true
    }

    @Suppress("NAME_SHADOWING")
    override fun onSurfaceTextureAvailable(surface: SurfaceTexture?, width: Int, height: Int) {
        val `surface` = Surface(surface)
        AdgMediaPlayerManager.instance.setSurface(surface)
        surface.release()
    }

    override fun onVideoSizeChanged(p0: IMediaPlayer?, p1: Int, p2: Int, p3: Int, p4: Int) {
        textureView?.requestLayout()
    }

    fun pause() {
        status = PlayerStatus.Pause
        AdgMediaPlayerManager.instance.pause()
    }

    fun start() {
        status = PlayerStatus.Playing
        AdgMediaPlayerManager.instance.start()
    }

    fun isPlaying(): Boolean = AdgMediaPlayerManager.instance.isPlaying

    fun seekTo(progress: Int) = AdgMediaPlayerManager.instance.seekTo(AdgMediaPlayerManager.instance.duration * progress / 100)


    fun onDestroy() {
        status = PlayerStatus.Release
        AdgMediaPlayerManager.instance.release()
    }
}
