package com.example.android_draw.view.line

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.util.AttributeSet
import android.view.View

/**
 *
 *  ┌─────────────────────────────────────────────────────────────┐
 *  │┌───┬───┬───┬───┬───┬───┬───┬───┬───┬───┬───┬───┬───┬───┬───┐│
 *  ││Esc│!1 │@2 │#3 │$4 │%5 │^6 │&7 │*8 │(9 │)0 │_- │+= │|\ │`~ ││
 *  │├───┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴───┤│
 *  ││ Tab │ Q │ W │ E │ R │ T │ Y │ U │ I │ O │ P │{[ │}] │ BS  ││
 *  │├─────┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴─────┤│
 *  ││ Ctrl │ A │ S │ D │ F │ G │ H │ J │ K │ L │: ;│" '│ Enter  ││
 *  │├──────┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴────┬───┤│
 *  ││ Shift  │ Z │ X │ C │ V │ B │ N │ M │< ,│> .│? /│Shift │Fn ││
 *  │└─────┬──┴┬──┴──┬┴───┴───┴───┴───┴───┴──┬┴───┴┬──┴┬─────┴───┘│
 *  │      │Fn │ Alt │         Space         │ Alt │Win│   HHKB   │
 *  │      └───┴─────┴───────────────────────┴─────┴───┘          │
 *  └─────────────────────────────────────────────────────────────┘
 * 版权：渤海新能 版权所有
 *
 * @author feiWang
 * 版本：1.5
 * 创建日期：2/24/21
 * 描述：Android_Draw
 * E-mail : 1276998208@qq.com
 * CSDN:https://blog.csdn.net/m0_37667770/article
 * GitHub:https://github.com/luhenchang
 */
class LHC_BrokenLine_View @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : View(context, attrs, defStyle) {

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawColor(Color.WHITE)
        //drawCicle(canvas)
        //drawLine(canvas)

        //画布的变换
        changeCanvas(canvas)


    }

    private fun changeCanvas(canvas: Canvas) {
        //初始化画笔
        val pathPaint = Paint()
        pathPaint.style = Paint.Style.STROKE
        pathPaint.strokeWidth=3f
        pathPaint.color = Color.GRAY
        pathPaint.isAntiAlias = true



        //x轴
        canvas.scale(1f,-1f)
        //向下平移所以y是负数这点需注意
        canvas.translate(0f, -height.toFloat())
        //画布平移
        canvas.translate(100f,100f)
            //绘制x和y轴
        val pathX= Path()
        pathX.moveTo(0f,0f)
        pathX.lineTo(width.toFloat(), 0f)
        val pathY= Path()
        pathY.moveTo(0f,0f)
        pathY.lineTo(0f, height.toFloat())


        pathPaint.color=Color.GRAY
        canvas.drawPath(pathX,pathPaint)
        canvas.drawPath(pathY,pathPaint)

    }

    private fun drawCicle(canvas: Canvas) {
        //初始化画笔
        val ciclePaint = Paint()
        ciclePaint.style = Paint.Style.FILL
        ciclePaint.color = Color.BLUE
        ciclePaint.isAntiAlias = true
        //绘制圆圈
        canvas.drawCircle(0f, 0f, 200f, ciclePaint)
        //画布平移
        canvas.translate(0f, 400f)
        canvas.rotate(60f)
        //绘制圆圈
        canvas.drawCircle(0f, 0f, 200f, ciclePaint)
    }
    private fun drawLine(canvas: Canvas) {
        //初始化画笔
        val pathPaint = Paint()
        pathPaint.style = Paint.Style.STROKE
        pathPaint.strokeWidth=11f
        pathPaint.color = Color.BLUE
        pathPaint.isAntiAlias = true
        canvas.scale(0.1f,0.1f)

        val path= Path()
        path.moveTo(0f,0f)
        path.lineTo(400f,400f)
        //绘制第一个线
        canvas.drawPath(path,pathPaint)
        //旋转画布
        canvas.rotate(10f)


        val pathX= Path()
        pathX.moveTo(0f,0f)
        pathX.lineTo(width.toFloat(), 0f)

        val pathY= Path()
        pathY.moveTo(0f,0f)
        pathY.lineTo(0f, height.toFloat())
        pathPaint.color=Color.GRAY
        canvas.drawPath(pathX,pathPaint)
        canvas.drawPath(pathY,pathPaint)


        //绘制线
        pathPaint.color=Color.RED
        canvas.drawPath(path,pathPaint)


        //rule
    }
}

