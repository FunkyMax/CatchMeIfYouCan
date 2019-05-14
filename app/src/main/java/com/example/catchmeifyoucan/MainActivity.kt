package com.example.catchmeifyoucan

import android.Manifest
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.view.Window
import android.view.WindowManager
import io.github.controlwear.virtual.joystick.android.JoystickView
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(){
    val gameController = GameController()
    val bluetoothService = BluetoothService()
    val blackBallHandler = Handler()
    val greenBallHandler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_main)
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), 1)

        val joystick = findViewById<JoystickView>(R.id.joystickView)
        joystick.alpha = .35f
        blackBallRunnable.run()
        greenBallRunnable.run()
    }

    private val blackBallRunnable = object : Runnable {
        override fun run() {
            gameController.moveBlackBallRandomly(blackBallView)
            blackBallHandler.postDelayed(this, 800)
        }
    }

    private val greenBallRunnable = object : Runnable {
        override fun run() {
            gameController.moveGreenBallWithJoystick(joystickView, greenCircleView)
            gameController.busted(greenCircleView, blackBallView)
            greenBallHandler.postDelayed(this, 17)

        }
    }
}
