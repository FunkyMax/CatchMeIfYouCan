package com.example.catchmeifyoucan.Game

import com.example.catchmeifyoucan.Activities.MainActivity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

const val DELAY = 25L;

class DataController {

    private val bluetoothLeService = MainActivity.bluetoothLeService

    fun sendDataToBluetoothModule(
        playerDMXValues: String,
        randomBlueDMXValues: String,
        randomYellowDMXValues: String,
        randomRedDMXValues: String
    ) {
        GlobalScope.launch{
            delay(DELAY)
            bluetoothLeService.write(playerDMXValues)
            delay(DELAY)
            bluetoothLeService.write(randomBlueDMXValues)
            delay(DELAY)
            bluetoothLeService.write(randomYellowDMXValues)
            delay(DELAY)
            bluetoothLeService.write(randomRedDMXValues)
        }
    }
}