package com.example.catchmeifyoucan.Game

import com.example.catchmeifyoucan.Activities.GameActivity
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

const val DELAY = 10L;

class DataController {

    private val bluetoothLeService = GameActivity.bluetoothLeService
    private var collisionData = ""

    fun sendPanAndTiltValues(
        playerDMXValues: String,
        randomBlueDMXValues: String,
        randomYellowDMXValues: String,
        randomRedDMXValues: String
    ) {
        GlobalScope.launch{
            bluetoothLeService.write(collisionData)
            delay(DELAY)
            bluetoothLeService.write(playerDMXValues)
            delay(DELAY)
            bluetoothLeService.write(randomBlueDMXValues)
            delay(DELAY)
            bluetoothLeService.write(randomYellowDMXValues)
            delay(DELAY)
            bluetoothLeService.write(randomRedDMXValues)
            delay(DELAY)
        }
    }

    fun setCollisionData(jsonObject: String) {
        collisionData = jsonObject
    }
}