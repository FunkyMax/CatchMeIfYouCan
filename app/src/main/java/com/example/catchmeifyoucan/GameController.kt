package com.example.catchmeifyoucan

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.widget.ImageView
import android.widget.TextView
import com.example.catchmeifyoucan.views.CircleView
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
    val displayMetricsX = 1380
    val displayMetricsY = 2900

    fun moveBlackBallRandomly(blackBall: ImageView){
        val animationX : ObjectAnimator  = ObjectAnimator.ofFloat(blackBall, "x", random.nextFloat()*1000)
        val animationY : ObjectAnimator  = ObjectAnimator.ofFloat(blackBall, "y", random.nextFloat()*2700)

        val animatorSet = AnimatorSet()
        animatorSet.playTogether(animationX,animationY)
        animatorSet.duration = blackBallAnimationDuration
        animatorSet.start()
    }

    fun moveGreenBallWithJoystick(joystick: JoystickView, greenCircle : CircleView, angleID: TextView, strengthID: TextView) {
        //println("x: " + greenCircle.x + ", y: " + greenCircle.y)
        joystick.setOnMoveListener { angle, strength ->
            angleID.text = "angle: " + angle
            angleFromJoystick = angle.toDouble()
            strengthID.text = "strength: " + strength
            strengthFromJoystick = strength * 0.6f

            newXVector = Math.cos(Math.toRadians(angleFromJoystick)).toFloat()
            newYVector = Math.sin(Math.toRadians(angleFromJoystick)).toFloat()
        //}

        if (isNotTouchingBorder(greenCircle)) {

            val animationX: ObjectAnimator = ObjectAnimator.ofFloat(
                greenCircle,
                "x",
                getXCoordinate(greenCircle) + newXVector * strengthFromJoystick
            )
            val animationY: ObjectAnimator = ObjectAnimator.ofFloat(
                greenCircle,
                "y",
                getYCoordinate(greenCircle) - newYVector * strengthFromJoystick
            )

            val animatorSet = AnimatorSet()
            animatorSet.playTogether(animationX, animationY)
            animatorSet.duration = greenBallAnimationDuration
            animatorSet.start()
        } else {
            println("uhsiduhsiusd")
        }
    }
    }

    private fun isNotTouchingBorder(greenCircle: CircleView) =
        !((newXVector * strengthFromJoystick + greenCircle.x) >= displayMetricsX || newXVector * strengthFromJoystick + greenCircle.x <= -50 || newYVector * strengthFromJoystick + greenCircle.y >= displayMetricsY || newYVector * strengthFromJoystick + greenCircle.y <= 0)

    fun getXCoordinate(greenBall: CircleView):Float{
        return greenBall.x
    }

    fun getYCoordinate(greenBall: CircleView):Float{
        return greenBall.y
    }
}