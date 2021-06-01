package com.example.android_draw.view.paint
import android.content.Context
import android.graphics.*
import android.os.Build
import android.util.AttributeSet
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.view.marginBottom
import com.example.android_draw.R


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
 * 创建日期：2/25/21
 * 描述：Android_Draw
 * E-mail : 1276998208@qq.com
 * CSDN:https://blog.csdn.net/m0_37667770/article
 * GitHub:https://github.com/luhenchang
 */
class LHC_Paint_View @JvmOverloads constructor(
    context: Context,
    attrs: AttributeSet? = null,
    defStyle: Int = 0
) : View(context, attrs, defStyle) {
    private val canvasTop=200f
    private val canvasMargerRight=100f
    private val yMargerLeft=200f
    private val xMargerBootom=250f
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    override fun onDraw(canvas: Canvas) {
        super.onDraw(canvas)
        canvas.drawColor(Color.WHITE)
        //绘制圆
        //drawCircleSetStyle(canvas)
        //抗锯齿
        //setAntiAlias(canvas)
        //shander
        //线性渐变
        //shaderStudyLinearGradient(canvas)
        //扫描渐变
        //shaderStudySweepGradient(canvas)
        //径向渐变
        //shaderStudyRadialGradient(canvas)


        //位图着色器
        //shaderStudyBitmapShader(canvas)
        //合成着色器 ComposeShader
        //shaderStudyComposeShader(canvas)

        //setStrokeCap
        //setStrokeCapStudy(canvas)

        //阴影设置
        //setShaderLayout(canvas)

        //路径作为图案绘制在路径上
        //setPathDashPathEffect(canvas)

        //两种不同的路径效果综合起来
        //setSumPathEffect(canvas)

        //setCornerPathEffectEffect(canvas)
        //discretePathEffectStudy(canvas)


      //案例开始绘制
      //1.cnavas坐标系的变换绘制
      canvasChangeSave(canvas)
      //2

    }
    //1.cnavas坐标系的变换绘制
    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun canvasChangeSave(canvas: Canvas) {
        val paint= Paint()
        paint.color= Color.argb(55,111,111,111)
        paint.strokeWidth=2f
        paint.style= Paint.Style.STROKE
        paint.strokeCap= Paint.Cap.ROUND

        //x轴竞相
        canvas.scale(1f,-1f)
        //坐标系向下平移
        canvas.translate(yMargerLeft, -height.toFloat()+xMargerBootom)
        //绘制圆圈测试ok没问题删掉就行
        //canvas.drawCircle(0f,0f,10f,paint


        val yGridHeight = (height.toFloat()-xMargerBootom-canvasTop)/5
        val xLength=width.toFloat()-yMargerLeft-canvasMargerRight

        val path=Path()
        path.lineTo(xLength,0f)

        canvas.save()
        //绘制线平行与X轴的
        for(index in 0 until 5){
            canvas.drawPath(path,paint)
            canvas.translate(0f,yGridHeight)

        }
        canvas.restore()



        //绘制折线
        drawLine(yGridHeight, canvas, paint)
        //绘制日期
        drawXTextOfBootomLine(canvas)
        //绘制y轴数值
        drawYTextOfYLeft(canvas)
        //绘制下面的文字框和文字,不是本节重点,所以为了快我就不分开讲解了
        drawRectAndText(canvas)

        //绘制顶部的文字
        drawTopText(canvas)

    }

    @RequiresApi(Build.VERSION_CODES.LOLLIPOP)
    private fun drawTopText(canvas: Canvas) {
        val paint= Paint()
        paint.color= Color.BLACK
        paint.strokeWidth=5f
        paint.style= Paint.Style.STROKE
        paint.strokeCap= Paint.Cap.ROUND
        paint.textSize=56f
        paint.letterSpacing=0.5f
        canvas.translate(-100f, (height-xMargerBootom-100f))
        canvas.scale(1f,-1f)
        canvas.drawText("近一年净值走势",0f,0f,paint)
        canvas.translate(0f,100f)

        paint.letterSpacing=0f
        paint.textSize=36f
        paint.strokeWidth=2f
        paint.color= Color.argb(150,111,111,111)
        canvas.drawText("累计净值： 1。97744   单位净值：1。97773   日张丢福：  -0。005%",0f,0f,paint)


    }

    private fun drawRectAndText(canvas: Canvas) {
        val paint= Paint()
        paint.color= Color.argb(50,111,111,111)
        paint.strokeWidth=5f
        paint.style= Paint.Style.STROKE
        paint.strokeCap= Paint.Cap.ROUND
        paint.textSize=36f
        canvas.save()
        canvas.translate(-100f,-100f)
        val topLine=Path()
        topLine.moveTo(0f,0f)
        topLine.lineTo(1790f,0f)
        topLine.rLineTo(0f,-100f)
        topLine.rLineTo(-1790f,0f)
        topLine.close()
        canvas.drawPath(topLine,paint)
        val oneWidth=1790f/5
        for (index in 0 until 5){
            canvas.save()
            canvas.scale(1f,-1f)
            paint.color=Color.GRAY
            canvas.drawText("${index*3}个月",40f,60f,paint)
            canvas.restore()
            canvas.translate(oneWidth,0f)
            val onetopLine=Path()
            onetopLine.moveTo(0f,0f)
            onetopLine.lineTo(0f,-100f)
            paint.color= Color.argb(50,111,111,111)
            canvas.drawPath(onetopLine,paint)

        }
        canvas.restore()

    }

    private fun drawYTextOfYLeft(canvas: Canvas) {
        val paint= Paint()
        paint.color= Color.argb(150,111,111,111)
        paint.strokeWidth=2f
        paint.style= Paint.Style.STROKE
        paint.strokeCap= Paint.Cap.ROUND
        paint.textSize=30f
        canvas.save()
        canvas.scale(1f,-1f)
        val yGridHeight = (height.toFloat()-xMargerBootom-canvasTop)/5
        for (index in 0 until 5){
            canvas.drawText("${10900+2900*index}",-100f,10f,paint)
            canvas.translate(0f,-yGridHeight)
        }
        canvas.restore()

    }

    private fun drawXTextOfBootomLine(canvas: Canvas) {
        val paint= Paint()
        paint.color= Color.argb(150,111,111,111)
        paint.strokeWidth=2f
        paint.style= Paint.Style.STROKE
        paint.strokeCap= Paint.Cap.ROUND
        paint.textSize=30f
        canvas.save()
        canvas.scale(1f,-1f)
        canvas.drawText("2020-02-25",0f,50f,paint)
        canvas.drawText("2020-08-21",width/3f,50f,paint)
        canvas.drawText("2021-02-25",1472f,50f,paint)


        canvas.restore()
    }


    private fun drawLine(
        yGridHeight: Float,
        canvas: Canvas,
        paint: Paint
    ) {
        //绘制折线
        val dataList: ArrayList<PointF> = ArrayList()
        dataList.add(PointF(20f, yGridHeight))
        dataList.add(PointF(30f, yGridHeight - 70f))
        dataList.add(PointF(60f, yGridHeight))
        dataList.add(PointF(120f, yGridHeight - 90))
        dataList.add(PointF(160f, 40f))
        dataList.add(PointF(200f, 90f))
        dataList.add(PointF(400f, yGridHeight + 30f))
        dataList.add(PointF(500f, yGridHeight + 60f))
        dataList.add(PointF(700f, yGridHeight * 2 + 90))
        dataList.add(PointF(760f, yGridHeight * 2 + 10))
        dataList.add(PointF(820f, yGridHeight * 2))
        dataList.add(PointF(870f, yGridHeight * 2 + 134))
        dataList.add(PointF(920f, yGridHeight * 2 + 54))
        dataList.add(PointF(970f, yGridHeight * 2 - 111))
        dataList.add(PointF(1170f, yGridHeight * 2 + 111))
        dataList.add(PointF(1270f, yGridHeight * 2 ))
        dataList.add(PointF(1370f, yGridHeight * 3 + 11))
        dataList.add(PointF(1470f, yGridHeight * 3 + 21))
        dataList.add(PointF(1570f, yGridHeight * 3 - 21))
        dataList.add(PointF(1670f, yGridHeight * 4 - 21))
        dataList.add(PointF(1690f, yGridHeight * 4 - 41))
        dataList.add(PointF(1790f, yGridHeight * 4 + 71))



        val fillPaint=Paint()
        fillPaint.style=Paint.Style.FILL
        val colorTop= intArrayOf(Color.argb(65,79,185,246),Color.WHITE)
        fillPaint.shader=LinearGradient(0f,yGridHeight * 4 + 71,0f,0f,colorTop,null,Shader.TileMode.CLAMP)



        //1.绘制折线渐变部分
        val linePathGrident = Path()
        linePathGrident.moveTo(20f, yGridHeight)
        for (index in 0 until dataList.size - 1) {
            linePathGrident.lineTo(dataList[index].x, dataList[index].y)
        }

        linePathGrident.lineTo(1690f, yGridHeight * 4 - 41)
        linePathGrident.lineTo(1690f,0f)
        linePathGrident.lineTo(20f,0f)
        linePathGrident.close()
        canvas.drawPath(linePathGrident, fillPaint)



        //2.绘制折线
        val linePath = Path()
        linePath.moveTo(20f, yGridHeight)
        for (index in 0 until dataList.size - 1) {
            linePath.lineTo(dataList[index].x, dataList[index].y)

        }

        val tPath = Path()
        tPath.addCircle(0f, 0f, 19f,Path.Direction.CCW)

        val tPath2 = Path()
        tPath2.addCircle(0f, 0f, 25f,Path.Direction.CCW)
        paint.setShadowLayer(30f,30f,0f,Color.BLUE)
        //PathDashPathEffect.Style.ROTATE
        val pathDshEffect1 = PathDashPathEffect(tPath, 45f, 16f, PathDashPathEffect.Style.ROTATE)
        val pathDshEffect2 = PathDashPathEffect(tPath2, 160f, 60f, PathDashPathEffect.Style.ROTATE)
        paint.pathEffect = ComposePathEffect(pathDshEffect2, pathDshEffect1)
        paint.color = Color.argb(255,209,103,58)
        canvas.drawPath(linePath, paint)
    }











    private fun discretePathEffectStudy(canvas: Canvas) {
        val paint= Paint()
        paint.color= Color.RED
        paint.strokeWidth=2f
        paint.style= Paint.Style.STROKE
        paint.strokeCap= Paint.Cap.ROUND
        canvas.translate(500f, 300f)



        val path=Path()
        path.moveTo(0f, 0f)
        path.lineTo(1100f, 0f)
        path.lineTo(1100f, 100f)


        //设置路径效果
        paint.pathEffect = DiscretePathEffect(4f,50f)
        canvas.drawPath(path, paint)


    }

    private fun setCornerPathEffectEffect(canvas: Canvas) {
        val paint= Paint()
        paint.color= Color.RED
        paint.strokeWidth=22f
        paint.style= Paint.Style.STROKE
        canvas.translate(500f, 300f)



        val path=Path()
        path.moveTo(0f, 0f)
        path.lineTo(1100f, 0f)
        path.lineTo(1100f, 100f)




        canvas.drawPath(path, paint)

        canvas.translate(0f, 200f)
        //设置路径效果
        paint.pathEffect = CornerPathEffect(44f)
        canvas.drawPath(path, paint)



    }

    private fun setSumPathEffect(canvas: Canvas) {
        val paint= Paint()
        paint.color= Color.RED
        paint.strokeWidth=22f
        paint.style= Paint.Style.STROKE
        canvas.translate(500f, 300f)


        val path=Path()
        path.moveTo(0f, 0f)
        path.lineTo(1100f, 0f)

        //要绘制在路径上的的圆圈
        val tPath = Path()
        tPath.addCircle(0f, 0f, 50f,Path.Direction.CCW)


        //要绘制在路径上的的正方形
        val tPath1 = Path()
        tPath1.addRect(-100f, -100f, 110f,120f,Path.Direction.CCW)

        val pathDeshEffect= PathDashPathEffect(tPath, 150f, 50f, PathDashPathEffect.Style.ROTATE)
        val pathDshEffect1= PathDashPathEffect(tPath1, 150f, 150f, PathDashPathEffect.Style.TRANSLATE)
        //组合多种路径效果
        paint.pathEffect = ComposePathEffect(pathDeshEffect, pathDshEffect1)


        canvas.drawPath(path, paint)
    }

    private fun setPathDashPathEffect(canvas: Canvas){
        val paint= Paint()
        paint.color= Color.RED
        paint.strokeWidth=22f
        paint.style= Paint.Style.STROKE
        canvas.translate(500f, 300f)


        val path=Path()
        path.moveTo(0f, 0f)
        path.lineTo(1100f, 0f)

        //要绘制在路径上的的圆圈
        val tPath = Path()
        tPath.addCircle(0f, 0f, 20f,Path.Direction.CCW)

        //PathDashPathEffect.Style.ROTATE
        paint.pathEffect = PathDashPathEffect(tPath, 150f, 50f, PathDashPathEffect.Style.ROTATE)
        canvas.drawPath(path, paint)


        //要绘制在路径上的的正方形
        val tPath1 = Path()
        tPath1.addRect(0f, 0f, 50f,50f,Path.Direction.CCW)


        //PathDashPathEffect.Style.TRANSLATE
        canvas.translate(0f, 220f)
        paint.pathEffect = PathDashPathEffect(tPath1, 150f, 150f, PathDashPathEffect.Style.TRANSLATE)
        canvas.drawPath(path, paint)


        //要绘制在路径上的的椭圆
        val tPath2 = Path()
        val rectF=RectF(0f,0f,50f,80f)
        tPath2.addOval(rectF,Path.Direction.CCW)

        //PathDashPathEffect.Style.MORPH
        canvas.translate(0f, 220f)
        paint.pathEffect = PathDashPathEffect(tPath2, 150f, 250f, PathDashPathEffect.Style.MORPH)
        canvas.drawPath(path, paint)


    }

    private fun setShaderLayout(canvas: Canvas) {
        val paint= Paint()
        paint.color= Color.RED
        paint.strokeWidth=22f
        paint.style= Paint.Style.STROKE
        val flatArray= floatArrayOf(10f, 10f)
        paint.pathEffect = DashPathEffect(flatArray, 11f)


        val cicle_path=Path()
        cicle_path.addCircle(0f, 0f, 150f, Path.Direction.CCW)

        //设置阴影
        paint.setShadowLayer(10f, 10f, 0f, Color.BLUE)
        canvas.translate((height / 2).toFloat(), 300f)
        canvas.drawCircle(0f, 0f, 150f, paint)



        canvas.translate(500f, 0f)
        val flatArray1= floatArrayOf(120f, 40f, 10f, 111f)
        paint.pathEffect = DashPathEffect(flatArray1, 11f)

        paint.setShadowLayer(30f, 30f, 0f, Color.GREEN)
        canvas.drawCircle(0f, 0f, 150f, paint)

        val flatArray2= floatArrayOf(20f, 40f, 222f, 222f)
        paint.pathEffect = DashPathEffect(flatArray2, 11f)
        canvas.translate(500f, 0f)
        paint.setShadowLayer(30f, 0f, 30f, Color.BLACK)
        canvas.drawCircle(0f, 0f, 150f, paint)


    }

    private fun setStrokeCapStudy(canvas: Canvas) {
        val paint= Paint()
        paint.color= Color.RED
        paint.strokeWidth=112f
        paint.style=Paint.Style.STROKE
        paint.strokeCap=Paint.Cap.BUTT

        canvas.translate(500f, 300f)

        val path=Path()
        path.moveTo(0f, 0f)
        path.lineTo(0f, 500f)
        canvas.drawPath(path, paint)


        canvas.translate(300f, 0f)
        paint.strokeCap=Paint.Cap.ROUND
        canvas.drawPath(path, paint)

        canvas.translate(300f, 0f)
        paint.strokeCap=Paint.Cap.SQUARE
        canvas.drawPath(path, paint)


    }

    private fun shaderStudyBitmapShader(canvas: Canvas) {
        val paint= Paint()
        paint.color= Color.RED
        paint.strokeWidth=2f
        paint.style=Paint.Style.FILL

        //public BitmapShader(@NonNull Bitmap bitmap, @NonNull TileMode tileX, @NonNull TileMode tileY)
        val bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.mn_6)
        paint.shader=BitmapShader(bitmap, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT)


        canvas.translate((height / 2).toFloat(), 300f)
        canvas.drawCircle(0f, 0f, 250f, paint)

        canvas.translate(500f, 0f)
        paint.shader=BitmapShader(bitmap, Shader.TileMode.MIRROR, Shader.TileMode.MIRROR)
        canvas.drawCircle(0f, 0f, 250f, paint)

        canvas.translate(500f, 0f)
        paint.shader=BitmapShader(bitmap, Shader.TileMode.CLAMP, Shader.TileMode.CLAMP)
        canvas.drawCircle(0f, 0f, 250f, paint)

    }

    private fun shaderStudyComposeShader(canvas: Canvas) {
        val paint= Paint()
        paint.color= Color.RED
        paint.strokeWidth=2f
        paint.style=Paint.Style.FILL


        val colors= intArrayOf(Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW)
        val position= floatArrayOf(0f, 0.5f, 0.9f, 1f)
        val shader01= RadialGradient(0f, 0f, 200f, colors, null, Shader.TileMode.CLAMP)
        val shader02=SweepGradient(0f, 0f, colors, null)
        paint.shader=ComposeShader(shader01, shader02, PorterDuff.Mode.MULTIPLY)


        canvas.translate((height / 2).toFloat(), 300f)
        canvas.drawCircle(0f, 0f, 250f, paint)


        canvas.translate(500f, 0f)
        val bitmap = BitmapFactory.decodeResource(context.resources, R.drawable.mn_6)
        val bitmapShader=BitmapShader(bitmap, Shader.TileMode.REPEAT, Shader.TileMode.REPEAT)
        paint.shader=ComposeShader(shader01, bitmapShader, PorterDuff.Mode.MULTIPLY)
        canvas.drawCircle(0f, 0f, 250f, paint)


    }

    private fun shaderStudyRadialGradient(canvas: Canvas) {
        val paint= Paint()
        paint.color= Color.RED
        paint.strokeWidth=2f
        paint.style=Paint.Style.FILL


        val colors= intArrayOf(Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW)
        val position= floatArrayOf(0f, 0.5f, 0.9f, 1f)
        //当positions为null的时候渐变色均匀分布,当position不为null时必须和色值colors的长度一致,且position 是从0f,0.2f,0.5,0.9f....1f表示角度比例进行着色
        paint.shader=RadialGradient(0f, 0f, 200f, colors, null, Shader.TileMode.CLAMP)


        canvas.translate((height / 2).toFloat(), 300f)
        canvas.drawCircle(0f, 0f, 250f, paint)

        //向右平移画布坐标系
        canvas.translate(700f, 0f)
        paint.shader=RadialGradient(0f, 100f, 200f, colors, null, Shader.TileMode.CLAMP)
        canvas.drawCircle(0f, 0f, 250f, paint)



    }

    //
    private fun shaderStudySweepGradient(canvas: Canvas) {
        val paint= Paint()
        paint.color= Color.RED
        paint.strokeWidth=2f
        paint.style=Paint.Style.FILL


        val colors= intArrayOf(Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW)
        val position= floatArrayOf(0f, 0.5f, 0.9f, 1f)
        //当positions为null的时候渐变色均匀分布,当position不为null时必须和色值colors的长度一致,且position 是从0f,0.2f,0.5,0.9f....1f表示角度比例进行着色
        paint.shader=SweepGradient(0f, 100f, colors, null)


        canvas.translate((height / 2).toFloat(), 300f)
        canvas.drawCircle(0f, 0f, 250f, paint)


    }

    //着色器设置
    private fun shaderStudyLinearGradient(canvas: Canvas) {
        val paint= Paint()
        paint.color= Color.RED
        paint.strokeWidth=133f
        paint.style= Paint.Style.FILL
        paint.isAntiAlias=true
        /**
         *public LinearGradient(float x0, float y0, float x1, float y1, @NonNull @ColorInt int[] colors,
         *@Nullable float[] positions, @NonNull TileMode tile)
         **/
        val colors= intArrayOf(Color.RED, Color.GREEN, Color.BLUE, Color.YELLOW)
        val position= floatArrayOf(0f, 0.5f, 0.9f, 1f)
        //当positions为null的时候渐变色均匀分布,当position不为null时必须和色值colors的长度一致,且position 是从0f,0.2f,0.5,0.9f....1f
        paint.shader=LinearGradient(0f, 0f, 1400f, 500f, colors, position, Shader.TileMode.CLAMP)
        canvas.translate(200f, (height / 2).toFloat())

        val path=Path()
        path.lineTo(1400f, 0f)
        path.lineTo(1400f, 500f)
        path.lineTo(0f, 500f)
        path.close()

        canvas.drawPath(path, paint)

    }


    /**
     * 填充类型
     */
    private fun drawCircleSetStyle(canvas: Canvas) {
        val paint= Paint()
        paint.color= Color.RED
        paint.strokeWidth=2f


        paint.style= Paint.Style.STROKE
        canvas.translate((height / 2).toFloat(), 300f)
        canvas.drawCircle(0f, 0f, 150f, paint)

        canvas.translate(400f, 0f)
        paint.style= Paint.Style.FILL
        canvas.drawCircle(0f, 0f, 150f, paint)


        canvas.translate(400f, 0f)
        paint.style= Paint.Style.FILL_AND_STROKE
        canvas.drawCircle(0f, 0f, 150f, paint)


    }

    /**
     * 是否抗锯齿的验证对比
     */
    private fun setAntiAlias(canvas: Canvas) {
        val paint= Paint()
        paint.color= Color.RED
        paint.strokeWidth=133f
        paint.style= Paint.Style.STROKE



        //不抗锯齿
        paint.isAntiAlias=false
        canvas.translate((height / 2).toFloat(), 600f)
        canvas.drawCircle(0f, 0f, 250f, paint)

        //设置抗锯齿
        paint.isAntiAlias=true
        canvas.translate(700f, 0f)
        canvas.drawCircle(0f, 0f, 250f, paint)
    }
}

