package com.adgvcxz.adgplayer.widget.views

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.widget.RelativeLayout
import android.widget.TextView
import com.adgvcxz.adgplayer.widget.R

/**
 * zhaowei
 * Created by zhaowei on 2017/3/25.
 */

class AdgProgressLayout : RelativeLayout {

    lateinit var duration: TextView
    lateinit var difference: TextView
    lateinit var durationLayout: View

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
        View.inflate(context, R.layout.adg_include_progress_layout, this)
        duration = findViewById(R.id.adg_video_progress_duration) as TextView
        difference = findViewById(R.id.adg_video_progress_difference) as TextView
        durationLayout = findViewById(R.id.adg_video_progress_duration_layout)
    }
}