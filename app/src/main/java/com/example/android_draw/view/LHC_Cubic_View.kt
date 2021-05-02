package com.example.android_draw.view

import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.MotionEvent.ACTION_DOWN
import android.view.MotionEvent.ACTION_MOVE
import android.view.View
import com.example.android_draw.R
import kotlin.math.max
import kotlin.math.pow
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
 * 创建日期：2/8/21
 * 描述：Android_Draw
 * E-mail : 1276998208@qq.com
 * CSDN:https://blog.csdn.net/m0_37667770/article
 * GitHub:https://github.com/luhenchang
 */
class LHC_Cubic_View @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var moveX: Float = 160f
    private var moveY: Float = 160f
    private var moveCubiX: Float = 80f
    private var moveCubiY: Float = 80f
    private var moveCubiXX: Float = 240f
    private var moveCubiYY: Float = 80f
    private var hCount: Int = 0
    private var wCount: Int = 0
    private var number = -420..420
    private lateinit var pointList: ArrayList<PointF>
    //二阶控制点
    private lateinit var controllRect: Rect
    //三阶控制点
    private lateinit var cubicLeftRect: Rect
    //三阶控制点
    private lateinit var cubicRightRect: Rect


    //网格的宽度
    var gridWidth = 80

    init {

    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        //绘制网格线
        drawGridLine(canvas)
        //绘制文字x和y轴的
        drawTextXAndY(canvas)
        //绘制直线方程
        drawzxLine(canvas)
        //二阶曲线
        drawQuz(canvas)
        //三阶曲线
        drawCubic(canvas)

    }

    private fun drawCubic(canvas: Canvas) {
       val cubicPath=Path()
       cubicPath.moveTo(0f,0f)
       cubicLeftRect= Rect(
           (moveCubiX - 30f).toInt(),
           (moveCubiY - 30f).toInt(),
           (moveCubiX + 30).toInt(),
           (moveCubiY + 30f).toInt()
       )
       cubicRightRect=Rect(
           (moveCubiXX - 30f).toInt(),
           (moveCubiYY - 30f).toInt(),
           (moveCubiXX + 30).toInt(),
           (moveCubiYY + 30f).toInt()
       )
        val lineLeft = Path()
        lineLeft.moveTo(0f, 0f)
        lineLeft.lineTo(moveCubiX, moveCubiY)
        lineLeft.lineTo(moveCubiXX, moveCubiYY)
        lineLeft.lineTo(320f, 0f)
        canvas.drawPath(lineLeft, getPaint(Paint.Style.STROKE,Color.GRAY))

        //canvas.drawRect(cubicLeftRect, getPaint(Paint.Style.FILL,Color.RED))
        //canvas.drawRect(cubicRightRect, getPaint(Paint.Style.FILL,Color.RED))
        canvas.drawCircle(moveCubiX, moveCubiY, 10f, getPaintCir(Paint.Style.FILL))
        canvas.drawCircle(moveCubiXX, moveCubiYY, 10f, getPaintCir(Paint.Style.FILL))

        cubicPath.cubicTo(moveCubiX,moveCubiY,moveCubiXX,moveCubiYY,320f,0f)
        canvas.drawPath(cubicPath, getPaint(Paint.Style.STROKE,Color.RED))
    }

    private fun drawQuz(canvas: Canvas) {
        //left < right && top < bottom && x >= left && x < right && y >= top && y < bottom;
        controllRect = Rect(
            (moveX - 30f).toInt(),
            (moveY - 30f).toInt(),
            (moveX + 30).toInt(),
            (moveY + 30f).toInt()
        )

        canvas.drawRect(controllRect, getPaint(Paint.Style.FILL))
        val quePath = Path()
        canvas.drawCircle(0f, 0f, 10f, getPaintCir(Paint.Style.FILL))
        canvas.drawCircle(320f, 0f, 10f, getPaintCir(Paint.Style.FILL))
        //第一个点和控制点的连线到最后一个点链线。为了方便观察
        val lineLeft = Path()
        lineLeft.moveTo(0f, 0f)
        lineLeft.lineTo(moveX, moveY)
        lineLeft.lineTo(320f, 0f)
        canvas.drawPath(lineLeft, getPaint(Paint.Style.STROKE))
        //第一个p0处画一个圆。第二个p1处画一个控制点圆,最后画一个。
        canvas.drawCircle(moveX, moveY, 10f, getPaintCir(Paint.Style.FILL))
        quePath.quadTo(moveX, moveY, 320f, 0f)
        canvas.drawPath(quePath, getPaint(Paint.Style.STROKE))
    }

    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            ACTION_DOWN,
            ACTION_MOVE -> {
                //在控制点附近范围内部,进行移动
                Log.e(
                    "x=",
                    "onTouchEvent: (x,y)" + (event.x - width / 2).toInt() + ":" + (-(event.y - height / 2)).toInt()
                )
                //二阶曲线
                if (controllRect.contains((event.x - width / 2).toInt(),(-(event.y - height / 2)).toInt())) {
                    Log.e("点击来","对" )
                    moveX = event.x - width / 2
                    moveY = -(event.y - height / 2)
                    invalidate()
                //三阶曲线控制点1
                }else if(cubicLeftRect.contains((event.x - width / 2).toInt(),(-(event.y - height / 2)).toInt())){
                    moveCubiX= event.x - width / 2
                    moveCubiY= -(event.y - height / 2)
                    invalidate()
                 //三阶曲线控制点2
                }else if(cubicRightRect.contains((event.x - width / 2).toInt(),(-(event.y - height / 2)).toInt())){
                    moveCubiXX= event.x - width / 2
                    moveCubiYY= -(event.y - height / 2)
                    invalidate()

                }
            }
        }
        return true
    }


    //直线方程y=2x-80
    private fun drawzxLine(canvas: Canvas) {
        pointList = ArrayList()
        //绘制方程式y=10x+20
        val gPaint = getPaint(Paint.Style.STROKE)
        number.forEach { t ->
            val point = PointF()
            if (t % 2 == 0) {//x轴偶数点进行绘制
                point.x = t.toFloat()
                point.y = 2f * t - 80
                pointList.add(point)
                canvas.drawPoint(point.x, point.y, gPaint)
            }
        }

        //绘制圆圈
        number.forEach { t ->
            val point = PointF()
            val pointDown = PointF()

            //(x-10)2+（y-10）2=1602
            point.x = t.toFloat()
            pointDown.x = t.toFloat()
            //y计算应该不用我说吧。
            point.y =
                sqrt(160.0.pow(2.0).toFloat() - ((point.x - 10).toDouble()).pow(2.0)).toFloat() + 10
            pointDown.y = -sqrt(
                160.0.pow(2.0).toFloat() - ((pointDown.x - 10).toDouble()).pow(2.0)
            ).toFloat() + 10
            canvas.drawPoint(point.x, point.y, gPaint)
            canvas.drawPoint(pointDown.x, pointDown.y, gPaint)

        }
    }

    private fun drawTextXAndY(canvas: Canvas) {

        val gPaint = Paint()
        gPaint.color = Color.BLUE
        gPaint.strokeWidth = 2f
        gPaint.isAntiAlias = true
        gPaint.style = Paint.Style.STROKE
        gPaint.textSize = 26f
        gPaint.color = Color.argb(255, 75, 151, 79)
        canvas.scale(-1f, 1f)


        canvas.save()

        canvas.scale(1f, -1f)

        //x轴正方形文字
        for (index in 1 until wCount / 2) {
            val rectText = Rect()
            canvas.translate(160f, 0f)
            gPaint.getTextBounds(
                (80 * index * 2).toString(),
                0,
                (80 * index * 2).toString().length,
                rectText
            )
            canvas.drawText(
                (80 * index * 2).toString(),
                -(rectText.width() / 2).toFloat(), rectText.height().toFloat() * 2f, gPaint
            )
        }
        canvas.restore()
        canvas.save()
        //x轴负方向文字绘制
        canvas.scale(1f, -1f)
        for (index in 1 until wCount / 2) {
            val rectText = Rect()
            canvas.translate(-160f, 0f)
            gPaint.getTextBounds(
                "-${(80 * index * 2)}",
                0,
                (80 * index * 2).toString().length,
                rectText
            )
            canvas.drawText(
                "-${(80 * index * 2)}",
                -(rectText.width() / 2).toFloat(), rectText.height().toFloat() * 2f, gPaint
            )
        }
        canvas.restore()

        canvas.save()
        //x轴负方向文字绘制
        canvas.scale(1f, -1f)
        canvas.translate(20f, 0f)
        //y轴负方向
        for (index in 1 until hCount / 2) {
            val rectText = Rect()
            canvas.translate(0f, 160f)
            gPaint.getTextBounds(
                "-${(80 * index * 2)}",
                0,
                (80 * index * 2).toString().length,
                rectText
            )
            canvas.drawText(
                "-${(80 * index * 2)}",
                0f, rectText.height().toFloat(), gPaint
            )
        }
        canvas.restore()

        canvas.save()
        canvas.scale(1f, 1f)
        canvas.translate(20f, 0f)
        //y轴正方向
        for (index in 1 until hCount / 2) {
            val rectText = Rect()
            canvas.translate(0f, 160f)
            canvas.save()
            canvas.scale(1f, -1f)
            gPaint.getTextBounds(
                "${(80 * index * 2)}",
                0,
                (80 * index * 2).toString().length,
                rectText
            )
            canvas.drawText(
                "${(80 * index * 2)}",
                0f, rectText.height().toFloat(), gPaint
            )
            canvas.restore()
        }

        canvas.restore()


    }

    private fun drawGridLine(canvas: Canvas) {
        //初始化一个画笔
        val gPaint = Paint()
        gPaint.color = Color.BLUE
        gPaint.strokeWidth = 2f
        gPaint.isAntiAlias = true
        gPaint.style = Paint.Style.FILL
        gPaint.shader = RadialGradient(
            0f,
            0f,
            max(width, height) / 2f,
            Color.BLUE,
            Color.YELLOW,
            Shader.TileMode.CLAMP
        )
        //onDraw中已经知道屏幕宽度和高度
        val screenWidth = width
        val screenHeight = height
        //宽的格子个数
        wCount = screenWidth / gridWidth
        //高的格子个数
        hCount = screenHeight / gridWidth

        //1.将坐标点移动到屏幕的中点
        canvas.translate((screenWidth / 2).toFloat(), (screenHeight / 2).toFloat())
        //整体坐标系方向顺时针进行变化
        //2.修改y轴上方为正方向。
        canvas.scale(1f, -1f)
        //绘制x轴和y轴
        canvas.drawLine(-screenWidth / 2f, 0f, screenWidth / 2f, 0f, gPaint)
        canvas.drawLine(0f, -screenHeight / 2f, 0f, screenHeight / 2f, gPaint)
        gPaint.color = Color.argb(61, 111, 111, 111)
        drawGridCode(canvas, screenWidth, gPaint, hCount, screenHeight, wCount)
        //2.修改y轴下方为正方向。
        canvas.scale(1f, -1f)
        drawGridCode(canvas, screenWidth, gPaint, hCount, screenHeight, wCount)
        //3.修改x轴左正方向。
        canvas.scale(-1f, 1f)
        drawGridCode(canvas, screenWidth, gPaint, hCount, screenHeight, wCount)
        //4.修改x作为正y上为正
        canvas.scale(1f, -1f)
        drawGridCode(canvas, screenWidth, gPaint, hCount, screenHeight, wCount)


    }

    private fun drawGridCode(
        canvas: Canvas,
        screenWidth: Int,
        gPaint: Paint,
        hCount: Int,
        screenHeight: Int,
        wCount: Int
    ) {
        //这里保存好坐标圆点为屏幕中心的快照到堆栈里面。方便后期操作。
        canvas.save()
        //绘制一条横着的线条,重圆点(0,0)开始
        //canvas.drawLine(0f, 0f, (screenWidth / 2).toFloat(), 0f, gPaint)

        //3.绘制完成第一象限的平行x轴的线
        for (index in 0 until hCount) {
            //坐标系圆点不断向上平移gridWidth的高度
            canvas.translate(0f, gridWidth.toFloat())
            //在平移完的圆点直接画直线即可
            canvas.drawLine(0f, 0f, (screenWidth / 2).toFloat(), 0f, gPaint)
        }
        //恢复到快照状态。即圆点在中心
        canvas.restore()
        canvas.save()
        //4.绘制平行y轴的
        //canvas.drawLine(0f, 0f, 0f, screenHeight / 2f, gPaint)
        for (index in 0 until wCount) {
            //坐标系圆点不断向上平移gridWidth的高度
            canvas.translate(gridWidth.toFloat(), 0f)
            //在平移完的圆点直接画直线即可
            canvas.drawLine(0f, 0f, 0f, screenHeight / 2f, gPaint)
        }
        //恢复到快照状态。即圆点在中心
        canvas.restore()
    }

}

private fun getPaint(style: Paint.Style): Paint {
    val gPaint = Paint()
    gPaint.color = Color.BLUE
    gPaint.strokeWidth = 2f
    gPaint.isAntiAlias = true
    gPaint.style = style
    gPaint.textSize = 26f
    gPaint.color = Color.argb(255, 75, 151, 79)
    return gPaint
}
private fun getPaint(style: Paint.Style,color:Int): Paint {
    val gPaint = Paint()
    gPaint.color = Color.BLUE
    gPaint.strokeWidth = 2f
    gPaint.isAntiAlias = true
    gPaint.style = style
    gPaint.textSize = 26f
    gPaint.color =color
    return gPaint
}
private fun getPaintCir(fill: Paint.Style): Paint {
    val gPaint = Paint()
    gPaint.strokeWidth = 12f
    gPaint.isAntiAlias = true
    gPaint.style = fill
    gPaint.textSize = 26f
    gPaint.color = Color.argb(255, 111, 111, 111)
    return gPaint
}