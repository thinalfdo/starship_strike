package com.example.shootinggame

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import kotlin.random.Random

class EneShip(private val context: Context) {
    var eneShip: Bitmap // Removed private modifier
    var xaxis: Int // Removed private modifier
    var yaxis: Int // Removed private modifier
    var eneVelocity: Int // Removed private modifier
    private val random: Random = Random

    init {
        eneShip = BitmapFactory.decodeResource(context.resources, R.drawable.rocket2)
        xaxis = 200 + random.nextInt(400)
        yaxis = 0
        eneVelocity = 14 + random.nextInt(10)
    }

    fun getEnemyBitmap(): Bitmap {
        return eneShip
    }

    fun getEnemySpaceshipWidth(): Int {
        return eneShip.width
    }

    fun getEnemySpaceshipHeight(): Int {
        return eneShip.height
    }
}
