package com.adgvcxz.adgplayer.extensions

import com.adgvcxz.adgplayer.AdgMediaPlayerManager
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

fun IMediaPlayer.videoSizeChangeRx(): Observable<VideoSize> {
    return Flowable.create<VideoSize>({
        val callback = IMediaPlayer.OnVideoSizeChangedListener { _, width, height, sarNum, sarDen ->
            it.onNext(VideoSize(width, height, sarNum, sarDen))
        }
        setOnVideoSizeChangedListener(callback)
    }, BackpressureStrategy.DROP).toObservable()
}

fun IMediaPlayer.videoPreparedRx(): Observable<Long> {
    return Flowable.create<Long>({
        val callback = IMediaPlayer.OnPreparedListener { _ ->
            it.onNext(AdgMediaPlayerManager.instance.duration)
        }
        setOnPreparedListener(callback)
    }, BackpressureStrategy.DROP).toObservable()
}

fun IMediaPlayer.videoBufferRx(): Observable<Int> {
    return Flowable.create<Int>({
        val callback = IMediaPlayer.OnBufferingUpdateListener {_, buffer ->
            it.onNext(buffer)
        }
        setOnBufferingUpdateListener(callback)
    }, BackpressureStrategy.DROP).toObservable()
}

fun IMediaPlayer.videoInfoRx(): Observable<VideoInfo> {
    return Flowable.create<VideoInfo>({
        val callback = IMediaPlayer.OnInfoListener { _, what, extra ->
            it.onNext(VideoInfo(what, extra))
            false
        }
        setOnInfoListener(callback)
    }, BackpressureStrategy.DROP).toObservable()
}