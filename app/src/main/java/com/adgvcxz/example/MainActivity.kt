package com.adgvcxz.example

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Button
import android.widget.SeekBar
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
        videoView.start("http://9890.vod.myqcloud.com/9890_4e292f9a3dd011e6b4078980237cc3d3.f20.mp4")
        val start = findViewById(R.id.start) as Button
        val seekBar = findViewById(R.id.seek_bar) as SeekBar
        start.setOnClickListener {
            if (videoView.isPlaying()) {
                videoView.pause()
                start.setText(R.string.start)
            } else {
                videoView.start()
                start.setText(R.string.pause)
            }
        }
        seekBar.setOnSeekBarChangeListener(this)
        videoView.progressObservable.subscribe {
            Log.e("zhaow", "$it    ====")
        }
    }


    override fun onProgressChanged(seekBar: SeekBar?, progress: Int, fromUser: Boolean) {
    }

    override fun onStartTrackingTouch(seekBar: SeekBar?) {
    }

    override fun onStopTrackingTouch(seekBar: SeekBar?) {
        videoView.seekTo(seekBar!!.progress)
    }

    override fun onDestroy() {
        super.onDestroy()
        videoView.onDestroy()
    }
}
