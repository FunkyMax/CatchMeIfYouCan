package com.example.catchmeifyoucan

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.widget.ImageView
import android.widget.TextView
import io.github.controlwear.virtual.joystick.android.JoystickView
import kotlin.random.Random

class GameController{
    private val blackBallAnimationDuration = 700L
    private val greenBallAnimationDuration = 30L
    private var angleFromJoystick = 0.0
    private var strengthFromJoystick = 0.0f
    private var newXVector2 = 0.0f
    private var newYVector2 = 0.0f
    private var random = Random

    fun moveBlackBallRandomly(blackBall: ImageView){
        val animationX : ObjectAnimator  = ObjectAnimator.ofFloat(blackBall, "x", random.nextFloat()*1000)
        val animationY : ObjectAnimator  = ObjectAnimator.ofFloat(blackBall, "y", random.nextFloat()*2700)

        val animatorSet = AnimatorSet()
        animatorSet.playTogether(animationX,animationY)
        animatorSet.duration = blackBallAnimationDuration
        animatorSet.start()
    }

    fun moveGreenBallWithJoystick(joystick: JoystickView, greenBall : ImageView, angleID: TextView, strengthID: TextView) {
        joystick.setOnMoveListener { angle, strength ->
            angleID.text = "angle: " + angle
            angleFromJoystick = angle.toDouble()
            strengthID.text = "strength: " + strength
            strengthFromJoystick = strength*0.6f

            newXVector2 = Math.cos(Math.toRadians(angleFromJoystick)).toFloat()
            newYVector2 = Math.sin(Math.toRadians(angleFromJoystick)).toFloat()
        }

        val animationX : ObjectAnimator  = ObjectAnimator.ofFloat(greenBall, "x", getXCoordinate(greenBall)+newXVector2*strengthFromJoystick)
        val animationY : ObjectAnimator  = ObjectAnimator.ofFloat(greenBall, "y", getYCoordinate(greenBall)-newYVector2*strengthFromJoystick)

        val animatorSet = AnimatorSet()
        animatorSet.playTogether(animationX,animationY)
        animatorSet.duration = greenBallAnimationDuration
        animatorSet.start()
    }

    fun getXCoordinate(greenBall: ImageView):Float{
        return greenBall.x
    }

    fun getYCoordinate(greenBall: ImageView):Float{
        return greenBall.y
    }
}