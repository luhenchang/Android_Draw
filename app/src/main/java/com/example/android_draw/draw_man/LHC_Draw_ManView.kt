package com.example.android_draw.draw_man

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.Path
import android.provider.CalendarContract
import android.util.AttributeSet
import android.util.Log
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
 * 创建日期：2/3/21
 * 描述：Android_Draw
 * E-mail : 1276998208@qq.com
 * CSDN:https://blog.csdn.net/m0_37667770/article
 * GitHub:https://github.com/luhenchang
 */
class LHC_Draw_ManView @JvmOverloads constructor(context: Context?, attrs: AttributeSet?=null, defStyleAttr: Int=0) : View(context, attrs, defStyleAttr) {
    @SuppressLint("DrawAllocation", "NewApi")
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        //画脸
        val facePaint = Paint()
        facePaint.strokeWidth = 1f
        facePaint.style = Paint.Style.STROKE
        facePaint.color = Color.BLACK
        facePaint.setShadowLayer(10f,10f,15f,Color.GRAY)

        canvas.translate(-180f, 0f)

        val path = Path()
        path.moveTo(300f, 300f)
        path.cubicTo(300f, 300f, 249f, 400f, 250f, 500f)
        path.rCubicTo(0f, 0f, 0f, 100f, 19f, 150f)
        //耳朵
        path.rCubicTo(0f, 0f, -80f, -100f, -80f, 90f)
        //耳朵下部分
        path.rCubicTo(0f, 0f, 8f, 30f, 30f, 100f)
        //l耳朵最底部
        path.rLineTo(20f, 50f)
        path.rLineTo(50f, 60f)
        //l 耳朵最下面平
        path.rLineTo(20f, -10f)

        canvas.drawPath(path, facePaint)

        //内耳朵线
        val path_in = Path()
        path_in.moveTo(210f, 750f)
        path_in.rCubicTo(0f, 0f, -25f, -160f, 90f, 45f)
        //l face
        path_in.rCubicTo(0f, 0f, 55f, 140f, 130f, 388f)
        path_in.rCubicTo(0f, 0f, 55f, 140f, 300f, 238f)


        //右边脸
        path_in.rCubicTo(0f, 0f, 55f, 60f, 110f, 0f)
        path_in.rCubicTo(0f, 0f, 150f, -140f, 200f, -300f)
        path_in.rCubicTo(0f, 0f, 40f, -80f, 56f, -250f)

        path_in.rCubicTo(0f, 0f, 10f, -240f, 86f, -230f)
        path_in.rCubicTo(0f, 0f, 20f, 40f, 0f, 80f)
        path_in.rCubicTo(0f, 0f, 43f, -200f, -80f, -75f)


        //右边头顶线路
        path_in.rCubicTo(0f, 0f, 30f, -250f, -120f, -400f)


        canvas.drawPath(path_in, facePaint)


        //画左边的眉毛
        val path_brow = Path()
        path_brow.moveTo(700f, 1100f)
        path_brow.rCubicTo(2f, -160f, -15f, -100f, -10f, -170f)

        facePaint.style= Paint.Style.FILL
        facePaint.color=Color.BLACK
        facePaint.setShadowLayer(10f,10f,15f,Color.GRAY)
        canvas.drawPath(path_brow, facePaint)

        val path_browLT = Path()
        path_browLT.moveTo(690f, 920f)
        path_browLT.rQuadTo(-250f, -160f, -300f,-170f)
        path_browLT.lineTo(690f, 928f)


        facePaint.style= Paint.Style.FILL
        canvas.drawPath(path_browLT, facePaint)

//
//        //画眼睛
//        val path_eye = Path()
//        path_eye.moveTo(600f, 890f)
//        path_eye.rLineTo(-160f, -60f)
//        path_eye.rCubicTo(0f, 0f, 60f, 160f, 200f, 100f)
//        facePaint.style= Paint.Style.STROKE
//        canvas.drawPath(path_eye, facePaint)


        val eyeFilePaint = Paint()
        eyeFilePaint.strokeWidth = 14f
        eyeFilePaint.style = Paint.Style.STROKE
        eyeFilePaint.color = Color.BLACK
        eyeFilePaint.style = Paint.Style.FILL
        eyeFilePaint.setShadowLayer(10f,10f,4f,Color.GRAY)





        //右边的眼眉毛
        val eyePathRgith = Path()
        eyePathRgith.moveTo(800f, 900f)
        eyePathRgith.rCubicTo(50f, -20f, 120f, -120f, 200f, -140f)


        canvas.drawPath(eyePathRgith, eyeFilePaint)


        val rectPath=Path()
        rectPath.moveTo(100f,100f)
        rectPath.rLineTo(100f,0f)
        rectPath.rLineTo(0f,100f)
        rectPath.rLineTo(-100f,0f)
        rectPath.close()
        val rectPaint = Paint()
        rectPaint.strokeWidth = 14f
        rectPaint.style = Paint.Style.STROKE
        rectPaint.color = Color.BLACK
        rectPaint.style = Paint.Style.FILL


        val ciclePath=Path()
        ciclePath.addCircle(200f,200f,100f,Path.Direction.CCW)
        rectPath.op(ciclePath,Path.Op.DIFFERENCE)
        canvas.drawPath(rectPath,rectPaint)

        val eyePath = Path()
        eyePath.moveTo(690f, 920f)
        eyePath.rQuadTo(-150f, -100f,-300f,-100f)
        eyePath.rQuadTo(130f, 3f,300f,104f)
        canvas.drawPath(eyePath, eyeFilePaint)

        val eyePathDown = Path()
        eyePathDown.moveTo(690f, 920f)
        eyePathDown.rQuadTo(-180f, 50f,-300f,-100f)
        eyePathDown.rQuadTo(160f, 150f,300f,100f)
        canvas.drawPath(eyePathDown, eyeFilePaint)


        val eyePathCircle = Path()
        eyePathCircle.moveTo(620f, 880f)
        eyePathCircle.rQuadTo(-90f, 33f,-100f,-40f)
        canvas.drawPath(eyePathCircle, eyeFilePaint)

        //右边的眼睛
//        val eyePathRigth = Path()
//        eyePathRigth.moveTo(860f, 910f)
//        eyePathRigth.rQuadTo(20f, -80f,200f,-120f)
//        eyePathRigth.rQuadTo(-80f, -20f,-220f,120f)
//
//        canvas.drawPath(eyePathRigth, eyeFilePaint)

    }
}