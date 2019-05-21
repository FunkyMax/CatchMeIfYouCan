package com.example.catchmeifyoucan.Game

import com.example.catchmeifyoucan.Bluetooth.BluetoothLeService

class DataController {

    val gameController = GameController()
    lateinit var bluetoothLeService : BluetoothLeService
    lateinit var xPosition : String
    lateinit var yPosition : String

    fun sendDataToHM10(){
        bluetoothLeService.write(xPosition)
        bluetoothLeService.write(yPosition)
    }


}