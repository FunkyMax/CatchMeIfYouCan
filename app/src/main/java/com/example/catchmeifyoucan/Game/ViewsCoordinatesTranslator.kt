package com.example.catchmeifyoucan.Game

import org.json.JSONObject
import kotlin.math.roundToInt

private const val T0 = 11002
private const val T45 = 21868
private const val P0 = 10752
private const val P45 = 16288

private const val radToDeg = 360 / (2 * Math.PI)
private const val power = 2.0
private const val sixteenToEightBitConverter = 256
private const val stretchFactor = 95 / 72
private const val normalizeToOneDegree = 45
private const val distanceToWall = 263.0
private const val wallWidth = 400
private const val wallHeight = 300

private const val playerMHOffsetX = 275
private const val randomGreenMHOffsetX = 225
private const val randomYellowMHOffsetX = 175
private const val randomRedMHOffsetX = 125

private const val playerPanChannel = "1"
private const val playerPanChannelTuning = "2"
private const val playerTiltChannel = "3"
private const val playerTiltChannelTuning = "4"
private const val randomGreenPanChannel = "26"
private const val randomGreenPanChannelTuning = "27"
private const val randomGreenTiltChannel = "28"
private const val randomGreenTiltChannelTuning = "29"
private const val randomYellowPanChannel = "51"
private const val randomYellowPanChannelTuning = "52"
private const val randomYellowTiltChannel = "53"
private const val randomYellowTiltChannelTuning = "54"
private const val randomRedPanChannel = "76"
private const val randomRedPanChannelTuning = "77"
private const val randomRedTiltChannel = "78"
private const val randomRedTiltChannelTuning = "79"

class ViewsCoordinatesTranslator(private val dataController: DataController) {

    private val playerJSONObject = JSONObject()
    private val playerJSONObjectTuning = JSONObject()
    private val randomGreenJSONObject = JSONObject()
    private val randomGreenJSONObjectTuning = JSONObject()
    private val randomYellowJSONObject = JSONObject()
    private val randomYellowJSONObjectTuning = JSONObject()
    private val randomRedJSONObject = JSONObject()
    private val randomRedJSONObjectTuning = JSONObject()

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
        transformPixelCoordinatesIntoWallCoordinates()
        calculateDMXValues()
        sendData()
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

    private fun calculateDMXValues() {
        // At first, pan and tilt values are calculated in degrees

        // calculate pan and tilt values in degrees for player
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

        // calculate pan and tilt values in degrees for randomGreenHeadlightBeam
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

        // calculate pan and tilt values in degrees for randomYellowHeadlightBeam
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

        // calculate pan and tilt values in degrees for randomRedHeadlightBeam
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

        // transform degree pan and tilt values into DMX friendly values
        val playerDMXPan =
            (P0 + ((P45 + (displayHeight - playerHeadlightBeamViewCurrentPixelY) * stretchFactor - P0) / normalizeToOneDegree) * playerDegreesPan).roundToInt()
        val playerDMXTilt = (T0 + ((T45 - T0) / normalizeToOneDegree) * playerDegreesTilt).roundToInt()
        val randomGreenDMXPan =
            (P0 + ((P45 + (displayHeight - randomGreenHeadlightBeamViewCurrentPixelY) * stretchFactor - P0) / normalizeToOneDegree) * randomGreenDegreesPan).roundToInt()
        val randomGreenDMXTilt = (T0 + ((T45 - T0) / normalizeToOneDegree) * randomGreenDegreesTilt).roundToInt()
        val randomYellowDMXPan =
            (P0 + ((P45 + (displayHeight - randomYellowHeadlightBeamViewCurrentPixelY) * stretchFactor - P0) / normalizeToOneDegree) * randomYellowDegreesPan).roundToInt()
        val randomYellowDMXTilt = (T0 + ((T45 - T0) / normalizeToOneDegree) * randomYellowDegreesTilt).roundToInt()
        val randomRedDMXPan =
            (P0 + ((P45 + (displayHeight - randomRedHeadlightBeamViewCurrentPixelY) * stretchFactor - P0) / normalizeToOneDegree) * randomRedDegreesPan).roundToInt()
        val randomRedDMXTilt = (T0 + ((T45 - T0) / normalizeToOneDegree) * randomRedDegreesTilt).roundToInt()

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

        // put values into corresponding JSONObjects
        playerJSONObject.put(playerPanChannel, playerDMXPanForChannel1)
        playerJSONObjectTuning.put(playerPanChannelTuning, playerDMXPanForChannel2)
        playerJSONObject.put(playerTiltChannel, playerDMXTiltForChannel3)
        playerJSONObjectTuning.put(playerTiltChannelTuning, playerDMXTiltForChannel4)

        randomGreenJSONObject.put(randomGreenPanChannel, randomGreenDMXPanForChannel26)
        randomGreenJSONObjectTuning.put(randomGreenPanChannelTuning, randomGreenDMXPanForChannel27)
        randomGreenJSONObject.put(randomGreenTiltChannel, randomGreenDMXTiltForChannel28)
        randomGreenJSONObjectTuning.put(randomGreenTiltChannelTuning, randomGreenDMXTiltForChannel29)

        randomYellowJSONObject.put(randomYellowPanChannel, randomYellowDMXPanForChannel51)
        randomYellowJSONObjectTuning.put(randomYellowPanChannelTuning, randomYellowDMXPanForChannel52)
        randomYellowJSONObject.put(randomYellowTiltChannel, randomYellowDMXTiltForChannel53)
        randomYellowJSONObjectTuning.put(randomYellowTiltChannelTuning, randomYellowDMXTiltForChannel54)

        randomRedJSONObject.put(randomRedPanChannel, randomRedDMXPanForChannel76)
        randomRedJSONObjectTuning.put(randomRedPanChannelTuning, randomRedDMXPanForChannel77)
        randomRedJSONObject.put(randomRedTiltChannel, randomRedDMXTiltForChannel78)
        randomRedJSONObjectTuning.put(randomRedTiltChannelTuning, randomRedDMXTiltForChannel79)
    }

    private fun sendData() {
        dataController.sendData(
            playerJSONObject.toString(),
            playerJSONObjectTuning.toString(),
            randomGreenJSONObject.toString(),
            randomGreenJSONObjectTuning.toString(),
            randomYellowJSONObject.toString(),
            randomYellowJSONObjectTuning.toString(),
            randomRedJSONObject.toString(),
            randomRedJSONObjectTuning.toString()
        )
    }

    fun setPlayerHeadlightBeamViewCurrentPixels(x : Int, y : Int){
        playerHeadlightBeamViewCurrentPixelX = x
        playerHeadlightBeamViewCurrentPixelY = y
    }

    fun setRandomGreenHeadlightBeamViewCurrentPixels(x : Int, y : Int){
        randomGreenHeadlightBeamViewCurrentPixelX = x
        randomGreenHeadlightBeamViewCurrentPixelY = y
    }

    fun setRandomYellowHeadlightBeamViewCurrentPixels(x : Int, y : Int){
        randomYellowHeadlightBeamViewCurrentPixelX = x
        randomYellowHeadlightBeamViewCurrentPixelY = y
    }

    fun setRandomRedHeadlightBeamViewCurrentPixels(x : Int, y : Int){
        randomRedHeadlightBeamViewCurrentPixelX = x
        randomRedHeadlightBeamViewCurrentPixelY = y
    }
}
