package com.adgvcxz.adgplayer.widget

import android.annotation.TargetApi
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.util.AttributeSet
import android.view.View
import com.adgvcxz.adgplayer.AdgVideoPlayer
import com.adgvcxz.adgplayer.bean.VideoProgress
import com.adgvcxz.adgplayer.bean.VideoSize
import com.adgvcxz.adgplayer.extensions.isPlaying
import com.adgvcxz.adgplayer.extensions.pause
import com.adgvcxz.adgplayer.extensions.seekTo
import com.adgvcxz.adgplayer.extensions.start
import com.adgvcxz.adgplayer.widget.extensions.duration
import com.adgvcxz.adgplayer.widget.util.OnVideoListener
import com.adgvcxz.adgplayer.widget.util.ScreenOrientation
import com.adgvcxz.adgplayer.widget.views.AdgSmallBottomLayout

/**
 * zhaowei
 * Created by zhaowei on 2017/3/22.
 */
class AdgVideoView : AdgBaseVideoView, OnVideoListener {

    private lateinit var smallBottomLayout: AdgSmallBottomLayout


    constructor(context: Context) : super(context) {
        initExtensionView()
    }

    constructor(context: Context, attrs: AttributeSet) : super(context, attrs) {
        initExtensionView()
    }

    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int) : super(context, attrs, defStyleAttr) {
        initExtensionView()
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context, attrs: AttributeSet, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes) {
        initExtensionView()
    }

    private fun initExtensionView() {
        setBackgroundColor(Color.BLACK)
        orientationEnable = true
        smallBottomLayout = AdgSmallBottomLayout(context)
        val lp = LayoutParams(LayoutParams.MATCH_PARENT, 40 * 3)
        lp.addRule(ALIGN_PARENT_BOTTOM)
        addView(smallBottomLayout, lp)
        smallBottomLayout.listener = this
        textureViewGroup.setOnClickListener {
            if (screenOrientation == ScreenOrientation.Portrait) {
                if (smallBottomLayout.visibility == View.VISIBLE) {
                    smallBottomLayout.visibility = View.GONE
                } else {
                    smallBottomLayout.visibility = View.VISIBLE
                }
            }
        }
    }

    fun bindPlayer() {
        AdgVideoPlayer.instance.bindView(this)
    }

    fun prepare(url: String) {
        AdgVideoPlayer.instance.prepare(url)
    }

    override fun onClickPlayBtn() {
        if (AdgVideoPlayer.instance.isPlaying()) {
            AdgVideoPlayer.instance.pause()
            smallBottomLayout.playBtn.setImageResource(R.mipmap.adg_video_play)
        } else {
            AdgVideoPlayer.instance.start()
            smallBottomLayout.playBtn.setImageResource(R.mipmap.adg_video_pause)
        }
    }

    override fun onClickFullScreen() {
        if (isFullScreen) {
            screenOrientation = ScreenOrientation.Portrait
        } else {
            screenOrientation = ScreenOrientation.Landscape
        }
    }

    override fun onChangeProgress(progress: Int) {
        AdgVideoPlayer.instance.seekTo(progress.toLong() * 1000)
    }

    override fun onVideoSizeChanged(videoSize: VideoSize) {
        super.onVideoSizeChanged(videoSize)
    }

    override fun onVideoBufferChanged(buffer: Int) {
        super.onVideoBufferChanged(buffer)
    }

    override fun onProgressChanged(videoProgress: VideoProgress) {
        val current = (videoProgress.progress / 1000).toInt()
        smallBottomLayout.setProgress(current)
        smallBottomLayout.progress.text = current.duration()
    }

    override fun onVideoPrepared() {
        val seconds = (AdgVideoPlayer.instance.duration / 1000).toInt()
        smallBottomLayout.seekBar.max = seconds
        smallBottomLayout.duration.text = seconds.duration()
    }
}
