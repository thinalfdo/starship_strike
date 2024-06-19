package com.example.shootinggame

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Point
import android.os.Handler
import android.view.MotionEvent
import android.view.View
import java.util.Random


class ShooterAction(var appcontext: Context) : View(appcontext) {


    var background: Bitmap
    var lifeLines: Bitmap
    var rechandler: Handler?
    var UPDATE_MILLIS: Long = 30
    var score = 0
    var noOfLives = 3


    var scorePaint: Paint
    var textSize = 60
    var paused = false
    var ourship: OurShip = OurShip(context)
    var eneship: EneShip = EneShip(context)

    var random: Random
    var eneShots: ArrayList<Shot>
    var ourShots: ArrayList<Shot>
    var pop: Explosion? = null
    var pops: ArrayList<Explosion>
    var eneShotAction = false
    val runnable = Runnable { invalidate() }

    init {

        val display = (context as Activity).windowManager.defaultDisplay
        val size = Point()
        display.getSize(size)
        screenWidth = size.x
        screenHeight = size.y
        random = Random()
        eneShots = ArrayList()
        ourShots = ArrayList()
        pops = ArrayList()
        ourship = OurShip(context)
        eneship = EneShip(context)

        rechandler = Handler()
        background = BitmapFactory.decodeResource(context.resources, R.drawable.back4)
        lifeLines = BitmapFactory.decodeResource(context.resources, R.drawable.life)
        scorePaint = Paint()
        scorePaint.setColor(Color.RED)
        scorePaint.textSize = textSize.toFloat()
        scorePaint.textAlign = Paint.Align.LEFT
        var ex: Int = 0
    }

    override fun onDraw(canvas: Canvas) {
        // Draw background, Points and life on Canvas
        canvas.drawBitmap(background, 0f, 0f, null)
        canvas.drawText("Score: $score", 1f, textSize.toFloat(), scorePaint)
        for (i in noOfLives downTo 1) {
            canvas.drawBitmap(
                lifeLines,
                (screenWidth - lifeLines.getWidth() * i).toFloat(),
                0f,
                null
            )
        }
        // When life becomes 0, stop game and launch GameOver Activity with points
        if (noOfLives <= 0) {
            paused = true
            rechandler = null
            val intent = Intent(context, GameOver::class.java)
            intent.putExtra("points", score)
            context.startActivity(intent)
            (context as Activity).finish()
        }
        // Move enemySpaceship
        eneship.xaxis += eneship.eneVelocity
        // If enemySpaceship collides with right wall, reverse enemyVelocity
        if (eneship.xaxis + eneship.getEnemySpaceshipWidth() >= screenWidth) {
            eneship.eneVelocity *= -1
        }
        // If enemySpaceship collides with left wall, again reverse enemyVelocity
        if (eneship.xaxis <= 0) {
            eneship.eneVelocity *= -1
        }
        // Till enemyShotAction is false, ourShuttle should fire shots from random travelled distance
        if (eneShotAction == false) {
            if (eneship.xaxis >= 200 + random.nextInt(400)) {
                val enemyShot = Shot(
                    context,
                    eneship.xaxis + eneship.getEnemySpaceshipWidth() / 2,
                    eneship.yaxis
                )
                eneShots.add(enemyShot)
                // We're making enemyShotAction to true so that ourShuttle can take a short at a time
                eneShotAction = true
            }
            eneShotAction = if (eneship.xaxis >= 400 + random.nextInt(800)) {
                val enemyShot = Shot(
                    context,
                    eneship.xaxis + eneship.getEnemySpaceshipWidth() / 2,
                    eneship.yaxis
                )
                eneShots.add(enemyShot)
                // We're making enemyShotAction to true so that ourShuttle can take a shot at a time
                true
            } else {
                val enemyShot = Shot(
                    context,
                    eneship.xaxis + eneship.getEnemySpaceshipWidth() / 2,
                    eneship.yaxis
                )
                eneShots.add(enemyShot)
                // We're making enemyShotAction to true so that ourShuttle can take a short at a time
                true
            }
        }
        // Draw the ourShuttle Spaceship
        canvas.drawBitmap(
            eneship.getEnemyBitmap(),
            eneship.xaxis.toFloat(),
            eneship.yaxis.toFloat(),
            null
        )
        // Draw our spaceship between the left and right edge of the screen
        if (ourship.xaxis > screenWidth - ourship.getOurSpaceshipWidth()) {
            ourship.xaxis = screenWidth - ourship.getOurSpaceshipWidth()
        } else if (ourship.xaxis < 0) {
            ourship.xaxis = 0
        }
        // Draw our Spaceship
        canvas.drawBitmap(
            ourship.getOurBitmap(),
            ourship.xaxis.toFloat(),
            ourship.yaxis.toFloat(),
            null
        )
        // Draw the ourShuttle shot downwards our spaceship and if it's being hit, decrement life, remove
        // the shot object from enemyShots ArrayList and show an explosion.
        // Else if, it goes away through the bottom edge of the screen also remove
        // the shot object from enemyShots.
        // When there is no enemyShots no the screen, change enemyShotAction to false, so that ourShuttle
        var spaceshipHit = false // Flag to track if spaceship is hit in this loop

        for (i in eneShots.size - 1 downTo 0) {
            eneShots[i].shy += 15
            canvas.drawBitmap(
                eneShots[i].shoting,
                eneShots[i].shx.toFloat(),
                eneShots[i].shy.toFloat(),
                null
            )
            if (eneShots[i].shx >= ourship.xaxis && eneShots[i].shx <= ourship.xaxis + ourship.getOurSpaceshipWidth() && eneShots[i].shy >= ourship.yaxis && eneShots[i].shy <= screenHeight) {
                if (!spaceshipHit) { // Check if spaceship has already been hit in this loop
                    noOfLives-- // Reduce life only if spaceship hasn't been hit before in this loop
                    spaceshipHit = true // Mark spaceship as hit
                }
                eneShots.removeAt(i)
                pop = Explosion(context, ourship.xaxis, ourship.yaxis)
                pops.add(pop!!)
            } else if (eneShots[i].shy >= screenHeight) {
                eneShots.removeAt(i)
            }
            if (eneShots.size < 1) {
                eneShotAction = false
            }
        }
        // Draw our spaceship shots towards the ourShuttle. If there is a collision between our shot and ourShuttle
        // spaceship, increment points, remove the shot from ourShots and create a new Explosion object.
        // Else if, our shot goes away through the top edge of the screen also remove
        // the shot object from enemyShots ArrayList.
        for (i in ourShots.indices) {
            ourShots[i].shy -= 15
            canvas.drawBitmap(
                ourShots[i].shoting,
                ourShots[i].shx.toFloat(),
                ourShots[i].shy.toFloat(),
                null
            )
            if (ourShots[i].shx >= eneship.xaxis && ourShots[i].shx <= eneship.xaxis + eneship.getEnemySpaceshipWidth() && ourShots[i].shy <= eneship.getEnemySpaceshipWidth() && ourShots[i].shy >= eneship.yaxis) {
                score++
                ourShots.removeAt(i)
                pop = Explosion(context, eneship.xaxis, eneship.yaxis)
                pops.add(pop!!)
            } else if (ourShots[i].shy <= 0) {
                ourShots.removeAt(i)
            }
        }
        // Do the explosion
        for (i in pops.indices) {
            if (i < pops.size) {
                val explosionBitmap = pops[i].getExplosion(pops[i].explosionFrame)
                val explosionX = pops[i].eX.toFloat()
                val explosionY = pops[i].eY.toFloat()

                // Check if explosionBitmap is not null before drawing
                explosionBitmap?.let {
                    canvas.drawBitmap(it, explosionX, explosionY, null)
                }

                pops[i].explosionFrame++

                if (pops[i].explosionFrame > 8) {
                    pops.removeAt(i)
                }
            }
        }
        // If not paused, we’ll call the postDelayed() method on handler object which will cause the
        // run method inside Runnable to be executed after 30 milliseconds, that is the value inside
        // UPDATE_MILLIS.
        if (!paused) handler!!.postDelayed(runnable, UPDATE_MILLIS)
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        val touchX = event.x.toInt()
        // When event.getAction() is MotionEvent.ACTION_UP, if ourShots arraylist size < 1,
        // create a new Shot.
        // This way we restrict ourselves of making just one shot at a time, on the screen.
        if (event.action == MotionEvent.ACTION_UP) {
            if (ourShots.size < 1) {
                val ourShot = Shot(
                    context,
                    ourship.xaxis + ourship.getOurSpaceshipWidth() / 2,
                    ourship.yaxis
                )
                ourShots.add(ourShot)
            }
        }
        // When event.getAction() is MotionEvent.ACTION_DOWN, control ourSpaceship
        if (event.action == MotionEvent.ACTION_DOWN) {
            ourship.xaxis = touchX
        }
        // When event.getAction() is MotionEvent.ACTION_MOVE, control ourSpaceship
        // along with the touch.
        if (event.action == MotionEvent.ACTION_MOVE) {
            ourship.xaxis = touchX
        }
        // Returning true in an onTouchEvent() tells Android system that you already handled
        // the touch event and no further handling is required.
        return true
    }

    companion object {
        var screenWidth: Int = 0
        var screenHeight: Int = 0
    }

}

