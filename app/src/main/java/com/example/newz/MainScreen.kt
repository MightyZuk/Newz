package com.example.newz

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import androidx.appcompat.app.AppCompatActivity

class MainScreen : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        supportActionBar?.hide()
        window?.statusBarColor = this.resources.getColor(R.color.dark_purple)
        setContentView(R.layout.activity_main_screen)

        Handler().postDelayed({
            val intent = Intent(this@MainScreen,MainActivity::class.java)
            startActivity(intent)
            finish()
        },500)
    }
}