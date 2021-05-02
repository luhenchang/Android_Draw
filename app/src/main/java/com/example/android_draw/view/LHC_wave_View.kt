package com.example.android_draw.view
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.animation.ValueAnimator.INFINITE
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
import android.view.MotionEvent.ACTION_DOWN
import android.view.MotionEvent.ACTION_MOVE
import android.view.View
import android.view.animation.LinearInterpolator
import com.example.android_draw.R
import kotlin.math.max
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
@Suppress("DEPRECATION")
class LHC_wave_View @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    private var moveX: Float = 0f
    private var moveY: Float = 0f
    private var hCount: Int = 0
    private var wCount: Int = 0


    //网格的宽度
    private var gridWidth = 80
    //半波长
    private val waveWidth=80f
    private val waveHeight=30f
    private var animal:ValueAnimator
    init {
        animal=ObjectAnimator.ofFloat(0f, waveWidth * 4)
        animal.duration = 2000
        animal.repeatCount = INFINITE
        animal.interpolator = LinearInterpolator()
        animal.addUpdateListener {
            invalidate()
        }
    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        //绘制网格线
        drawGridLine(canvas)
        //绘制文字x和y轴的
        drawTextXAndY(canvas)
        //裁剪画布
        clipCanvas(canvas,2)
        //绘制波浪
        drawWave(canvas)
        //绘制
        drawFish(canvas)
    }
    private val arrList: java.util.ArrayList<Int> = arrayListOf( R.drawable.yu, R.drawable.ziyuan, R.drawable.jingyu)
    private fun drawFish(canvas: Canvas) {

        val bmp = BitmapFactory.decodeResource(resources, arrList[0])
        canvas.drawBitmap(bmp, waveWidth * 3, -waveHeight * 2.2f, getPaint(Paint.Style.FILL))


        val bmpzy = BitmapFactory.decodeResource(resources,arrList[1])
        canvas.drawBitmap(bmpzy, waveWidth, -waveHeight *3f, getPaint(Paint.Style.FILL))

        val bmpjy= BitmapFactory.decodeResource(resources,arrList[2])
        canvas.drawBitmap(bmpjy, waveWidth* 6, -waveHeight * 3.5f, getPaint(Paint.Style.FILL))

    }

    private fun clipCanvas(canvas: Canvas,type:Int) {
        when (type) {
            0 -> {
                val rect = Rect(
                    (waveWidth * 4).toInt(),
                    160,
                    (waveWidth * 8).toInt(),
                    (-waveWidth * 4).toInt()
                )
                canvas.clipRect(rect)
            }
            1 -> {
                val circlePath = Path()
                circlePath.addCircle(480f, 0f, 160f, Path.Direction.CCW)
                canvas.drawCircle(480f, 0f, 160f, getPaint(Paint.Style.STROKE))
                canvas.clipPath(circlePath)
            }
            2 -> {
                val rundRect = Path()
                rundRect.addRoundRect(waveWidth * 4,waveWidth*3,waveWidth* 8,-waveWidth*3,60f,60f,Path.Direction.CCW)
                //canvas.drawPath(rundRect,getPaint(Paint.Style.STROKE))
                canvas.clipPath(rundRect)

            }
        }
    }


    //绘制波浪
    private fun drawWave(canvas: Canvas) {
        canvas.translate(this.animal.animatedValue as Float, 0f) //内层海浪
        val wavePath=Path()
        wavePath.moveTo(0f, -waveWidth * 6)
        wavePath.lineTo(0f, 0f)
        wavePath.quadTo(waveWidth, waveHeight, waveWidth * 2, 0f)
        wavePath.quadTo(waveWidth * 3, -waveHeight, waveWidth * 4, 0f)
        wavePath.quadTo(waveWidth * 5, waveHeight, waveWidth * 6, 0f)
        wavePath.quadTo(waveWidth * 7, -waveHeight, waveWidth * 8, 0f)
        wavePath.lineTo(waveWidth * 8, -waveWidth * 6)
        canvas.drawPath(wavePath, getPaintBefor(Paint.Style.FILL))

        canvas.translate(animal.animatedValue as Float, 0f) //内层海浪
        val wavepathOut=Path()
        wavepathOut.moveTo(-waveWidth * 7, -waveWidth * 6)
        wavepathOut.lineTo(-waveWidth * 7, 0f)
        wavepathOut.quadTo(-waveWidth * 7, waveHeight, -waveWidth * 6, 0f)
        wavepathOut.quadTo(-waveWidth * 5, -waveHeight, -waveWidth * 4, 0f)
        wavepathOut.quadTo(-waveWidth * 3, waveHeight, -waveWidth * 2, 0f)
        wavepathOut.quadTo(-waveWidth, -waveHeight, 0f, 0f)
        wavepathOut.quadTo(waveWidth, waveHeight, waveWidth * 2, 0f)
        wavepathOut.quadTo(waveWidth * 3, -waveHeight, waveWidth * 4, 0f)
        wavepathOut.quadTo(waveWidth * 5, waveHeight, waveWidth * 6, 0f)
        wavepathOut.quadTo(waveWidth * 7, -waveHeight, waveWidth * 8, 0f)
        wavepathOut.lineTo(waveWidth * 8, -waveWidth * 6)
        canvas.drawPath(wavepathOut, getPaint(Paint.Style.FILL))


    }


    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            ACTION_DOWN,
            ACTION_MOVE -> {
                animal.start()
                //在控制点附近范围内部,进行移动
                Log.e(
                    "x=",
                    "onTouchEvent: (x,y)" + (event.x - width / 2).toInt() + ":" + (-(event.y - height / 2)).toInt()
                )
                moveX = event.x - width / 2
                moveY = -(event.y - height / 2)
                invalidate()

            }
        }
        return true
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
        //曲线curve
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
    private fun getPaintBefor(style: Paint.Style): Paint {
        val gPaint = Paint()
        gPaint.color = Color.BLUE
        gPaint.strokeWidth = 2f
        gPaint.isAntiAlias = true
        gPaint.style = style
        gPaint.textSize = 26f
        gPaint.color = Color.argb(255, 75, 151, 79)
        val linearGradient = LinearGradient(
            waveWidth * 4, -waveWidth * 8,
            waveWidth * 4,
            80f,
            Color.argb(155, 27, 134, 244),
            Color.argb(195, 24, 220, 253),
            Shader.TileMode.CLAMP
        )
        gPaint.shader=linearGradient
        return gPaint
    }
    private fun getPaint(style: Paint.Style): Paint {
        val gPaint = Paint()
        gPaint.color = Color.BLUE
        gPaint.strokeWidth = 2f
        gPaint.isAntiAlias = true
        gPaint.style = style
        gPaint.textSize = 26f
        gPaint.color = Color.argb(255, 75, 151, 79)
        val linearGradient = LinearGradient(
            waveWidth * 4, -waveWidth * 2,
            waveWidth * 4,
            80f,
            Color.argb(255, 27, 134, 244),
            Color.argb(255, 24, 220, 253),
            Shader.TileMode.CLAMP
        )
        gPaint.shader=linearGradient
        return gPaint
    }

}

