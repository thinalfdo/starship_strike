package com.example.shootinggame

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity


class Starting : AppCompatActivity() {
    public override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.startup)
    }

    fun startGame(view: View?) {
        startActivity(Intent(this, MainActivity::class.java))
        finish()
    }
}

