package com.adgvcxz.adgplayer.widget.extensions

import android.app.Activity
import android.content.pm.ActivityInfo
import android.support.v7.app.AppCompatActivity
import android.view.Surface
import android.view.WindowManager
import com.adgvcxz.adgplayer.widget.util.ScreenOrientation

/**
 * zhaowei
 * Created by zhaowei on 2017/3/16.
 */

fun Activity.landscape() {
    requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
    fullScreen()
}

fun Activity.portrait() {
    requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT
    quitFullScreen()
}

fun Activity.reverseLandscape() {
    requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_REVERSE_LANDSCAPE
    fullScreen()
}

fun Activity.fullScreen() {
    if (this is AppCompatActivity) {
        this.supportActionBar?.setShowHideAnimationEnabled(false)
        this.supportActionBar?.hide()
    }
    window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
}

fun Activity.quitFullScreen() {
    if (this is AppCompatActivity) {
        this.supportActionBar?.setShowHideAnimationEnabled(false)
        this.supportActionBar?.show()
    }
    window.clearFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN)
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


