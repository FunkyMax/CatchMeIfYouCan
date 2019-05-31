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
import com.example.catchmeifyoucan.Game.DataController
import com.example.catchmeifyoucan.Game.GameController
import com.example.catchmeifyoucan.R
import kotlinx.android.synthetic.main.activity_main.*

const val REQUEST_PERMISSION_ACCESS_FINE_LOCATION = 1

class MainActivity : AppCompatActivity(){

    // Making the BluetoothLeService a "static" field because the same instance is gonna be needed in a few classes. Further the BluetoothLeService can only be initialized in MainActivity because getSystemService() can only be called in here.
    companion object{
        lateinit var bluetoothLeService: BluetoothLeService
        val gameController = GameController()

        fun getBluetoothService(): BluetoothLeService{
            return bluetoothLeService
        }
    }

    // We need a reference to a BluetoothAdapter in here since initializing the BluetoothLeService takes place in MainActivity. See above for more info.
    private lateinit var bluetoothAdapter : BluetoothAdapter

    //private val gameController = GameController()
    private lateinit var dataController: DataController    // lateinit var because it cannot be initialized here because bluetoothLeService hasn't been initialized so far and the DataController needs the bluetoothLeService

    // Initializing the necessary Handlers
    private val playerHeadlightBeamViewHandler = Handler()
    private val randomHeadlightBeamViewHandler = Handler()
    private val dataControllerHandler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        setContentView(R.layout.activity_main)
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_PERMISSION_ACCESS_FINE_LOCATION)
        joystickView.alpha = .35f

        setupBluetoothConnection()
        randomHeadlightBeamViewRunnable.run()
        playerHeadlightBeamViewRunnable.run()
    }

    private val playerHeadlightBeamViewRunnable = object : Runnable {
        override fun run() {
            gameController.movePlayerHeadlightBeamViewWithJoystick(joystickView, playerHeadlightBeamView)
            gameController.collisionDetection(playerHeadlightBeamView, randomHeadlightBeamView)
            playerHeadlightBeamViewHandler.postDelayed(this, 17)
        }
    }

    private val randomHeadlightBeamViewRunnable = object : Runnable {
        override fun run() {
            gameController.moveRandomHeadlightBeamView(randomHeadlightBeamView)
            randomHeadlightBeamViewHandler.postDelayed(this, 800)
        }
    }

    private val dataControllerRunnable = object : Runnable {
        override fun run() {
            //dataController.sendDataToBluetoothModule()
            dataControllerHandler.postDelayed(this, 500)
        }
    }

    private fun setupBluetoothConnection(){
        val bluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothAdapter = bluetoothManager.adapter
        bluetoothLeService = BluetoothLeService(bluetoothManager)
        if (bluetoothLeService.initialize()) {
            dataController = DataController()
            dataControllerRunnable.run()
        }
    }

    fun onLedClicked(view: View){
        if (view is CheckBox){
            if (view.isChecked){
                //bluetoothLeService.write("1")
                dataController.sendDataToBluetoothModule()
            }
            else {
                //bluetoothLeService.write("0")
                dataController.sendDataToBluetoothModule()
            }
        }
    }
}
