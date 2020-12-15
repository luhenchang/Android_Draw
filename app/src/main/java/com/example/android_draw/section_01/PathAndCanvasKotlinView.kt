package com.example.android_draw.section_01

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.os.Build
import android.util.AttributeSet
import android.view.View
import androidx.annotation.RequiresApi

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
 * 创建日期：2020/12/9
 * 描述：Android_Draw
 * E-mail : 1276998208@qq.com
 * CSDN:https://blog.csdn.net/m0_37667770/article
 * GitHub:https://github.com/luhenchang
 */
class PathAndCanvasKotlinView : View {
    constructor(context: Context?) : super(context)
    constructor(context: Context?, attrs: AttributeSet?) : super(context, attrs)
    constructor(context: Context?, attrs: AttributeSet?, defStyleAttr: Int) : super(
        context,
        attrs,
        defStyleAttr
    )

    @RequiresApi(Build.VERSION_CODES.O)
    @SuppressLint("DrawAllocation")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        val paint = Paint()
        //paint.color = Color.RED
        paint.style = Paint.Style.STROKE
        //巨形后面会一一说到
        val rect = Rect(100, 100, 200, 200)
        canvas.drawRect(rect, paint)
        canvas.save()
        paint.setShadowLayer(15f, 10f, 10f, Color.RED)
        var linearGradient = LinearGradient(
            0f, 0f,
            200f,
            300f,
            Color.RED,
            Color.BLUE,
            Shader.TileMode.CLAMP
        )
        paint.setShader(linearGradient)
        canvas.drawCircle(200f, 500f, 100f, paint)
        canvas.restore()
        var path=Path()
        var path1=Path()
        path1.moveTo(250f,250f)
        path1.lineTo(200f,300f)
        path1.lineTo(230f,150f)
        path1.close()
        path.fillType= Path.FillType.EVEN_ODD
        path.moveTo(100f, 100f)
        path.lineTo(100f, 200f)
        path.lineTo(200f, 200f)
        path.lineTo(200f, 100f)
        path.rMoveTo(10f,10f)
        path.rLineTo(150f,100f)
        //path.offset(200f,250f)
        path.offset(200f,300f,path)
        path.setLastPoint(400f,1300f)
        var matrix=Matrix()
        matrix.setTranslate(300f,100f)
        matrix.setScale(0.5f,0.5f)
        path.transform(matrix)
        path.close()
        canvas.drawPath(path, paint)







    }
}