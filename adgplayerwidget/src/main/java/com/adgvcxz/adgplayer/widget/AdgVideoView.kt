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
import com.adgvcxz.adgplayer.widget.extensions.dimen
import com.adgvcxz.adgplayer.widget.extensions.duration
import com.adgvcxz.adgplayer.widget.extensions.seconds
import com.adgvcxz.adgplayer.widget.util.OnSmallBottomListener
import com.adgvcxz.adgplayer.widget.util.ScreenOrientation
import com.adgvcxz.adgplayer.widget.views.AdgProgressLayout
import com.adgvcxz.adgplayer.widget.views.AdgSmallBottomLayout

/**
 * zhaowei
 * Created by zhaowei on 2017/3/22.
 */
class AdgVideoView : AdgBaseVideoView, OnSmallBottomListener {

    private lateinit var smallBottomLayout: AdgSmallBottomLayout
    private lateinit var progressLayout: AdgProgressLayout


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
        initSmallBottomLayout()
        initProgressLayout()
    }

    private fun initSmallBottomLayout() {
        smallBottomLayout = AdgSmallBottomLayout(context)
        val lp = LayoutParams(LayoutParams.MATCH_PARENT, R.dimen.adg_bottom_layout_height.dimen(context))
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

    private fun initProgressLayout() {
        progressLayout = AdgProgressLayout(context)
        val lp = LayoutParams(LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT)
        lp.addRule(CENTER_IN_PARENT)
        addView(progressLayout, lp)
        progressLayout.visibility = View.GONE
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
        val seconds = AdgVideoPlayer.instance.duration.seconds()
        progressLayout.duration.text = "${progress.duration()} / ${seconds.duration()}"
    }

    override fun onVideoSizeChanged(videoSize: VideoSize) {
        super.onVideoSizeChanged(videoSize)
    }

    override fun onVideoBufferChanged(buffer: Int) {
        super.onVideoBufferChanged(buffer)
    }

    override fun onProgressChanged(videoProgress: VideoProgress) {
        val current = videoProgress.progress.seconds()
        smallBottomLayout.setProgress(current)
        smallBottomLayout.progress.text = current.duration()
    }

    override fun onVideoPrepared() {
        val seconds = AdgVideoPlayer.instance.duration.seconds()
        smallBottomLayout.seekBar.max = seconds
        smallBottomLayout.duration.text = seconds.duration()
    }

    override fun onStartTrackingTouch() {
        progressLayout.visibility = View.VISIBLE
        val progress = AdgVideoPlayer.instance.currentPosition.seconds()
        val seconds = AdgVideoPlayer.instance.duration.seconds()
        progressLayout.duration.text = "${progress.duration()} / ${seconds.duration()}"
    }

    override fun onStopTrackingTouch() {
        progressLayout.visibility = View.GONE
    }
}
