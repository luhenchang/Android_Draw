package com.example.android_draw.view.worter

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator

@SuppressLint("WrongConstant")
class LHC_WaterDrop_View @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {

    private var mCurAnimValue: Float = 1f
    private var mCurAnimValueY: Float = 1f


    private var animator: ValueAnimator = ValueAnimator.ofFloat(1f, 0f)
    private var animatorY: ValueAnimator = ValueAnimator.ofFloat(0f, 1f)

    init {
        animator.duration = 1500
        animator.interpolator = AccelerateDecelerateInterpolator()
        animator.addUpdateListener { animation ->
            mCurAnimValue = animation.animatedValue as Float
            invalidate()
        }
        animator.repeatMode = ValueAnimator.INFINITE
        animator.start()
        animatorY.duration = 1500
        animatorY.interpolator = AccelerateDecelerateInterpolator()
        animatorY.addUpdateListener { animation ->
            mCurAnimValueY = animation.animatedValue as Float
            invalidate()
        }
        animatorY.repeatMode = ValueAnimator.INFINITE
        animatorY.start()
    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)

        val paint = Paint()
        paint.color = Color.RED
        paint.style = Paint.Style.FILL
        paint.strokeWidth = 10f
        paint.isAntiAlias = true
        val r = 100.0f-60f*(1-mCurAnimValue)
        canvas.drawCircle(100f,100f,100f,paint)
        canvas.scale(1f, -1f)
        canvas.translate(width / 2f, -height / 2f)
        //圆的坐标和中心点的坐标计算
        //1.首先 原点为(0f,0f)且半径r=100f--->那么p6(0f,r),p5=(r/2,r),p4(r,r/2),p3(r,0f)
        //2.第二象限里面 p2(r,-r/2),p1(r/2,-r),p0(0f,r)
        //3.第三象限里面 p11(-r/2,-r),p10(-r,-r/2),p9(-r,0f)
        //4.第四象限里面 p8(-r,r/2),p7(r/2,r),p6(0f,r)
        val P0 = PointF(0f, -r + mCurAnimValueY * 160)
        val P1 = PointF(r / 2, -r + mCurAnimValueY * 160)
        val P2 = PointF(r, -r / 2 + mCurAnimValueY * 160)
        val P3 = PointF(r, 0f + mCurAnimValueY * 160)
        val P4 = PointF(r, r / 2 + mCurAnimValueY * 160)
        val P5 = PointF(r / 2, r + mCurAnimValueY * 160)
        val P6 = PointF(0f, r + mCurAnimValueY * 160)
        val P7 = PointF(-r / 2, r + mCurAnimValueY * 160)
        val P8 = PointF(-r, r / 2 + mCurAnimValueY * 160)
        val P9 = PointF(-r, 0f + mCurAnimValueY * 160)
        val P10 = PointF(-r, -r / 2 + mCurAnimValueY * 160)
        val P11 = PointF(-r / 2, -r + mCurAnimValueY * 160)

        val path = Path()
        path.moveTo(P0.x, P0.y - 60 * mCurAnimValue)
        //p1->p2->p3
        path.cubicTo(P1.x, P1.y - 30 * mCurAnimValue, P2.x, P2.y - 30 * mCurAnimValue, P3.x, P3.y)
        //p4->p5->p6
        path.cubicTo(P4.x, P4.y, P5.x, P5.y, P6.x, P6.y)
        //p7->p8->p9
        path.cubicTo(P7.x, P7.y, P8.x, P8.y, P9.x, P9.y)
        //p10->p11->p0
        path.cubicTo(
            P10.x,
            P10.y - 30 * mCurAnimValue,
            P11.x,
            P11.y - 30 * mCurAnimValue,
            P0.x,
            P0.y - 60 * mCurAnimValue
        )
        path.close()


        canvas.drawPath(path, paint)

    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action and MotionEvent.ACTION_MASK) {
            MotionEvent.ACTION_DOWN -> {
                animator.start()
                animatorY.start()
            }
        }
        return true
    }


}