package com.adgvcxz.adgplayer

import io.reactivex.subjects.PublishSubject
import io.reactivex.subjects.Subject

/**
 * zhaowei
 * Created by zhaowei on 2017/3/12.
 */

enum class PlayerStatus {
    Init,
    Preparing,
    Prepared,
    Buffering,
    Playing,
    Pause,
    Completed,
    Release,
    Destroy,
    Error;


    fun rx(): Subject<PlayerStatus> = PublishSubject.create<PlayerStatus>().toSerialized()
}
