package com.example.catchmeifyoucan.Activities

import android.Manifest
import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.ActivityCompat
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.Window
import android.view.WindowManager
import android.widget.CheckBox
import com.example.catchmeifyoucan.Bluetooth.BluetoothLeService
import com.example.catchmeifyoucan.Game.GameController
import com.example.catchmeifyoucan.R
import kotlinx.android.synthetic.main.activity_main.*


class MainActivity : AppCompatActivity(){
    private lateinit var bluetoothLeService : BluetoothLeService
    private lateinit var bluetoothAdapter : BluetoothAdapter
    private val gameController = GameController()
    private val blackBallHandler = Handler()
    private val greenBallHandler = Handler()
    private val REQUEST_PERMISSION_ACCESS_FINE_LOCATION = 1
    private val HM10_ADDRESS = "34:03:DE:37:AC:D1"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_main)
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_PERMISSION_ACCESS_FINE_LOCATION)
        joystickView.alpha = .35f

        setupBluetoothConnection()
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

    private fun setupBluetoothConnection(){
        val bluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothAdapter = bluetoothManager.adapter
        bluetoothLeService = BluetoothLeService(bluetoothManager)
        bluetoothLeService.initialize()
        bluetoothLeService.connect(HM10_ADDRESS)
    }

    fun onLedClicked(view: View){
        if (view is CheckBox){
            if (view.isChecked){
               bluetoothLeService.write("1")
            }
            else {
                bluetoothLeService.write("0")
            }
        }
    }
}
