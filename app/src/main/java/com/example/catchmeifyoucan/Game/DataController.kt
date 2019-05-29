package com.example.catchmeifyoucan.Game

import com.example.catchmeifyoucan.Activities.MainActivity

class DataController {

    private val gameController = MainActivity.gameController
    private val bluetoothLeService = MainActivity.bluetoothLeService
    private var playerHeadlightBeamViewCurrentX = 0
    private var playerHeadlightBeamViewCurrentY = 0
    private var randomHeadlightBeamViewCurrentX = 0
    private var randomHeadlightBeamViewCurrentY = 0

    fun sendDataToBluetoothModule(){
        getViewsCoordinates()
        bluetoothLeService.write(playerHeadlightBeamViewCurrentX.toString())
        bluetoothLeService.write(playerHeadlightBeamViewCurrentY.toString())
        bluetoothLeService.write(randomHeadlightBeamViewCurrentX.toString())
        bluetoothLeService.write(randomHeadlightBeamViewCurrentY.toString())
    }

    private fun getViewsCoordinates(){
        playerHeadlightBeamViewCurrentX = gameController.getPlayerHeadlightBeamViewCurrentX()
        playerHeadlightBeamViewCurrentY = gameController.getPlayerHeadlightBeamViewCurrentY()
        randomHeadlightBeamViewCurrentX = gameController.getRandomHeadlightBeamViewCurrentX()
        randomHeadlightBeamViewCurrentY = gameController.getRandomHeadlightBeamViewCurrentY()
    }

}