package com.adgvcxz.example

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.adgvcxz.adgplayer.widget.AdgVideoView

/**
 * zhaowei
 * Created by zhaowei on 2017/3/10.
 */

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        val videoView = findViewById(R.id.video_view) as AdgVideoView
        videoView.start("http://9890.vod.myqcloud.com/9890_4e292f9a3dd011e6b4078980237cc3d3.f20.mp4")
    }

}
