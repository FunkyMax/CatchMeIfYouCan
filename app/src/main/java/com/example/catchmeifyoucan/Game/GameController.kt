package com.example.catchmeifyoucan.Game

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import com.example.catchmeifyoucan.Views.BlackBallView
import com.example.catchmeifyoucan.Views.GreenCircleView
import io.github.controlwear.virtual.joystick.android.JoystickView
import kotlin.random.Random

class GameController{
    private var displayWidthBorder = 1340
    private var displayHeightBorder = 2860
    private val blackBallAnimationDuration = 1100L
    private val greenCircleAnimationDuration= 34L
    private val minDistance = 750
    private val maxDistance = 1500
    private val random = Random

    private var catchCounter = 0

    private var strengthFromJoystick = 0.0f
    private var greenCirclesNewXPosition = 0.0f
    private var greenCirclesNewYPosition = 0.0f

    private var blackBallNewXPosition = 0.0f
    private var blackBallNewYPosition = 0.0f

    private lateinit var animatorSet : AnimatorSet
    private lateinit var greenCircleAnimationDirectionX : ObjectAnimator
    private lateinit var greenCircleAnimationDirectionY : ObjectAnimator

    fun moveBlackBallRandomly(blackBall: BlackBallView){
        for (i in 0..10){
            blackBallNewXPosition = random.nextFloat()*displayWidthBorder
            blackBallNewYPosition = random.nextFloat()*displayHeightBorder

            val distance = (Math.sqrt(Math.pow(blackBallNewXPosition.toDouble() - blackBall.x, 2.0) + Math.pow(blackBallNewYPosition - blackBall.y.toDouble(), 2.0)))
            if (distance > minDistance && distance < maxDistance){
                break
            }
        }

        val animationX : ObjectAnimator  = ObjectAnimator.ofFloat(blackBall, "x", blackBallNewXPosition)
        val animationY : ObjectAnimator  = ObjectAnimator.ofFloat(blackBall, "y", blackBallNewYPosition)

        animatorSet = AnimatorSet()
        animatorSet.playTogether(animationX,animationY)
        animatorSet.duration = blackBallAnimationDuration
        animatorSet.start()
    }

    fun moveGreenBallWithJoystick(joystick: JoystickView, greenCircle : GreenCircleView) {
        joystick.setOnMoveListener { angle, strength ->
            strengthFromJoystick = strength * 0.45f

            val newXVector = Math.cos(Math.toRadians(angle.toDouble())).toFloat()
            greenCirclesNewXPosition = greenCircle.x + newXVector * strengthFromJoystick
            val newYVector = Math.sin(Math.toRadians(angle.toDouble())).toFloat()
            greenCirclesNewYPosition = greenCircle.y - newYVector * strengthFromJoystick

            if (greenCircleDoesNotTouchScreenBorders()) {
                greenCircleAnimationDirectionX = ObjectAnimator.ofFloat(
                    greenCircle, "x", greenCirclesNewXPosition
                )
                greenCircleAnimationDirectionY = ObjectAnimator.ofFloat(
                    greenCircle, "y", greenCirclesNewYPosition
                )

                animatorSet = AnimatorSet().apply {
                    playTogether(greenCircleAnimationDirectionX, greenCircleAnimationDirectionY)
                    duration = greenCircleAnimationDuration
                    start()
                }
            }
            else if (greenCircleIsInCorner()) {
                strengthFromJoystick = 0f
            }
            else if (greenCircleTouchesWidthBorders()) {
                greenCircleAnimationDirectionY= ObjectAnimator.ofFloat(
                    greenCircle, "y", greenCirclesNewYPosition).apply {
                    duration = greenCircleAnimationDuration
                    start()
                }
            }
            else if (greenCircleTouchesHeightBorders()) {
                greenCircleAnimationDirectionX = ObjectAnimator.ofFloat(
                    greenCircle, "x", greenCirclesNewXPosition).apply {
                    duration = greenCircleAnimationDuration
                    start()
                }
            }
        }
    }

    fun busted(greenCircleView: GreenCircleView, blackBallView: BlackBallView){
        val greenCircleCoordinates = IntArray(2)
        val blackBallCoordinates = IntArray(2)
        greenCircleView.getLocationOnScreen(greenCircleCoordinates)
        blackBallView.getLocationOnScreen(blackBallCoordinates)
        val greenCircleX = greenCircleCoordinates[0]
        val greenCircleY = greenCircleCoordinates[1]
        val blackBallX = blackBallCoordinates[0]
        val blackBallY = blackBallCoordinates[1]

        val distance = Math.sqrt(Math.pow(greenCircleX.toDouble() - blackBallX, 2.0) + Math.pow(greenCircleY - blackBallY.toDouble(), 2.0))
        if(distance <= 50 ){
            catchCounter +=1
            println(catchCounter)
            if (catchCounter == 3){
                println("SUCCESS")
                greenCircleView.x = 700f
                greenCircleView.y = 2300f
                catchCounter = 0
            }
        }
    }

    private fun greenCircleTouchesHeightBorders() = greenCirclesNewYPosition > displayHeightBorder || greenCirclesNewYPosition <= 0

    private fun greenCircleTouchesWidthBorders() = greenCirclesNewXPosition > displayWidthBorder || greenCirclesNewXPosition <= 0

    private fun greenCircleIsInCorner() =
        (greenCirclesNewXPosition > displayWidthBorder && greenCirclesNewYPosition <= 0) || (greenCirclesNewXPosition <= 0 && greenCirclesNewYPosition <= 0) || (greenCirclesNewXPosition > displayWidthBorder && greenCirclesNewYPosition > displayHeightBorder) || (greenCirclesNewXPosition <= 0 && greenCirclesNewYPosition > displayHeightBorder)

    private fun greenCircleDoesNotTouchScreenBorders() =
        greenCirclesNewXPosition < displayWidthBorder && greenCirclesNewXPosition >= 0 && greenCirclesNewYPosition < displayHeightBorder && greenCirclesNewYPosition >= 0


    fun getXCoordinate(greenBall: GreenCircleView):Float{
        return greenBall.x
    }

    fun getYCoordinate(greenBall: GreenCircleView):Float{
        return greenBall.y
    }
}