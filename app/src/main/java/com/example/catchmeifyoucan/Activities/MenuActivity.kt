package com.example.catchmeifyoucan.Activities

import android.bluetooth.BluetoothAdapter
import android.bluetooth.BluetoothManager
import android.content.Context
import android.content.Intent
import android.media.MediaPlayer
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.Window
import android.widget.ImageView
import com.example.catchmeifyoucan.R
import com.example.catchmeifyoucan.Services.BluetoothLeService


class MenuActivity : AppCompatActivity() {

    // Making BluetoothLeService a "static" field because the same instance is going to be needed in DataController class.
    companion object{
        lateinit var bluetoothLeService: BluetoothLeService
    }

    private var mediaPlayer = MediaPlayer()
    private lateinit var bluetoothView : ImageView

    // We need a reference to a BluetoothAdapter in here since initialization of BluetoothLeService takes place in MenuActivity.
    private lateinit var bluetoothAdapter : BluetoothAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        requestWindowFeature(Window.FEATURE_NO_TITLE)
        setContentView(R.layout.menu)
        window.decorView.apply {
            systemUiVisibility =
                View.SYSTEM_UI_FLAG_HIDE_NAVIGATION or View.SYSTEM_UI_FLAG_FULLSCREEN or View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY
        }

        bluetoothView = findViewById(R.id.bt_View)
        setupBluetoothConnection()

        playMusic()
        //mediaPlayer.isLooping = true
    }

    fun onButtonClicked(view: View) {
        val startGameActivity = Intent(this, GameActivity::class.java)
        startActivity(startGameActivity)
    }

    private fun setupBluetoothConnection(){
        val bluetoothManager = getSystemService(Context.BLUETOOTH_SERVICE) as BluetoothManager
        bluetoothAdapter = bluetoothManager.adapter
        bluetoothLeService = BluetoothLeService(bluetoothManager)
        if (bluetoothLeService.initialize()) {
            bluetoothView.setImageResource(R.mipmap.bt_connected)
        }
    }

    private fun playMusic(){

        // https://stackoverflow.com/questions/30681002/play-list-of-mp3-file-with-mediaplayer-in-android

        val musicArray = arrayOf(R.raw.catch_me_if_you_can_main_theme, R.raw.bensound_jazzcomedy, R.raw.bensound_jazzyfrenchy, R.raw.bensound_thejazzpiano)
        var index = 0
        mediaPlayer = MediaPlayer.create(this, musicArray[index])
        mediaPlayer.start()
        mediaPlayer.setOnCompletionListener{
            println("NEXT TRACK")
            if (index == musicArray.size - 1) index  = 0
            else index += 1
            mediaPlayer.release()
            mediaPlayer = MediaPlayer.create(this, musicArray[index])
        }
    }
}