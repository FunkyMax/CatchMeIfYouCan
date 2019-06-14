package com.example.catchmeifyoucan.views

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View
import com.example.catchmeifyoucan.R

class ArrowView(context: Context?, attrs: AttributeSet?) : View(context, attrs) {
    private var paint = Paint()
    private val path = Path()
    private val arrowArray = FloatArray(16)

    init {
        paint = Paint(Paint.ANTI_ALIAS_FLAG)

        val ta = context?.obtainStyledAttributes(attrs, R.styleable.ArrowView)
        val int = ta?.getColor(R.styleable.ArrowView_arrow_color, Color.GREEN)
        paint.setColor(int!!)
        ta.recycle()

        arrowArray.set(0, 0f)       //x
        arrowArray.set(1, 150f)     //y

        arrowArray.set(2, 700f)     //x
        arrowArray.set(3, 150f)     //y

        arrowArray.set(4, 700f)     //x
        arrowArray.set(5, 0f)       //y

        arrowArray.set(6, 950f)     //x
        arrowArray.set(7, 250f)     //y

        arrowArray.set(8, 700f)     //x
        arrowArray.set(9, 500f)     //y

        arrowArray.set(10, 700f)    //x
        arrowArray.set(11, 350f)    //y

        arrowArray.set(12, 0f)      //x
        arrowArray.set(13, 350f)    //y
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        drawArrow(canvas)
    }

    private fun drawArrow(canvas: Canvas?) {
        path.moveTo(arrowArray[0], arrowArray[1])
        path.lineTo(arrowArray[2], arrowArray[3])
        path.lineTo(arrowArray[4], arrowArray[5])
        path.lineTo(arrowArray[6], arrowArray[7])
        path.lineTo(arrowArray[8], arrowArray[9])
        path.lineTo(arrowArray[10], arrowArray[11])
        path.lineTo(arrowArray[12], arrowArray[13])
        path.close()
        canvas?.drawPath(path, paint)
    }
}