package com.adgvcxz.adgplayer.widget

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.util.Log
import android.view.TextureView
import android.view.View
import com.adgvcxz.adgplayer.AdgMediaPlayerManager

/**
 * zhaowei
 * Created by zhaowei on 2017/3/11.
 */

class AdgTextureView: TextureView {


    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {

        Log.e("zhaow", "==========")

        val videoWidth = AdgMediaPlayerManager.instance.videoWidth
        val videoHeight = AdgMediaPlayerManager.instance.videoHeight
        val width = View.getDefaultSize(videoWidth, widthMeasureSpec)
        val height = View.getDefaultSize(videoHeight, heightMeasureSpec)
        Log.e("zhaow", "$videoWidth     $videoHeight   $width     $height")
        if (videoWidth != 0 && videoHeight != 0) {
            val x = (videoWidth.toFloat()) / width
            val h = videoHeight / x
            Log.e("zhaow", "$width       $h")
            setMeasuredDimension(width, h.toInt())
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        }
    }
}
