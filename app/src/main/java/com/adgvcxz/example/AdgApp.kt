package com.adgvcxz.example

import android.app.Application
import com.squareup.leakcanary.LeakCanary

/**
 * zhaowei
 * Created by zhaowei on 2017/3/12.
 */

class AdgApp : Application() {
    override fun onCreate() {
        super.onCreate()
        LeakCanary.install(this)
    }
}