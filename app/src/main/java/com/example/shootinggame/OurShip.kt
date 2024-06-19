package com.example.shootinggame

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import kotlin.random.Random

class OurShip(context: Context) {
    val context: Context // Removed private modifier
    var ourShip: Bitmap // Removed private modifier
    var xaxis: Int // Changed to var
    var yaxis: Int // Changed to var
    val random: Random

    init {
        this.context = context
        ourShip = BitmapFactory.decodeResource(context.resources, R.drawable.enemy)
        random = Random.Default
        // Ensure SpaceShooter.screenWidth is already initialized before accessing it
        xaxis = if (ShooterAction.screenWidth > 0) {
            random.nextInt(ShooterAction.screenWidth)
        } else {
            // Handle the case when screenWidth is not positive
            // For example, set ox to a default value
            0
        }
        yaxis = ShooterAction.screenHeight - ourShip.height
    }

    fun getOurBitmap(): Bitmap {
        return ourShip
    }

    fun getOurSpaceshipWidth(): Int {
        return ourShip.width
    }
}
