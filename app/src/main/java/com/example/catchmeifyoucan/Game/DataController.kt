package com.example.catchmeifyoucan.Game

import com.example.catchmeifyoucan.Activities.MenuActivity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

const val DELAY = 10L

class DataController {

    private val bluetoothLeService = MenuActivity.bluetoothLeService
    private var playerMovingHeadBrightnessData = ""
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

            if (playerMovingHeadBrightnessData.isNotEmpty()) {
                bluetoothLeService.write(playerMovingHeadBrightnessData)
                println(playerMovingHeadBrightnessData)
                delay(DELAY)
            }
            if (randomGreenMovingHeadBrightnessData.isNotEmpty()) {
                bluetoothLeService.write(randomGreenMovingHeadBrightnessData)
                println(randomGreenMovingHeadBrightnessData)
                delay(DELAY)
            }
            if (randomYellowMovingHeadBrightnessData.isNotEmpty()) {
                bluetoothLeService.write(randomYellowMovingHeadBrightnessData)
                println(randomYellowMovingHeadBrightnessData)
                delay(DELAY)
            }
            if (randomRedMovingHeadBrightnessData.isNotEmpty()) {
                bluetoothLeService.write(randomRedMovingHeadBrightnessData)
                println(randomRedMovingHeadBrightnessData)
                delay(DELAY)
            }
        }
    }

    fun setPlayerMovingHeadBrightnessData(jsonObject: String) {
        playerMovingHeadBrightnessData = jsonObject
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

    fun resetEachMHsJSONBrightnessData() {
        playerMovingHeadBrightnessData = ""
        randomGreenMovingHeadBrightnessData = ""
        randomYellowMovingHeadBrightnessData = ""
        randomRedMovingHeadBrightnessData = ""
    }

    fun setResetData(jsonObject: String){
        bluetoothLeService.write(jsonObject)
    }
}