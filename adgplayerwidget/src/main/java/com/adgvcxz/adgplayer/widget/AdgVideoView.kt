package com.adgvcxz.adgplayer.widget

import android.annotation.TargetApi
import android.app.Activity
import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.util.Log
import android.view.TextureView
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.RelativeLayout
import com.adgvcxz.adgplayer.AdgVideoPlayer
import com.adgvcxz.adgplayer.AdgVideoPlayerListener
import com.adgvcxz.adgplayer.IAdgVideoView
import com.adgvcxz.adgplayer.PlayerStatus
import com.adgvcxz.adgplayer.bean.VideoProgress
import com.adgvcxz.adgplayer.bean.VideoSize
import com.adgvcxz.adgplayer.extensions.addListener
import com.adgvcxz.adgplayer.extensions.removeListener
import com.adgvcxz.adgplayer.widget.extensions.androidContentView
import com.adgvcxz.adgplayer.widget.util.OrientationHelper
import com.adgvcxz.adgplayer.widget.util.ScreenOrientation

/**
 * zhaowei
 * Created by zhaowei on 2017/3/11.
 */

class AdgVideoView : RelativeLayout, IAdgVideoView, AdgVideoPlayerListener {

    private var textureView: AdgTextureView? = null

    private lateinit var originParent: ViewGroup
    private lateinit var originLayoutParams: ViewGroup.LayoutParams
    private val orientationHelper: OrientationHelper by lazy { OrientationHelper(context as Activity) }


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


    constructor(context: Context) : super(context) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        init()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        init()
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        init()
    }

    private fun init() {
        addTextureView()
        AdgVideoPlayer.instance.addListener(this)
        AdgVideoPlayer.instance.bindView(this)
    }

    private fun addTextureView() {
        textureView = AdgTextureView(context)
        val lp = LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT)
        lp.addRule(CENTER_IN_PARENT)
        addView(textureView, lp)
    }


    fun fullScreen() {
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
        AdgVideoPlayer.instance.unBindView()
        AdgVideoPlayer.instance.removeListener(this)
        orientationHelper.disable()
    }


    override fun setSurfaceTextureListener(listener: TextureView.SurfaceTextureListener) {
        textureView?.surfaceTextureListener = listener
    }

    override fun recreate() {
        addTextureView()
    }

    override fun onStatusChanged(status: PlayerStatus) {

    }

    override fun onVideoSizeChanged(videoSize: VideoSize) {
        textureView?.videoWidth = videoSize.width
        textureView?.videoHeight = videoSize.height
        textureView?.requestLayout()
    }

    override fun onVideoBufferChanged(buffer: Int) {

    }

    override fun onProgressChanged(videoProgress: VideoProgress) {

    }
}
