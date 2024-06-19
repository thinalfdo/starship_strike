package com.example.shootinggame

import android.content.Intent
import android.os.Bundle
import android.widget.ImageButton
import androidx.activity.enableEdgeToEdge
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat

class startscreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_startscreen)
        val move = findViewById<ImageButton>(R.id.imageButton3)

        move.setOnClickListener {

            val intent = Intent(this, Starting::class.java)
            startActivity(intent)
        }

    }
}