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

private const val distanceToWall = 400.0
private const val wallWidth = 400
private const val wallHeight = 300
private const val displayWidth = 2860
private const val displayHeight = 1340

private const val playerMHOffsetX = 275
private const val randomGreenMHOffsetX = 225
private const val randomYellowMHOffsetX = 175
private const val randomRedMHOffsetX = 125

private const val playerPanChannel = "1"
private const val playerTiltChannel = "3"
private const val randomGreenPanChannel = "26"
private const val randomGreenTiltChannel = "28"
private const val randomYellowPanChannel = "51"
private const val randomYellowTiltChannel = "53"
private const val randomRedPanChannel = "76"
private const val randomRedTiltChannel = "78"

class ViewsCoordinatesTranslator() {
    private val dataController = GameController.dataController
    private val gameActivity = GameActivity()
    private val gameController = gameActivity.getGameController()

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
            (((-playerHeadlightBeamViewCurrentPixelY.toDouble() + displayHeight) / displayHeight) * wallHeight) + 10

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

        val playerDMXPanForChannel1 =
            (Math.floor(playerDMXPan.toDouble() / sixteenToEightBitConverter)).toInt()
        val playerDMXTiltForChannel3 =
            (Math.floor(playerDMXTilt.toDouble() / sixteenToEightBitConverter)).toInt()

        val randomGreenDMXPanForChannel26 =
            (Math.floor(randomGreenDMXPan.toDouble() / sixteenToEightBitConverter)).toInt()
        val randomGreenDMXTiltForChannel28 =
            (Math.floor(randomGreenDMXTilt.toDouble() / sixteenToEightBitConverter)).toInt()

        val randomYellowDMXPanForChannel51 =
            (Math.floor(randomYellowDMXPan.toDouble() / sixteenToEightBitConverter)).toInt()
        val randomYellowDMXTiltForChannel53 =
            (Math.floor(randomYellowDMXTilt.toDouble() / sixteenToEightBitConverter)).toInt()

        val randomRedDMXPanForChannel76 =
            (Math.floor(randomRedDMXPan.toDouble() / sixteenToEightBitConverter)).toInt()
        val randomRedDMXTiltForChannel78 =
            (Math.floor(randomRedDMXTilt.toDouble() / sixteenToEightBitConverter)).toInt()

        // put values into corresponding JSONObjects
        playerJSONObject.put(playerPanChannel, playerDMXPanForChannel1)
        playerJSONObject.put(playerTiltChannel, playerDMXTiltForChannel3)

        randomGreenJSONObject.put(randomGreenPanChannel, randomGreenDMXPanForChannel26)
        randomGreenJSONObject.put(randomGreenTiltChannel, randomGreenDMXTiltForChannel28)

        randomYellowJSONObject.put(randomYellowPanChannel, randomYellowDMXPanForChannel51)
        randomYellowJSONObject.put(randomYellowTiltChannel, randomYellowDMXTiltForChannel53)

        randomRedJSONObject.put(randomRedPanChannel, randomRedDMXPanForChannel76)
        randomRedJSONObject.put(randomRedTiltChannel, randomRedDMXTiltForChannel78)
    }

    private fun sendData() {
        dataController.sendData(
            playerJSONObject.toString(),
            randomGreenJSONObject.toString(),
            randomYellowJSONObject.toString(),
            randomRedJSONObject.toString()
        )
    }
}
