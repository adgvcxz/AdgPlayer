package com.adgvcxz.adgplayer

/**
 * zhaowei
 * Created by zhaowei on 2017/3/12.
 */

enum class PlayerStatus {
    Init,
    Preparing,
    Buffering,
    Playing,
    Pause,
    Completed,
    Release,
    Destroy,
    Error;
}
