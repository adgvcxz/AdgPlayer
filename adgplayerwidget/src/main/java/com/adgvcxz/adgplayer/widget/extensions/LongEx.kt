package com.adgvcxz.adgplayer.widget.extensions

/**
 * zhaowei
 * Created by zhaowei on 2017/3/25.
 */

fun Long.seconds(): Int {
    return (this / 1000).toInt()
}
