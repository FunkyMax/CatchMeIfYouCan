package com.example.catchmeifyoucan.Game

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import com.example.catchmeifyoucan.views.PlayerHeadlightBeamView
import com.example.catchmeifyoucan.views.RandomHeadlightBeamView
import io.github.controlwear.virtual.joystick.android.JoystickView
import kotlin.math.roundToInt
import kotlin.random.Random

class GameController{
    private val displayWidthBorder = 2860
    private val displayHeightBorder = 1340
    private val playerHeadlightBeamViewResetX = 700f
    private val playerHeadlightBeamViewResetY = 2300f
    private val playerHeadlightBeamAnimationDuration= 34L
    private val randomHeadlightBeamAnimationDuration = 1600L
    private val randomHeadlightMinDistanceToNextPosition = 750
    private val randomHeadlightMaxDistanceToNextPosition = 1500
    private val random = Random

    private var catchCounter = 0
    private var strengthFromJoystick = 0f

    private val playerHeadlightBeamViewCurrentCoordinates = IntArray(2)
    private var playerHeadlightBeamViewCurrentX = 0f
    private var playerHeadlightBeamViewCurrentY = 0f
    private var playerHeadlightBeamViewNextX = 0f
    private var playerHeadlightBeamViewNextY = 0f

    private val randomHeadlightBeamViewCurrentCoordinates = IntArray(2)
    private var randomHeadlightBeamViewCurrentX = 0f
    private var randomHeadlightBeamViewCurrentY = 0f
    private var randomHeadlightBeamViewNextX = 0f
    private var randomHeadlightBeamViewNextY = 0f

    private lateinit var animatorSet : AnimatorSet
    private lateinit var playerHeadlightBeamViewAnimationDirectionLengthX : ObjectAnimator
    private lateinit var playerHeadlightBeamViewAnimationDirectionLengthY : ObjectAnimator

    fun moveRandomHeadlightBeamView(randomHeadlightBeamView: RandomHeadlightBeamView){
        for (i in 0..10){
            randomHeadlightBeamViewNextX = random.nextFloat() * displayWidthBorder
            randomHeadlightBeamViewNextY = random.nextFloat() * displayHeightBorder

            val distance = (Math.sqrt(Math.pow(randomHeadlightBeamViewNextX.toDouble() - randomHeadlightBeamView.x, 2.0) + Math.pow(randomHeadlightBeamViewNextY - randomHeadlightBeamView.y.toDouble(), 2.0)))
            if (distance > randomHeadlightMinDistanceToNextPosition && distance < randomHeadlightMaxDistanceToNextPosition) {
                break
            }
        }

        val animationX : ObjectAnimator  = ObjectAnimator.ofFloat(randomHeadlightBeamView, "x", randomHeadlightBeamViewNextX)
        val animationY : ObjectAnimator  = ObjectAnimator.ofFloat(randomHeadlightBeamView, "y", randomHeadlightBeamViewNextY)

        animatorSet = AnimatorSet()
        animatorSet.playTogether(animationX,animationY)
        animatorSet.duration = randomHeadlightBeamAnimationDuration
        animatorSet.start()
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


    //TODO: Collision Detection muss noch angepasst werden. Erst nach 3 menschlich wahrgenommenen Kollisionen soll die playerHeadlightBeamView Position zurückgesetzt werden.
    fun collisionDetection(playerHeadlightBeamView: PlayerHeadlightBeamView, randomHeadlightBeamView: RandomHeadlightBeamView){
        playerHeadlightBeamView.getLocationOnScreen(playerHeadlightBeamViewCurrentCoordinates)
        randomHeadlightBeamView.getLocationOnScreen(randomHeadlightBeamViewCurrentCoordinates)

        //Getting the current coordinates of the 2 views.
        playerHeadlightBeamViewCurrentX = playerHeadlightBeamViewCurrentCoordinates[0].toFloat()
        playerHeadlightBeamViewCurrentY = playerHeadlightBeamViewCurrentCoordinates[1].toFloat()
        randomHeadlightBeamViewCurrentX = randomHeadlightBeamViewCurrentCoordinates[0].toFloat()
        randomHeadlightBeamViewCurrentY = randomHeadlightBeamViewCurrentCoordinates[1].toFloat()

        val distance = Math.sqrt(
            Math.pow(
                playerHeadlightBeamViewCurrentX.toDouble() - randomHeadlightBeamViewCurrentX,
                2.0
            ) + Math.pow(playerHeadlightBeamViewCurrentY - randomHeadlightBeamViewCurrentY.toDouble(), 2.0)
        )
        if(distance <= 50 ){
            //println(distance)
            //catchCounter +=1
            //println(catchCounter)
            if (catchCounter == 3){
                //println("SUCCESS")
                playerHeadlightBeamViewCurrentX = playerHeadlightBeamViewResetX
                playerHeadlightBeamViewCurrentY = playerHeadlightBeamViewResetY
                catchCounter = 0
            }
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

    fun getRandomHeadlightBeamViewCurrentX(): Int {
        return randomHeadlightBeamViewCurrentX.roundToInt()
    }

    fun getRandomHeadlightBeamViewCurrentY(): Int {
        return randomHeadlightBeamViewCurrentY.roundToInt()
    }
}