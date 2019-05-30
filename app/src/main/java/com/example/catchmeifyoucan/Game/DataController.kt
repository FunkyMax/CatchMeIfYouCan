package com.example.catchmeifyoucan.Game

import com.example.catchmeifyoucan.Activities.MainActivity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

const val ESCAPE_CHARACTER = "ßß"

class DataController {

    private val gameController = MainActivity.gameController
    private val bluetoothLeService = MainActivity.bluetoothLeService
    private var playerHeadlightBeamViewCurrentX = 0
    private var playerHeadlightBeamViewCurrentY = 0
    private var randomHeadlightBeamViewCurrentX = 0
    private var randomHeadlightBeamViewCurrentY = 0

    fun sendDataToBluetoothModule(){
        GlobalScope.launch{
            getViewsCoordinates()
            super.toString()
            bluetoothLeService.write(makeString(playerHeadlightBeamViewCurrentX))
            delay(20)
            bluetoothLeService.write(makeString(playerHeadlightBeamViewCurrentY))
            delay(20)
            bluetoothLeService.write(makeString(randomHeadlightBeamViewCurrentX))
            delay(20)
            bluetoothLeService.write(makeString(randomHeadlightBeamViewCurrentY))
            delay(20)
        }
    }

    private fun getViewsCoordinates(){
        playerHeadlightBeamViewCurrentX = gameController.getPlayerHeadlightBeamViewCurrentX()
        playerHeadlightBeamViewCurrentY = gameController.getPlayerHeadlightBeamViewCurrentY()
        randomHeadlightBeamViewCurrentX = gameController.getRandomHeadlightBeamViewCurrentX()
        randomHeadlightBeamViewCurrentY = gameController.getRandomHeadlightBeamViewCurrentY()
    }

    private fun makeString(int: Int) : String{
        return int.toString() + ESCAPE_CHARACTER
    }
}