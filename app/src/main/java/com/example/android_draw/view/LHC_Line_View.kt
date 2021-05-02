package com.zj.utils.utils.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.ScaleGestureDetector
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
 * 创建日期：1/26/21
 * 描述：OsmDroid
 * E-mail : 1276998208@qq.com
 * CSDN:https://blog.csdn.net/m0_37667770/article
 * GitHub:https://github.com/luhenchang
 */
@Suppress("UNREACHABLE_CODE")
class LHC_Line_View @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : View(context, attrs, defStyle) {
    private var xwidthMax: Float=0f
    private var maxXInit: Float=0f
    private var minXInit: Float=0f
    private var viewxToY=0f
    val grid_wh = 200f
    var pointList: ArrayList<ViewPoint> = ArrayList()
    var titleList: ArrayList<String> = ArrayList()
    var marginXAndY = 100f
    var arrowLRHeight = 10f
    var arrowLength = marginXAndY + 20
    var rctArrayList: ArrayList<Rect> = ArrayList()
    var rightTopSubject = ""
    lateinit var canvas: Canvas
    var blackRect: Rect? = null
    var visible = false


    private var mScaleGestureDetector: ScaleGestureDetector? = null
    private var preScale=1f //之前的伸缩值
    private var curScale =1f //当前的伸缩值
    init {
        initScaleGestureDetector()
        pointList.add(ViewPoint(0f, 10f))
        pointList.add(ViewPoint(120f, 140f))
        pointList.add(ViewPoint(380f, 200f))
        pointList.add(ViewPoint(420f, 360f))
        pointList.add(ViewPoint(600f, 300f))
        pointList.add(ViewPoint(600f, 300f))
        pointList.add(ViewPoint(700f, 300f))
        pointList.add(ViewPoint(800f, 360f))
        pointList.add(ViewPoint(900f, 300f))
        pointList.add(ViewPoint(950f, 300f))
        pointList.add(ViewPoint(1050f, 500f))
        pointList.add(ViewPoint(1150f, 300f))
        pointList.add(ViewPoint(1250f, 300f))
        pointList.add(ViewPoint(1450f, 200f))
        pointList.add(ViewPoint(1750f, 300f))
        pointList.add(ViewPoint(1850f, 400f))



        titleList.add("10万")
        titleList.add("140万")
        titleList.add("200万")
        titleList.add("400万")
        titleList.add("340万")
        titleList.add("340万")
        titleList.add("340万")
        titleList.add("400万")
        titleList.add("340万")
        titleList.add("340万")
        titleList.add("340万")
        titleList.add("340万")
        titleList.add("340万")
        titleList.add("400万")
        titleList.add("340万")
        titleList.add("340万")


    }

    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        this.canvas = canvas
        val back_paint = Paint()
        back_paint.style = Paint.Style.FILL
        back_paint.color = Color.WHITE
        back_paint.strokeWidth = 10f

        canvas.save()

        //1.变换坐标系为我们常见的
        changeCanvaXY(canvas)

        //2.画x轴和y轴
        drawXAndY(canvas)

        //3.画文字
        drawXTitle(canvas)
        //3.1绘制y轴旁边的文字
        drawYTitle(canvas)

        //2.画网格
        //drawGridView(canvas)

        //3.画线
        drawLine(pointList, canvas)


        //4.绘制文字
        drawText(canvas)

        //5.绘制巨型框写一些信息。
        drawRRect(canvas)

        //6.点击地方出现一个弹出ok？当然ok
        drawWindowRect(canvas)

    }

    private fun initScaleGestureDetector() {
        mScaleGestureDetector = ScaleGestureDetector(
            context,
            object : ScaleGestureDetector.SimpleOnScaleGestureListener() {
                override fun onScaleBegin(detector: ScaleGestureDetector): Boolean {
                    return true
                }

                override fun onScale(detector: ScaleGestureDetector): Boolean {
                    curScale = detector.scaleFactor * preScale //当前的伸缩值*之前的伸缩值 保持连续性
                    Log.e("ScaleGestureDetector", "onScale: " + detector.scaleFactor)
                    if (curScale > 5 || curScale < 0.1) {//当放大倍数大于5或者缩小倍数小于0.1倍 就不伸缩图片 返回true取消处理伸缩手势事件
                        preScale = curScale;
                        return true
                    }
                    preScale = curScale;//保存上一次的伸缩值
                    invalidate()
                    return false
                }

                override fun onScaleEnd(detector: ScaleGestureDetector) {}
            })
    }
    private fun drawWindowRect(canvas: Canvas) {
        if (blackRect != null && visible) {
            val rrPaint = Paint()
            rrPaint.color = Color.BLACK
            rrPaint.style = Paint.Style.FILL
            rrPaint.strokeWidth = 1f
            rrPaint.setShadowLayer(5f, -5f, -5f, Color.argb(50, 111, 111, 111))
            canvas.drawRoundRect(
                blackRect!!.left.toFloat(),
                blackRect!!.top.toFloat(),
                blackRect!!.right.toFloat(),
                blackRect!!.bottom.toFloat(),
                10f,
                10f,
                rrPaint
            )
            canvas.save()
            canvas.translate(blackRect!!.left.toFloat(), blackRect!!.top.toFloat())
            canvas.scale(1f, -1f)

            val ttPaint = Paint()
            ttPaint.color = Color.WHITE
            ttPaint.style = Paint.Style.FILL
            ttPaint.strokeWidth = 1f
            ttPaint.strokeCap = Paint.Cap.ROUND
            ttPaint.textSize = 24f
            //0f,0f表示圆点,这样看着可能好理解，20f,30f是我不打算测量文字直接写死的偏移。。
            canvas.drawText("M:${rightTopSubject}", 0f + 20f, 0f + 30f, ttPaint)
        }

    }

    private fun drawRRect(canvas: Canvas) {
        //右上角画一个
        val rrPaint = Paint()
        rrPaint.color = Color.WHITE
        rrPaint.style = Paint.Style.FILL
        rrPaint.strokeWidth = 1f
        rrPaint.setShadowLayer(5f, -5f, -5f, Color.argb(50, 111, 111, 111))
        canvas.drawRoundRect(
            measuredWidth - 400f + viewxToY,
            measuredHeight - 150f,
            measuredWidth - 180f + viewxToY,
            measuredHeight - 250f,
            5f,
            5f,
            rrPaint
        )
        rrPaint.setShadowLayer(5f, 1f, 1f, Color.argb(50, 111, 111, 111))
        canvas.drawRoundRect(
            measuredWidth - 400f + viewxToY,
            measuredHeight - 150f,
            measuredWidth - 180f + viewxToY,
            measuredHeight - 250f,
            5f,
            5f,
            rrPaint
        )
        rrPaint.color = Color.GREEN
        canvas.drawCircle(measuredWidth - 390f + viewxToY, measuredHeight - 170f, 5f, rrPaint)

        canvas.save()
        //变换坐标
        canvas.translate(measuredWidth - 380f, measuredHeight - 180f)
        canvas.scale(1f, -1f)
        val ttPaint = Paint()
        ttPaint.color = Color.BLACK
        ttPaint.style = Paint.Style.FILL
        ttPaint.strokeWidth = 1f
        ttPaint.textSize = 24f
        canvas.drawText("总额: 2980万", 0f + viewxToY, 0f, ttPaint)
        canvas.drawText("点击金额:$rightTopSubject", 0f + viewxToY, getTextHeight(ttPaint), ttPaint)
        canvas.restore()
    }

    //绘制x轴的文字
    private fun drawXTitle(canvas: Canvas) {
        val xtitle_paint = Paint()
        xtitle_paint.color = Color.BLACK
        xtitle_paint.textSize = 24f*curScale
        xtitle_paint.strokeWidth = 10f
        val xwidth = (measuredWidth - marginXAndY) / 6
        xtitle_paint.shader = getShadersStarAndEnd(xwidth, 0f)
        //骚的东西看Api试一试就明白了
        xtitle_paint.setShadowLayer(20f, 20f, 20f, Color.BLUE)
        for (index in 0 until 11) {
            canvas.save()
            //平移坐标圆点到绘制文字点
            canvas.translate(
                xwidth * (index + 1) * curScale,
                -(getTextHeight(xtitle_paint)) * curScale
            )
            //坐标系变换,目的让文字正常摆放。
            canvas.scale(1f, -1f)

            //绘制背景骚一波
            //绘制背景
            canvas.drawRoundRect(
                -getTextWidth(xtitle_paint, "${index + 2}月月") / 2 + viewxToY, -getTextHeight(
                    xtitle_paint
                ) / 2, getTextWidth(xtitle_paint, "${index + 2}月月") / 2 + viewxToY, getTextHeight(
                    xtitle_paint
                ) / 2, 10f, 10f, getTextBackgroudPaint(40, 5, 0)
            )
            //绘制文字
            canvas.drawText(
                "${index + 2}月",
                0,
                "${index + 2}月".length,
                -getTextWidth(
                    xtitle_paint,
                    "${index + 2}月"
                ) / 2 * curScale + viewxToY,
                getTextHeight(xtitle_paint) / 3 * curScale,
                xtitle_paint
            )
            canvas.restore()
        }
    }

    //绘制x轴的文字
    private fun drawYTitle(canvas: Canvas) {
        val ytitle_paint = Paint()
        ytitle_paint.color = Color.BLACK
        ytitle_paint.textSize = 24f
        ytitle_paint.strokeWidth = 10f
        val yHeight = (measuredHeight - marginXAndY) / 5
        val yyHeight = (measuredHeight.toFloat()) / 5
        ytitle_paint.setShadowLayer(20f, 20f, 20f, Color.BLUE)

        for (index in 0 until 5) {
            //为了炫酷。哈哈
            ytitle_paint.shader = getShadersStarAndEnd(
                getTextWidth(
                    ytitle_paint,
                    "${yHeight * (index + 1)}万"
                ) / 2, 0f
            )

            canvas.save()
            //平移坐标圆点到绘制文字点
            canvas.translate(
                -getTextWidth(ytitle_paint, "${yHeight * (index + 1)}万万") / 2,
                yHeight * (index + 1)
            )
            //坐标系变换,目的让文字正常摆放。
            canvas.scale(1f, -1f)
            //绘制背景。为了调整位置。多加了个字符。就不具体计算了
            val backPaint = getTextBackgroudPaint(40, 5, 0)
            canvas.drawRoundRect(
                -getTextWidth(ytitle_paint, "${yyHeight.toInt() * (index + 1)}万") / 2,
                -(getTextHeight(
                    ytitle_paint
                ) + getTextHeight(ytitle_paint) / 2) / 1.2f,
                getTextWidth(
                    ytitle_paint,
                    "${yyHeight.toInt() * (index + 1)}万"
                ) / 2,
                -(getTextHeight(ytitle_paint) + getTextHeight(ytitle_paint) / 2) / 6f,
                10f,
                10f,
                backPaint
            )
            canvas.drawText(
                "${yyHeight.toInt() * (index + 1)}万",
                0,
                "${yyHeight.toInt() * (index + 1)}万".length,
                -getTextWidth(
                    ytitle_paint,
                    "${yyHeight.toInt() * (index + 1)}万"
                ) / 2,
                -getTextHeight(ytitle_paint) / 2,
                ytitle_paint
            )
            canvas.restore()
        }
    }

    private fun drawXAndY(canvas: Canvas) {
        val x_paint = Paint()
        x_paint.style = Paint.Style.STROKE
        x_paint.color = Color.WHITE
        x_paint.strokeWidth = 3f
        x_paint.shader = getShaderXY(true)
        x_paint.setShadowLayer(6f, 4f, -3f, Color.argb(100, 255, 100, 100))


        val y_paint = Paint()
        y_paint.style = Paint.Style.STROKE
        y_paint.color = Color.WHITE/**/
        y_paint.strokeWidth = 3f
        y_paint.shader = getShaderXY(false)
        y_paint.setShadowLayer(6f, -6f, -3f, Color.argb(100, 100, 255, 100))

        val path = Path()
        path.moveTo(0f, 0f)
        path.lineTo(0f, measuredHeight + 10f)


        //Y轴的箭头绘制
        val verticlePath = Path()
        verticlePath.moveTo(-arrowLRHeight, measuredHeight - arrowLength)
        verticlePath.lineTo(0f, measuredHeight - marginXAndY)
        verticlePath.lineTo(arrowLRHeight, measuredHeight - arrowLength)
        path.addPath(verticlePath)

        //画y轴
        canvas.drawPath(path, y_paint)
        xwidthMax=12*((measuredWidth-marginXAndY)/6)
        //绘制x轴
        val pathx = Path()
        //手势滑动的距离加上
        pathx.moveTo((0f + viewxToY) * curScale, 0f)
        pathx.lineTo((xwidthMax - marginXAndY + viewxToY) * curScale, 0f)

        val horizontalPath = Path()
        horizontalPath.moveTo((xwidthMax - arrowLength + viewxToY) * curScale, arrowLRHeight)
        horizontalPath.lineTo((xwidthMax - marginXAndY + viewxToY) * curScale, 0f)
        horizontalPath.lineTo((xwidthMax - arrowLength + viewxToY) * curScale, -arrowLRHeight)
        pathx.addPath(horizontalPath)
        //画x轴
        canvas.drawPath(pathx, x_paint)
    }

    private fun drawText(canvas: Canvas) {
        rctArrayList.clear()
        //4.重写定义画笔
        for (index in 0 until titleList.size) {
            val text_paint = Paint()
            text_paint.color = Color.WHITE
            text_paint.textSize = 22f*curScale
            text_paint.strokeWidth = 10f*curScale

            val textBackPaint = getTextBackgroudPaint(225, 165, 65)
            //4.绘制文字之前保存在堆栈将坐标系。
            canvas.save()
            //平移一步到位。往上平移一点。比定点高。避免重复
            val titleWidth = getTextWidth(textBackPaint, titleList[index])
            rctArrayList.add(
                Rect(
                    (pointList[index].x.toInt() + viewxToY).toInt(),
                    pointList[index].y.toInt(),
                    ((pointList[index].x + titleWidth).toInt() + viewxToY).toInt(),
                    (pointList[index].y + getTextHeight(
                        text_paint
                    )).toInt()
                )
            )
            canvas.translate(
                (pointList[index].x + viewxToY) * curScale, (pointList[index].y + getTextHeight(
                    textBackPaint
                )) * curScale
            )
            //存储巨型进行点击时候判断
            //变换坐标
            canvas.scale(1f, -1f)
            canvas.rotate((10).toFloat())

            //绘制背景
            canvas.drawRoundRect(
                0f,
                -getTextHeight(textBackPaint) * curScale,
                getTextWidth(
                    textBackPaint,
                    titleList[index]
                ) * curScale,
                getTextHeight(textBackPaint) / 2 * curScale,
                10f * curScale,
                10f * curScale,
                getTextBackgroudPaint(
                    225,
                    165,
                    65
                )
            )
            //绘制文字
            canvas.drawText(titleList[index], 0, titleList[index].length, 0f, 0f, text_paint)
            //恢复坐标系
            canvas.restore()
        }


    }

    var clickDow = false
    var  startX=0f
    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        mScaleGestureDetector!!.onTouchEvent(event)
        if (event.action == MotionEvent.ACTION_DOWN) {
            clickDow = true
            startX=event.x
            return true
        }
        if (event.action==MotionEvent.ACTION_MOVE){
            //每次通知计算滑动的一点点
            val dis = event.x - startX
            //记录这次移动结束的event.x就是下一次的滑动起始滑动的位置
            startX = event.x
            //将每次的滑动小段距离在当前距离的基础上叠加起来
            minXInit=measuredWidth-xwidthMax
            if (viewxToY + dis < minXInit) {
                viewxToY = minXInit
            } else if (viewxToY + dis > maxXInit) {
                viewxToY = maxXInit
            } else {
                viewxToY += dis
            }
            invalidate()
        }
        if (event.action == MotionEvent.ACTION_UP&&clickDow) {
            //通知刷新View
            for (index in 0 until rctArrayList.size) {
                //转换坐标为
                val contais = rctArrayList[index].contains(
                    (event.x.toInt() - marginXAndY).toInt(),
                    (measuredHeight - marginXAndY - event.y.toInt()).toInt()
                )
                if (contais) {
                    //ToastUtils.showLong("点击文字=$index")
                    rightTopSubject = titleList[index]
                    //初始化Rect
                    val x = event.x - marginXAndY
                    val y = measuredHeight - event.y - marginXAndY
                    //明确坐标系的正负方向很重要
                    blackRect = Rect(
                        (x - 70).toInt(),
                        y.toInt(),
                        (x + 70).toInt(),
                        (y - 200).toInt()
                    )
                    //可显示
                    visible=true
                    invalidate()
                    //当然动画什么的都可以去刷新...这里比较简单的搞一搞效果而已
                    postDelayed({
                        visible = false
                        invalidate()
                    }, 2000)
                    break
                }
            }
            clickDow = false
        }
        return super.onTouchEvent(event)
    }

    private fun getTextHeight(textBackPaint: Paint): Float {
        val fontMetrics: Paint.FontMetrics = textBackPaint.fontMetrics
        val height1 = fontMetrics.descent - fontMetrics.ascent + fontMetrics.leading
        // height2测得的高度可能稍微比height1高一些
        // height2测得的高度可能稍微比height1高一些
        val height2 = fontMetrics.bottom - fontMetrics.top + fontMetrics.leading
        return height2
    }

    private fun getTextWidth(textBackPaint: Paint, textStr: String): Float {
        // 文字的宽度
        // 文字的宽度
        val strWidth = textBackPaint.measureText(textStr)
        return strWidth
    }

    private fun getTextBackgroudPaint(alpha: Int, centerAlpha: Int, endAlpha: Int): Paint {
        val paint = Paint()
        paint.textSize = 22f
        val random = Random()
        val R = random.getRandom(225)
        val G = random.getRandom(225)
        val B = random.getRandom(225)

        val R1 = random.getRandom(225)
        val G1 = random.getRandom(225)
        val B1 = random.getRandom(225)

        val R2 = random.getRandom(225)
        val G2 = random.getRandom(225)
        val B2 = random.getRandom(225)
        val shadeColors = intArrayOf(
            Color.argb(alpha, R, G, B), Color.argb(centerAlpha, R1, G1, B1), Color.argb(
                endAlpha,
                R2,
                G2,
                B2
            )
        )
        val mShader = LinearGradient(0f, 0f, 44f, 44f, shadeColors, null, Shader.TileMode.CLAMP)
        paint.shader = mShader
        return paint
    }

    //绘制折线图
    private fun drawLine(pointList: java.util.ArrayList<ViewPoint>, canvas: Canvas) {
        val linePaint = Paint()
        val path = Path()
        linePaint.style = Paint.Style.STROKE
        linePaint.color = Color.argb(255, 225, 225, 255)
        linePaint.strokeWidth = 10f


        val circle_paint = Paint()
        circle_paint.strokeWidth = 10f
        circle_paint.style = Paint.Style.FILL


        //连线
        path.moveTo(viewxToY * curScale, 0f)
        for (index in 0 until pointList.size) {
            path.lineTo((pointList[index].x + viewxToY) * curScale, pointList[index].y * curScale)
        }
        canvas.drawPath(path, linePaint)

        path.reset()
        //渐变色菜的填充
        for (index in 0 until pointList.size) {
            path.lineTo((pointList[index].x + viewxToY) * curScale, pointList[index].y * curScale)
        }
        val endIndex = pointList.size - 1
        path.lineTo((pointList[endIndex].x + viewxToY) * curScale, 0f)
        path.close()
        linePaint.style = Paint.Style.FILL
        linePaint.shader = getShader()
        linePaint.setShadowLayer(16f, 6f, -6f, Color.argb(100, 100, 255, 100))
        canvas.drawPath(path, linePaint)


        //画定点圆圈
        for (index in 0 until pointList.size) {
            circle_paint.shader = getShaders()
            canvas.drawCircle(
                (pointList[index].x + viewxToY) * curScale,
                pointList[index].y * curScale,
                16f,
                circle_paint
            )
        }


    }

    private fun getShader(): Shader {
        val shadeColors = intArrayOf(
            Color.argb(255, 250, 49, 33), Color.argb(209, 250, 49, 33), Color.argb(
                0,
                250,
                49,
                33
            )
        )
        return LinearGradient(
            (measuredWidth / 2).toFloat(),
            measuredHeight.toFloat(),
            (measuredWidth / 2).toFloat(),
            0f,
            shadeColors,
            null,
            Shader.TileMode.CLAMP
        )
    }


    //变换为熟悉的坐标系
    private fun changeCanvaXY(canvas: Canvas) {
        //竟变化坐标。y轴向上为正
        canvas.scale(1f, -1f)
        //平移坐标系到左下角
        canvas.translate(marginXAndY, -(measuredHeight.toFloat()) + marginXAndY)
    }

    //绘制网格
    private fun drawGridView(canvas: Canvas) {
        val grid_paint = Paint()

        grid_paint.style = Paint.Style.STROKE
        grid_paint.color = Color.argb(45, 111, 111, 111)
        grid_paint.strokeWidth = 1f

        //平行y轴的线段
        val pathY = Path()
        pathY.moveTo(grid_wh, 0f)
        pathY.lineTo(grid_wh, measuredHeight.toFloat())
        canvas.drawPath(pathY, grid_paint)

        //平行x轴的线段
        val pathX = Path()
        pathX.moveTo(0f, grid_wh)
        pathX.lineTo(measuredWidth.toFloat(), grid_wh)
        canvas.drawPath(pathX, grid_paint)

        //x轴个数
        val countX = measuredWidth / grid_wh
        //y轴个数
        val countY = measuredHeight / grid_wh
        canvas.save()

        for (index in 0 until countY.toInt()) {
            canvas.translate(0f, grid_wh)
            canvas.drawPath(pathX, grid_paint)

        }

        canvas.restore()
        canvas.save()
        for (index in 0 until countX.toInt()) {
            canvas.translate(grid_wh, 0f)
            canvas.drawPath(pathY, grid_paint)
        }
        canvas.restore()

    }

    fun getShaderXY(isX: Boolean): Shader {
        val random = Random()
        val R = random.getRandom(225)
        val G = random.getRandom(225)
        val B = random.getRandom(225)

        val R1 = random.getRandom(225)
        val G1 = random.getRandom(225)
        val B1 = random.getRandom(225)

        val R2 = random.getRandom(225)
        val G2 = random.getRandom(225)
        val B2 = random.getRandom(225)
        val shadeColors = intArrayOf(
            Color.argb(225, R, G, B), Color.argb(225, R1, G1, B1), Color.argb(
                200,
                R2,
                G2,
                B2
            )
        )
        lateinit var mShader: LinearGradient
        if (isX) {
            mShader = LinearGradient(
                0f,
                0f,
                measuredWidth.toFloat(),
                0f,
                shadeColors,
                null,
                Shader.TileMode.CLAMP
            )

        } else {
            mShader = LinearGradient(
                0f,
                0f,
                0f,
                measuredHeight.toFloat(),
                shadeColors,
                null,
                Shader.TileMode.CLAMP
            )

        }
        return mShader
    }

    fun getShadersStarAndEnd(endX: Float, endY: Float): Shader {
        val random = Random()
        val R = random.getRandom(225)
        val G = random.getRandom(225)
        val B = random.getRandom(225)

        val R1 = random.getRandom(225)
        val G1 = random.getRandom(225)
        val B1 = random.getRandom(225)

        val R2 = random.getRandom(225)
        val G2 = random.getRandom(225)
        val B2 = random.getRandom(225)
        val shadeColors = intArrayOf(
            Color.argb(225, R, G, B), Color.argb(225, R1, G1, B1), Color.argb(
                200,
                R2,
                G2,
                B2
            )
        )
        val mShader = LinearGradient(0f, 0f, endX, endY, shadeColors, null, Shader.TileMode.CLAMP)
        return mShader
    }

    fun getShaders(): Shader {
        val random = Random()
        val R = random.getRandom(225)
        val G = random.getRandom(225)
        val B = random.getRandom(225)

        val R1 = random.getRandom(225)
        val G1 = random.getRandom(225)
        val B1 = random.getRandom(225)

        val R2 = random.getRandom(225)
        val G2 = random.getRandom(225)
        val B2 = random.getRandom(225)
        val shadeColors = intArrayOf(
            Color.argb(225, R, G, B), Color.argb(225, R1, G1, B1), Color.argb(
                200,
                R2,
                G2,
                B2
            )
        )
        val mShader = LinearGradient(0f, 0f, 44f, 44f, shadeColors, null, Shader.TileMode.CLAMP)
        return mShader
    }

}

data class ViewPoint @JvmOverloads constructor(var x: Float, var y: Float)

internal class Random {
    fun getRandom(lower: Float, upper: Float): Float {
        val min = Math.min(lower, upper)
        val max = Math.max(lower, upper)
        return getRandom(max - min) + min
    }

    fun getRandom(upper: Float): Float {
        return RANDOM.nextFloat() * upper
    }

    fun getRandom(upper: Int): Int {
        return RANDOM.nextInt(upper)
    }

    companion object {
        private val RANDOM = java.util.Random()
    }
}