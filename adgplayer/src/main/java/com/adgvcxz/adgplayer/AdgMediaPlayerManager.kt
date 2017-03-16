package com.adgvcxz.adgplayer

import tv.danmaku.ijk.media.player.IMediaPlayer
import tv.danmaku.ijk.media.player.IjkMediaPlayer

/**
 * zhaowei
 * Created by zhaowei on 2017/3/11.
 */

class AdgMediaPlayerManager private constructor() {

    private object Holder {
        val Instance = AdgMediaPlayer(generateMediaPlayer())
    }

    companion object {
        val instance: AdgMediaPlayer by lazy {
            Holder.Instance
        }

        fun generateMediaPlayer(): IMediaPlayer {
            return IjkMediaPlayer()
        }
    }
}
