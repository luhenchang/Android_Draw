package com.example.android_draw.view

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.TypedArray
import android.graphics.*
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.core.graphics.drawable.toBitmap
import com.example.android_draw.R

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
 * 创建日期：2/10/21
 * 描述：Android_Draw
 * E-mail : 1276998208@qq.com
 * CSDN:https://blog.csdn.net/m0_37667770/article
 * GitHub:https://github.com/luhenchang
 */
class LHC_Image_View @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var moveX=300f
    private var moveY=127f
    private val MIDDLE_VALUE=127
    private var mdrawable: Drawable?
    lateinit var bitmap:Bitmap
    init {
        val array: TypedArray = context.obtainStyledAttributes(attrs, R.styleable.LHC_Image_View)
        mdrawable = array.getDrawable(R.styleable.LHC_Image_View_defImag)

    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        if (mdrawable!=null){
            bitmap =mdrawable!!.toBitmap(width,height, Bitmap.Config.ARGB_8888)
        }
        //实例化一支画笔
        val mPaint = Paint()
        mPaint.strokeWidth=10f
        //实例化处理色相的颜色矩阵
        val colorMatrix =  ColorMatrix()
        //获得色相的计算公式
        val degress = (moveY - MIDDLE_VALUE) * 1.0F / MIDDLE_VALUE * 180
        //0表示红色看源码
        colorMatrix.setRotate(0, degress)

        //将调好的颜色设置给画笔
        mPaint.colorFilter = ColorMatrixColorFilter(colorMatrix)
        //然后我们用调整好的颜色画笔将原来的图片bmp画到新的bitmap上
        canvas.drawBitmap(bitmap, 0f, 0f, mPaint)

        //网格绘制
        drawGrid(canvas)

        //直线和圆圈绘制
        drawLineAndCubit(canvas)
    }

    private fun drawLineAndCubit(canvas: Canvas) {
        val paint=Paint()
        paint.color=Color.GRAY
        paint.strokeWidth=2f
        paint.style= Paint.Style.STROKE
        //斜线段
        val xpath=Path()
        xpath.moveTo(10f,10f)
        xpath.lineTo(width.toFloat(), width.toFloat()-120)
        canvas.drawPath(xpath,paint)
        //起点和终点圆圈
        paint.style= Paint.Style.FILL
        paint.color=Color.RED
        canvas.drawCircle(15f,15f,15f,paint)
        canvas.drawCircle(width.toFloat()-15, width.toFloat()-120-15,15f,paint)
        val cubicPath=Path()
        cubicPath.moveTo(0f,0f)
        paint.style= Paint.Style.STROKE
        paint.strokeWidth=5f

        cubicPath.quadTo(moveX, moveY,width.toFloat()-15, width.toFloat()-120-15)

        //绘制曲线
        canvas.drawPath(cubicPath,paint)
    }

    private fun drawGrid(canvas: Canvas) {
        //1.我们左下角为屏幕的圆点进行操作
        canvas.translate(0f, height.toFloat())
        canvas.scale(1f,-1f)
        canvas.save()

        val xpath=Path()
        xpath.moveTo(0f,0f)
        xpath.lineTo(width.toFloat(), 0f)

        val paint=Paint()
        paint.color=Color.GRAY
        paint.strokeWidth=2f
        paint.style= Paint.Style.STROKE
        for (index in 0 until 6){
            canvas.translate(0f,160f)
            canvas.drawPath(xpath,paint)
        }
        canvas.restore()

        val ypath=Path()
        ypath.moveTo(0f,0f)
        ypath.lineTo(0f, width.toFloat()-120)

        val painty=Paint()
        painty.color=Color.GRAY
        painty.strokeWidth=2f
        painty.style= Paint.Style.STROKE
        canvas.save()

        for (index in 0 until 6){
            canvas.translate(160f,0f)
            canvas.drawPath(ypath,paint)
        }
        canvas.restore()



    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN,
            MotionEvent.ACTION_MOVE -> {
                //在控制点附近范围内部,进行移动
                moveX= event.x
                moveY= -(event.y - height)
                invalidate()
            }
        }
        return true
    }

}