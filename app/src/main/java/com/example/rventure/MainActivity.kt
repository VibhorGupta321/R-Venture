package com.example.rventure

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import android.view.animation.AnimationUtils
import android.widget.ImageView
import android.widget.TextView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_main)

        val leftAnim = AnimationUtils.loadAnimation(this, R.anim.left_animation)
        val bottomAnim = AnimationUtils.loadAnimation(this, R.anim.bottom_animation)
        val logo = findViewById<ImageView>(R.id.cycle_image)
        val title = findViewById<TextView>(R.id.title_text)
        val slogan = findViewById<TextView>(R.id.tagline_text)
        logo.startAnimation(leftAnim)
        title.startAnimation(bottomAnim)
        slogan.startAnimation(bottomAnim)

        val splashTimeout = 5000
        val homeIntent = Intent(this@MainActivity, Login::class.java)
                Handler().postDelayed({
                    startActivity(homeIntent)
                    finish()
                                      },splashTimeout.toLong())
    }
}