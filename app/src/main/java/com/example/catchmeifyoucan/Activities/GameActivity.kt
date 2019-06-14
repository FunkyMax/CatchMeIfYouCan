package com.example.catchmeifyoucan.Activities

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.Window
import com.example.catchmeifyoucan.Bluetooth.BluetoothLeService
import com.example.catchmeifyoucan.Game.DataController
import com.example.catchmeifyoucan.Game.GameController
import com.example.catchmeifyoucan.Game.ViewsCoordinatesTranslator
import com.example.catchmeifyoucan.R
import kotlinx.android.synthetic.main.game.*
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class GameActivity : AppCompatActivity(){

    // Making the BluetoothLeService a "static" field because the same instance is gonna be needed in a few classes. Further the BluetoothLeService can only be initialized in GameActivity because getSystemService() can only be called from here.
    companion object{
        lateinit var bluetoothLeService: BluetoothLeService
        lateinit var gameController: GameController
        lateinit var dataController: DataController
    }

    // We need a reference to a BluetoothAdapter in here since initializing the BluetoothLeService takes place in GameActivity. See above for more info.
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
        setContentView(R.layout.game)
        window.decorView.apply {
            systemUiVisibility =
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        }
    }

    override fun onStart() {
        super.onStart()
        setupBluetoothConnection()
    }

    override fun onResume() {
        super.onResume()
        randomBlueHeadlightBeamViewRunnable.run()
        randomYellowHeadlightBeamViewRunnable.run()
        randomYellowHeadlightBeamViewVisibilityRunnable.run()
        randomRedHeadlightBeamViewRunnable.run()
        viewsCoordinatesTranslatorRunnable.run();
        GlobalScope.launch {
            delay(100)
            playerHeadlightBeamViewRunnable.run()
        }
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
            viewsCoordinatesTranslatorHandler.postDelayed(this, 50)
        }
    }

    private fun setupBluetoothConnection(){
        val bluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothAdapter = bluetoothManager.adapter
        bluetoothLeService = BluetoothLeService(bluetoothManager)
        if (bluetoothLeService.initialize()) {
            dataController = DataController()
            gameController = GameController()
            viewsCoordinatesTranslator = ViewsCoordinatesTranslator()
        }
    }
}