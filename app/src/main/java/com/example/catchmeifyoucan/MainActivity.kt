package com.example.catchmeifyoucan

import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.view.Window
import android.view.WindowManager
import io.github.controlwear.virtual.joystick.android.JoystickView
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(){
    val handler = Handler()
    val gameController = GameController()


    private val runnable = object : Runnable {
        override fun run() {
            gameController.moveBlackBallRandomly(blackBall)
            handler.postDelayed(this, 700)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_main)
        val joystick = findViewById<JoystickView>(R.id.joystickView)
        joystick.setOnMoveListener { angle, strength ->
            angleID.text = "angle: " + angle
            strengthID.text = "strength: " + strength
        }
        //joystick.alpha = 0f
        runnable.run()
    }
}
