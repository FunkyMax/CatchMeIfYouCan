package com.example.catchmeifyoucan.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class BlackBallView : View {
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)

    override fun onDraw(canvas: Canvas?) {
        val paint = Paint()
        paint.isAntiAlias = true
        paint.color = Color.BLACK
        canvas?.drawCircle(30f,30f,30f, paint)
    }
}