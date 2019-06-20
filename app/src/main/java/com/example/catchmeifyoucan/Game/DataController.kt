package com.example.catchmeifyoucan.Game

import com.example.catchmeifyoucan.Activities.MenuActivity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

const val DELAY = 10L

class DataController {

    private val bluetoothLeService = MenuActivity.bluetoothLeService
    private var randomGreenMovingHeadBrightnessData = ""
    private var randomYellowMovingHeadBrightnessData = ""
    private var randomRedMovingHeadBrightnessData = ""

    fun sendData(
        playerDMXValues: String,
        randomGreenDMXValues: String,
        randomYellowDMXValues: String,
        randomRedDMXValues: String
    ) {
        GlobalScope.launch{
            bluetoothLeService.write(playerDMXValues)
            delay(DELAY)
            bluetoothLeService.write(randomGreenDMXValues)
            delay(DELAY)
            bluetoothLeService.write(randomYellowDMXValues)
            delay(DELAY)
            bluetoothLeService.write(randomRedDMXValues)
            delay(DELAY)

            if (randomGreenMovingHeadBrightnessData.isNotEmpty()) {
                bluetoothLeService.write(randomGreenMovingHeadBrightnessData)
                delay(DELAY)
            }
            if (randomYellowMovingHeadBrightnessData.isNotEmpty()) {
                bluetoothLeService.write(randomYellowMovingHeadBrightnessData)
                delay(DELAY)
            }
            if (randomRedMovingHeadBrightnessData.isNotEmpty()) {
                bluetoothLeService.write(randomRedMovingHeadBrightnessData)
                delay(DELAY)
            }
        }
    }

    fun setRandomGreenMovingHeadBrightnessData(jsonObject: String) {
        randomGreenMovingHeadBrightnessData = jsonObject
    }

    fun setRandomYellowMovingHeadBrightnessData(jsonObject: String) {
        randomYellowMovingHeadBrightnessData = jsonObject
    }

    fun setRandomRedMovingHeadBrightnessData(jsonObject: String) {
        randomRedMovingHeadBrightnessData = jsonObject
    }

    fun setResetData(jsonObject: String){
        bluetoothLeService.write(jsonObject)
    }
}