package com.example.catchmeifyoucan.Game

import com.example.catchmeifyoucan.Activities.MainActivity
import kotlin.math.roundToInt

const val T0 = 11520
const val T45 = 22400
const val P0 = 10752
const val P45 = 16256

const val radToDeg = 360 / (2 * Math.PI)
const val power = 2.0
const val sixteenToEightBitConverter = 256

const val distanceToWall = 300.0
const val wallWidth = 400
const val wallHeight = 300
const val displayWidth = 1600
const val displayHeight = 1200

const val playerMHOffsetX = 169
const val randomMHOffsetX = 231

class ViewsCoordinatesTranslator() {
    private val gameController = MainActivity.gameController
    private val dataController = DataController()
    private lateinit var playerDMXValuesArray: ByteArray
    private lateinit var randomDMXValuesArray: ByteArray

    private var playerHeadlightBeamViewCurrentPixelX = 0
    private var playerHeadlightBeamViewCurrentWallX = 0.0
    private var playerHeadlightBeamViewCurrentPixelY = 0
    private var playerHeadlightBeamViewCurrentWallY = 0.0

    private var randomHeadlightBeamViewCurrentPixelX = 0
    private var randomHeadlightBeamViewCurrentWallX = 0.0
    private var randomHeadlightBeamViewCurrentPixelY = 0
    private var randomHeadlightBeamViewCurrentWallY = 0.0

    fun translateCoordinatesAndSendToBluetoothModule() {
        getViewsCoordinatesInPixels()
        transformPixelCoordinatesIntoWallCoordinates()
        calculateDMXValuesFromPanAndTiltValues()
        sendData()
    }

    private fun getViewsCoordinatesInPixels() {
        playerHeadlightBeamViewCurrentPixelX = gameController.getPlayerHeadlightBeamViewCurrentX()
        playerHeadlightBeamViewCurrentPixelY = gameController.getPlayerHeadlightBeamViewCurrentY()
        randomHeadlightBeamViewCurrentPixelX = gameController.getRandomHeadlightBeamViewCurrentX()
        randomHeadlightBeamViewCurrentPixelY = gameController.getRandomHeadlightBeamViewCurrentY()
    }

    private fun transformPixelCoordinatesIntoWallCoordinates() {
        playerHeadlightBeamViewCurrentWallX =
            -(((playerHeadlightBeamViewCurrentPixelX / displayWidth) * wallWidth) - playerMHOffsetX).toDouble()
        playerHeadlightBeamViewCurrentWallY =
            -(((playerHeadlightBeamViewCurrentPixelY / displayHeight) * wallHeight) - displayHeight).toDouble()
        randomHeadlightBeamViewCurrentWallX =
            -(((randomHeadlightBeamViewCurrentPixelX / displayWidth) * wallWidth) - randomMHOffsetX).toDouble()
        randomHeadlightBeamViewCurrentWallY =
            -(((randomHeadlightBeamViewCurrentPixelY / displayWidth) * wallHeight) - displayHeight).toDouble()
    }

    private fun calculateDMXValuesFromPanAndTiltValues() {
        // calculate Pan and Tilt Values in Degrees
        val playerDegreesPan = Math.asin(
            playerHeadlightBeamViewCurrentWallX /
                    Math.sqrt(
                        Math.pow(playerHeadlightBeamViewCurrentWallX, power) + Math.pow(
                            playerHeadlightBeamViewCurrentWallY,
                            power
                        ) + Math.pow(distanceToWall, power)
                    )
        ) * radToDeg
        val playerDegreesTilt = Math.asin(
            playerHeadlightBeamViewCurrentWallY /
                    Math.sqrt(
                        Math.pow(playerHeadlightBeamViewCurrentWallX, power) + Math.pow(
                            playerHeadlightBeamViewCurrentWallY,
                            power
                        ) + Math.pow(distanceToWall, power)
                    )
        ) * radToDeg
        val randomDegreesPan = Math.asin(
            randomHeadlightBeamViewCurrentWallX /
                    Math.sqrt(
                        Math.pow(randomHeadlightBeamViewCurrentWallX, power) + Math.pow(
                            randomHeadlightBeamViewCurrentWallY,
                            power
                        ) + Math.pow(distanceToWall, power)
                    )
        ) * radToDeg
        val randomDegreesTilt = Math.asin(
            randomHeadlightBeamViewCurrentWallY /
                    Math.sqrt(
                        Math.pow(randomHeadlightBeamViewCurrentWallX, power) + Math.pow(
                            randomHeadlightBeamViewCurrentWallY,
                            power
                        ) + Math.pow(distanceToWall, power)
                    )
        ) * radToDeg

        // transform Degree Pan and Tilt Values into DMX friendly values
        val playerDMXPan = (P0 + ((P45 - P0) / 45) * playerDegreesPan).roundToInt()
        val playerDMXTilt = (T0 + ((T45 - T0) / 45) * playerDegreesTilt).roundToInt()
        val randomDMXPan = (P0 + ((P45 - P0) / 45) * randomDegreesPan).roundToInt()
        val randomDMXTilt = (T0 + ((T45 - T0) / 45) * randomDegreesTilt).roundToInt()

        val playerDMXPanForChannel1 = (Math.floor(playerDMXPan.toDouble() / sixteenToEightBitConverter)).toInt()
        val playerDMXPanForChannel2 = playerDMXPan.rem(sixteenToEightBitConverter)
        val playerDMXPanForChannel3 = (Math.floor(playerDMXTilt.toDouble() / sixteenToEightBitConverter)).toInt()
        val playerDMXPanForChannel4 = playerDMXTilt.rem(sixteenToEightBitConverter)

        val randomDMXPanForChannel33 = (Math.floor(randomDMXPan.toDouble() / sixteenToEightBitConverter)).toInt()
        val randomDMXPanForChannel34 = randomDMXPan.rem(sixteenToEightBitConverter)
        val randomDMXPanForChannel35 = (Math.floor(randomDMXTilt.toDouble() / sixteenToEightBitConverter)).toInt()
        val randomDMXPanForChannel36 = randomDMXTilt.rem(sixteenToEightBitConverter)


        // put values into corresponding arrays
        val playerDMXValuesIntegerArray =
            arrayOf(playerDMXPanForChannel1, playerDMXPanForChannel2, playerDMXPanForChannel3, playerDMXPanForChannel4)
        playerDMXValuesArray =
            playerDMXValuesIntegerArray.foldIndexed(ByteArray(playerDMXValuesIntegerArray.size)) { i, a, v ->
                a.apply {
                    set(
                        i,
                        v.toByte()
                    )
                }
            }
        val randomDMXValuesIntegerArray = arrayOf(
            randomDMXPanForChannel33,
            randomDMXPanForChannel34,
            randomDMXPanForChannel35,
            randomDMXPanForChannel36
        )
        randomDMXValuesArray =
            randomDMXValuesIntegerArray.foldIndexed(ByteArray(randomDMXValuesIntegerArray.size)) { i, a, v ->
                a.apply {
                    set(
                        i,
                        v.toByte()
                    )
                }
            }

    }

    private fun sendData() {
        dataController.sendDataToBluetoothModule(playerDMXValuesArray, randomDMXValuesArray)
    }
}
