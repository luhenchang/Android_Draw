package com.example.android_draw.view
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import kotlin.math.PI
import kotlin.math.sin

/**
 * 声波振幅的绘制
 */
class LHC_MediaRecorder_View @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr){
    //最大的振幅高度
    val amplitudeMax=40f
    //距离水平两边的距离
    val marginHorizontalLR=100f
    //画笔
    val paintOfAxis=Paint()
    //x轴的总长度
    var allWidthPx=0f
    var pintArrayList= ArrayList<SinPoint>()
    init {
       paintOfAxis.color= Color.BLACK
       paintOfAxis.strokeWidth=3f
       paintOfAxis.style= Paint.Style.STROKE
       for(index in 0 until 180){
           pintArrayList.add(SinPoint(index, amplitudeMax*sin(PI/180*index).toFloat(),0f))
       }
    }
    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        //1.变换绘制坐标轴
        canvas.scale(1f,-1f)
        canvas.translate(marginHorizontalLR,-height/2f)
        val xPath=Path()
        xPath.moveTo(0f,0f)
        //2.绘制x轴
        xPath.lineTo(width-2f*horizontalFadingEdgeLength,0f)
        canvas.drawPath(xPath,paintOfAxis)
        canvas.drawCircle(0f,0f,100f,paintOfAxis)
        //3.绘制 Sinx
        //4.总长度为 width - marginHorizontalLR*2 是总的x轴长度
        allWidthPx = width - marginHorizontalLR*2
        //5.
        pintArrayList.forEach { it
            canvas.drawPoint(it.x.toFloat(),it.y,paintOfAxis)
        }







    }
}
//
class SinPoint(var x: Int, var y: Float,var z:Float)