package com.adgvcxz.adgplayer.bean

/**
 * zhaowei
 * Created by zhaowei on 2017/3/13.
 */

data class VideoSize(val width: Int, val height: Int, val sarNum: Int, val sarDen: Int)

data class VideoBuffer(val buffer: Long, val duration: Long)

data class VideoProgress(val progress: Long, val duration: Long)

data class VideoInfo(val what: Int, val extra: Int)
