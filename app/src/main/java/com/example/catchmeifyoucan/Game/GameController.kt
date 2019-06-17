package com.example.catchmeifyoucan.Game

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.os.Handler
import android.view.View
import android.widget.TextView
import com.example.catchmeifyoucan.views.PlayerHeadlightBeamView
import com.example.catchmeifyoucan.views.RandomGreenHeadlightBeamView
import com.example.catchmeifyoucan.views.RandomRedHeadlightBeamView
import com.example.catchmeifyoucan.views.RandomYellowHeadlightBeamView
import io.github.controlwear.virtual.joystick.android.JoystickView
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import org.json.JSONObject
import kotlin.math.roundToInt
import kotlin.random.Random

private const val displayWidthBorder = 2860
private const val displayHeightBorder = 1340
private const val collisionMaxDistance = 75
private const val collisionForbiddenDuration = 2500L
private const val randomYellowAppearanceDuration = 7500L
private const val power = 2.0
private const val visualCollisionBrightnessFeedback = 1
private const val visualCollisionBrightnessFeedbackReset = 100

private const val EMPTY_STRING = ""
private const val randomGreenBrightnessChannel = "32"
private const val randomYellowBrightnessChannel = "57"
private const val randomRedBrightnessChannel = "82"

public const val gameDuration = 45000L

class GameController{

    companion object{
        // reference to DataController to be able to send out Data
        val dataController = DataController()
    }

    // game specific fields
    private val startTime = System.currentTimeMillis()
    private val random = Random
    private var strengthFromJoystick = 0f
    private var speed = 0.45f
    public var score = 0
    private lateinit var viewsCoordinatesTranslator : ViewsCoordinatesTranslator
    private lateinit var animatorSet: AnimatorSet

    // JSONObject which is sent after the game has finished in order to reset the pan and tilt values of each MH
    private val resetMHsJSONObject = JSONObject()

    // player specific fields
    private val playerHeadlightBeamAnimationDuration = 34L
    private val playerHeadlightBeamViewCurrentCoordinates = IntArray(2)
    private var playerHeadlightBeamViewCurrentX = 0f
    private var playerHeadlightBeamViewCurrentY = 0f
    private var playerHeadlightBeamViewNextX = 0f
    private var playerHeadlightBeamViewNextY = 0f
    private lateinit var playerHeadlightBeamViewAnimationDirectionLengthX: ObjectAnimator
    private lateinit var playerHeadlightBeamViewAnimationDirectionLengthY: ObjectAnimator

    // randomGreenHeadlightBeam specific fields
    private val randomGreenHeadlightBeamAnimationDuration = 2000L
    private val randomGreenHeadlightBeamViewCurrentCoordinates = IntArray(2)
    private var randomGreenHeadlightBeamViewCurrentX = 0f
    private var randomGreenHeadlightBeamViewCurrentY = 0f
    private var randomGreenHeadlightBeamViewNextX = 0f
    private var randomGreenHeadlightBeamViewNextY = 0f
    private val randomGreenHeadlightMinDistanceToNextPosition = 1000
    private val randomGreenHeadlightMaxDistanceToNextPosition = 1500

    // randomYellowHeadlightBeam specific fields
    private val randomYellowHeadlightBeamAnimationDuration = 1050L
    private val randomYellowHeadlightBeamViewCurrentCoordinates = IntArray(2)
    private var randomYellowHeadlightBeamViewCurrentX = 0f
    private var randomYellowHeadlightBeamViewCurrentY = 0f
    private var randomYellowHeadlightBeamViewNextX = 0f
    private var randomYellowHeadlightBeamViewNextY = 0f
    private val randomYellowHeadlightMinDistanceToNextPosition = 1000
    private val randomYellowHeadlightMaxDistanceToNextPosition = 1250

    // randomYellowHeadlightBeam specific fields
    private val randomRedHeadlightBeamAnimationDuration = 1400L
    private val randomRedHeadlightBeamViewCurrentCoordinates = IntArray(2)
    private var randomRedHeadlightBeamViewCurrentX = 0f
    private var randomRedHeadlightBeamViewCurrentY = 0f
    private var randomRedHeadlightBeamViewNextX = 0f
    private var randomRedHeadlightBeamViewNextY = 0f
    private val randomRedHeadlightMinDistanceToNextPosition = 750
    private val randomRedHeadlightMaxDistanceToNextPosition = 1200

    // Initializing the necessary Handlers
    private val playerHeadlightBeamViewHandler = Handler()
    private val randomHeadlightBeamViewsHandler = Handler()
    private val randomYellowHeadlightBeamViewVisibilityHandler = Handler()
    private val viewsCoordinatesTranslatorHandler = Handler()

    private lateinit var joystickView: JoystickView
    private lateinit var playerHeadlightBeamView: PlayerHeadlightBeamView
    private lateinit var randomGreenHeadlightBeamView: RandomGreenHeadlightBeamView
    private lateinit var randomYellowHeadlightBeamView: RandomYellowHeadlightBeamView
    private lateinit var randomRedHeadlightBeamView: RandomRedHeadlightBeamView
    private lateinit var scoreTextView: TextView
    private lateinit var timeTextView: TextView

    fun initializeGameController(views : Array<View>){

        viewsCoordinatesTranslator = ViewsCoordinatesTranslator()

        // Setting the views
        joystickView = views.get(0) as JoystickView
        playerHeadlightBeamView = views.get(1) as PlayerHeadlightBeamView
        randomGreenHeadlightBeamView = views.get(2) as RandomGreenHeadlightBeamView
        randomYellowHeadlightBeamView = views.get(3) as RandomYellowHeadlightBeamView
        randomRedHeadlightBeamView = views.get(4) as RandomRedHeadlightBeamView
        scoreTextView = views.get(5) as TextView
        timeTextView = views.get(6) as TextView

        // Setting the runnables
        val playerHeadlightBeamViewRunnable = object : Runnable {
            override fun run() {
                movePlayerHeadlightBeamViewWithJoystick(joystickView, playerHeadlightBeamView)
                collisionDetection(
                    playerHeadlightBeamView,
                    randomGreenHeadlightBeamView,
                    randomYellowHeadlightBeamView,
                    randomRedHeadlightBeamView
                )
                playerHeadlightBeamViewHandler.postDelayed(this, 17)
            }
        }

        val randomGreenHeadlightBeamViewRunnable = object : Runnable {
            override fun run() {
                moveRandomHeadlightBeamView(randomGreenHeadlightBeamView)
                randomHeadlightBeamViewsHandler.postDelayed(this, 1300)
            }
        }

        val randomYellowHeadlightBeamViewRunnable = object : Runnable {
            override fun run() {
                moveRandomHeadlightBeamView(randomYellowHeadlightBeamView)
                randomHeadlightBeamViewsHandler.postDelayed(this, 1000)
            }
        }

        val randomRedHeadlightBeamViewRunnable = object : Runnable {
            override fun run() {
                moveRandomHeadlightBeamView(randomRedHeadlightBeamView)
                randomHeadlightBeamViewsHandler.postDelayed(this, 1100)
            }
        }

        val randomYellowHeadlightBeamViewVisibilityRunnable = object : Runnable {
            override fun run() {
                val alpha = if (randomYellowHeadlightBeamView.alpha == 0.01f) 1f else 0.01f
                randomYellowHeadlightBeamView.alpha = alpha
                dataController.setRandomYellowMovingHeadBrightnessData(
                    getCorrespondingBrightnessJSONObject(
                        randomYellowBrightnessChannel,
                        (alpha * 100).toInt()
                    )
                )
                randomHeadlightBeamViewsHandler.postDelayed(this, randomYellowAppearanceDuration)
            }
        }

        val viewsCoordinatesTranslatorRunnable = object : Runnable {
            override fun run() {
                viewsCoordinatesTranslator.translateCoordinatesAndSendToBluetoothModule()
                viewsCoordinatesTranslatorHandler.postDelayed(this, 9 * DELAY)
            }
        }

        // Run the runnables
        randomGreenHeadlightBeamViewRunnable.run()
        randomYellowHeadlightBeamViewRunnable.run()
        randomYellowHeadlightBeamViewVisibilityRunnable.run()
        randomRedHeadlightBeamViewRunnable.run()
        viewsCoordinatesTranslatorRunnable.run()
        playerHeadlightBeamViewRunnable.run()

        // Fill resetMHsJSONObject with reset pan and tilt values
        resetMHsJSONObject.put("R", 1)
    }

    fun endGame(){
        playerHeadlightBeamViewHandler.removeCallbacksAndMessages(null)
        randomHeadlightBeamViewsHandler.removeCallbacksAndMessages(null)
        randomYellowHeadlightBeamViewVisibilityHandler.removeCallbacksAndMessages(null)
        viewsCoordinatesTranslatorHandler.removeCallbacksAndMessages(null)
        GlobalScope.launch {
            delay(100)
            dataController.setResetData(resetMHsJSONObject.toString())
        }
    }

    private fun moveRandomHeadlightBeamView(view: View?) {
        if (view is RandomGreenHeadlightBeamView) {
            moveViewRandomly(
                view,
                randomGreenHeadlightBeamViewNextX,
                randomGreenHeadlightBeamViewNextY,
                randomGreenHeadlightMinDistanceToNextPosition,
                randomGreenHeadlightMaxDistanceToNextPosition,
                randomGreenHeadlightBeamAnimationDuration
            )
        } else if (view is RandomYellowHeadlightBeamView) {
            moveViewRandomly(
                view,
                randomYellowHeadlightBeamViewNextX,
                randomYellowHeadlightBeamViewNextY,
                randomYellowHeadlightMinDistanceToNextPosition,
                randomYellowHeadlightMaxDistanceToNextPosition,
                randomYellowHeadlightBeamAnimationDuration
            )
        } else if (view is RandomRedHeadlightBeamView) {
            moveViewRandomly(
                view,
                randomRedHeadlightBeamViewNextX,
                randomRedHeadlightBeamViewNextY,
                randomRedHeadlightMinDistanceToNextPosition,
                randomRedHeadlightMaxDistanceToNextPosition,
                randomRedHeadlightBeamAnimationDuration
            )
        }
    }

    private fun movePlayerHeadlightBeamViewWithJoystick(joystick: JoystickView, playerHeadlightBeamView : PlayerHeadlightBeamView) {
        joystick.setOnMoveListener { angle, strength ->
            strengthFromJoystick = strength * speed

            val newXVector = Math.cos(Math.toRadians(angle.toDouble())).toFloat()
            playerHeadlightBeamViewNextX = playerHeadlightBeamView.x + newXVector * strengthFromJoystick
            val newYVector = Math.sin(Math.toRadians(angle.toDouble())).toFloat()
            playerHeadlightBeamViewNextY = playerHeadlightBeamView.y - newYVector * strengthFromJoystick

            if (playerHeadlightBeamViewDoesNotTouchScreenBorders()) {
                playerHeadlightBeamViewAnimationDirectionLengthX = ObjectAnimator.ofFloat(
                    playerHeadlightBeamView, "x", playerHeadlightBeamViewNextX
                )
                playerHeadlightBeamViewAnimationDirectionLengthY = ObjectAnimator.ofFloat(
                    playerHeadlightBeamView, "y", playerHeadlightBeamViewNextY
                )

                animatorSet = AnimatorSet().apply {
                    playTogether(playerHeadlightBeamViewAnimationDirectionLengthX, playerHeadlightBeamViewAnimationDirectionLengthY)
                    duration = playerHeadlightBeamAnimationDuration
                    start()
                }
            }
            else if (playerHeadlightBeamViewIsInCorner()) {
                strengthFromJoystick = 0f
            }
            else if (playerHeadlightBeamViewTouchesWidthBorders()) {
                playerHeadlightBeamViewAnimationDirectionLengthY= ObjectAnimator.ofFloat(
                    playerHeadlightBeamView, "y", playerHeadlightBeamViewNextY).apply {
                    duration = playerHeadlightBeamAnimationDuration
                    start()
                }
            }
            else if (playerHeadlightBeamViewTouchesHeightBorders()) {
                playerHeadlightBeamViewAnimationDirectionLengthX = ObjectAnimator.ofFloat(
                    playerHeadlightBeamView, "x", playerHeadlightBeamViewNextX).apply {
                    duration = playerHeadlightBeamAnimationDuration
                    start()
                }
            }
        }
        timeTextView.text = ((gameDuration - (System.currentTimeMillis() - startTime))/1000+1).toInt().toString()
    }


    private fun collisionDetection(
        playerHeadlightBeamView: PlayerHeadlightBeamView,
        randomGreenHeadlightBeamView: RandomGreenHeadlightBeamView,
        randomYellowHeadlightBeamView: RandomYellowHeadlightBeamView,
        randomRedHeadlightBeamView: RandomRedHeadlightBeamView
    ) {
        playerHeadlightBeamView.getLocationOnScreen(playerHeadlightBeamViewCurrentCoordinates)
        randomGreenHeadlightBeamView.getLocationOnScreen(randomGreenHeadlightBeamViewCurrentCoordinates)
        randomYellowHeadlightBeamView.getLocationOnScreen(randomYellowHeadlightBeamViewCurrentCoordinates)
        randomRedHeadlightBeamView.getLocationOnScreen(randomRedHeadlightBeamViewCurrentCoordinates)

        //Getting the current coordinates of views.
        playerHeadlightBeamViewCurrentX = playerHeadlightBeamViewCurrentCoordinates[0].toFloat()
        playerHeadlightBeamViewCurrentY = playerHeadlightBeamViewCurrentCoordinates[1].toFloat()
        randomGreenHeadlightBeamViewCurrentX = randomGreenHeadlightBeamViewCurrentCoordinates[0].toFloat()
        randomGreenHeadlightBeamViewCurrentY = randomGreenHeadlightBeamViewCurrentCoordinates[1].toFloat()
        randomYellowHeadlightBeamViewCurrentX = randomYellowHeadlightBeamViewCurrentCoordinates[0].toFloat()
        randomYellowHeadlightBeamViewCurrentY = randomYellowHeadlightBeamViewCurrentCoordinates[1].toFloat()
        randomRedHeadlightBeamViewCurrentX = randomRedHeadlightBeamViewCurrentCoordinates[0].toFloat()
        randomRedHeadlightBeamViewCurrentY = randomRedHeadlightBeamViewCurrentCoordinates[1].toFloat()

        val distanceBetweenPlayerAndRandomGreenHeadlightBeamView =
            Math.sqrt(
                Math.pow(
                    playerHeadlightBeamViewCurrentX.toDouble() - randomGreenHeadlightBeamViewCurrentX,
                    power
                ) + Math.pow(playerHeadlightBeamViewCurrentY - randomGreenHeadlightBeamViewCurrentY.toDouble(), power)
            )

        val distanceBetweenPlayerAndRandomYellowHeadlightBeamView =
            Math.sqrt(
                Math.pow(
                    playerHeadlightBeamViewCurrentX.toDouble() - randomYellowHeadlightBeamViewCurrentX,
                    power
                ) + Math.pow(playerHeadlightBeamViewCurrentY - randomYellowHeadlightBeamViewCurrentY.toDouble(), power)
            )

        val distanceBetweenPlayerAndRandomRedHeadlightBeamView =
            Math.sqrt(
                Math.pow(
                    playerHeadlightBeamViewCurrentX.toDouble() - randomRedHeadlightBeamViewCurrentX,
                    power
                ) + Math.pow(playerHeadlightBeamViewCurrentY - randomRedHeadlightBeamViewCurrentY.toDouble(), power)
            )

        if (distanceBetweenPlayerAndRandomGreenHeadlightBeamView <= collisionMaxDistance && distanceBetweenPlayerAndRandomGreenHeadlightBeamView > 0 && randomGreenHeadlightBeamView.alpha == 1f) {
                score += 2
            speed += 0.1f
            disableCollisions(randomGreenHeadlightBeamView)
        } else if (distanceBetweenPlayerAndRandomYellowHeadlightBeamView <= collisionMaxDistance && distanceBetweenPlayerAndRandomYellowHeadlightBeamView > 0 && randomYellowHeadlightBeamView.alpha == 1f) {
                score += 5
            speed += 0.1f
                disableCollisions(randomYellowHeadlightBeamView)
        } else if (distanceBetweenPlayerAndRandomRedHeadlightBeamView <= collisionMaxDistance && distanceBetweenPlayerAndRandomRedHeadlightBeamView > 0 && randomRedHeadlightBeamView.alpha == 1f) {
                score -= 3
            speed = 0.45f
                disableCollisions(randomRedHeadlightBeamView)
        }
        scoreTextView.text = score.toString()
    }

    private fun moveViewRandomly(
        view: View,
        viewNextX: Float,
        viewNextY: Float,
        viewMinDistance: Int,
        viewMaxDistance: Int,
        viewAnimationDuration: Long
    ) {
        var viewNextX = viewNextX
        var viewNextY = viewNextY
        for (i in 0..10) {
            viewNextX = random.nextFloat() * displayWidthBorder
            viewNextY = random.nextFloat() * displayHeightBorder

            val distance = (Math.sqrt(
                Math.pow(viewNextX.toDouble() - view.x, power) + Math.pow(
                    viewNextY.toDouble() - view.y,
                    power
                )
            ))
            if (distance > viewMinDistance && distance < viewMaxDistance) {
                break
            }
        }

        val animationX: ObjectAnimator =
            ObjectAnimator.ofFloat(view, "x", viewNextX)
        val animationY: ObjectAnimator =
            ObjectAnimator.ofFloat(view, "y", viewNextY)

        animatorSet = AnimatorSet()
        animatorSet.playTogether(animationX, animationY)
        animatorSet.duration = viewAnimationDuration
        animatorSet.start()
    }

    private fun disableCollisions(randomHeadlightBeamView: View) {
        println(score)

        GlobalScope.launch {
            randomHeadlightBeamView.alpha = .01f
            dataController
            resolveCollisionData(randomHeadlightBeamView, visualCollisionBrightnessFeedback)
            delay(collisionForbiddenDuration)
            randomHeadlightBeamView.alpha = 1f
            resolveCollisionData(randomHeadlightBeamView, visualCollisionBrightnessFeedbackReset)
        }
    }

    private fun resolveCollisionData(randomHeadlightBeamView: View, feedback: Int) {
        if (randomHeadlightBeamView is RandomGreenHeadlightBeamView) {
            dataController.setRandomGreenMovingHeadBrightnessData(
                getCorrespondingBrightnessJSONObject(
                    randomGreenBrightnessChannel,
                    feedback
                )
            )
        } else if (randomHeadlightBeamView is RandomYellowHeadlightBeamView) {
            dataController.setRandomYellowMovingHeadBrightnessData(
                getCorrespondingBrightnessJSONObject(
                    randomYellowBrightnessChannel,
                    feedback
                )
            )
        } else if (randomHeadlightBeamView is RandomRedHeadlightBeamView) {
            dataController.setRandomRedMovingHeadBrightnessData(
                getCorrespondingBrightnessJSONObject(
                    randomRedBrightnessChannel,
                    feedback
                )
            )
        }
    }

    private fun getCorrespondingBrightnessJSONObject(channel: String, brightness: Int): String {
        val jsonObject = JSONObject()
        jsonObject.put("B", 1)
        jsonObject.put(channel, brightness)
        //transferDataToDataControllerAndReleaseJSONObjectAfterwards(jsonObject)
        return jsonObject.toString()
    }

    private fun transferDataToDataControllerAndReleaseJSONObjectAfterwards(jsonObject: JSONObject) {
        GlobalScope.launch {
            if (jsonObject.has(randomGreenBrightnessChannel)) dataController.setRandomGreenMovingHeadBrightnessData(
                jsonObject.toString()
            )
            else if (jsonObject.has(randomYellowBrightnessChannel)) dataController.setRandomYellowMovingHeadBrightnessData(
                jsonObject.toString()
            )
            else if (jsonObject.has(randomRedBrightnessChannel)) dataController.setRandomRedMovingHeadBrightnessData(
                jsonObject.toString()
            )
            delay(collisionForbiddenDuration)
            dataController.resetEachMHsJSONBrightnessData()
        }
    }

    private fun playerHeadlightBeamViewTouchesHeightBorders() = playerHeadlightBeamViewNextY > displayHeightBorder || playerHeadlightBeamViewNextY <= 0

    private fun playerHeadlightBeamViewTouchesWidthBorders() = playerHeadlightBeamViewNextX > displayWidthBorder || playerHeadlightBeamViewNextX <= 0

    private fun playerHeadlightBeamViewIsInCorner() =
        (playerHeadlightBeamViewNextX > displayWidthBorder && playerHeadlightBeamViewNextY <= 0) || (playerHeadlightBeamViewNextX <= 0 && playerHeadlightBeamViewNextY <= 0) || (playerHeadlightBeamViewNextX > displayWidthBorder && playerHeadlightBeamViewNextY > displayHeightBorder) || (playerHeadlightBeamViewNextX <= 0 && playerHeadlightBeamViewNextY > displayHeightBorder)

    private fun playerHeadlightBeamViewDoesNotTouchScreenBorders() =
        playerHeadlightBeamViewNextX < displayWidthBorder && playerHeadlightBeamViewNextX >= 0 && playerHeadlightBeamViewNextY < displayHeightBorder && playerHeadlightBeamViewNextY >= 0


    fun getPlayerHeadlightBeamViewCurrentX(): Int {
        return playerHeadlightBeamViewCurrentX.roundToInt()
    }

    fun getPlayerHeadlightBeamViewCurrentY(): Int {
        return playerHeadlightBeamViewCurrentY.roundToInt()
    }

    fun getRandomGreenHeadlightBeamViewCurrentX(): Int {
        return randomGreenHeadlightBeamViewCurrentX.roundToInt()
    }

    fun getRandomGreenHeadlightBeamViewCurrentY(): Int {
        return randomGreenHeadlightBeamViewCurrentY.roundToInt()
    }

    fun getRandomYellowHeadlightBeamViewCurrentX(): Int {
        return randomYellowHeadlightBeamViewCurrentX.roundToInt()
    }

    fun getRandomYellowHeadlightBeamViewCurrentY(): Int {
        return randomYellowHeadlightBeamViewCurrentY.roundToInt()
    }

    fun getRandomRedHeadlightBeamViewCurrentX(): Int {
        return randomRedHeadlightBeamViewCurrentX.roundToInt()
    }

    fun getRandomRedHeadlightBeamViewCurrentY(): Int {
        return randomRedHeadlightBeamViewCurrentY.roundToInt()
    }
}