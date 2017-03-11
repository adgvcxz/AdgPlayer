package com.adgvcxz.adgplayer

import tv.danmaku.ijk.media.player.IjkMediaPlayer

/**
 * zhaowei
 * Created by zhaowei on 2017/3/11.
 */

class AdgMediaPlayerManager private constructor() {

    private object Holder {
        val Instance = AdgMediaPlayer(IjkMediaPlayer())
    }

    companion object {
        val instance: AdgMediaPlayer by lazy {
            Holder.Instance
        }
    }
}
