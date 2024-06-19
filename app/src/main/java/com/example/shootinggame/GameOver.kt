package com.example.shootinggame

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class GameOver : AppCompatActivity() {

    private lateinit var tvScore: TextView
    private lateinit var tvHighScore: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.gameover)
        val points = intent.extras!!.getInt("points")
        tvScore = findViewById(R.id.tvPoints)
        tvHighScore = findViewById(R.id.tvHighScore)
        tvScore.text = points.toString()

        checkAndSetHighScore(points)
    }

    private fun checkAndSetHighScore(points: Int) {
        val prefs = getSharedPreferences("game_prefs", MODE_PRIVATE)
        val highScore = prefs.getInt("highScore", 0)

        if (points > highScore) {
            prefs.edit().putInt("highScore", points).apply()
            tvHighScore.text = "High Score: $points"
        } else {
            tvHighScore.text = "High Score: $highScore"
        }
    }

    fun restart(view: View) {
        val intent = Intent(this, Starting::class.java)
        startActivity(intent)
        finish()
    }

    fun exit(view: View) {
        finish()
    }
}

