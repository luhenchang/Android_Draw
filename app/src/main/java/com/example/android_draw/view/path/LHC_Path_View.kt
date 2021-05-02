package com.example.android_draw.view.path

import android.animation.Animator
import android.animation.ValueAnimator
import android.content.Context
import android.graphics.*
import android.os.Build
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.animation.addPauseListener
import java.util.*


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
 * 创建日期：2/28/21
 * 描述：Android_Draw
 * E-mail : 1276998208@qq.com
 * CSDN:https://blog.csdn.net/m0_37667770/article
 * GitHub:https://github.com/luhenchang
 */
@RequiresApi(Build.VERSION_CODES.KITKAT)
class LHC_Path_View @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var animator: ValueAnimator
    private var tans: FloatArray = FloatArray(2)
    private var pos: FloatArray = FloatArray(2)
    private var mCurAnimValue: Float = 0f
    private val canvasTop = 200f
    private val canvasMargerRight = 100f
    private val yMargerLeft = 200f
    private val xMargerBootom = 250f
    private val mlist: LinkedList<Float> = LinkedList()
    private val rlist: LinkedList<Float> = LinkedList()
    private val blist: LinkedList<Float> = LinkedList()
    private var animalEnd: Boolean=false


    init {
        mlist.add(150f)
        mlist.add(230f)
        mlist.add(220f)
        mlist.add(210f)
        mlist.add(120f)
        mlist.add(140f)
        mlist.add(280f)


        rlist.add(60f)
        rlist.add(120f)
        rlist.add(110f)
        rlist.add(100f)
        rlist.add(30f)
        rlist.add(60f)
        rlist.add(80f)



        blist.add(40f)
        blist.add(50f)
        blist.add(60f)
        blist.add(80f)
        blist.add(20f)
        blist.add(30f)
        blist.add(20f)

        animator = ValueAnimator.ofFloat(0f,1f)
        animator.addUpdateListener { animation ->
            mCurAnimValue = animation.animatedValue as Float
            invalidate()
        }
        animator.addListener(object : Animator.AnimatorListener{

            override fun onAnimationStart(animation: Animator?) {
                animalEnd=false

            }

            override fun onAnimationEnd(animation: Animator?) {
                animalEnd=true
                invalidate()
            }

            override fun onAnimationCancel(animation: Animator?) {
            }

            override fun onAnimationRepeat(animation: Animator?) {
            }
        })
        animator.duration = 6000


    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        //画布变换
        translateCanvas(canvas)
        //moveToAndLineTo(canvas)
        //rMove和rLineTo
        //rmoveAndLintTo(canvas)
        //
        // pathCircleRect(canvas)

        //绘制平行x轴的线段
        drawGridLine(canvas)
        //绘制xy轴上面的刻度和文字
        drawXcalibration(canvas)
        //绘制蓝色的折线
        drawCalibration(canvas)
        //draw绘制红色线
        drawRedCalibration(canvas)
        //绘制绿色的线条
        drawGreeenCalibration(canvas)


    }

    val resultGreenPath = Path()
    private fun drawGreeenCalibration(canvas: Canvas) {
        val paint = Paint()
        paint.color = Color.argb(255, 111, 211, 111)
        paint.strokeWidth = 2f
        paint.style = Paint.Style.STROKE
        paint.strokeCap = Paint.Cap.ROUND
        paint.textSize = 23f
        paint.setShadowLayer(20f, 10f, 10f, Color.GREEN)
        //对y轴高度进行分割每一份高度=(height-xMargerBootom-canvasTop)/6
        //对x轴进行分割每一份宽度=(width - yMargerLeft - canvasMargerRight)/7
        canvas.save()
        //平行x轴的线条
        val gridHeight = (height - xMargerBootom - canvasTop) / 6
        val unitPx = gridHeight / 50
        val xWidth = width - yMargerLeft - canvasMargerRight
        val griWidth = xWidth / 7
        val oneOfLineX = Path()
        canvas.translate(100f, 0f)
        for (index in 0 until blist.size) {
            if (index == 0) {
                oneOfLineX.moveTo(0f, blist[0] * unitPx)
                paint.style = Paint.Style.FILL
                canvas.drawCircle(0f, blist[0] * unitPx, 10f, paint)
            } else {
                oneOfLineX.lineTo(griWidth * index, unitPx * blist[index])
                paint.style = Paint.Style.FILL
                canvas.drawCircle(griWidth * index, blist[index] * unitPx, 10f, paint)

            }

        }
        paint.style = Paint.Style.STROKE
        val mPathMeasure = PathMeasure(oneOfLineX, false)
        val length: Float = mPathMeasure.length
        val stop: Float = length
        val start = 0f
        //用于截取整个path中某个片段,通过参数startD和stopD来控制截取的长度,
        // 并将截取后的path保存到参数dst中,最后一个参数表示起始点是否使用moveTo将路径的新起始点移到结果path的起始点中,
        // 通常设置为true
        val pos = FloatArray(2)
        val tans = FloatArray(2)

        mPathMeasure.getSegment(start, stop * (mCurAnimValue), resultGreenPath, true)
        mPathMeasure.getPosTan(length * mCurAnimValue, pos, tans)
        val endPath = Path()
        for (index in pos.indices) {
            endPath.lineTo(pos[0], pos[1])
        }
        canvas.drawPath(resultGreenPath, paint)

        canvas.restore()
    }

    val resultRedPath = Path()

    /**
     * 每个小册大家最关心的是干货是否满满,内容是否精细。接下来我们可以看看章节内容和实现效果。
     * 1.艺术在于绘制
     * 2.绘制相关API
     * 3.画布的学习
     * 4.画笔的学习
     * 5.路径的学习
     * 6.自定义文字的测量和绘制
     * 7.路径的测量和动画
     * 8.基本的几何图形绘制
     * 9.路径的测量和动画
     * 10.画布的裁剪
     * 11.自定义好看的界面效果
     * 12.曲线的学习
     * 13.曲线的绘制
     * 14.折线的绘制
     * 15.手势的学习
     * 16.动画的学习
     * 17.绘制中的动画
     * 18.绘制中增加手势点击
     * 19.绘制中的手势滑动
     * 20.绘制中的手势缩放
     * 21.图片的学习
     * 22.绘制k线手势滑动和点击
     * 23.绘制中的裁剪
     * 24.绘制中的测量
     * 25.手势+测量+绘制的整合
     * 26.自定义绘制-小游戏
     *
     */
    private fun drawRedCalibration(canvas: Canvas) {
        val paint = Paint()
        paint.color = Color.argb(55, 211, 111, 111)
        paint.strokeWidth = 2f
        paint.style = Paint.Style.STROKE
        paint.strokeCap = Paint.Cap.ROUND
        paint.textSize = 23f
        paint.setShadowLayer(20f, 10f, 10f, Color.BLUE)
        //对y轴高度进行分割每一份高度=(height-xMargerBootom-canvasTop)/6
        //对x轴进行分割每一份宽度=(width - yMargerLeft - canvasMargerRight)/7
        canvas.save()
        //平行x轴的线条
        val gridHeight = (height - xMargerBootom - canvasTop) / 6
        val unitPx = gridHeight / 50
        val xWidth = width - yMargerLeft - canvasMargerRight
        val griWidth = xWidth / 7
        val oneOfLineX = Path()
        canvas.translate(100f, 0f)
        for (index in 0 until rlist.size) {
            if (index == 0) {
                oneOfLineX.moveTo(0f, rlist[0] * unitPx)
                paint.style = Paint.Style.FILL
                canvas.drawCircle(0f, rlist[0] * unitPx, 10f, paint)
            } else {
                oneOfLineX.lineTo(griWidth * index, unitPx * rlist[index])
                paint.style = Paint.Style.FILL
                canvas.drawCircle(griWidth * index, rlist[index] * unitPx, 10f, paint)

            }

        }
        paint.style = Paint.Style.FILL
        val mPathMeasure = PathMeasure(oneOfLineX, false)
        val length: Float = mPathMeasure.length
        val stop: Float = length
        val start = 0f
        //用于截取整个path中某个片段,通过参数startD和stopD来控制截取的长度,
        // 并将截取后的path保存到参数dst中,最后一个参数表示起始点是否使用moveTo将路径的新起始点移到结果path的起始点中,
        // 通常设置为true


        resultRedPath.reset()

        mPathMeasure.getSegment(start, stop * mCurAnimValue, resultRedPath, true)
        mPathMeasure.setPath(resultRedPath, false)
        val pos = FloatArray(2)
        mPathMeasure.getPosTan(length - 1, pos, null)


        resultRedPath.lineTo(pos[0], 0F)
        resultRedPath.lineTo(0F, 0F)
        resultRedPath.close()

        canvas.drawPath(resultRedPath, paint)

        if (animalEnd){
            val oneOfLineX = Path()
            paint.strokeWidth=5f
            paint.color = Color.argb(255, 211, 111, 111)
            for (index in 0 until rlist.size) {
                if (index == 0) {
                    oneOfLineX.moveTo(0f, rlist[0] * unitPx)
                    paint.style = Paint.Style.FILL
                    canvas.drawCircle(0f, rlist[0] * unitPx, 10f, paint)
                } else {
                    oneOfLineX.lineTo(griWidth * index, unitPx * rlist[index])
                    paint.style = Paint.Style.FILL
                    canvas.drawCircle(griWidth * index, rlist[index] * unitPx, 10f, paint)

                }

            }
            paint.style=Paint.Style.STROKE
            canvas.drawPath(oneOfLineX,paint)
        }

        canvas.restore()
    }


    val resultPath = Path()
    private fun drawCalibration(canvas: Canvas) {
        val paint = Paint()
        paint.color = Color.argb(55, 111, 111, 211)
        paint.strokeWidth = 2f
        paint.style = Paint.Style.STROKE
        paint.strokeCap = Paint.Cap.ROUND
        paint.textSize = 23f
        paint.setShadowLayer(20f, 10f, 10f, Color.BLUE)
        //对y轴高度进行分割每一份高度=(height-xMargerBootom-canvasTop)/6
        //对x轴进行分割每一份宽度=(width - yMargerLeft - canvasMargerRight)/7
        canvas.save()
        //平行x轴的线条
        val gridHeight = (height - xMargerBootom - canvasTop) / 6
        val unitPx = gridHeight / 50
        val xWidth = width - yMargerLeft - canvasMargerRight
        val griWidth = xWidth / 7
        val oneOfLineX = Path()
        canvas.translate(100f, 0f)
        for (index in 0 until mlist.size) {
            if (index == 0) {
                oneOfLineX.moveTo(griWidth * (mlist.size - 1), 0f)
                oneOfLineX.lineTo(0f, 0f)
                oneOfLineX.lineTo(0f, mlist[0] * unitPx)
                paint.style = Paint.Style.FILL
                canvas.drawCircle(0f, mlist[0] * unitPx, 10f, paint)
            } else {
                oneOfLineX.lineTo(griWidth * index, unitPx * mlist[index])
                paint.style = Paint.Style.FILL
                canvas.drawCircle(griWidth * index, mlist[index] * unitPx, 10f, paint)

            }

        }
        paint.style = Paint.Style.FILL
        val mPathMeasure = PathMeasure(oneOfLineX, false)
        val length: Float = mPathMeasure.length
        val stop: Float = length
        val start = 0f
        //用于截取整个path中某个片段,通过参数startD和stopD来控制截取的长度,
        // 并将截取后的path保存到参数dst中,最后一个参数表示起始点是否使用moveTo将路径的新起始点移到结果path的起始点中,
        // 通常设置为true
        resultPath.reset()


        mPathMeasure.getSegment(start, stop * mCurAnimValue, resultPath, true)
        mPathMeasure.setPath(resultPath, false)
        val pos = FloatArray(2)
        mPathMeasure.getPosTan(length - 1, pos, null)


        resultPath.lineTo(pos[0], 0F)
        resultPath.lineTo(0F, 0F)
        resultPath.close()

        canvas.drawPath(resultPath, paint)
        if (animalEnd){
            val oneOfLineX = Path()
            paint.color = Color.argb(255, 111, 111, 211)
            for (index in 0 until mlist.size) {
                if (index == 0) {
                    oneOfLineX.moveTo(griWidth * (mlist.size - 1), 0f)
                    oneOfLineX.lineTo(0f, 0f)
                    oneOfLineX.lineTo(0f, mlist[0] * unitPx)
                    paint.style = Paint.Style.FILL
                    canvas.drawCircle(0f, mlist[0] * unitPx, 10f, paint)
                } else {
                    oneOfLineX.lineTo(griWidth * index, unitPx * mlist[index])
                    paint.style = Paint.Style.FILL
                    canvas.drawCircle(griWidth * index, mlist[index] * unitPx, 10f, paint)

                }

            }
            paint.style=Paint.Style.STROKE
            canvas.drawPath(oneOfLineX,paint)
        }
        canvas.restore()
    }

    private fun drawXcalibration(canvas: Canvas) {
        val paint = Paint()
        paint.color = Color.argb(155, 111, 111, 111)
        paint.strokeWidth = 2f
        paint.style = Paint.Style.STROKE
        paint.strokeCap = Paint.Cap.ROUND
        paint.textSize = 23f
        //对y轴高度进行分割每一份高度=(height-xMargerBootom-canvasTop)/6
        //对x轴进行分割每一份宽度=(width - yMargerLeft - canvasMargerRight)/7
        canvas.save()
        //平行x轴的线条
        val gridHeight = (height - xMargerBootom - canvasTop) / 6
        val xWidth = width - yMargerLeft - canvasMargerRight
        val griWidth = xWidth / 7
        for (index in 0 until 8) {
            val oneOfLineX = Path()
            oneOfLineX.moveTo(0f, 0f)
            oneOfLineX.lineTo(0f, -10f)
            canvas.drawPath(oneOfLineX, paint)
            //为了文字翻转正常进行状态储存避免影响刻度绘制
            canvas.save()
            canvas.scale(1f, -1f)
            //文字位置最好进行测量文字高度和宽度来决定
            canvas.drawText("onesDay", griWidth / 3, 28f, paint)
            canvas.restore()
            canvas.translate(griWidth, 0f)
        }
        canvas.restore()

        //y轴文字
        canvas.save()
        for (index in 0 until 8) {
            //为了文字翻转正常进行状态储存避免影响刻度绘制
            canvas.save()
            canvas.scale(1f, -1f)
            //文字位置最好进行测量文字高度和宽度来决定
            if (index < 7) {
                canvas.drawText("${index * 50}", -30f, 0f, paint)
            }
            canvas.restore()
            canvas.translate(0f, gridHeight)
        }
        canvas.restore()
    }

    private fun drawGridLine(canvas: Canvas) {
        val paint = Paint()
        paint.color = Color.argb(55, 111, 111, 111)
        paint.strokeWidth = 2f
        paint.style = Paint.Style.STROKE
        paint.strokeCap = Paint.Cap.ROUND
        //对y轴高度进行分割每一份高度=(height-xMargerBootom-canvasTop)/6
        //对x轴进行分割每一份宽度=(width - yMargerLeft - canvasMargerRight)/7
        canvas.save()
        //平行x轴的线条
        val gridHeight = (height - xMargerBootom - canvasTop) / 6
        val xWidth = width - yMargerLeft - canvasMargerRight
        val griWidth = xWidth / 7
        for (index in 0 until 7) {
            val oneOfLineX = Path()
            oneOfLineX.moveTo(0f, 0f)
            oneOfLineX.lineTo(xWidth, 0f)
            canvas.drawPath(oneOfLineX, paint)
            canvas.translate(0f, gridHeight)
        }
        canvas.restore()
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        if (event.action == MotionEvent.ACTION_DOWN) {
            animator.start()
            return true
        }
        return super.onTouchEvent(event)

    }
    private fun pathCircleRect(canvas: Canvas) {
        val paint = Paint()
        paint.color = Color.argb(255, 0, 0, 0)
        paint.strokeWidth = 2f
        paint.style = Paint.Style.STROKE
        paint.strokeCap = Paint.Cap.ROUND

        //绘制圆
        canvas.translate(400f, 200f)
        val path = Path()
        path.addCircle(0f, 0f, 100f, Path.Direction.CCW)
        canvas.drawPath(path, paint)

        //绘制矩形
        canvas.translate(200f, 0f)
        val pathRect = Path()
        val rect = RectF(110f, 0f, 200f, 150f)
        pathRect.addRect(rect, Path.Direction.CW)
        canvas.drawPath(pathRect, paint)

        //绘制椭圆
        canvas.translate(200f, 0f)
        val pathOvelRect = Path()
        val overRectF = RectF(110f, -110f, 200f, 150f)
        pathOvelRect.addOval(overRectF, Path.Direction.CW)
        canvas.drawPath(pathOvelRect, paint)


    }

    private fun rmoveAndLintTo(canvas: Canvas) {
        val paint = Paint()
        paint.color = Color.argb(255, 0, 0, 0)
        paint.strokeWidth = 2f
        paint.style = Paint.Style.STROKE
        paint.strokeCap = Paint.Cap.ROUND

        //绘制
        val path = Path()
        path.moveTo(0f, 0f)
        path.lineTo(100f, 100f)

        //绘制巨型路径
        //相对于100f,100f作为（0，0）进行向右边200 底边
        path.rLineTo(200f, 0f)
        //相对于上一个点作为（0，0）进行向上100绘制线段 右边
        path.rLineTo(0f, 100f)
        //相对于上一个点作为（0,0）进行向左边200绘制线段 上边
        path.rLineTo(-200f, 0f)
        //相对于上一个点作为（0，0）向下绘制100的线段 左边
        path.rLineTo(0f, -100f)
        val matrix = Matrix()
        matrix.setScale(2f, 2f)
        matrix.setRotate(45f)
        path.transform(matrix)

        canvas.drawPath(path, paint)

//
//        canvas.save()
//        canvas.translate(100f, 100f)
//        canvas.drawPath(path, paint)
//        canvas.restore()


//        path.offset(100f,100f)

    }

    private fun moveToAndLineTo(canvas: Canvas) {
        //画笔
        val paint = Paint()
        paint.color = Color.argb(155, 111, 111, 111)
        paint.strokeWidth = 2f
        paint.style = Paint.Style.STROKE
        paint.strokeCap = Paint.Cap.ROUND

        //利用moveTo和lintTo绘制y轴
        val yPath = Path()
        yPath.moveTo(0f, 0f)
        yPath.lineTo(0f, height - xMargerBootom - canvasTop)
        canvas.drawPath(yPath, paint)

        //利用moveTo和lintTo绘制x轴
        val xPath = Path()
        xPath.moveTo(0f, 0f)
        xPath.lineTo(width - yMargerLeft - canvasMargerRight, 0f)
        canvas.drawPath(xPath, paint)


    }

    private fun translateCanvas(canvas: Canvas) {
        val paint = Paint()
        paint.color = Color.argb(55, 111, 111, 111)
        paint.strokeWidth = 2f
        paint.style = Paint.Style.STROKE
        paint.strokeCap = Paint.Cap.ROUND
        //x轴竞相
        canvas.scale(1f, -1f)
        //坐标系向下平移
        canvas.translate(yMargerLeft, -height.toFloat() + xMargerBootom)
    }


}