package com.adgvcxz.adgplayer.extensions

import android.app.Activity
import android.content.pm.ActivityInfo
import android.view.Surface
import com.adgvcxz.adgplayer.ScreenOrientation

/**
 * zhaowei
 * Created by zhaowei on 2017/3/16.
 */

fun Activity.landscape() {
    requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
}

fun Activity.portrait() {
    requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
}

fun Activity.reverseLandscape() {
    requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE
}

fun Activity.screenOrientation(): ScreenOrientation {
    when (window.windowManager.defaultDisplay.rotation) {
        Surface.ROTATION_90 -> return ScreenOrientation.ReverseLandscape
        Surface.ROTATION_270 -> return ScreenOrientation.Landscape
        else -> return ScreenOrientation.Portrait
    }
}

fun Activity.rotateScreen(orientation: ScreenOrientation) {
    when (orientation) {
        ScreenOrientation.Landscape -> landscape()
        ScreenOrientation.ReverseLandscape -> reverseLandscape()
        else -> portrait()
    }
}


