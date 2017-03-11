package com.adgvcxz.adgplayer.widget

import android.annotation.TargetApi
import android.content.Context
import android.graphics.SurfaceTexture
import android.os.Build
import android.util.AttributeSet
import android.view.Surface
import android.view.TextureView
import android.widget.RelativeLayout
import android.widget.RelativeLayout.LayoutParams
import com.adgvcxz.adgplayer.AdgMediaPlayerManager
import tv.danmaku.ijk.media.player.IMediaPlayer
import java.lang.ref.WeakReference

/**
 * zhaowei
 * Created by zhaowei on 2017/3/11.
 */

class AdgVideoView : RelativeLayout, TextureView.SurfaceTextureListener, IMediaPlayer.OnVideoSizeChangedListener {

    private var textureView: AdgTextureView? = null

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
}
