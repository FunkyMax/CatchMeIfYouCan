package com.example.catchmeifyoucan.Game

import com.example.catchmeifyoucan.Activities.MainActivity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

const val ESCAPE_CHARACTER = "~"
const val DELAY = 50L;

class DataController {

    private val gameController = MainActivity.gameController
    private val bluetoothLeService = MainActivity.bluetoothLeService
    private var playerHeadlightBeamViewCurrentX = 0
    private var playerHeadlightBeamViewCurrentY = 0
    private var randomHeadlightBeamViewCurrentX = 0
    private var randomHeadlightBeamViewCurrentY = 0

    fun sendDataToBluetoothModule(playerDMXValuesArray: ByteArray, randomDMXValuesArray: ByteArray) {
        GlobalScope.launch{

            bluetoothLeService.write(playerDMXValuesArray)
            delay(DELAY)
            bluetoothLeService.write(randomDMXValuesArray)
            /*bluetoothLeService.write(makeString(playerHeadlightBeamViewCurrentX))
            delay(DELAY)
            bluetoothLeService.write(makeString(playerHeadlightBeamViewCurrentY))
            delay(DELAY)
            bluetoothLeService.write(makeString(randomHeadlightBeamViewCurrentX))
            delay(DELAY)
            bluetoothLeService.write(makeString(randomHeadlightBeamViewCurrentY))
            delay(DELAY)*/
        }
    }

    private fun makeString(int: Int) : String{
        return int.toString() + ESCAPE_CHARACTER
    }
}