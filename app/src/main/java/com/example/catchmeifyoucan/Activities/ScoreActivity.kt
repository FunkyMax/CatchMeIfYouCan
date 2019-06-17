package com.example.catchmeifyoucan.Activities

import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.Window
import com.example.catchmeifyoucan.R
import kotlinx.android.synthetic.main.score.*

class ScoreActivity: AppCompatActivity() {

    companion object{
        private var overallHighScore = -100
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.score)
        window.decorView.apply {
            systemUiVisibility =
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        }
    }

    override fun onResume() {
        super.onResume()
        val extra = intent.getStringExtra("score")
        score.text = extra.toString()
        if (extra.toInt() > overallHighScore) overallHighScore = extra.toInt()
        highScore.text = overallHighScore.toString()
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