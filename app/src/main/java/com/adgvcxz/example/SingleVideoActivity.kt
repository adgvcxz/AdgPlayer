package com.adgvcxz.example

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import com.adgvcxz.adgplayer.AdgVideoPlayer
import com.adgvcxz.adgplayer.AdgVideoPlayerListener
import com.adgvcxz.adgplayer.PlayerStatus
import com.adgvcxz.adgplayer.bean.VideoProgress
import com.adgvcxz.adgplayer.bean.VideoSize
import com.adgvcxz.adgplayer.extensions.*
import com.adgvcxz.adgplayer.widget.AdgBaseVideoView
import com.adgvcxz.adgplayer.widget.util.ScreenOrientation

/**
 * zhaowei
 * Created by zhaowei on 2017/3/17.
 */

class SingleVideoActivity : BaseVideoActivity(), SeekBar.OnSeekBarChangeListener, AdgVideoPlayerListener {

    private val Video1 = "http://9890.vod.myqcloud.com/9890_4e292f9a3dd011e6b4078980237cc3d3.f20.mp4"
    private val Video2 = "http://baobab.wdjcdn.com/14564977406580.mp4"

    private lateinit var videoView: AdgBaseVideoView
    private lateinit var seekBar: SeekBar
    private lateinit var time: TextView
    private lateinit var progressBar: View
    private var videoUrl = Video1
    private var touchSeekBar = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_single_video)
        videoView = findViewById(R.id.video_view) as AdgBaseVideoView
        val start = findViewById(R.id.start) as Button
        seekBar = findViewById(R.id.seek_bar) as SeekBar
        val brightness = findViewById(R.id.brightness) as SeekBar
        time = findViewById(R.id.time) as TextView
        progressBar = findViewById(R.id.progress_bar)
        start.setOnClickListener {
            if (AdgVideoPlayer.instance.isPlaying()) {
                AdgVideoPlayer.instance.pause()
                start.setText(R.string.start)
            } else {
                AdgVideoPlayer.instance.start()
                start.setText(R.string.pause)
            }
        }

        findViewById(R.id.mute).setOnClickListener {
            if (AdgVideoPlayer.instance.volume == 1F) {
                AdgVideoPlayer.instance.volume = 0F
            } else {
                AdgVideoPlayer.instance.volume = 1F
            }
        }

        findViewById(R.id.horizontal).setOnClickListener {
            if (videoView.screenOrientation != ScreenOrientation.Portrait) {
                videoView.screenOrientation = ScreenOrientation.Portrait
            } else {
                videoView.screenOrientation = ScreenOrientation.Landscape
            }
        }

        findViewById(R.id.quality).setOnClickListener {
            if (Video1 == videoUrl) {
                videoUrl = Video2
                AdgVideoPlayer.instance.changeQuality(Video2)
            } else {
                videoUrl = Video1
                AdgVideoPlayer.instance.changeQuality(Video1)
            }
        }

        findViewById(R.id.screen).setOnClickListener {
            videoView.orientationEnable = true
        }

        seekBar.setOnSeekBarChangeListener(this)
        brightness.setOnSeekBarChangeListener(this)

        AdgVideoPlayer.instance.bindView(videoView)
//        AdgVideoPlayer.instance.prepare("http://baobab.wdjcdn.com/14564977406580.mp4")
        AdgVideoPlayer.instance.prepare(videoUrl)
        brightness.progress = (AdgVideoPlayer.instance.brightness * 100).toInt()

        AdgVideoPlayer.instance.addListener(this)

    }


    override fun onProgressChanged(seekBar: SeekBar, progress: Int, fromUser: Boolean) {
        when (seekBar.id) {
            R.id.brightness -> AdgVideoPlayer.instance.brightness = seekBar.progress.toFloat() / 100
            R.id.seek_bar -> {
                touchSeekBar = true
                AdgVideoPlayer.instance.seekTo(seekBar.progress * AdgVideoPlayer.instance.duration / 100)
                time.text = "${seekBar.progress * AdgVideoPlayer.instance.duration / 100 / 1000}s / ${AdgVideoPlayer.instance.duration / 1000}s"
            }
        }
    }

    override fun onStartTrackingTouch(seekBar: SeekBar) {

    }

    override fun onStopTrackingTouch(seekBar: SeekBar) {
        when (seekBar.id) {
            R.id.brightness -> AdgVideoPlayer.instance.brightness = seekBar.progress.toFloat() / 100
            R.id.seek_bar -> {
                touchSeekBar = false
                AdgVideoPlayer.instance.seekTo(seekBar.progress * AdgVideoPlayer.instance.duration / 100)
            }
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        videoView.onDestroy()
        AdgVideoPlayer.instance.removeListener(this)
    }

    override fun onBackPressed() {
        if (videoView.screenOrientation != ScreenOrientation.Portrait) {
            videoView.screenOrientation = ScreenOrientation.Portrait
        } else {
            super.onBackPressed()
        }
    }

    override fun onStatusChanged(status: PlayerStatus) {
        if (status == PlayerStatus.Preparing) {
            time.text = "${AdgVideoPlayer.instance.duration / 1000}s"
        }
        if (status == PlayerStatus.Buffering || status == PlayerStatus.Preparing) {
            progressBar.visibility = View.VISIBLE
        } else {
            progressBar.visibility = View.GONE
        }
    }

    override fun onVideoSizeChanged(videoSize: VideoSize) {

    }

    override fun onVideoBufferChanged(buffer: Int) {
        seekBar.secondaryProgress = buffer
    }

    override fun onProgressChanged(videoProgress: VideoProgress) {
        if (!touchSeekBar) {
            seekBar.progress = (videoProgress.progress.toDouble() / videoProgress.duration * 100).toInt()
            time.text = "${videoProgress.progress / 1000}s / ${videoProgress.duration / 1000}s"
        }
    }
}
