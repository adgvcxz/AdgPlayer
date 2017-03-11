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
class AdgMediaPlayer(private val mediaPlayer: IMediaPlayer): IMediaPlayer by mediaPlayer
        , IMediaPlayer.OnVideoSizeChangedListener {

    var onSizeChangeListener: WeakReference<IMediaPlayer.OnVideoSizeChangedListener>? = null

    init {
        mediaPlayer.setAudioStreamType(AudioManager.STREAM_MUSIC)
        //播放时，屏幕保持常亮
        mediaPlayer.setScreenOnWhilePlaying(true)
        mediaPlayer.setOnVideoSizeChangedListener(this)
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


    override fun onVideoSizeChanged(player: IMediaPlayer?, width: Int, height: Int, sar_num: Int, sar_den: Int) {
        //http://www.cnblogs.com/yinxiangpei/articles/3949041.html
        onSizeChangeListener?.get()?.onVideoSizeChanged(player, width, height, sar_num, sar_den)
        Log.e("zhaow", "$width    $height   $sar_num   $sar_den    ${mediaPlayer.videoWidth}   ${mediaPlayer.videoHeight}     ${onSizeChangeListener?.get()}")
    }
}