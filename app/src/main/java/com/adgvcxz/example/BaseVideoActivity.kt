package com.adgvcxz.example

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.adgvcxz.adgplayer.AdgVideoPlayer
import com.adgvcxz.adgplayer.extensions.destroy

/**
 * zhaowei
 * Created by zhaowei on 2017/3/21.
 */

open class BaseVideoActivity: AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        AdgVideoPlayer.instance.init(this)
    }

    override fun onDestroy() {
        super.onDestroy()
        AdgVideoPlayer.instance.destroy()
        AdgVideoPlayer.instance.unBindView()
    }
}
