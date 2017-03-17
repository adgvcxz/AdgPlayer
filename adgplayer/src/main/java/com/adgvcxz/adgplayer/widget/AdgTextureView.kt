package com.adgvcxz.adgplayer.widget

import android.annotation.TargetApi
import android.content.Context
import android.os.Build
import android.util.AttributeSet
import android.view.TextureView
import android.view.View
import com.adgvcxz.adgplayer.AdgMediaPlayer

/**
 * zhaowei
 * Created by zhaowei on 2017/3/11.
 */

class AdgTextureView : TextureView {


    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(context, attrs, defStyleAttr)
    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int, defStyleRes: Int) : super(context, attrs, defStyleAttr, defStyleRes)


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {

        val videoWidth = AdgMediaPlayer.instance.mediaPlayer.videoWidth
        val videoHeight = AdgMediaPlayer.instance.mediaPlayer.videoHeight
        val width = View.getDefaultSize(videoWidth, widthMeasureSpec)
        val height = View.getDefaultSize(videoHeight, heightMeasureSpec)
        if (videoWidth != 0 && videoHeight != 0) {
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
