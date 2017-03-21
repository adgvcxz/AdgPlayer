package com.adgvcxz.adgplayer

import com.adgvcxz.adgplayer.bean.VideoProgress
import com.adgvcxz.adgplayer.bean.VideoSize

/**
 * zhaowei
 * Created by zhaowei on 2017/3/20.
 */
interface AdgVideoPlayerListener {

    fun onStatusChanged(status: PlayerStatus)

    fun onVideoSizeChanged(videoSize: VideoSize)

    fun onVideoBufferChanged(buffer: Int)

    fun onProgressChanged(videoProgress: VideoProgress)
}