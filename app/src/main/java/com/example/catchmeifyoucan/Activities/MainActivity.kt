package com.example.catchmeifyoucan.Activities

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.Window
import android.widget.CheckBox
import com.example.catchmeifyoucan.Bluetooth.BluetoothLeService
import com.example.catchmeifyoucan.Game.GameController
import com.example.catchmeifyoucan.Game.ViewsCoordinatesTranslator
import com.example.catchmeifyoucan.R
import kotlinx.android.synthetic.main.activity_main.*

private const val REQUEST_PERMISSION_ACCESS_FINE_LOCATION = 1

class MainActivity : AppCompatActivity(){

    // Making the BluetoothLeService a "static" field because the same instance is gonna be needed in a few classes. Further the BluetoothLeService can only be initialized in MainActivity because getSystemService() can only be called from here.
    companion object{
        lateinit var bluetoothLeService: BluetoothLeService
        val gameController = GameController()
    }

    // We need a reference to a BluetoothAdapter in here since initializing the BluetoothLeService takes place in MainActivity. See above for more info.
    private lateinit var bluetoothAdapter : BluetoothAdapter
    private lateinit var viewsCoordinatesTranslator: ViewsCoordinatesTranslator

    // Initializing the necessary Handlers
    private val playerHeadlightBeamViewHandler = Handler()
    private val randomHeadlightBeamViewsHandler = Handler()
    private val randomYellowHeadlightBeamViewVisibilityHandler = Handler()
    private val viewsCoordinatesTranslatorHandler = Handler()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.activity_main)
        window.decorView.apply {
            systemUiVisibility =
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        }
        //window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN)
        //requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE
        //ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), REQUEST_PERMISSION_ACCESS_FINE_LOCATION)

        setupBluetoothConnection()
        playerHeadlightBeamViewRunnable.run()
        randomBlueHeadlightBeamViewRunnable.run()
        randomYellowHeadlightBeamViewRunnable.run()
        randomYellowHeadlightBeamViewVisibilityRunnable.run()
        randomRedHeadlightBeamViewRunnable.run()
        viewsCoordinatesTranslatorRunnable.run();
    }

    private val playerHeadlightBeamViewRunnable = object : Runnable {
        override fun run() {
            gameController.movePlayerHeadlightBeamViewWithJoystick(joystickView, playerHeadlightBeamView)
            gameController.collisionDetection(
                playerHeadlightBeamView,
                randomBlueHeadlightBeamView,
                randomYellowHeadlightBeamView,
                randomRedHeadlightBeamView
            )
            playerHeadlightBeamViewHandler.postDelayed(this, 17)
        }
    }

    private val randomBlueHeadlightBeamViewRunnable = object : Runnable {
        override fun run() {
            gameController.moveRandomHeadlightBeamView(randomBlueHeadlightBeamView)
            randomHeadlightBeamViewsHandler.postDelayed(this, 1300)
        }
    }

    private val randomYellowHeadlightBeamViewRunnable = object : Runnable {
        override fun run() {
            gameController.moveRandomHeadlightBeamView(randomYellowHeadlightBeamView)
            randomHeadlightBeamViewsHandler.postDelayed(this, 1000)
        }
    }

    private val randomRedHeadlightBeamViewRunnable = object : Runnable {
        override fun run() {
            gameController.moveRandomHeadlightBeamView(randomRedHeadlightBeamView)
            randomHeadlightBeamViewsHandler.postDelayed(this, 1100)
        }
    }

    private val randomYellowHeadlightBeamViewVisibilityRunnable = object : Runnable {
        override fun run() {
            val visibility: Int = if (randomYellowHeadlightBeamView.visibility == 0) 8 else 0
            randomYellowHeadlightBeamView.visibility = visibility
            randomHeadlightBeamViewsHandler.postDelayed(this, 11000)
        }
    }

    private val viewsCoordinatesTranslatorRunnable = object : Runnable {
        override fun run() {
            viewsCoordinatesTranslator.translateCoordinatesAndSendToBluetoothModule()
            viewsCoordinatesTranslatorHandler.postDelayed(this, 100)
        }
    }

    private fun setupBluetoothConnection(){
        val bluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothAdapter = bluetoothManager.adapter
        bluetoothLeService = BluetoothLeService(bluetoothManager)
        if (bluetoothLeService.initialize()) {
            viewsCoordinatesTranslator = ViewsCoordinatesTranslator()
        }
    }

    fun onLedClicked(view: View){
        if (view is CheckBox){
            if (view.isChecked){
                viewsCoordinatesTranslator.translateCoordinatesAndSendToBluetoothModule()
                //bluetoothLeService.write("1")
            }
            else {
                viewsCoordinatesTranslator.translateCoordinatesAndSendToBluetoothModule()
                //bluetoothLeService.write("0")
            }
        }
    }
}
