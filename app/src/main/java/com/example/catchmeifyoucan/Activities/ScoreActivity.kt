package com.example.catchmeifyoucan.Activities

import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.Window
import com.example.catchmeifyoucan.Game.EMPTY_STRING
import com.example.catchmeifyoucan.R
import com.example.catchmeifyoucan.Services.VibratorService
import kotlinx.android.synthetic.main.score.*

class ScoreActivity: AppCompatActivity() {

    companion object{
        private var overallHighScore = 0
    }

    private lateinit var vibratorService: VibratorService
    private lateinit var mediaPlayer: MediaPlayer

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.score)
        window.decorView.apply {
            systemUiVisibility =
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        }
        vibratorService = VibratorService(this)
    }

    override fun onResume() {
        super.onResume()
        val extra = intent.getStringExtra("score")
        score.text = extra.toString()
        if (extra.toInt() > overallHighScore) {
            mediaPlayer = MediaPlayer.create(this, R.raw.new_highscore_sfx)
            mediaPlayer.setVolume(.1f, .1f)
            mediaPlayer.start()
            vibratorService.highscoreVibration()
            overallHighScore = extra.toInt()
            highScore.text = EMPTY_STRING
            highScoreTextView.text = "You set a new highscore!"
        } else {
            highScore.text = overallHighScore.toString()
        }
    }

    override fun onStop() {
        super.onStop()
        finish()
    }

    fun onButtonClicked(view: View){
        val startMenuActivity = Intent(this, MenuActivity::class.java)
        startMenuActivity.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)
        startMenuActivity.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP)
        startActivity(startMenuActivity)
    }
}