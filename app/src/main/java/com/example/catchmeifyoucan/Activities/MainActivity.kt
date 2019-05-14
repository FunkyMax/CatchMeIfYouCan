package com.example.catchmeifyoucan.Activities

import android.Manifest
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.view.Window
import android.view.WindowManager
import com.example.catchmeifyoucan.Bluetooth.BluetoothService
import com.example.catchmeifyoucan.Game.GameController
import com.example.catchmeifyoucan.R
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(){
    private val gameController = GameController()
    private val bluetoothService = BluetoothService()
    private val blackBallHandler = Handler()
    private val greenBallHandler = Handler()
    private val REQUEST_PERMISSION_ACCESS_FINE_LOCATION = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_main)
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_PERMISSION_ACCESS_FINE_LOCATION)
        joystickView.alpha = .35f

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
