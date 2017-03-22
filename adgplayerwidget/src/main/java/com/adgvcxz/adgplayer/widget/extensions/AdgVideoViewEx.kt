package com.adgvcxz.adgplayer.widget.extensions

import android.app.Activity
import android.view.ViewGroup
import android.view.Window
import com.adgvcxz.adgplayer.widget.AdgBaseVideoView

/**
 * zhaowei
 * Created by zhaowei on 2017/3/12.
 */

fun AdgBaseVideoView.androidContentView(): ViewGroup {
    return (context as Activity).findViewById(Window.ID_ANDROID_CONTENT) as ViewGroup
}