package com.example.catchmeifyoucan.Game

import com.example.catchmeifyoucan.Activities.MainActivity

class DataController {

    private val gameController = GameController()
    private val bluetoothLeService = MainActivity.getBluetoothService()
    private var playerHeadlightBeamViewCurrentX : Float = 0f
    private var playerHeadlightBeamViewCurrentY : Float = 0f
    private var randomHeadlightBeamViewCurrentX : Float = 0f
    private var randomHeadlightBeamViewCurrentY : Float = 0f

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
        sendDataToBluetoothModule()
    }

}