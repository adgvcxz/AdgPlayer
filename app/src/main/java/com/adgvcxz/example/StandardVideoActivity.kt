package com.adgvcxz.example

import android.os.Bundle
import com.adgvcxz.adgplayer.widget.AdgVideoView

/**
 * zhaowei
 * Created by zhaowei on 2017/3/22.
 */
class StandardVideoActivity : BaseVideoActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_standard)
        val videoView = findViewById(R.id.video_view) as AdgVideoView
        videoView.bindPlayer()
        videoView.prepare("http://9890.vod.myqcloud.com/9890_4e292f9a3dd011e6b4078980237cc3d3.f20.mp4")
    }
}