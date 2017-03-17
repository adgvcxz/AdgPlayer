package com.adgvcxz.adgplayer.extensions

import android.app.Activity
import android.view.ViewGroup
import android.view.Window
import com.adgvcxz.adgplayer.widget.AdgVideoView

/**
 * zhaowei
 * Created by zhaowei on 2017/3/12.
 */

fun AdgVideoView.androidContentView(): ViewGroup {
    return (context as Activity).findViewById(Window.ID_ANDROID_CONTENT) as ViewGroup
}