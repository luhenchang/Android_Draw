package com.example.android_draw.view.line

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.View
import androidx.core.graphics.setBlendMode

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
 * 创建日期：2/25/21
 * 描述：Android_Draw
 * E-mail : 1276998208@qq.com
 * CSDN:https://blog.csdn.net/m0_37667770/article
 * GitHub:https://github.com/luhenchang
 */
class LHC_Rule_View @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : View(context, attrs, defStyle) {

    val ruleWidth=1800f
    val ruleHeight=300f
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawColor(Color.WHITE)
        //初始化画笔
        val pathPaint = Paint()
        pathPaint.style = Paint.Style.STROKE
        pathPaint.strokeWidth=3f
        pathPaint.color = Color.argb(111,111,111,111)
        pathPaint.isAntiAlias = true


        //1.平移动画布
        canvas.translate(200f,200f)

        //2.绘制尺子矩形框
        val rulePath=Path()
        rulePath.moveTo(0f,0f)
        rulePath.lineTo(ruleWidth,0f)
        rulePath.lineTo(ruleWidth,ruleHeight)
        rulePath.lineTo(0f,ruleHeight)
        rulePath.close()
        canvas.drawPath(rulePath,pathPaint)


        //3.平移画布进行绘制刻度
        translateCanvasAndDrawRule(canvas)
        //4.画上部小刻度
        drawSmallCalibration(canvas)
        //5.平移画布绘制下面的刻度
        translateCanvasAndDrawDownRule(canvas)


    }
    //5.平移画布绘制下面的刻度
    private fun translateCanvasAndDrawDownRule(canvas: Canvas) {
        //初始化画笔
        val pathPaint = Paint()
        pathPaint.style = Paint.Style.STROKE
        pathPaint.strokeWidth=5f
        pathPaint.color = Color.argb(111,111,111,111)
        pathPaint.isAntiAlias = true


        val textPaint = Paint()
        textPaint.textSize=28f
        textPaint.strokeWidth=2f
        textPaint.color=Color.BLACK
        textPaint.style = Paint.Style.STROKE
        textPaint.isAntiAlias = true

        //沿着x轴镜像
        canvas.scale(1f,-1f)

        //向下平移尺子的高度即可
        canvas.translate(0f,-ruleHeight)

        //保存画布状态
        canvas.save()


        //每一等分的长度px=ruleWidth/5
        val oneOfWidth=(ruleWidth-100*2)/12
        for (index in 0 until 13){
            //画布向右边平移
            if (index>0) {
                canvas.translate(oneOfWidth, 0f)
            }
            //绘制刻度
            val calibrationPath=Path()
            calibrationPath.moveTo(0f,0f)
            calibrationPath.lineTo(0f,80f)
            canvas.drawPath(calibrationPath,pathPaint)

            //为了绘制文字时候不干扰我们绘制刻度线我们先进行保存起来将画布状态。
            canvas.save()
            //当然后面我们不会写死而是跟矩文字的测量宽度来获取画布的平移距离。不信你看后面的大于等于10数字有偏离并不是刻度中间对齐吧。
            canvas.translate(-8.5f,85f)
            canvas.scale(1f,-1f)
            canvas.drawText(index.toString(),0f,0f,textPaint)

            //恢复画刻度的状态
            canvas.restore()


            //保存每次绘制去的小刻度画布起始点。
            canvas.save()
            val smallOfWidth=oneOfWidth/10
            if(index<12)
            for (index in 0 until 9) {
                //画布向右边平移
                canvas.translate(smallOfWidth, 0f)
                //绘制刻度
                val calibrationPath = Path()
                calibrationPath.moveTo(0f, 0f)
                if (index==4) {
                    calibrationPath.lineTo(0f, 55f)
                }else{
                    calibrationPath.lineTo(0f, 40f)
                }
                canvas.drawPath(calibrationPath, pathPaint)
            }
            //恢复每次绘制小刻度之前的坐标点都在每个刻度上。
            canvas.restore()
        }
        //画布坐标系返回到第一个状态
        canvas.restore()
    }

    private fun translateCanvasAndDrawRule(canvas: Canvas) {
        //初始化画笔
        val pathPaint = Paint()
        pathPaint.style = Paint.Style.STROKE
        pathPaint.strokeWidth=5f
        pathPaint.color = Color.argb(111,211,111,111)
        pathPaint.isAntiAlias = true



        val textPaint = Paint()
        textPaint.textSize=28f
        textPaint.strokeWidth=2f
        textPaint.color=Color.RED
        textPaint.style = Paint.Style.STROKE
        textPaint.isAntiAlias = true

        //平移画布圆点到第一个刻度位置
        canvas.translate(100f,0f)

        //保存画布状态
        canvas.save()


        //每一等分的长度px=ruleWidth/5
        val oneOfWidth=(ruleWidth-100*2)/5
        for (index in 0 until 6){
            //画布向右边平移
            if (index>0) {
                canvas.translate(oneOfWidth, 0f)
            }
            //绘制刻度
            val calibrationPath=Path()
            calibrationPath.moveTo(0f,0f)
            calibrationPath.lineTo(0f,80f)
            canvas.drawPath(calibrationPath,pathPaint)

            //为了绘制文字时候不干扰我们绘制刻度线我们先进行保存起来将画布状态。
            canvas.save()
            //当然后面我们不会写死而是跟矩文字的测量宽度来获取画布的平移距离。不信你看后面的大于等于10数字有偏离并不是刻度中间对齐吧。
            canvas.translate(-8.5f,99f)
            canvas.drawText(index.toString(),0f,0f,textPaint)

            //恢复画刻度的状态
            canvas.restore()
        }
        //画布坐标系返回到第一个状态
        canvas.restore()


    }
    //4.画上部小刻度
    private fun drawSmallCalibration(canvas: Canvas) {
        val pathPaint = Paint()
        pathPaint.style = Paint.Style.STROKE
        pathPaint.strokeWidth=2f
        pathPaint.color = Color.argb(111,111,111,111)
        pathPaint.isAntiAlias = true
        //平移画布圆点到第一个刻度位置

        //先保存画布状态
        canvas.save()
        val oneOfWidth=(ruleWidth-100*2)/5
        val smallOfWidth=oneOfWidth/10
        for (index in 0 until 5) {
            if (index>0) {
                canvas.translate(oneOfWidth, 0f)
            }
            //保存每次绘制去的小刻度画布起始点。
            canvas.save()
            for (index in 0 until 9) {
                //画布向右边平移
                canvas.translate(smallOfWidth, 0f)
                //绘制刻度
                val calibrationPath = Path()
                calibrationPath.moveTo(0f, 0f)
                if (index==4) {
                    calibrationPath.lineTo(0f, 55f)
                }else{
                    calibrationPath.lineTo(0f, 40f)
                }
                canvas.drawPath(calibrationPath, pathPaint)
            }
            //恢复每次绘制小刻度之前的坐标点都在每个刻度上。
            canvas.restore()
        }
        canvas.restore()
    }
}