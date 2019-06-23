package com.example.catchmeifyoucan.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.view.View
import com.example.catchmeifyoucan.R

class HeadlightBeamView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {
    private val paint = Paint(Paint.ANTI_ALIAS_FLAG)

    init {
        val ta = context?.obtainStyledAttributes(attrs, R.styleable.HeadlightBeamView)
        val color = ta?.getColor(R.styleable.HeadlightBeamView_circle_color, Color.GREEN)
        paint.color = color!!
        ta.recycle()
    }

    override fun onDraw(canvas: Canvas?) {
        canvas?.drawCircle(30f, 30f, 30f, paint)
    }
}