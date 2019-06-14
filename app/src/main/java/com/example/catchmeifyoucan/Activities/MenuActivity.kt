package com.example.catchmeifyoucan.Activities

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.Window
import com.example.catchmeifyoucan.R

class MenuActivity : AppCompatActivity() {

    private lateinit var mediaPlayer : MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.menu)
        window.decorView.apply {
            systemUiVisibility =
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        }
        mediaPlayer = MediaPlayer.create(this, R.raw.catch_me_if_you_can_main_theme)
        mediaPlayer.start()
    }

    fun onButtonClicked(view: View) {
        val startGameActivity = Intent(this, GameActivity::class.java)
        startActivity(startGameActivity)
    }
}