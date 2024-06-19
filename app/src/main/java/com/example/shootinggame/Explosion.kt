package com.example.shootinggame

import android.content.Context
import android.graphics.Bitmap
import android.graphics.BitmapFactory

class Explosion(context: Context, val eX: Int, val eY: Int) {

    // Use an array of nullable Bitmaps to avoid potential null checks
    public val pop: Array<Bitmap?> = Array(9) { null }

    // Use a property with backing field for explosionFrame
    public  var explosionFrame = 0

    init {
        // Load each explosion frame using with-resources for automatic closing
        pop[0] = with(context.resources) {
            BitmapFactory.decodeResource(this, R.drawable.explosion0)
        }
        pop[1] = with(context.resources) {
            BitmapFactory.decodeResource(this, R.drawable.explosion1)
        }
        pop[2] = with(context.resources) {
            BitmapFactory.decodeResource(this, R.drawable.explosion2)
        }
        pop[3] = with(context.resources) {
            BitmapFactory.decodeResource(this, R.drawable.explosion3)
        }
        pop[4] = with(context.resources) {
            BitmapFactory.decodeResource(this, R.drawable.explosion4)
        }
        pop[5] = with(context.resources) {
            BitmapFactory.decodeResource(this, R.drawable.explosion5)
        }
        pop[6] = with(context.resources) {
            BitmapFactory.decodeResource(this, R.drawable.explosion6)
        }
        pop[7] = with(context.resources) {
            BitmapFactory.decodeResource(this, R.drawable.explosion7)
        }
        // ... repeat for remaining frames ...
        pop[8] = with(context.resources) {
            BitmapFactory.decodeResource(this, R.drawable.explosion8)
        }
    }

    fun getExplosion(explosionFrame: Int): Bitmap? {
        // Return the Bitmap at the requested frame, or null if out of bounds
        return if (explosionFrame in 0..pop.lastIndex) {
            pop[explosionFrame]
        } else {
            null
        }
    }
}