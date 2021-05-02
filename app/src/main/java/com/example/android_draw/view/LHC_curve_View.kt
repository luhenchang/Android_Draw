package com.example.android_draw.view

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.graphics.drawable.Drawable
import android.speech.tts.TextToSpeech
import android.text.SpannableString
import android.text.Spanned
import android.text.SpannedString
import android.text.style.ForegroundColorSpan
import android.util.AttributeSet
import android.util.Log
import android.view.View
import android.view.textclassifier.TextLinks
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.core.graphics.blue
import androidx.core.graphics.drawable.toBitmap
import androidx.core.graphics.red
import com.example.android_draw.R
import kotlinx.android.synthetic.main.activity_main.view.*
import org.w3c.dom.Text


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
 * 创建日期：2/18/21
 * 描述：Android_Draw
 * E-mail : 1276998208@qq.com
 * CSDN:https://blog.csdn.net/m0_37667770/article
 * GitHub:https://github.com/luhenchang
 */
//新建类LHC_curve_View
@Suppress("DEPRECATION")
class LHC_curve_View @JvmOverloads constructor(
    context: Context?,
    attrs: AttributeSet? = null,
    defStyleAttr: Int = 0
) : View(context, attrs, defStyleAttr) {
    val leftWidth = 180f
    val bottomHeight = 240f

    //x轴的宽度
    var x_scaleWidth = 0f

    //方格的宽高
    private var grid_width = 0f
    private var dataList: ArrayList<Int> = ArrayList()
    private var imgList: ArrayList<Int> = ArrayList()

    init {
        dataList.add(0)
        dataList.add(0)
        dataList.add(0)
        dataList.add(1400)
        dataList.add(400)
        dataList.add(1350)
        dataList.add(0)

        imgList.add(R.drawable.head_lhc)
        imgList.add(R.drawable.mn_1)
        imgList.add(R.drawable.mn_2)
        imgList.add(R.drawable.mn_3)
        imgList.add(R.drawable.mn_4)
        imgList.add(R.drawable.mn_5)
        imgList.add(R.drawable.mn_6)



    }

    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        //2.画布平移
        translateCanvas(canvas)
        //3.绘制x轴也就是最底部的一条线,线长度为 width-leftWidth-80左右?
        drawLine(canvas)
        //4.绘制x轴的文字
        drawDownOfXLineText(canvas)
        //5.绘制y轴的文字
        drawLeftOfYLineText(canvas)
        //6.绘制曲线
        drawCaves(canvas)
        //7.绘制文字
        drawTextButton(canvas)
        //8.绘制每天最高获得者的头像...纯虚构故事对不对..
        drawHeaderToCanvas(canvas)
        //9.实在是个人癖好。强迫症写完最后的文字吧
        drawTopTextToCanvas(canvas)
    }
    //9.实在是个人癖好。强迫症写完最后的文字吧
    @SuppressLint("Range")
    private fun drawTopTextToCanvas(canvas: Canvas) {
        val text_paint = Paint()
        text_paint.strokeWidth = 2f
        text_paint.style = Paint.Style.FILL
        text_paint.color = Color.argb(255, 0, 0, 0)
        text_paint.textSize =66f
        val rectText = Rect()
        val rectTextYuan = Rect()

        canvas.save()
        canvas.scale(1f, -1f)
        canvas.translate((width/2).toFloat()-100,-500f)
        val text="1347"
        val textyu="元"

        text_paint.getTextBounds(text, 0,text.length, rectText)

        canvas.drawText(
            text,
            -rectText.width().toFloat() - 42f,
            rectText.height().toFloat() / 2,
            text_paint
        )
        text_paint.color = Color.argb(111, 111, 111, 111)
        text_paint.getTextBounds(textyu, 0,textyu.length, rectTextYuan)
        text_paint.textSize =33f
        canvas.drawText(
            textyu,
             80+ -rectTextYuan.width().toFloat() - 42f,
            rectTextYuan.height().toFloat() / 2,
            text_paint
        )

        canvas.translate(0f,50f)
        canvas.drawText(
            "较前天",
            -rectTextYuan.width().toFloat() - 180f,
            rectTextYuan.height().toFloat() / 2,
            text_paint
        )
        canvas.translate(100f,0f)
        text_paint.color = Color.argb(255, 223, 129, 120)
        canvas.drawText(
            "+971.99(251.19%)",
            -rectTextYuan.width().toFloat() - 180f,
            rectTextYuan.height().toFloat() / 2,
            text_paint
        )
        canvas.translate(-100f,50f)
        text_paint.color = Color.argb(111, 111, 111, 111)
        canvas.drawText(
            "对应图中虚线部分进行最高评奖",
            -rectTextYuan.width().toFloat() - 180f,
            rectTextYuan.height().toFloat() / 2,
            text_paint
        )
        //暂时没找到canvas绘制富文本的方法。只能一个个测量绘制文字了。别学我,好好测量测量有待提高自己的小学计算。

//        val textSpanned1 =  SpannableString("Hello World");
//        textSpanned1.setSpan(ForegroundColorSpan(Color.RED), 0, 3, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)
//        text_paint.reset()
//        text_paint.textSize=44f
//        canvas.drawText(textSpanned1,0,10,0f,0f,text_paint)
        canvas.restore()
    }

    //8.绘制每天最高获得者的头像...纯虚构故事对不对..
    private fun drawHeaderToCanvas(canvas: Canvas) {
        val bitmap_paint = Paint()
        bitmap_paint.strokeWidth = 2f
        bitmap_paint.style = Paint.Style.STROKE
        bitmap_paint.xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC_IN)
        bitmap_paint.isAntiAlias =true
        canvas.save()
        val srcRect1=Rect(0, 0, 80, 80)
        val dstRect1=Rect(0, 0, 40, 40)
        val danweiY = (grid_width - 40) / 500
        for (index in 0 until dataList.size) {
            val mdrawable = ContextCompat.getDrawable(context, imgList[index])
            val bitmap = getBitmap(bitmap_paint, mdrawable!!)
            canvas.save()
            canvas.translate(
                grid_width * index - bitmap.width / 4,
                danweiY * dataList[index] + 20
            )
            //这里绘制图片到画布上
            val circlePath = Path()
            circlePath.addCircle(20f,20f, 20f, Path.Direction.CCW)
            canvas.clipPath(circlePath)
            canvas.drawBitmap(bitmap, srcRect1, dstRect1, bitmap_paint)
            canvas.restore()
        }
        canvas.restore()


    }

    /**
     * 转换图片成圆形
     * @param bitmap 传入Bitmap对象
     * @return
     */
    private fun getBitmap(bitmap_paint: Paint, mdrawable: Drawable): Bitmap {
        val bitmap = mdrawable.toBitmap(80, 80, Bitmap.Config.ARGB_8888)
        val bitmapCanvas = Canvas(bitmap)
        bitmapCanvas.drawARGB(0, 0, 0, 0);
        val circlePath = Path()
        circlePath.addCircle(40f, 40f, 15f, Path.Direction.CCW)
        bitmapCanvas.clipPath(circlePath, Region.Op.INTERSECT)
        val srcRect = Rect(20, 20, 60, 60)
        val dstRect = Rect(20, 20, 60, 60)
        bitmapCanvas.drawBitmap(bitmap, srcRect, dstRect, bitmap_paint)
        return bitmap
    }


    //平移画布
    private fun translateCanvas(canvas: Canvas) {
        //y轴向上为负方向
        canvas.scale(1f, -1f)
        //坐标系向右平移leftWidth,向下平移动 height-bootomHeight,这里向上是正哦。所以向下平移是负数
        canvas.translate(leftWidth, -(height - bottomHeight))
    }

    //绘制平行x轴的线
    private fun drawLine(canvas: Canvas) {
        canvas.drawColor(Color.argb(255, 255, 255, 255))
        val line_paint = Paint()
        line_paint.strokeWidth = 2f
        line_paint.style = Paint.Style.STROKE
        line_paint.color = Color.argb(100, 188, 188, 188)

        x_scaleWidth = (width - leftWidth - 80f)
        grid_width = x_scaleWidth / 6
        //绘制最底部一条线
        val x_path = Path()
        x_path.moveTo(0f, 0f)
        x_path.lineTo(x_scaleWidth, 0f)
        canvas.drawPath(x_path, line_paint)

        canvas.save()
        //通过平移画布绘制剩余的平行x轴线
        (0 until 3).forEach { index ->
            canvas.translate(0f, grid_width - 40f)
            canvas.drawPath(x_path, line_paint)
        }
        canvas.restore()

    }

    //水平方向的x轴下面的文字
    private fun drawDownOfXLineText(canvas: Canvas) {
        val text_paint = Paint()
        text_paint.strokeWidth = 2f
        text_paint.style = Paint.Style.STROKE
        text_paint.color = Color.argb(100, 111, 111, 111)
        text_paint.textSize = 19f

        val rectText = Rect()
        canvas.save()
        //将文字旋转摆正，此时坐标系y向下是正
        canvas.scale(1f, -1f)
        (0 until 7).forEach { index ->
            if (index > 0) {
                canvas.translate(grid_width, 0f)
            }
            val strTx = "11.${11 + index}"
            text_paint.getTextBounds(strTx, 0, strTx.length, rectText)
            canvas.drawText(
                strTx,
                -rectText.width().toFloat() / 2,
                rectText.height().toFloat() * 2.5f,
                text_paint
            )
        }
        canvas.restore()

    }

    private fun drawLeftOfYLineText(canvas: Canvas) {
        val text_paint = Paint()
        text_paint.strokeWidth = 2f
        text_paint.style = Paint.Style.STROKE
        text_paint.color = Color.argb(100, 111, 111, 111)
        text_paint.textSize = 19f

        val rectText = Rect()
        canvas.save()
        //将文字旋转摆正，此时坐标系y向下是正
        (0 until 4).forEach { index ->
            if (index > 0) {
                canvas.translate(0f, grid_width - 40f)
            }
            var strTx = ""
            if (index == 0) {
                strTx = "${index}"
            } else if (index == 1) {
                strTx = "${500}"
            } else if (index == 2) {
                strTx = "1k"
            } else {
                strTx = "1.5k"
            }

            canvas.save()
            canvas.scale(1f, -1f)
            text_paint.getTextBounds(strTx, 0, strTx.length, rectText)
            canvas.drawText(
                strTx,
                -rectText.width().toFloat() - 42f,
                rectText.height().toFloat() / 2,
                text_paint
            )
            canvas.restore()
        }
        canvas.restore()

    }

    //绘制曲线
    private fun drawCaves(canvas: Canvas) {
        val text_paint = Paint()
        text_paint.strokeWidth = 2f
        text_paint.style = Paint.Style.FILL
        text_paint.color = Color.argb(100, 111, 111, 111)

        val caves_path = Path()
        //500=grid_width-40 每个单位的长度的=像素长度
        val danweiY = (grid_width - 40) / 500
        val danweiX = (grid_width)
        val linearGradient = LinearGradient(
            0f, 1500 * danweiY,
            0f,
            0f,
            Color.argb(255, 229, 160, 144),
            Color.argb(255, 251, 244, 240),
            Shader.TileMode.CLAMP
        )
        text_paint.shader = linearGradient
        for (index in 0 until dataList.size - 1) {
            val xMoveDistance = 20
            val yMoveDistance = 40

            if (dataList[index] == dataList[index + 1]) {
                caves_path.lineTo(danweiX * (index + 1), 0f)
            } else if (dataList[index] < dataList[index + 1]) {//y1<y2情况
                val centerX = (grid_width * index + grid_width * (1 + index)) / 2
                val centerY =
                    (dataList[index].toFloat() * danweiY + dataList[index + 1].toFloat() * danweiY) / 2
                val controX0 = (grid_width * index + centerX) / 2
                val controY0 = (dataList[index].toFloat() * danweiY + centerY) / 2
                val controX1 = (centerX + grid_width * (1 + index)) / 2
                val controY1 = (centerY + dataList[index + 1].toFloat() * danweiY) / 2
                caves_path.cubicTo(
                    controX0 + xMoveDistance,
                    controY0 - yMoveDistance,
                    controX1 - xMoveDistance,
                    controY1 + yMoveDistance,
                    grid_width * (1 + index),
                    dataList[index + 1].toFloat() * danweiY
                )
            } else {
                val centerX = (grid_width * index + grid_width * (1 + index)) / 2
                val centerY =
                    (dataList[index].toFloat() * danweiY + dataList[index + 1].toFloat() * danweiY) / 2
                val controX0 = (grid_width * index + centerX) / 2
                val controY0 = (dataList[index].toFloat() * danweiY + centerY) / 2
                val controX1 = (centerX + grid_width * (1 + index)) / 2
                val controY1 = (centerY + dataList[index + 1].toFloat() * danweiY) / 2
                caves_path.cubicTo(
                    controX0 + xMoveDistance,
                    controY0 + yMoveDistance,
                    controX1 - xMoveDistance,
                    controY1 - yMoveDistance,
                    grid_width * (1 + index),
                    dataList[index + 1].toFloat() * danweiY
                )

            }
        }
        canvas.drawCircle(0f, 0f, 10f, text_paint)
        //绘制闭合渐变曲线
        canvas.drawPath(caves_path, text_paint)
        val line_paint = Paint()
        line_paint.strokeWidth = 3f
        line_paint.style = Paint.Style.STROKE
        line_paint.color = Color.argb(255, 212, 100, 77)
        //绘制外环红色线
        canvas.drawPath(caves_path, line_paint)
        line_paint.style = Paint.Style.FILL
        //画圈。
        for (index in 0 until dataList.size) {
            canvas.drawCircle(grid_width * index, danweiY * dataList[index], 8f, line_paint)
        }
    }

    //绘制前7天和后7天文字按钮
    private fun drawTextButton(canvas: Canvas) {
        val line_paint = Paint()
        line_paint.strokeWidth = 2f
        line_paint.style = Paint.Style.STROKE
        line_paint.color = Color.argb(188, 76, 126, 245)
        line_paint.textSize=32f
        val buttonPath = Path()
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP) {
            buttonPath.addRoundRect(110f, -120f, 270f, -180f, 80f, 80f, Path.Direction.CCW)
        }
        canvas.drawPath(buttonPath, line_paint)
        canvas.save()
        canvas.scale(1f, -1f)
        line_paint.style = Paint.Style.FILL
        canvas.drawText("前 七 天", 140f, 165f, line_paint)
        canvas.restore()

        canvas.save()
        canvas.translate(260f, 0f)
        line_paint.style = Paint.Style.STROKE
        canvas.drawPath(buttonPath, line_paint)
        canvas.scale(1f, -1f)
        line_paint.style = Paint.Style.FILL
        canvas.drawText("后 七 天", 140f, 165f, line_paint)
        canvas.restore()

    }
}