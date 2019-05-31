package com.example.catchmeifyoucan.Game

import com.example.catchmeifyoucan.Activities.MainActivity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

const val ESCAPE_CHARACTER = "~"
const val DELAY = 25L;

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
            bluetoothLeService.write(makeString(playerHeadlightBeamViewCurrentX))
            delay(DELAY)
            bluetoothLeService.write(makeString(playerHeadlightBeamViewCurrentY))
            delay(DELAY)
            bluetoothLeService.write(makeString(randomHeadlightBeamViewCurrentX))
            delay(DELAY)
            bluetoothLeService.write(makeString(randomHeadlightBeamViewCurrentY))
            delay(DELAY)
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