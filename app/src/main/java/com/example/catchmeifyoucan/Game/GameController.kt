package com.example.catchmeifyoucan.Game

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.view.View
import com.example.catchmeifyoucan.Activities.GameActivity
import com.example.catchmeifyoucan.views.PlayerHeadlightBeamView
import com.example.catchmeifyoucan.views.RandomBlueHeadlightBeamView
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
private const val collisionMaxDistance = 50
private const val collisionForbiddenDuration = 2500L
private const val gameDuration = 45000L
private const val randomYellowAppearanceDuration = 7500L
private const val power = 2.0
private const val visualCollisionBrightnessFeedback = 20
private const val visualCollisionBrightnessFeedbackReset = 80

class GameController{
    // reference to DataController to be able to send out Data
    private val dataController = GameActivity.dataController

    // game specific fields
    private val random = Random
    private var score = 0
    private var strengthFromJoystick = 0f
    private lateinit var animatorSet: AnimatorSet

    // player specific fields
    private val playerHeadlightBeamAnimationDuration = 34L
    private val playerHeadlightBeamViewCurrentCoordinates = IntArray(2)
    private var playerHeadlightBeamViewCurrentX = 0f
    private var playerHeadlightBeamViewCurrentY = 0f
    private var playerHeadlightBeamViewNextX = 0f
    private var playerHeadlightBeamViewNextY = 0f
    private lateinit var playerHeadlightBeamViewAnimationDirectionLengthX: ObjectAnimator
    private lateinit var playerHeadlightBeamViewAnimationDirectionLengthY: ObjectAnimator

    // randomBlueHeadlightBeam specific fields
    private val randomBlueHeadlightBeamAnimationDuration = 2000L
    private val randomBlueHeadlightBeamViewCurrentCoordinates = IntArray(2)
    private var randomBlueHeadlightBeamViewCurrentX = 0f
    private var randomBlueHeadlightBeamViewCurrentY = 0f
    private var randomBlueHeadlightBeamViewNextX = 0f
    private var randomBlueHeadlightBeamViewNextY = 0f
    private val randomBlueHeadlightMinDistanceToNextPosition = 1000
    private val randomBlueHeadlightMaxDistanceToNextPosition = 1500

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

    fun moveRandomHeadlightBeamView(view: View?) {
        if (view is RandomBlueHeadlightBeamView) {
            moveViewRandomly(
                view,
                randomBlueHeadlightBeamViewNextX,
                randomBlueHeadlightBeamViewNextY,
                randomBlueHeadlightMinDistanceToNextPosition,
                randomBlueHeadlightMaxDistanceToNextPosition,
                randomBlueHeadlightBeamAnimationDuration
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

    fun movePlayerHeadlightBeamViewWithJoystick(joystick: JoystickView, playerHeadlightBeamView : PlayerHeadlightBeamView) {
        joystick.setOnMoveListener { angle, strength ->
            strengthFromJoystick = strength * 0.45f

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
    }


    fun collisionDetection(
        playerHeadlightBeamView: PlayerHeadlightBeamView,
        randomBlueHeadlightBeamView: RandomBlueHeadlightBeamView,
        randomYellowHeadlightBeamView: RandomYellowHeadlightBeamView,
        randomRedHeadlightBeamView: RandomRedHeadlightBeamView
    ) {
        playerHeadlightBeamView.getLocationOnScreen(playerHeadlightBeamViewCurrentCoordinates)
        randomBlueHeadlightBeamView.getLocationOnScreen(randomBlueHeadlightBeamViewCurrentCoordinates)
        randomYellowHeadlightBeamView.getLocationOnScreen(randomYellowHeadlightBeamViewCurrentCoordinates)
        randomRedHeadlightBeamView.getLocationOnScreen(randomRedHeadlightBeamViewCurrentCoordinates)

        //Getting the current coordinates of views.
        playerHeadlightBeamViewCurrentX = playerHeadlightBeamViewCurrentCoordinates[0].toFloat()
        playerHeadlightBeamViewCurrentY = playerHeadlightBeamViewCurrentCoordinates[1].toFloat()
        randomBlueHeadlightBeamViewCurrentX = randomBlueHeadlightBeamViewCurrentCoordinates[0].toFloat()
        randomBlueHeadlightBeamViewCurrentY = randomBlueHeadlightBeamViewCurrentCoordinates[1].toFloat()
        randomYellowHeadlightBeamViewCurrentX = randomYellowHeadlightBeamViewCurrentCoordinates[0].toFloat()
        randomYellowHeadlightBeamViewCurrentY = randomYellowHeadlightBeamViewCurrentCoordinates[1].toFloat()
        randomRedHeadlightBeamViewCurrentX = randomRedHeadlightBeamViewCurrentCoordinates[0].toFloat()
        randomRedHeadlightBeamViewCurrentY = randomRedHeadlightBeamViewCurrentCoordinates[1].toFloat()

        val distanceBetweenPlayerAndRandomBlueHeadlightBeamView =
            Math.sqrt(
                Math.pow(
                    playerHeadlightBeamViewCurrentX.toDouble() - randomBlueHeadlightBeamViewCurrentX,
                    power
                ) + Math.pow(playerHeadlightBeamViewCurrentY - randomBlueHeadlightBeamViewCurrentY.toDouble(), power)
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

        //if (collisionAllowed) {
        if (distanceBetweenPlayerAndRandomBlueHeadlightBeamView <= collisionMaxDistance && randomBlueHeadlightBeamView.alpha == 1f) {
                score += 2
                disableCollisions(randomBlueHeadlightBeamView)
        } else if (distanceBetweenPlayerAndRandomYellowHeadlightBeamView <= collisionMaxDistance && randomYellowHeadlightBeamView.alpha == 1f) {
                score += 5
                disableCollisions(randomYellowHeadlightBeamView)
        } else if (distanceBetweenPlayerAndRandomRedHeadlightBeamView <= collisionMaxDistance && randomRedHeadlightBeamView.alpha == 1f) {
                score -= 3
                disableCollisions(randomRedHeadlightBeamView)
            }
        //}
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
            randomHeadlightBeamView.alpha = .05f
            resolveCollisionData(randomHeadlightBeamView, visualCollisionBrightnessFeedback)
            delay(collisionForbiddenDuration)
            randomHeadlightBeamView.alpha = 1f
            resolveCollisionData(randomHeadlightBeamView, visualCollisionBrightnessFeedbackReset)
        }
    }

    private fun resolveCollisionData(randomHeadlightBeamView: View, feedback: Int) {
        val jsonObject = JSONObject()
        jsonObject.put("C", feedback)

        if (randomHeadlightBeamView is RandomBlueHeadlightBeamView) {
            jsonObject.put("32", feedback)
        } else if (randomHeadlightBeamView is RandomYellowHeadlightBeamView) {
            jsonObject.put("57", feedback)
        } else if (randomHeadlightBeamView is RandomRedHeadlightBeamView) {
            jsonObject.put("82", (feedback / 2) - 1)
        }

        dataController.setCollisionData(jsonObject.toString())
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

    fun getRandomBlueHeadlightBeamViewCurrentX(): Int {
        return randomBlueHeadlightBeamViewCurrentX.roundToInt()
    }

    fun getRandomBlueHeadlightBeamViewCurrentY(): Int {
        return randomBlueHeadlightBeamViewCurrentY.roundToInt()
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