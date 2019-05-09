package com.example.catchmeifyoucan

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.widget.ImageView
import com.example.catchmeifyoucan.views.CircleView
import io.github.controlwear.virtual.joystick.android.JoystickView
import kotlin.random.Random

class GameController{
    private val displayWidth = 1380
    private val displayHeight = 2900
    private val blackBallAnimationDuration = 700L
    private val greenCircleAnimationDuration= 34L

    private var angleFromJoystick = 0.0
    private var strengthFromJoystick = 0.0f
    private var newXPosition = 0.0f
    private var newYPosition = 0.0f

    private var random = Random

    private lateinit var animatorSet : AnimatorSet
    private lateinit var greenCircleAnimationDirectionX : ObjectAnimator
    private lateinit var greenCircleAnimationDirectionY : ObjectAnimator


    fun moveBlackBallRandomly(blackBall: ImageView){
        val animationX : ObjectAnimator  = ObjectAnimator.ofFloat(blackBall, "x", random.nextFloat()*1000)
        val animationY : ObjectAnimator  = ObjectAnimator.ofFloat(blackBall, "y", random.nextFloat()*2700)

        val animatorSet = AnimatorSet()
        animatorSet.playTogether(animationX,animationY)
        animatorSet.duration = blackBallAnimationDuration
        animatorSet.start()
    }

    fun moveGreenBallWithJoystick(joystick: JoystickView, greenCircle : CircleView) {
        joystick.setOnMoveListener { angle, strength ->
            //println("X: " + greenCircle.x + ", Y: " + greenCircle.y)
            angleFromJoystick = angle.toDouble()
            strengthFromJoystick = strength * 0.6f

            val newXVector = Math.cos(Math.toRadians(angleFromJoystick)).toFloat()
            newXPosition = greenCircle.x + newXVector * strengthFromJoystick
            val newYVector = Math.sin(Math.toRadians(angleFromJoystick)).toFloat()
            newYPosition = greenCircle.y - newYVector * strengthFromJoystick
            //}

            if (greenCircleDoesNotTouchScreenBorders()) {
                greenCircleAnimationDirectionX = ObjectAnimator.ofFloat(
                    greenCircle, "x", newXPosition
                )
                greenCircleAnimationDirectionY = ObjectAnimator.ofFloat(
                    greenCircle, "y", newYPosition
                )
                animatorSet = AnimatorSet()
                animatorSet.playTogether(greenCircleAnimationDirectionX, greenCircleAnimationDirectionY)
                animatorSet.duration = greenCircleAnimationDuration
                animatorSet.start()
            } else {
                if (greenCircleIsInCorner()) {
                     strengthFromJoystick = 0f
                }
                else if (greenCircleTouchesWidthBorders()) {

                    greenCircleAnimationDirectionY= ObjectAnimator.ofFloat(greenCircle, "y", newYPosition)
                    greenCircleAnimationDirectionY.duration = greenCircleAnimationDuration

                    greenCircleAnimationDirectionY.start()
                }
                else if (greenCircleTouchesHeightBorders()) {

                    greenCircleAnimationDirectionX = ObjectAnimator.ofFloat(greenCircle, "x", newXPosition)
                    greenCircleAnimationDirectionX.duration = greenCircleAnimationDuration

                    greenCircleAnimationDirectionX.start()
                }
            }
        }
    }

    private fun greenCircleTouchesHeightBorders() = newYPosition > displayHeight || newYPosition <= 0

    private fun greenCircleTouchesWidthBorders() = newXPosition > displayWidth || newXPosition <= 0

    private fun greenCircleIsInCorner() =
        (newXPosition > displayWidth && newYPosition <= 0) || (newXPosition <= 0 && newYPosition <= 0) || (newXPosition > displayWidth && newYPosition > displayHeight) || (newXPosition <= 0 && newYPosition > displayHeight)

    private fun greenCircleDoesNotTouchScreenBorders() =
        newXPosition < displayWidth && newXPosition >= 0 && newYPosition < displayHeight && newYPosition >= 0


    fun getXCoordinate(greenBall: CircleView):Float{
        return greenBall.x
    }

    fun getYCoordinate(greenBall: CircleView):Float{
        return greenBall.y
    }
}