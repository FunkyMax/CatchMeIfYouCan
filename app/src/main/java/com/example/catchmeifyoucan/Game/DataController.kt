package com.example.catchmeifyoucan.Game

import com.example.catchmeifyoucan.Activities.MenuActivity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

const val DELAY = 10L

class DataController {

    private val bluetoothLeService = MenuActivity.bluetoothLeService
    private var extraData = ""

    fun sendPanAndTiltValues(
        playerDMXValues: String,
        randomBlueDMXValues: String,
        randomYellowDMXValues: String,
        randomRedDMXValues: String
    ) {
        GlobalScope.launch{
            bluetoothLeService.write(playerDMXValues)
            delay(DELAY)
            bluetoothLeService.write(randomBlueDMXValues)
            delay(DELAY)
            bluetoothLeService.write(randomYellowDMXValues)
            delay(DELAY)
            bluetoothLeService.write(randomRedDMXValues)
            delay(DELAY)
            if (!extraData.isEmpty()){
                bluetoothLeService.write(extraData)
                delay(DELAY)
            }
        }
    }

    fun setCollisionData(jsonObject: String) {
        extraData = jsonObject
    }

    fun setResetData(jsonObject: String){
        bluetoothLeService.write(jsonObject)
    }
}