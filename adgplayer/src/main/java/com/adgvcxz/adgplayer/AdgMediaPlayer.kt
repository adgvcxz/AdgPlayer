package com.adgvcxz.adgplayer

import android.content.Context
import android.media.AudioManager
import android.net.Uri
import tv.danmaku.ijk.media.player.IMediaPlayer
import tv.danmaku.ijk.media.player.IjkMediaPlayer

/**
 * zhaowei
 * Created by zhaowei on 2017/3/10.
 */
class AdgMediaPlayer private constructor() {

    private object Holder {
        val Instance = AdgMediaPlayer()
    }

    companion object {
        internal val instance: AdgMediaPlayer by lazy {
            Holder.Instance
        }
    }

    internal lateinit var mediaPlayer: IMediaPlayer


    fun prepare(context: Context, url: String) {
        initPlayer()
        mediaPlayer.setDataSource(context, Uri.parse(url))
        mediaPlayer.prepareAsync()
        mediaPlayer.start()
    }

    fun initPlayer() {
        mediaPlayer = IjkMediaPlayer()
        initOptions()
    }

    private fun initOptions() {
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC)
        //播放时，屏幕保持常亮
        mediaPlayer.setScreenOnWhilePlaying(true)

        if (mediaPlayer is IjkMediaPlayer) {
            //启用硬编码
            (mediaPlayer as IjkMediaPlayer).setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec", 1)
            (mediaPlayer as IjkMediaPlayer).setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec-auto-rotate", 1)
            (mediaPlayer as IjkMediaPlayer).setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec-handle-resolution-change", 1)
        }
    }
}