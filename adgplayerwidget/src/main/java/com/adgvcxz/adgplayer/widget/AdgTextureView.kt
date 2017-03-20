package com.adgvcxz.adgplayer.widget

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.TextureView
import android.view.View

/**
 * zhaowei
 * Created by zhaowei on 2017/3/11.
 */

class AdgTextureView : TextureView {

    internal var videoWidth: Int = 0
    internal var videoHeight: Int = 0


    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        if (videoWidth != 0 && videoHeight != 0) {
            val width = View.getDefaultSize(videoWidth, widthMeasureSpec)
            val height = View.getDefaultSize(videoHeight, heightMeasureSpec)
            val x = (videoWidth.toFloat()) / width
            var targetWidth = videoWidth / x
            var targetHeight = videoHeight / x
            if (targetHeight > height) {
                targetWidth = targetWidth * height / targetHeight
                targetHeight = height.toFloat()
            }
            setMeasuredDimension(targetWidth.toInt(), targetHeight.toInt())
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        }
    }
}
