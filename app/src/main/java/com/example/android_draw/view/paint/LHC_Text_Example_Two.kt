package com.example.android_draw.view.paint

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.graphics.text.MeasuredText
import android.os.Build
import android.util.AttributeSet
import android.view.View
import android.view.animation.Animation
import android.view.animation.BounceInterpolator
import androidx.annotation.RequiresApi
import androidx.core.view.marginBottom
import com.example.android_draw.R
import kotlin.math.withSign


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
 * 版权：Lenovo 版权所有
 *
 * @author feiWang
 * 版本：1.5
 * 创建日期：2/25/21
 * 描述：Android_Draw
 * E-mail : 1276998208@qq.com
 * CSDN:https://blog.csdn.net/m0_37667770/article
 * GitHub:https://github.com/luhenchang
 */
class LHC_Text_Example_Two @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : View(context, attrs, defStyle) {
    private var offSet: Float = 0f
    private var offSetY: Float = 0f
    private var flagColor= Color.argb(255,102,138,240)



    init {
        val objectAnimator = ObjectAnimator.ofFloat(-18f, 20f)
        objectAnimator.repeatCount = ValueAnimator.INFINITE
        objectAnimator.interpolator =(BounceInterpolator())
        objectAnimator.addUpdateListener {
            offSet = it.animatedValue as Float
            if(offSet>5){
                flagColor = Color.RED
            }else{
                flagColor = Color.argb(255,102,138,240)

            }

            postInvalidate()
        }
        objectAnimator.duration =1100
        objectAnimator.start()



        val objectAnimatorY = ObjectAnimator.ofFloat(16f,-2f)
        objectAnimatorY.repeatCount = ValueAnimator.INFINITE
        objectAnimatorY.addUpdateListener {
            offSetY= it.animatedValue as Float
            postInvalidate()
        }
        objectAnimatorY.duration = 1000
        objectAnimatorY.start()
    }

    @RequiresApi(Build.VERSION_CODES.M)
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawColor(Color.BLACK)
        canvas.translate((width / 3).toFloat(), (height / 2).toFloat())
        //裁剪画布
        clipCanvas(canvas)
        //绘制文字和动画波浪等
        //drawWaveAndText(canvas)

        //跳舞的文字
        dancingText(canvas)


    }

    private fun dancingText(canvas: Canvas) {
        val paint = Paint()
        paint.textSize = 90f
        paint.color = flagColor
        paint.strokeWidth = 10f
        paint.style = Paint.Style.FILL


        val hIPathDown = Path()
        //左上点
        hIPathDown.moveTo(offSet, offSetY)
        //左下点
        hIPathDown.rLineTo(0f, 150f)
        //左下二点
        hIPathDown.rLineTo(30f, 0f)

        hIPathDown.rLineTo(0f, -70f)
        hIPathDown.rLineTo(40f, 0f)
        hIPathDown.rLineTo(0f, 70f)
        hIPathDown.rLineTo(30f, 0f)
        hIPathDown.rLineTo(0f, -150f)
        hIPathDown.rLineTo(-30f, 0f)
        hIPathDown.rLineTo(0f, 70f)
        hIPathDown.rLineTo(-40f, 0f)
        hIPathDown.rLineTo(0f, -70f)
        hIPathDown.close()
        paint.setShadowLayer(13f,5f,10f, flagColor)

        canvas.drawPath(hIPathDown, paint)

        //绘制一个文字
        val hIPath = Path()
        hIPath.moveTo(0f, 0f)
        hIPath.rLineTo(0f, 160f)
        hIPath.rLineTo(30f, 0f)
        hIPath.rLineTo(0f, -70f)
        hIPath.rLineTo(40f, 0f)
        hIPath.rLineTo(0f, 70f)
        hIPath.rLineTo(30f, 0f)
        hIPath.rLineTo(0f, -160f)
        hIPath.rLineTo(-30f, 0f)
        hIPath.rLineTo(0f, 70f)
        hIPath.rLineTo(-40f, 0f)
        hIPath.rLineTo(0f, -70f)
        hIPath.close()

        paint.color = Color.WHITE
        paint.setShadowLayer(13f,5f,10f, Color.WHITE)
        canvas.drawPath(hIPath, paint)
    }


    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun drawWaveAndText(canvas: Canvas) {
        val paint = Paint()
        paint.textSize = 90f
        paint.color = Color.BLACK
        paint.strokeWidth = 10f
        paint.style = Paint.Style.FILL
        //测量文字宽度
        val rect = Rect()
        paint.getTextBounds("给婆老你爱我", 0, 6, rect)

        val colorStar = Color.argb(255, 27, 134, 244)
        val colors = intArrayOf(colorStar, Color.YELLOW, Color.GREEN)
        val positions = floatArrayOf(0.2f, 11f, 0.2f)


        //让渐变动起来从而感觉到文字闪动起来了
        val transMatrix = Matrix()
        transMatrix.postTranslate(-rect.width() + rect.width() * 2 * offSet, 0f)
        val linearGradient = LinearGradient(
            0f,
            0f,
            rect.width().toFloat(),
            0f,
            colors,
            positions,
            Shader.TileMode.CLAMP
        )
        linearGradient.setLocalMatrix(transMatrix)

        paint.shader = linearGradient
        canvas.drawText("最爱吃张亮麻辣烫", 0f, 0f, paint)

    }

    private fun clipCanvas(canvas: Canvas) {
        val clipPath = Path()
        val rectF = RectF(-110f, -200f, 800f, 400f)
        clipPath.addRoundRect(rectF, 50f, 50f, Path.Direction.CCW)
        canvas.clipPath(clipPath)
    }


}

