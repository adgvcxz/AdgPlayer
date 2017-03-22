package com.adgvcxz.example

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.ViewGroup
import com.adgvcxz.adgplayer.AdgVideoPlayer
import com.adgvcxz.adgplayer.widget.AdgBaseVideoView

/**
 * zhaowei
 * Created by zhaowei on 2017/3/21.
 */

class MusicalActivity : BaseVideoActivity() {

    private lateinit var recyclerView: RecyclerView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
//        recyclerView = LayoutInflater.from(this).inflate(R.layout.activity_musical, null, false) as RecyclerView
//        setContentView(recyclerView)
//        recyclerView.layoutManager = LinearLayoutManager(this)
//        recyclerView.adapter = MusicalAdapter()
    }

    inner class MusicalAdapter : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
            val view = AdgBaseVideoView(parent.context)
            view.layoutParams = ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT)
            return object : RecyclerView.ViewHolder(view) {}
        }

        override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
            holder.itemView.setOnClickListener {
                AdgVideoPlayer.instance.bindView(holder.itemView as AdgBaseVideoView)
                AdgVideoPlayer.instance.prepare("http://9890.vod.myqcloud.com/9890_4e292f9a3dd011e6b4078980237cc3d3.f20.mp4")
            }
        }

        override fun getItemCount(): Int = 20

    }
}
