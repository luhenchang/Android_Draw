package com.example.android_draw.view.paint

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.os.Build
import android.util.AttributeSet
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.view.marginBottom
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
 * 创建日期：2/25/21
 * 描述：Android_Draw
 * E-mail : 1276998208@qq.com
 * CSDN:https://blog.csdn.net/m0_37667770/article
 * GitHub:https://github.com/luhenchang
 */
class LHC_Text_Example @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : View(context, attrs, defStyle) {
    private var offSet = 0f
    private val mCenterY = 0f
    private val mWLenght = 600f

    private val moveLength = 800f


    init {
        val objectAnimator = ObjectAnimator.ofFloat(0f, moveLength)
        objectAnimator.repeatCount = ValueAnimator.INFINITE
        objectAnimator.addUpdateListener {
            offSet = it.animatedValue as Float
            postInvalidate()
        }
        objectAnimator.duration = 6000
        objectAnimator.start()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawColor(Color.WHITE)
        canvas.translate(0f, (height / 2).toFloat())
        //裁剪画布
        clipCanvas(canvas)
        //绘制文字和动画波浪等
        drawWaveAndText(canvas)


    }


    //能不能让文字动起来随着波浪斗起来?
    val resultRedPath = Path()

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun drawWaveAndText(canvas: Canvas) {
        val paint = Paint()
        paint.textSize = 90f
        paint.color = Color.BLACK
        paint.strokeWidth = 10f
        paint.style = Paint.Style.STROKE
        paint.letterSpacing = 1.2f




        //绘制波浪
        val mPath = Path()
        mPath.reset()
        mPath.moveTo(-moveLength + offSet, mCenterY)
        for (i in 0 until 30) {
            mPath.quadTo(
                -mWLenght / 4 * 3 + i * mWLenght + offSet, mCenterY + 70,
                -mWLenght / 2 + i * mWLenght + offSet, mCenterY
            )
            mPath.quadTo(
                -mWLenght / 4 + i * mWLenght + offSet, mCenterY - 70,
                0 + i * mWLenght + offSet, mCenterY
            )

        }
        mPath.lineTo(width.toFloat(), height.toFloat())
        mPath.lineTo(0f, height.toFloat())
        mPath.close()
        paint.style = Paint.Style.FILL
        val linearGradient = LinearGradient(
            0f,
            0f,
            0f, height/2f,
            Color.argb(55, 27, 134, 244),
            Color.argb(195, 24, 220, 253),
            Shader.TileMode.CLAMP
        )
        paint.shader = linearGradient
        canvas.drawPath(mPath, paint)


        //测量绘制路径
        resultRedPath.reset()
        //path.length
        val mPathMeasure = PathMeasure(mPath, false)
        val length: Float = mPathMeasure.length
        val stop: Float = length
        val start = 0f
        //获取路径中的片段 1.第一次例如 0 ---- lenght*0.1 之间的片段
        //               2.第二次例如 0 ---- lenght*0.2 之间的片段
        mPathMeasure.getSegment(start, stop * offSet, resultRedPath, true)

        val pos = FloatArray(2)
        //沿当前轮廓采样的距离。采样的最后一点的坐标和tan
        mPathMeasure.getPosTan(length, pos, null)
        resultRedPath.lineTo(pos[0], 0F)
        resultRedPath.lineTo(0F, 0F)
        resultRedPath.close()
        val colors= intArrayOf(Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW)
        paint.shader=RadialGradient(400f,0f,800f,colors,null, Shader.TileMode.CLAMP)
        canvas.drawTextOnPath("钱房私点给婆老你爱我", mPath, pos[0], pos[1], paint)

    }

    private fun clipCanvas(canvas: Canvas) {
        val clipPath = Path()
        val rectF = RectF(400f, -200f, 1400f, 400f)
        clipPath.addRoundRect(rectF, 50f, 50f, Path.Direction.CCW)
        canvas.clipPath(clipPath)
    }



}

