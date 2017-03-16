package com.adgvcxz.example

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.widget.Button
import android.widget.SeekBar
import android.widget.TextView
import com.adgvcxz.adgplayer.PlayerStatus
import com.adgvcxz.adgplayer.ScreenOrientation
import com.adgvcxz.adgplayer.widget.AdgVideoView

/**
 * zhaowei
 * Created by zhaowei on 2017/3/10.
 */

class MainActivity : AppCompatActivity(), SeekBar.OnSeekBarChangeListener {

    private lateinit var videoView: AdgVideoView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        videoView = findViewById(R.id.video_view) as AdgVideoView
        val start = findViewById(R.id.start) as Button
        val seekBar = findViewById(R.id.seek_bar) as SeekBar
        val brightness = findViewById(R.id.brightness) as SeekBar
        val time = findViewById(R.id.time) as TextView
        val progressBar = findViewById(R.id.progress_bar)
        start.setOnClickListener {
            if (videoView.isPlaying()) {
                videoView.pause()
                start.setText(R.string.start)
            } else {
                videoView.start()
                start.setText(R.string.pause)
            }
        }

        findViewById(R.id.mute).setOnClickListener {
            if (videoView.volume == 1F) {
                videoView.volume = 0F
            } else {
                videoView.volume = 1F
            }
        }

        findViewById(R.id.horizontal).setOnClickListener {
            if (videoView.screenOrientation != ScreenOrientation.Portrait) {
                videoView.screenOrientation = ScreenOrientation.Portrait
            } else {
                videoView.screenOrientation = ScreenOrientation.Landscape
            }
        }

        seekBar.setOnSeekBarChangeListener(this)
        brightness.setOnSeekBarChangeListener(this)
        videoView.progressListener = {
            seekBar.progress = (it.progress.toDouble() / it.duration * 100).toInt()
            time.text = "${it.progress / 1000}s / ${videoView.duration / 1000}s"
        }

        videoView.bufferListener = {
            seekBar.secondaryProgress = it
        }

        videoView.statusObservable.subscribe {
            if (it == PlayerStatus.Prepared) {
                time.text = "${videoView.duration / 1000}s"
            }
            if (it == PlayerStatus.Buffering || it == PlayerStatus.Preparing) {
                progressBar.visibility = View.VISIBLE
            } else {
                progressBar.visibility = View.GONE
            }
        }

        videoView.start("http://9890.vod.myqcloud.com/9890_4e292f9a3dd011e6b4078980237cc3d3.f20.mp4")
        brightness.progress = (videoView.brightness * 100).toInt()

    }


    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
    }

    override fun onStartTrackingTouch(seekBar: SeekBar) {
        when (seekBar.id) {
            R.id.brightness -> videoView.brightness = seekBar.progress.toFloat() / 100
            R.id.progress_bar -> videoView.seekTo(seekBar.progress)
        }
    }

    override fun onStopTrackingTouch(seekBar: SeekBar) {
        when (seekBar.id) {
            R.id.brightness -> videoView.brightness = seekBar.progress.toFloat() / 100
            R.id.progress_bar -> videoView.seekTo(seekBar.progress)
        }

    }

    override fun onDestroy() {
        super.onDestroy()
        videoView.onDestroy()
    }
}
