package com.example.catchmeifyoucan.Game

import com.example.catchmeifyoucan.Activities.MainActivity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

const val DELAY = 50L;

class DataController {

    private val bluetoothLeService = MainActivity.bluetoothLeService

    fun sendDataToBluetoothModule(playerDMXValuesArray: ByteArray, randomDMXValuesArray: ByteArray) {
        GlobalScope.launch{
            delay(DELAY)
            bluetoothLeService.write(playerDMXValuesArray)
            delay(DELAY)
            bluetoothLeService.write(randomDMXValuesArray)
        }
    }
}