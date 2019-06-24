package com.example.catchmeifyoucan.Game

import com.example.catchmeifyoucan.Activities.MenuActivity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

const val DELAY = 15L

class DataController {

    private val bluetoothLeService = MenuActivity.bluetoothLeService
    private var randomGreenMovingHeadBrightnessData = ""
    private var randomYellowMovingHeadBrightnessData = ""
    private var randomRedMovingHeadBrightnessData = ""

    fun sendData(
        playerDMXValues: String,
        playerDMXValuesTuning: String,
        randomGreenDMXValues: String,
        randomGreenDMXValuesTuning: String,
        randomYellowDMXValues: String,
        randomYellowDMXValuesTuning: String,
        randomRedDMXValues: String,
        randomRedDMXValuesTuning: String
    ) {
        GlobalScope.launch {
            bluetoothLeService.write(playerDMXValues)
            delay(DELAY)
            bluetoothLeService.write(randomGreenDMXValues)
            delay(DELAY)
            bluetoothLeService.write(randomYellowDMXValues)
            delay(DELAY)
            bluetoothLeService.write(randomRedDMXValues)
            delay(DELAY)
            bluetoothLeService.write(playerDMXValuesTuning)
            delay(DELAY)
            bluetoothLeService.write(randomGreenDMXValuesTuning)
            delay(DELAY)
            bluetoothLeService.write(randomYellowDMXValuesTuning)
            delay(DELAY)
            bluetoothLeService.write(randomRedDMXValuesTuning)
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

    fun sendResetData(jsonObject: String){
        bluetoothLeService.write(jsonObject)
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
}