package com.example.catchmeifyoucan

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.graphics.Path
import android.widget.ImageView
import android.widget.TextView
import io.github.controlwear.virtual.joystick.android.JoystickView
import kotlin.math.cos
import kotlin.random.Random

class GameController{
    private val duration : Long = 700
    private var random = Random
    private val distance = 100

    fun moveBlackBallRandomly(blackBall: ImageView){
        val animationX : ObjectAnimator  = ObjectAnimator.ofFloat(blackBall, "x", random.nextFloat()*1000)
        val animationY : ObjectAnimator  = ObjectAnimator.ofFloat(blackBall, "y", random.nextFloat()*2700)

        val animatorSet = AnimatorSet()
        animatorSet.playTogether(animationX,animationY)
        animatorSet.duration = duration
        animatorSet.start()
    }

    fun moveGreenBallWithJoystick(joystick: JoystickView, greenBall : ImageView, angleID: TextView, strengthID: TextView) {
        var strengthFromJoystick = 0
        var angleFromJoystick = 0.0
        joystick.setOnMoveListener { angle, strength ->
            angleID.text = "angle: " + angle.toString()
            angleFromJoystick = angle.toDouble()
            strengthID.text = "strength: " + strength.toString()
            strengthFromJoystick = strength
        }
        var currentXPosition = greenBall.x
        var currentYPosition = greenBall.y
        val path = Path()
        path.lineTo(currentXPosition + distance,currentYPosition + distance)
        cos(angleFromJoystick)
    }
}