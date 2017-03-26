package com.adgvcxz.adgplayer.widget.util

/**
 * zhaowei
 * Created by zhaowei on 2017/3/25.
 */

interface OnSmallBottomListener: OnVideoListener {

    fun onStartTrackingTouch()

    fun onStopTrackingTouch(seconds: Int)

}
