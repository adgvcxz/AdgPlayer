package com.adgvcxz.adgplayer

import android.view.TextureView

/**
 * zhaowei
 * Created by zhaowei on 2017/3/20.
 */

interface IAdgVideoView {

    fun setSurfaceTextureListener(listener: TextureView.SurfaceTextureListener)

    fun recreate()

}
