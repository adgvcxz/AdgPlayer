package com.adgvcxz.adgplayer.widget

import android.annotation.TargetApi
import android.content.Context
import android.graphics.Color
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.widget.ImageView
import com.adgvcxz.adgplayer.AdgVideoPlayer
import com.adgvcxz.adgplayer.extensions.isPlaying
import com.adgvcxz.adgplayer.extensions.pause
import com.adgvcxz.adgplayer.extensions.start
import com.adgvcxz.adgplayer.widget.util.ScreenOrientation

/**
 * zhaowei
 * Created by zhaowei on 2017/3/22.
 */
class AdgVideoView: AdgBaseVideoView {

    private lateinit var fullScreenView: ImageView
    private lateinit var playBtn: ImageView


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
        View.inflate(context, R.layout.adg_include_video_view, this)
        fullScreenView = findViewById(R.id.adg_video_fullscreen) as ImageView
        playBtn = findViewById(R.id.adg_video_play) as ImageView
        fullScreenView.setOnClickListener {
            if (isFullScreen) {
                screenOrientation = ScreenOrientation.Portrait
            } else {
                screenOrientation = ScreenOrientation.Landscape
            }
        }
        playBtn.setOnClickListener {
            if (AdgVideoPlayer.instance.isPlaying()) {
                AdgVideoPlayer.instance.pause()
                playBtn.setImageResource(R.mipmap.adg_video_play)
            } else {
                AdgVideoPlayer.instance.start()
                playBtn.setImageResource(R.mipmap.adg_video_pause)
            }
        }
    }

    fun bindPlayer() {
        AdgVideoPlayer.instance.bindView(this)
    }

    fun prepare(url: String) {
        AdgVideoPlayer.instance.prepare(url)
    }
}
