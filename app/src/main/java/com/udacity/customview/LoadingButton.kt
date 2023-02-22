package com.udacity.customview

import android.animation.ValueAnimator
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Rect
import android.graphics.RectF
import android.util.AttributeSet
import android.view.View
import android.view.animation.LinearInterpolator
import com.udacity.R
import com.udacity.model.ButtonState
import kotlin.properties.Delegates

class LoadingButton @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var widthSize = 0
    private var heightSize = 0

    private val startAngle = 0f
    private var sweepAngle = 0f

    private var rectangleText=""
    private var rectangleWidth=0f

    private var isCompleted=false

    // ANTI_ALIAS_FLAG to make edges of the shapes smooth
    private val rectanglePaint=Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style=Paint.Style.FILL
        color=context.getColor(R.color.colorPrimary)
    }

    private val circlePaint=Paint(Paint.ANTI_ALIAS_FLAG).apply {
        style=Paint.Style.FILL
        color=context.getColor(R.color.colorAccent)
    }

    private val circleAnimator = ValueAnimator.ofFloat(0f,360f).apply {
        duration=1000 //time to complete the animation
        interpolator=LinearInterpolator() // constant rate progress
        addUpdateListener {
            sweepAngle= it.animatedValue as Float
            invalidate()
        }
    }

    private val rectangleAnimator = ValueAnimator.ofFloat(0f,width.toFloat()).apply {
        duration=1000 //time to complete the animation
        interpolator=LinearInterpolator() // constant rate progress
        addUpdateListener {
            rectangleWidth= it.animatedValue as Float
            rectanglePaint.color=context.getColor(R.color.colorPrimaryDark)
            invalidate()
        }
    }


    var buttonState: ButtonState by Delegates.observable<ButtonState>(ButtonState.Completed) { p, old, new ->
        when(new){
            ButtonState.Clicked-> {}

            ButtonState.Loading->{
                isCompleted=false
                rectangleText=context.getString(R.string.button_loading)
                rectangleAnimator.start()
                circleAnimator.start()
           }
            ButtonState.Completed->{
                circleAnimator.cancel()
                rectangleAnimator.cancel()
                rectangleText="Download"
                isCompleted=true
            }
        }
        invalidate()
    }


    init {
        rectangleText="Download"
    }


    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        canvas?.drawRectangle()
        canvas?.drawCircle()
        if(isCompleted){
            rectanglePaint.color=context.getColor(R.color.colorPrimary)
            circlePaint.color=Color.TRANSPARENT }
        else circlePaint.color=context.getColor(R.color.colorAccent)
        invalidate()
    }

    private fun Canvas.drawCircle(){
        drawCircleBackground(circlePaint.color,startAngle, sweepAngle)
    }

    private fun Canvas.drawCircleBackground(color:Int, startAngle:Float,sweepAngle:Float){
        val rectf=RectF(0f,0f,height.toFloat(),height.toFloat())
        drawArc(rectf,startAngle,sweepAngle,true,circlePaint)
    }
    private fun Canvas.drawRectangle(){
        drawRectangleBackground()
        drawRectangleText()
    }
    private fun Canvas.drawRectangleBackground(){
        val rectangle= Rect(0,0,width,height)
        drawRect(rectangle,rectanglePaint)
    }

    private fun Canvas.drawRectangleText(){
        val textPaint=Paint(Paint.ANTI_ALIAS_FLAG).apply {
            textSize=50.0f
            textAlign=Paint.Align.CENTER
            color= Color.WHITE
        }

        /*
        ascent -> the distance between baseline and the highest letter
        descent -> the distance between baseline and lowest point of letter for example :
        y letter the first half of it will be above of baseline and second half will be below baseline
        so the distance between them is called descent
         */
        val textRange=textPaint.descent()+textPaint.ascent()
        drawText(rectangleText,width/2f,(height/2)-(textRange/2),textPaint)
    }

    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        val minw: Int = paddingLeft + paddingRight + suggestedMinimumWidth
        val w: Int = resolveSizeAndState(minw, widthMeasureSpec, 1)
        val h: Int = resolveSizeAndState(
            MeasureSpec.getSize(w),
            heightMeasureSpec,
            0
        )
        widthSize = w
        heightSize = h
        setMeasuredDimension(w, h)
    }

}