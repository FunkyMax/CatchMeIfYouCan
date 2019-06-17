package com.example.catchmeifyoucan.Game

import com.example.catchmeifyoucan.Activities.GameActivity
import org.json.JSONObject
import kotlin.math.roundToInt

private const val T0 = 11520
private const val T45 = 22400
private const val P0 = 10752
private const val P45 = 16256

private const val radToDeg = 360 / (2 * Math.PI)
private const val power = 2.0
private const val sixteenToEightBitConverter = 256

private const val distanceToWall = 360.0
private const val wallWidth = 400
private const val wallHeight = 300
private const val displayWidth = 2860
private const val displayHeight = 1340

private const val playerMHOffsetX = 140
private const val randomGreenMHOffsetX = 166
private const val randomYellowMHOffsetX = 230
private const val randomRedMHOffsetX = 280

class ViewsCoordinatesTranslator {
    private val gameController = GameActivity.gameController
    private val dataController = GameController.dataController

    private val playerJSONObject = JSONObject()
    private val randomGreenJSONObject = JSONObject()
    private val randomYellowJSONObject = JSONObject()
    private val randomRedJSONObject = JSONObject()

    private var playerHeadlightBeamViewCurrentPixelX = 0
    private var playerHeadlightBeamViewCurrentWallX = 0.0
    private var playerHeadlightBeamViewCurrentPixelY = 0
    private var playerHeadlightBeamViewCurrentWallY = 0.0

    private var randomGreenHeadlightBeamViewCurrentPixelX = 0
    private var randomGreenHeadlightBeamViewCurrentWallX = 0.0
    private var randomGreenHeadlightBeamViewCurrentPixelY = 0
    private var randomGreenHeadlightBeamViewCurrentWallY = 0.0

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

        randomGreenHeadlightBeamViewCurrentPixelX = gameController.getRandomGreenHeadlightBeamViewCurrentX()
        randomGreenHeadlightBeamViewCurrentPixelY = gameController.getRandomGreenHeadlightBeamViewCurrentY()

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

        randomGreenHeadlightBeamViewCurrentWallX =
            (((randomGreenHeadlightBeamViewCurrentPixelX.toDouble() / displayWidth) * wallWidth) - randomGreenMHOffsetX)
        randomGreenHeadlightBeamViewCurrentWallY =
            (((-randomGreenHeadlightBeamViewCurrentPixelY.toDouble() + displayHeight) / displayHeight) * wallHeight)

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

        // calculate Pan and Tilt Values in Degrees for randomGreenHeadlightBeam
        val randomGreenDegreesPan = Math.asin(
            randomGreenHeadlightBeamViewCurrentWallX /
                    Math.sqrt(
                        Math.pow(randomGreenHeadlightBeamViewCurrentWallX, power) + Math.pow(
                            randomGreenHeadlightBeamViewCurrentWallY,
                            power
                        ) + Math.pow(distanceToWall, power)
                    )
        ) * radToDeg
        val randomGreenDegreesTilt = Math.asin(
            randomGreenHeadlightBeamViewCurrentWallY /
                    Math.sqrt(
                        Math.pow(randomGreenHeadlightBeamViewCurrentWallX, power) + Math.pow(
                            randomGreenHeadlightBeamViewCurrentWallY,
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
        val randomGreenDMXPan = (P0 + ((P45 - P0) / 45) * randomGreenDegreesPan).roundToInt()
        val randomGreenDMXTilt = (T0 + ((T45 - T0) / 45) * randomGreenDegreesTilt).roundToInt()
        val randomYellowDMXPan = (P0 + ((P45 - P0) / 45) * randomYellowDegreesPan).roundToInt()
        val randomYellowDMXTilt = (T0 + ((T45 - T0) / 45) * randomYellowDegreesTilt).roundToInt()
        val randomRedDMXPan = (P0 + ((P45 - P0) / 45) * randomRedDegreesPan).roundToInt()
        val randomRedDMXTilt = (T0 + ((T45 - T0) / 45) * randomRedDegreesTilt).roundToInt()

        val playerDMXPanForChannel1 = (Math.floor(playerDMXPan.toDouble() / sixteenToEightBitConverter)).toInt()
        val playerDMXPanForChannel2 = playerDMXPan.rem(sixteenToEightBitConverter)
        val playerDMXTiltForChannel3 = (Math.floor(playerDMXTilt.toDouble() / sixteenToEightBitConverter)).toInt()
        val playerDMXTiltForChannel4 = playerDMXTilt.rem(sixteenToEightBitConverter)

        val randomGreenDMXPanForChannel26 =
            (Math.floor(randomGreenDMXPan.toDouble() / sixteenToEightBitConverter)).toInt()
        val randomGreenDMXPanForChannel27 = randomGreenDMXPan.rem(sixteenToEightBitConverter)
        val randomGreenDMXTiltForChannel28 =
            (Math.floor(randomGreenDMXTilt.toDouble() / sixteenToEightBitConverter)).toInt()
        val randomGreenDMXTiltForChannel29 = randomGreenDMXTilt.rem(sixteenToEightBitConverter)

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
        println("randomGreenDMXPanForChannel26: " + randomGreenDMXPanForChannel26)
        //println("randomGreenDMXPanForChannel27: " + randomGreenDMXPanForChannel27)
        println("randomGreenDMXTiltForChannel28: " + randomGreenDMXTiltForChannel28)
        //println("randomGreenDMXTiltForChannel29: " + randomGreenDMXTiltForChannel29)
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
        playerJSONObject.put("1", playerDMXPanForChannel1)
        playerJSONObject.put("3", playerDMXTiltForChannel3)

        randomGreenJSONObject.put("26", randomGreenDMXPanForChannel26)
        randomGreenJSONObject.put("28", randomGreenDMXTiltForChannel28)

        randomYellowJSONObject.put("51", randomYellowDMXPanForChannel51)
        randomYellowJSONObject.put("53", randomYellowDMXTiltForChannel53)

        randomRedJSONObject.put("76", randomRedDMXPanForChannel76)
        randomRedJSONObject.put("78", randomRedDMXTiltForChannel78)
    }

    private fun sendData() {
        dataController.sendPanAndTiltValues(
            playerJSONObject.toString(),
            randomGreenJSONObject.toString(),
            randomYellowJSONObject.toString(),
            randomRedJSONObject.toString()
        )
    }
}
