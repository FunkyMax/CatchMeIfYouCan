package com.example.catchmeifyoucan.Services

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.os.VibrationEffect
import android.os.Vibrator

class VibratorService(context: Context) : Service() {

    private val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
    private val vibratePatternHighscore = longArrayOf(80, 80, 150, 800)

    fun collisionVibration() {
        vibrator.vibrate(VibrationEffect.createOneShot(300, 200))
    }

    fun highscoreVibration() {
        vibrator.vibrate(VibrationEffect.createWaveform(vibratePatternHighscore, -1))
    }

    override fun onBind(p0: Intent?): IBinder? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}