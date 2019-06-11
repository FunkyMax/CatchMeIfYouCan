package com.example.catchmeifyoucan.Game

import com.example.catchmeifyoucan.Activities.MainActivity
import org.json.JSONObject
import kotlin.math.roundToInt

const val T0 = 11520
const val T45 = 22400
const val P0 = 10752
const val P45 = 16256

const val radToDeg = 360 / (2 * Math.PI)
private const val power = 2.0
const val sixteenToEightBitConverter = 256

const val distanceToWall = 460.0
const val wallWidth = 400
const val wallHeight = 300
const val displayWidth = 2860
const val displayHeight = 1340

const val playerMHOffsetX = 140
const val randomBlueMHOffsetX = 166
const val randomYellowMHOffsetX = 230
const val randomRedMHOffsetX = 280

class ViewsCoordinatesTranslator() {
    private val gameController = MainActivity.gameController
    private val dataController = MainActivity.dataController

    private lateinit var playerJSONObject: JSONObject
    private lateinit var randomBlueJSONObject: JSONObject
    private lateinit var randomYellowJSONObject: JSONObject
    private lateinit var randomRedJSONObject: JSONObject

    private var playerHeadlightBeamViewCurrentPixelX = 0
    private var playerHeadlightBeamViewCurrentWallX = 0.0
    private var playerHeadlightBeamViewCurrentPixelY = 0
    private var playerHeadlightBeamViewCurrentWallY = 0.0

    private var randomBlueHeadlightBeamViewCurrentPixelX = 0
    private var randomBlueHeadlightBeamViewCurrentWallX = 0.0
    private var randomBlueHeadlightBeamViewCurrentPixelY = 0
    private var randomBlueHeadlightBeamViewCurrentWallY = 0.0

    private var randomYellowHeadlightBeamViewCurrentPixelX = 0
    private var randomYellowHeadlightBeamViewCurrentWallX = 0.0
    private var randomYellowHeadlightBeamViewCurrentPixelY = 0
    private var randomYellowHeadlightBeamViewCurrentWallY = 0.0

    private var randomRedHeadlightBeamViewCurrentPixelX = 0
    private var randomRedHeadlightBeamViewCurrentWallX = 0.0
    private var randomRedHeadlightBeamViewCurrentPixelY = 0
    private var randomRedHeadlightBeamViewCurrentWallY = 0.0

    fun translateCoordinatesAndSendToBluetoothModule() {
        getViewsCoordinatesInPixels()
        transformPixelCoordinatesIntoWallCoordinates()
        calculateDMXValuesFromPanAndTiltValues()
        sendData()
    }

    private fun getViewsCoordinatesInPixels() {
        playerHeadlightBeamViewCurrentPixelX = gameController.getPlayerHeadlightBeamViewCurrentX()
        playerHeadlightBeamViewCurrentPixelY = gameController.getPlayerHeadlightBeamViewCurrentY()

        randomBlueHeadlightBeamViewCurrentPixelX = gameController.getRandomBlueHeadlightBeamViewCurrentX()
        randomBlueHeadlightBeamViewCurrentPixelY = gameController.getRandomBlueHeadlightBeamViewCurrentY()

        randomYellowHeadlightBeamViewCurrentPixelX = gameController.getRandomYellowHeadlightBeamViewCurrentX()
        randomYellowHeadlightBeamViewCurrentPixelY = gameController.getRandomYellowHeadlightBeamViewCurrentY()

        randomRedHeadlightBeamViewCurrentPixelX = gameController.getRandomRedHeadlightBeamViewCurrentX()
        randomRedHeadlightBeamViewCurrentPixelY = gameController.getRandomRedHeadlightBeamViewCurrentY()
    }

    private fun transformPixelCoordinatesIntoWallCoordinates() {
        playerHeadlightBeamViewCurrentWallX =
            (((playerHeadlightBeamViewCurrentPixelX.toDouble() / displayWidth) * wallWidth) - playerMHOffsetX)
        playerHeadlightBeamViewCurrentWallY =
            (((-playerHeadlightBeamViewCurrentPixelY.toDouble() + displayHeight) / displayHeight) * wallHeight)

        randomBlueHeadlightBeamViewCurrentWallX =
            (((randomBlueHeadlightBeamViewCurrentPixelX.toDouble() / displayWidth) * wallWidth) - randomBlueMHOffsetX)
        randomBlueHeadlightBeamViewCurrentWallY =
            (((-randomBlueHeadlightBeamViewCurrentPixelY.toDouble() + displayHeight) / displayHeight) * wallHeight)

        randomYellowHeadlightBeamViewCurrentWallX =
            (((randomYellowHeadlightBeamViewCurrentPixelX.toDouble() / displayWidth) * wallWidth) - randomYellowMHOffsetX)
        randomYellowHeadlightBeamViewCurrentWallY =
            (((-randomYellowHeadlightBeamViewCurrentPixelY.toDouble() + displayHeight) / displayHeight) * wallHeight)

        randomRedHeadlightBeamViewCurrentWallX =
            (((randomRedHeadlightBeamViewCurrentPixelX.toDouble() / displayWidth) * wallWidth) - randomRedMHOffsetX)
        randomRedHeadlightBeamViewCurrentWallY =
            (((-randomRedHeadlightBeamViewCurrentPixelY.toDouble() + displayHeight) / displayHeight) * wallHeight)
    }

    private fun calculateDMXValuesFromPanAndTiltValues() {
        // calculate Pan and Tilt Values in Degrees for player
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

        // calculate Pan and Tilt Values in Degrees for randomBlueHeadlightBeam
        val randomBlueDegreesPan = Math.asin(
            randomBlueHeadlightBeamViewCurrentWallX /
                    Math.sqrt(
                        Math.pow(randomBlueHeadlightBeamViewCurrentWallX, power) + Math.pow(
                            randomBlueHeadlightBeamViewCurrentWallY,
                            power
                        ) + Math.pow(distanceToWall, power)
                    )
        ) * radToDeg
        val randomBlueDegreesTilt = Math.asin(
            randomBlueHeadlightBeamViewCurrentWallY /
                    Math.sqrt(
                        Math.pow(randomBlueHeadlightBeamViewCurrentWallX, power) + Math.pow(
                            randomBlueHeadlightBeamViewCurrentWallY,
                            power
                        ) + Math.pow(distanceToWall, power)
                    )
        ) * radToDeg

        // calculate Pan and Tilt Values in Degrees for randomYellowHeadlightBeam
        val randomYellowDegreesPan = Math.asin(
            randomYellowHeadlightBeamViewCurrentWallX /
                    Math.sqrt(
                        Math.pow(randomYellowHeadlightBeamViewCurrentWallX, power) + Math.pow(
                            randomYellowHeadlightBeamViewCurrentWallY,
                            power
                        ) + Math.pow(distanceToWall, power)
                    )
        ) * radToDeg
        val randomYellowDegreesTilt = Math.asin(
            randomYellowHeadlightBeamViewCurrentWallY /
                    Math.sqrt(
                        Math.pow(randomYellowHeadlightBeamViewCurrentWallX, power) + Math.pow(
                            randomYellowHeadlightBeamViewCurrentWallY,
                            power
                        ) + Math.pow(distanceToWall, power)
                    )
        ) * radToDeg

        // calculate Pan and Tilt Values in Degrees for randomRedHeadlightBeam
        val randomRedDegreesPan = Math.asin(
            randomRedHeadlightBeamViewCurrentWallX /
                    Math.sqrt(
                        Math.pow(randomRedHeadlightBeamViewCurrentWallX, power) + Math.pow(
                            randomRedHeadlightBeamViewCurrentWallY,
                            power
                        ) + Math.pow(distanceToWall, power)
                    )
        ) * radToDeg
        val randomRedDegreesTilt = Math.asin(
            randomRedHeadlightBeamViewCurrentWallY /
                    Math.sqrt(
                        Math.pow(randomRedHeadlightBeamViewCurrentWallX, power) + Math.pow(
                            randomRedHeadlightBeamViewCurrentWallY,
                            power
                        ) + Math.pow(distanceToWall, power)
                    )
        ) * radToDeg

        // transform Degree Pan and Tilt Values into DMX friendly values
        val playerDMXPan = (P0 + ((P45 - P0) / 45) * playerDegreesPan).roundToInt()
        val playerDMXTilt = (T0 + ((T45 - T0) / 45) * playerDegreesTilt).roundToInt()
        val randomBlueDMXPan = (P0 + ((P45 - P0) / 45) * randomBlueDegreesPan).roundToInt()
        val randomBlueDMXTilt = (T0 + ((T45 - T0) / 45) * randomBlueDegreesTilt).roundToInt()
        val randomYellowDMXPan = (P0 + ((P45 - P0) / 45) * randomYellowDegreesPan).roundToInt()
        val randomYellowDMXTilt = (T0 + ((T45 - T0) / 45) * randomYellowDegreesTilt).roundToInt()
        val randomRedDMXPan = (P0 + ((P45 - P0) / 45) * randomRedDegreesPan).roundToInt()
        val randomRedDMXTilt = (T0 + ((T45 - T0) / 45) * randomRedDegreesTilt).roundToInt()

        val playerDMXPanForChannel1 = (Math.floor(playerDMXPan.toDouble() / sixteenToEightBitConverter)).toInt()
        val playerDMXPanForChannel2 = playerDMXPan.rem(sixteenToEightBitConverter)
        val playerDMXTiltForChannel3 = (Math.floor(playerDMXTilt.toDouble() / sixteenToEightBitConverter)).toInt()
        val playerDMXTiltForChannel4 = playerDMXTilt.rem(sixteenToEightBitConverter)

        val randomBlueDMXPanForChannel26 =
            (Math.floor(randomBlueDMXPan.toDouble() / sixteenToEightBitConverter)).toInt()
        val randomBlueDMXPanForChannel27 = randomBlueDMXPan.rem(sixteenToEightBitConverter)
        val randomBlueDMXTiltForChannel28 =
            (Math.floor(randomBlueDMXTilt.toDouble() / sixteenToEightBitConverter)).toInt()
        val randomBlueDMXTiltForChannel29 = randomBlueDMXTilt.rem(sixteenToEightBitConverter)

        val randomYellowDMXPanForChannel51 =
            (Math.floor(randomYellowDMXPan.toDouble() / sixteenToEightBitConverter)).toInt()
        val randomYellowDMXPanForChannel52 = randomYellowDMXPan.rem(sixteenToEightBitConverter)
        val randomYellowDMXTiltForChannel53 =
            (Math.floor(randomYellowDMXTilt.toDouble() / sixteenToEightBitConverter)).toInt()
        val randomYellowDMXTiltForChannel54 = randomYellowDMXTilt.rem(sixteenToEightBitConverter)

        val randomRedDMXPanForChannel76 =
            (Math.floor(randomRedDMXPan.toDouble() / sixteenToEightBitConverter)).toInt()
        val randomRedDMXPanForChannel77 = randomRedDMXPan.rem(sixteenToEightBitConverter)
        val randomRedDMXTiltForChannel78 =
            (Math.floor(randomRedDMXTilt.toDouble() / sixteenToEightBitConverter)).toInt()
        val randomRedDMXTiltForChannel79 = randomRedDMXTilt.rem(sixteenToEightBitConverter)

        /*
        println("playerDMXPanForChannel1: " + playerDMXPanForChannel1)
        //println("playerDMXPanForChannel2: " + playerDMXPanForChannel2)
        println("playerDMXTiltForChannel3: " + playerDMXTiltForChannel3)
        //println("playerDMXTiltForChannel4: " + playerDMXTiltForChannel4)
        println("\n")
        println("randomBlueDMXPanForChannel26: " + randomBlueDMXPanForChannel26)
        //println("randomBlueDMXPanForChannel27: " + randomBlueDMXPanForChannel27)
        println("randomBlueDMXTiltForChannel28: " + randomBlueDMXTiltForChannel28)
        //println("randomBlueDMXTiltForChannel29: " + randomBlueDMXTiltForChannel29)
        println("\n")
        println("randomYellowDMXPanForChannel51: " + randomYellowDMXPanForChannel51)
        //println("randomYellowDMXPanForChannel52: " + randomYellowDMXPanForChannel52)
        println("randomYellowDMXTiltForChannel53: " + randomYellowDMXTiltForChannel53)
        //println("randomYellowDMXTiltForChannel54: " + randomYellowDMXTiltForChannel54)
        println("\n")
        println("randomRedDMXPanForChannel76: " + randomRedDMXPanForChannel76)
        //println("randomRedDMXPanForChannel77: " + randomRedDMXPanForChannel77)
        println("randomRedDMXTiltForChannel78: " + randomRedDMXTiltForChannel78)
        //println("randomRedDMXTiltForChannel79: " + randomRedDMXTiltForChannel79)
        */

        // put values into corresponding JSONObjects
        playerJSONObject = JSONObject()
        playerJSONObject.put("1", playerDMXPanForChannel1)
        playerJSONObject.put("3", playerDMXTiltForChannel3)

        randomBlueJSONObject = JSONObject()
        randomBlueJSONObject.put("26", randomBlueDMXPanForChannel26)
        randomBlueJSONObject.put("28", randomBlueDMXTiltForChannel28)

        randomYellowJSONObject = JSONObject()
        randomYellowJSONObject.put("51", randomYellowDMXPanForChannel51)
        randomYellowJSONObject.put("53", randomYellowDMXTiltForChannel53)

        randomRedJSONObject = JSONObject()
        randomRedJSONObject.put("76", randomRedDMXPanForChannel76)
        randomRedJSONObject.put("78", randomRedDMXTiltForChannel78)
    }

    private fun sendData() {
        dataController.sendPanAndTiltValues(
            playerJSONObject.toString(),
            randomBlueJSONObject.toString(),
            randomYellowJSONObject.toString(),
            randomRedJSONObject.toString()
        )
    }
}
