package com.example.catchmeifyoucan.Game

import com.example.catchmeifyoucan.Activities.MainActivity

class DataController {

    val gameController = GameController()
    val bluetoothLeService = MainActivity.getBluetoothService()
    var playerHeadlightBeamViewCurrentX : Float = 0f
    var playerHeadlightBeamViewCurrentY : Float = 0f
    var randomHeadlightBeamViewCurrentX : Float = 0f
    var randomHeadlightBeamViewCurrentY : Float = 0f

    private fun getViewsCoordinates(){
        playerHeadlightBeamViewCurrentX = gameController.getPlayerHeadlightBeamViewCurrentX()
        playerHeadlightBeamViewCurrentY = gameController.getPlayerHeadlightBeamViewCurrentY()
        randomHeadlightBeamViewCurrentX = gameController.getRandomHeadlightBeamViewCurrentX()
        randomHeadlightBeamViewCurrentY = gameController.getRandomHeadlightBeamViewCurrentY()
        sendDataToBluetoothModule()
    }

    fun sendDataToBluetoothModule(){
        getViewsCoordinates()
        bluetoothLeService.write(playerHeadlightBeamViewCurrentX.toString())
        bluetoothLeService.write(playerHeadlightBeamViewCurrentY.toString())
        bluetoothLeService.write(randomHeadlightBeamViewCurrentX.toString())
        bluetoothLeService.write(randomHeadlightBeamViewCurrentY.toString())
    }
}