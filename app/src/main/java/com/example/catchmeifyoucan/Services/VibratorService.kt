package com.example.catchmeifyoucan.Services

import android.app.Service
import android.content.Context
import android.content.Intent
import android.os.IBinder
import android.os.VibrationEffect
import android.os.Vibrator

class VibratorService(context: Context) : Service() {

    private val vibrator = context.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator

    fun collisionVibration() {
        vibrator.vibrate(VibrationEffect.createOneShot(300, 200))
    }

    override fun onBind(p0: Intent?): IBinder? {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }

}