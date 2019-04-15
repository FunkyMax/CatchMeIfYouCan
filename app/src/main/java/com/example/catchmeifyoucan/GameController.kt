package com.example.catchmeifyoucan

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.widget.ImageView
import kotlin.random.Random

class GameController{
    var duration : Long = 700
    var randomX : Random = Random
    var randomY : Random = Random

    fun moveBlackBallRandomly(image: ImageView){
        val animationX : ObjectAnimator  = ObjectAnimator.ofFloat(image, "x", randomX.nextFloat()*1000)
        val animationY : ObjectAnimator  = ObjectAnimator.ofFloat(image, "y", randomY.nextFloat()*2700)

        val animatorSet = AnimatorSet()
        animatorSet.playTogether(animationX,animationY)
        animatorSet.duration = duration
        animatorSet.start()
    }
    }