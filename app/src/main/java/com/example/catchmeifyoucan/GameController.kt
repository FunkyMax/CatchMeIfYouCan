package com.example.catchmeifyoucan

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.widget.ImageView
import android.widget.TextView
import io.github.controlwear.virtual.joystick.android.JoystickView
import kotlin.random.Random

class GameController{
    private val duration : Long = 700
    private var random : Random = Random
    var pace = 0

    fun moveBlackBallRandomly(blackBall: ImageView){
        val animationX : ObjectAnimator  = ObjectAnimator.ofFloat(blackBall, "x", random.nextFloat()*1000)
        val animationY : ObjectAnimator  = ObjectAnimator.ofFloat(blackBall, "y", random.nextFloat()*2700)

        val animatorSet = AnimatorSet()
        animatorSet.playTogether(animationX,animationY)
        animatorSet.duration = duration
        animatorSet.start()
    }

    fun moveGreenBallWithJoystick(joystick: JoystickView, greenBall : ImageView, angleID: TextView, strengthID: TextView) {
        joystick.setOnMoveListener { angle, strength ->
            angleID.text = "angle: " + angle.toString()
            strengthID.text = "strength: " + strength.toString()

        }
    }
}