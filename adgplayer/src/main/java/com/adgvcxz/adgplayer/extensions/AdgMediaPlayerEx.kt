package com.adgvcxz.adgplayer.extensions

import android.util.Log
import com.adgvcxz.adgplayer.AdgVideoPlayer
import com.adgvcxz.adgplayer.AdgVideoPlayerListener
import com.adgvcxz.adgplayer.PlayerStatus
import java.lang.ref.WeakReference

/**
 * zhaowei
 * Created by zhaowei on 2017/3/13.
 */

fun AdgVideoPlayer.pause() {
    mainPlayer.pause()
    status = PlayerStatus.Pause
}

fun AdgVideoPlayer.start() {
    mainPlayer.start()
    status = PlayerStatus.Playing
}

fun AdgVideoPlayer.isPlaying(): Boolean {
    return mainPlayer.isPlaying
}

fun AdgVideoPlayer.seekTo(progress: Long) {
    if (status == PlayerStatus.Pause || status == PlayerStatus.Buffering || status == PlayerStatus.Playing
            || status == PlayerStatus.Completed) {
        if (status == PlayerStatus.Pause) {
            start()
        }
        //todo 会自动回退1~3秒 据说是因为缺少I帧导致的 估计是的
        mainPlayer.seekTo(progress)
    }
}

fun AdgVideoPlayer.addListener(listener: AdgVideoPlayerListener) {
    val reference = WeakReference(listener)
    if (!listeners.contains(reference)) {
        listeners.add(reference)
    }
}

fun AdgVideoPlayer.removeListener(listener: AdgVideoPlayerListener) {
    for (current in listeners) {
        if (current.get() === listener) {
            listeners.remove(current)
            break
        }
    }
}

fun AdgVideoPlayer.destroy() {
    status = PlayerStatus.Release
    current = -1
    isStart = false
    mainPlayer.release()
}
