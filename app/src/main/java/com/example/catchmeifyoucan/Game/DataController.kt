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
        val dmxArray = arrayOf(playerDMXValues, playerDMXValuesTuning, randomGreenDMXValues, randomGreenDMXValuesTuning, randomYellowDMXValues, randomYellowDMXValuesTuning, randomRedDMXValues, randomRedDMXValuesTuning, randomGreenMovingHeadBrightnessData, randomYellowMovingHeadBrightnessData, randomRedMovingHeadBrightnessData)
        GlobalScope.launch{
            for (data in dmxArray){
                if (data.isNotEmpty()) bluetoothLeService.write(data)
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