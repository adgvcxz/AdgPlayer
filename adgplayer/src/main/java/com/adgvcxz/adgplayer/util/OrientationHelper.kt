package com.adgvcxz.adgplayer.util

import android.app.Activity
import android.provider.Settings
import android.util.Log
import android.view.OrientationEventListener
import com.adgvcxz.adgplayer.ScreenOrientation
import com.adgvcxz.adgplayer.extensions.rotateScreen
import com.adgvcxz.adgplayer.extensions.screenOrientation

/**
 * zhaowei
 * Created by zhaowei on 2017/3/16.
 */

class OrientationHelper(val activity: Activity) {

    var screenOrientation: ScreenOrientation = activity.screenOrientation()
        set(value) {
            if (field != value) {
                field = value
                activity.rotateScreen(value)
            }
        }

    var autoRotate = true
    var lastAutoRotate = true

    private val orientationListener: OrientationEventListener = object : OrientationEventListener(activity) {

        override fun onOrientationChanged(orientation: Int) {
            if (autoRotate) {
                if (orientation == OrientationEventListener.ORIENTATION_UNKNOWN
                        || Settings.System.getInt(activity.contentResolver, Settings.System.ACCELEROMETER_ROTATION, 0) != 1) {
                    return
                }
                if (orientation >= 330 || orientation <= 30) {
                    screenOrientation = ScreenOrientation.Portrait
                    if (!lastAutoRotate) {
                        lastAutoRotate = true
                    }
                } else if (orientation in 60..120 && lastAutoRotate) {
                    screenOrientation = ScreenOrientation.ReverseLandscape
                } else if (orientation in 240..300 && lastAutoRotate) {
                    screenOrientation = ScreenOrientation.Landscape
                }
            }
        }
    }

    init {
        orientationListener.enable()
    }

    fun disable() = orientationListener.disable()

    fun enable() = orientationListener.enable()

    fun rotateScreen(orientation: ScreenOrientation) {
        if (orientation != screenOrientation) {
            autoRotate = orientation == ScreenOrientation.Portrait
            screenOrientation = orientation
            lastAutoRotate = false
        }
    }
}
