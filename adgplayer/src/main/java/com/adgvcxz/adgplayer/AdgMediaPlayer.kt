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
class AdgMediaPlayer(private var mediaPlayer: IMediaPlayer) : IMediaPlayer by mediaPlayer
        , IMediaPlayer.OnBufferingUpdateListener, IMediaPlayer.OnInfoListener, IMediaPlayer.OnPreparedListener {

    var playable = true

    init {
        initOptions()
    }

    fun prepare(context: Context, url: String) {
        initPlayer()
        mediaPlayer.setDataSource(context, Uri.parse(url))
        playable = false
        mediaPlayer.prepareAsync()
        mediaPlayer.start()
    }

    fun initPlayer() {
        if (!playable) {
            mediaPlayer.release()
            mediaPlayer = AdgMediaPlayerManager.generateMediaPlayer()
            initOptions()
        }
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

    override fun release() {
        mediaPlayer.release()
        playable = false
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