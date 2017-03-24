package com.adgvcxz.adgplayer.widget.views

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.Gravity
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.SeekBar
import android.widget.TextView
import com.adgvcxz.adgplayer.widget.R
import com.adgvcxz.adgplayer.widget.util.OnVideoListener

/**
 * zhaowei
 * Created by zhaowei on 2017/3/24.
 */

class AdgSmallBottomLayout : LinearLayout, SeekBar.OnSeekBarChangeListener {

    internal lateinit var fullScreenView: ImageView
    internal lateinit var playBtn: ImageView
    internal lateinit var seekBar: SeekBar
    internal lateinit var duration: TextView
    internal lateinit var progress: TextView
    internal var listener: OnVideoListener? = null
    private var isTouchSeekbar = false


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
        View.inflate(context, R.layout.adg_include_video_view, this)
        orientation = LinearLayout.HORIZONTAL
        gravity = Gravity.CENTER_VERTICAL
        visibility = View.GONE
        fullScreenView = findViewById(R.id.adg_video_small_bottom_layout_fullscreen) as ImageView
        playBtn = findViewById(R.id.adg_video_small_bottom_layout_play) as ImageView
        seekBar = findViewById(R.id.adg_video_small_bottom_layout_seek_bar) as SeekBar
        duration = findViewById(R.id.adg_video_small_bottom_layout_duration) as TextView
        progress = findViewById(R.id.adg_video_small_bottom_layout_progress) as TextView
        fullScreenView.setOnClickListener {
            this.listener?.onClickPlayBtn()
        }
        playBtn.setOnClickListener {
            this.listener?.onClickFullScreen()
        }
        seekBar.setOnSeekBarChangeListener(this)
    }

    override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
        if (isTouchSeekbar) {
            listener?.onChangeProgress(progress)
        }
    }

    override fun onStartTrackingTouch(seekBar: SeekBar) {
        isTouchSeekbar = true
    }

    override fun onStopTrackingTouch(seekBar: SeekBar) {
        if (isTouchSeekbar) {
            listener?.onChangeProgress(seekBar.progress)
        }
        isTouchSeekbar = false
    }

    fun setProgress(progress: Int) {
        if (!isTouchSeekbar) {
            seekBar.progress = progress
        }
    }
}
