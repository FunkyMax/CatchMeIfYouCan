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
    private var newXVector = 0.0f
    private var newYVector = 0.0f
    private var random = Random
    val displayMetricsX = 1200
    val displayMetricsY = 2720

    fun moveBlackBallRandomly(blackBall: ImageView){
        val animationX : ObjectAnimator  = ObjectAnimator.ofFloat(blackBall, "x", random.nextFloat()*1000)
        val animationY : ObjectAnimator  = ObjectAnimator.ofFloat(blackBall, "y", random.nextFloat()*2700)

        val animatorSet = AnimatorSet()
        animatorSet.playTogether(animationX,animationY)
        animatorSet.duration = blackBallAnimationDuration
        animatorSet.start()
    }

    fun moveGreenBallWithJoystick(joystick: JoystickView, greenBall : ImageView, angleID: TextView, strengthID: TextView) {
        //println("x: " + greenBall.x + ", y: " + greenBall.y)
        joystick.setOnMoveListener { angle, strength ->
            angleID.text = "angle: " + angle
            angleFromJoystick = angle.toDouble()
            strengthID.text = "strength: " + strength
            strengthFromJoystick = strength * 0.6f

            newXVector = Math.cos(Math.toRadians(angleFromJoystick)).toFloat()
            newYVector = Math.sin(Math.toRadians(angleFromJoystick)).toFloat()
        }

            if (!((newXVector * strengthFromJoystick + greenBall.x) >= displayMetricsX || newXVector * strengthFromJoystick + greenBall.x <= -50 || newYVector * strengthFromJoystick + greenBall.y >= displayMetricsY || newYVector * strengthFromJoystick + greenBall.y <= 0)){

                val animationX : ObjectAnimator  = ObjectAnimator.ofFloat(greenBall, "x", getXCoordinate(greenBall)+newXVector*strengthFromJoystick)
                val animationY : ObjectAnimator  = ObjectAnimator.ofFloat(greenBall, "y", getYCoordinate(greenBall)-newYVector*strengthFromJoystick)

                val animatorSet = AnimatorSet()
                animatorSet.playTogether(animationX,animationY)
                animatorSet.duration = greenBallAnimationDuration
                animatorSet.start()
            }
            else{
                println("uhsiduhsiusd")
            }
    }

    fun getXCoordinate(greenBall: ImageView):Float{
        return greenBall.x
    }

    fun getYCoordinate(greenBall: ImageView):Float{
        return greenBall.y
    }
}