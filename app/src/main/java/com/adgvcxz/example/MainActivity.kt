package com.adgvcxz.example

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity

/**
 * zhaowei
 * Created by zhaowei on 2017/3/10.
 */

class MainActivity : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        findViewById(R.id.single_video).setOnClickListener {
            startActivity(Intent(this, SingleVideoActivity::class.java))
        }
        findViewById(R.id.musical_ly).setOnClickListener {
            startActivity(Intent(this, MusicalActivity::class.java))
        }
    }
}
