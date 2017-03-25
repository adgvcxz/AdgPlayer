package com.adgvcxz.adgplayer.widget.extensions

import android.content.Context
import java.text.DecimalFormat

/**
 * zhaowei
 * Created by zhaowei on 2017/3/24.
 */

val TimeFormat = DecimalFormat("00")

fun Int.duration(): String {
    if (this < 3600) {
        return "${TimeFormat.format(this / 60)}:${TimeFormat.format(this % 60)}"
    } else {
        val hour = this / 3600
        return "${TimeFormat.format(hour)}:${TimeFormat.format((this - hour * 3600) / 60)}:${TimeFormat.format(this % 60)}"
    }
}

fun Int.dimen(context: Context): Int {
    return context.resources.getDimensionPixelSize(this)
}