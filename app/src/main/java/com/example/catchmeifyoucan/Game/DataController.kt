package com.example.catchmeifyoucan.Game

import com.example.catchmeifyoucan.Activities.MainActivity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

const val DELAY = 25L;

class DataController {

    private val bluetoothLeService = MainActivity.bluetoothLeService

    fun sendDataToBluetoothModule(
        playerDMXValuesArray: ByteArray,
        randomBlueDMXValuesArray: ByteArray,
        randomYellowDMXValuesArray: ByteArray,
        randomRedHeadlightBeamView: ByteArray
    ) {
        GlobalScope.launch{
            delay(DELAY)
            bluetoothLeService.write(playerDMXValuesArray)
            delay(DELAY)
            bluetoothLeService.write(randomBlueDMXValuesArray)
            delay(DELAY)
            bluetoothLeService.write(randomYellowDMXValuesArray)
            delay(DELAY)
            bluetoothLeService.write(randomRedHeadlightBeamView)
        }
    }
}