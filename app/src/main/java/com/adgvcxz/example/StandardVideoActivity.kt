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
        videoView.prepare("http://staticfile.cxylg.com/94NWfqRSWgta-SCVideo.2.mp4")
    }
}