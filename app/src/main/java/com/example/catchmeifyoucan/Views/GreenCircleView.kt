package com.example.catchmeifyoucan.Views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View

class GreenCircleView : View {
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    private val paint = Paint()

    override fun onDraw(canvas: Canvas?) {
        paint.isAntiAlias = true
        paint.color = Color.GREEN
        canvas?.drawCircle(50f,50f,50f, paint)
    }
}