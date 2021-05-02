package com.zj.utils.utils.view

import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.ScaleGestureDetector
import android.view.ScaleGestureDetector.SimpleOnScaleGestureListener
import kotlin.math.sqrt


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
 * 创建日期：2/1/21
 * 描述：OsmDroid
 * E-mail : 1276998208@qq.com
 * CSDN:https://blog.csdn.net/m0_37667770/article
 * GitHub:https://github.com/luhenchang
 */
class LHC_Scroll_distance_View @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0) : androidx.appcompat.widget.AppCompatImageView(context, attrs, defStyle) {
    private var oriDis: Float=0f
    private var eventModeType: Float=0f
    private var scalseFactor: Float=1f
    private var mScaleGestureDetector: ScaleGestureDetector? = null

    private var preScale=1f //之前的伸缩值
    private var curScale =1f //当前的伸缩值
    var viewxToY = 60f
     var maxXInit=0f
    init {
        initScaleGestureDetector()
    }
    override fun onDraw(canvas: Canvas) {
        maxXInit= measuredWidth.toFloat()
        drawCircle(canvas)
    }
    private val TAG = "ScaleGestureDemoView"


    private fun drawCircle(canvas: Canvas) {
        val linePaint = Paint()
        linePaint.isAntiAlias = true
        linePaint.strokeWidth = 20f
        linePaint.strokeCap = Paint.Cap.ROUND
        linePaint.color = Color.RED
        linePaint.style = Paint.Style.FILL
        canvas.drawCircle(viewxToY, (measuredHeight / 2).toFloat(), 60f * curScale, linePaint)
    }
    private fun initScaleGestureDetector() {
        mScaleGestureDetector = ScaleGestureDetector(context, object : SimpleOnScaleGestureListener() {
            override fun onScaleBegin(detector: ScaleGestureDetector): Boolean {
                return true
            }

            override fun onScale(detector: ScaleGestureDetector): Boolean {
                Log.e(TAG, "curScale-detector:" + detector.scaleFactor)
                curScale = detector.scaleFactor * preScale //当前的伸缩值*之前的伸缩值 保持连续性
//                1.1*1    ..    preScale=1.1*1
//                1.2*1.1  ..    preScale=1.2*1.1
//                1.3*1.2*1.1  ..    preScale=1.3*1.2*1.1


                Log.e(TAG, "curScale-Start:" + curScale)
                if (curScale > 5|| curScale < 0.1) {//当放大倍数大于5或者缩小倍数小于0.1倍 就不伸缩图片 返回true取消处理伸缩手势事件
                    preScale = curScale;
                    return true
                }
                preScale = curScale;//保存上一次的伸缩值
                Log.e(TAG, "curScale-preScale:" + preScale)

                invalidate()
                return false
            }

            override fun onScaleEnd(detector: ScaleGestureDetector) {}
        })
    }

    private var startX = 0f
    override fun onTouchEvent(event: MotionEvent): Boolean {
        mScaleGestureDetector!!.onTouchEvent(event)
        when (event.action and MotionEvent.ACTION_MASK) {
            MotionEvent.ACTION_DOWN -> {
                startX = event.x
            }
            MotionEvent.ACTION_POINTER_DOWN -> {
                //多点触控
                oriDis = distance(event)

            }
            MotionEvent.ACTION_MOVE -> {
                //每次通知计算滑动的一点点
                val dis = event.x - startX
                //记录这次移动结束的event.x就是下一次的滑动起始滑动的位置
                startX = event.x
                //将每次的滑动小段距离在当前距离的基础上叠加起来
                viewxToY = viewxToY + dis
                //通知刷新View
                invalidate()
            }
        }
        return true
    }

    /**
     * 计算两个手指间的距离
     *
     * @param event 触摸事件
     * @return 放回两个手指之间的距离
     */
    private fun distance(event: MotionEvent): Float {
        val x = event.getX(0) - event.getX(1)
        val y = event.getY(0) - event.getY(1)
        return sqrt((x * x + y * y).toDouble()).toFloat() //两点间距离公式
    }
}

//xInit = if (xInit + dis < minXInit) minXInit else Math.min(xInit + dis, maxXInit)
