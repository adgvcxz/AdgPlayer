package com.adgvcxz.adgplayer.extensions

import com.adgvcxz.adgplayer.AdgMediaPlayer
import com.adgvcxz.adgplayer.bean.VideoInfo
import com.adgvcxz.adgplayer.bean.VideoSize
import io.reactivex.BackpressureStrategy
import io.reactivex.Flowable
import io.reactivex.Observable
import tv.danmaku.ijk.media.player.IMediaPlayer

/**
 * zhaowei
 * Created by zhaowei on 2017/3/13.
 */

fun AdgMediaPlayer.videoSizeChangeRx(): Observable<VideoSize> {
    return Flowable.create<VideoSize>({
        val callback = IMediaPlayer.OnVideoSizeChangedListener { _, width, height, sarNum, sarDen ->
            it.onNext(VideoSize(width, height, sarNum, sarDen))
        }
        mediaPlayer.setOnVideoSizeChangedListener(callback)
    }, BackpressureStrategy.DROP).toObservable()
}

fun AdgMediaPlayer.videoPreparedRx(): Observable<Long> {
    return Flowable.create<Long>({
        val callback = IMediaPlayer.OnPreparedListener { _ ->
            it.onNext(mediaPlayer.duration)
        }
        mediaPlayer.setOnPreparedListener(callback)
    }, BackpressureStrategy.DROP).toObservable()
}

fun AdgMediaPlayer.videoBufferRx(): Observable<Int> {
    return Flowable.create<Int>({
        val callback = IMediaPlayer.OnBufferingUpdateListener { _, buffer ->
            it.onNext(buffer)
        }
        mediaPlayer.setOnBufferingUpdateListener(callback)
    }, BackpressureStrategy.DROP).toObservable()
}

fun AdgMediaPlayer.videoInfoRx(): Observable<VideoInfo> {
    return Flowable.create<VideoInfo>({
        val callback = IMediaPlayer.OnInfoListener { _, what, extra ->
            it.onNext(VideoInfo(what, extra))
            false
        }
        mediaPlayer.setOnInfoListener(callback)
    }, BackpressureStrategy.DROP).toObservable()
}