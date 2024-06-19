package com.example.shootinggame

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory


class Shot(var context: Context, var shx: Int, var shy: Int) {
    var shoting: Bitmap

    init {
        shoting = BitmapFactory.decodeResource(
            context.resources,
            R.drawable.shot
        )
    }

    val shotWidth: Int
        get() = shoting.getWidth()
    val shotHeight: Int
        get() = shoting.getHeight()
}

