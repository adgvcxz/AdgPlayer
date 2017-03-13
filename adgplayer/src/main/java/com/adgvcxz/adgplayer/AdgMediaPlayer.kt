package com.adgvcxz.adgplayer

import android.content.Context
import android.media.AudioManager
import android.net.Uri
import android.util.Log
import tv.danmaku.ijk.media.player.IMediaPlayer
import tv.danmaku.ijk.media.player.IjkMediaPlayer
import java.lang.ref.WeakReference

/**
 * zhaowei
 * Created by zhaowei on 2017/3/10.
 */
class AdgMediaPlayer(private val mediaPlayer: IMediaPlayer) : IMediaPlayer by mediaPlayer
        , IMediaPlayer.OnBufferingUpdateListener, IMediaPlayer.OnInfoListener, IMediaPlayer.OnPreparedListener {

    init {
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC)
        //播放时，屏幕保持常亮
        mediaPlayer.setScreenOnWhilePlaying(true)
        mediaPlayer.setOnBufferingUpdateListener(this)
        mediaPlayer.setOnInfoListener(this)
        mediaPlayer.setOnPreparedListener(this)

        if (mediaPlayer is IjkMediaPlayer) {
            //启用硬编码
            mediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec", 1)
            mediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec-auto-rotate", 1)
            mediaPlayer.setOption(IjkMediaPlayer.OPT_CATEGORY_PLAYER, "mediacodec-handle-resolution-change", 1)
        }
    }

    fun start(context: Context, url: String) {
        mediaPlayer.setDataSource(context, Uri.parse(url))
        mediaPlayer.prepareAsync()
        mediaPlayer.start()
    }


    override fun onBufferingUpdate(player: IMediaPlayer?, percent: Int) {
        Log.e("zhaow", "onBufferingUpdate        $percent")
    }

    override fun onInfo(player: IMediaPlayer?, what: Int, extra: Int): Boolean {
        Log.e("zhaow", "onInfo   $what    $extra")
        return false
    }

    override fun onPrepared(player: IMediaPlayer?) {
        Log.e("zhaow", "onPrepared")
    }
}