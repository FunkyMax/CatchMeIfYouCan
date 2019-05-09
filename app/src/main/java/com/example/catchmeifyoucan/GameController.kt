package com.example.catchmeifyoucan

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import com.example.catchmeifyoucan.views.BlackBallView
import com.example.catchmeifyoucan.views.GreenCircleView
import io.github.controlwear.virtual.joystick.android.JoystickView
import kotlin.random.Random

class GameController{
    private var displayWidthBorder = 1340
    private var displayHeightBorder = 2860
    private val blackBallAnimationDuration = 700L
    private val greenCircleAnimationDuration= 34L
    private val random = Random

    private var strengthFromJoystick = 0.0f
    private var greenCirclesNewXPosition = 0.0f
    private var greenCirclesNewYPosition = 0.0f

    private var blackBallsNewXPosition = 0.0f
    private var blackBallsNewYPosition = 0.0f

    private lateinit var animatorSet : AnimatorSet
    private lateinit var greenCircleAnimationDirectionX : ObjectAnimator
    private lateinit var greenCircleAnimationDirectionY : ObjectAnimator

    private lateinit var blackBallAnimationDirectionX : ObjectAnimator
    private lateinit var blackBallAnimationDirectionY : ObjectAnimator


    fun moveBlackBallRandomly(blackBall: BlackBallView){
        val animationX : ObjectAnimator  = ObjectAnimator.ofFloat(blackBall, "x", random.nextFloat()*1000)
        val animationY : ObjectAnimator  = ObjectAnimator.ofFloat(blackBall, "y", random.nextFloat()*2700)

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
            } else {
                if (greenCircleIsInCorner()) {
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