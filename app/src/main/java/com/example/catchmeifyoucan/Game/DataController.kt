package com.example.catchmeifyoucan.Game

import com.example.catchmeifyoucan.Activities.MainActivity

class DataController {

    val gameController = GameController()
    val bluetoothLeService = MainActivity.getBluetoothService()
    var playerHeadlightBeamCurrentX : Float = 0f
    var playerHeadlightBeamCurrentY : Float = 0f
    var randomHeadlightBeamCurrentX : Float = 0f
    var randomHeadlightBeamCurrentY : Float = 0f

    fun getViewCoordinates(){
        playerHeadlightBeamCurrentX = gameController.getPlayerHeadlightBeamViewCurrentX()
        playerHeadlightBeamCurrentY = gameController.getPlayerHeadlightBeamViewCurrentY()
        randomHeadlightBeamCurrentX = gameController.getRandomHeadlightBeamViewCurrentX()
        randomHeadlightBeamCurrentY = gameController.getRandomHeadlightBeamViewCurrentY()
        sendDataToHM10()
    }

    private fun sendDataToHM10(){
        bluetoothLeService.write(playerHeadlightBeamCurrentX.toString())
        bluetoothLeService.write(playerHeadlightBeamCurrentY.toString())
        bluetoothLeService.write(randomHeadlightBeamCurrentX.toString())
        bluetoothLeService.write(randomHeadlightBeamCurrentY.toString())
    }
}