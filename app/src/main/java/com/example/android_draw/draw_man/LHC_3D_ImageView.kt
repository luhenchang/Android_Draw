package com.example.android_draw.draw_man

import android.annotation.SuppressLint
import android.content.Context
import android.content.res.TypedArray
import android.graphics.*
import android.util.AttributeSet
import android.view.MotionEvent
import androidx.appcompat.widget.AppCompatImageView
import androidx.core.graphics.drawable.toBitmap
import com.example.android_draw.R

class LHC_3D_ImageView @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : AppCompatImageView(context, attrs, defStyleAttr) {
    private var heightWidth: Int = 0
    var bitmap: Bitmap
    var width: Double = 0.0

    init {
        val array: TypedArray = context.obtainStyledAttributes(attrs, R.styleable.LHC_3D_ImageView)
        val resourcesId = array.getDrawable(R.styleable.LHC_3D_ImageView_resouceId)
        bitmap = resourcesId!!.toBitmap()
        heightWidth = array.getFloat(R.styleable.LHC_3D_ImageView_rectHeight, 600f).toInt()
        if(bitmap != null){
            width = (heightWidth.toDouble() / bitmap.height * bitmap.width)
            bitmap = zoomImage(bitmap, width, heightWidth.toDouble())
        }

    }

    private var intrinsicHeight = 0
    private var intrinsicWidth = 0

    /**
     * 计算图片有多少张
     */
    private var imgNumber = 0

    private var mMatrix: Matrix? = null

    /**
     * 当前图片编号
     */
    private var currentSrcNumber = 1

    /**
     * 滑动的差值
     */
    var dX = 0f

    /**
     * 按下的起始位置
     */
    var startX = 0f


    override fun onMeasure(widthMeasureSpec: Int, heightMeasureSpec: Int) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        intrinsicHeight = heightWidth
        intrinsicWidth = width.toInt()
        imgNumber = intrinsicWidth / intrinsicHeight
        //进行屏幕画布的显示区域
        setMeasuredDimension(heightWidth, heightWidth)
    }

    override fun onSizeChanged(w: Int, h: Int, oldw: Int, oldh: Int) {
        super.onSizeChanged(w, h, oldw, oldh)
        mMatrix = matrix
    }

    override fun onDraw(canvas: Canvas?) {
        super.onDraw(canvas)
        mMatrix?.run {
            reset()
            val slidingDistance = -(heightWidth * (imgNumber - (currentSrcNumber - 1))).toFloat()
            postTranslate(slidingDistance, 0f)
            canvas!!.drawBitmap(bitmap, this, null)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        when (event.action) {
            MotionEvent.ACTION_DOWN -> {
                startX = event.x
            }
            MotionEvent.ACTION_MOVE -> {
                val endX = event.x
                dX = endX - startX
                if (dX > 10) {
                    modifySrcR()
                }
                if (dX < -10) {
                    modifySrcL()
                }
                startX = event.x
            }
        }
        return true
    }


    /**
     * 向左滑动 图片向左移动一个单位
     */
    private fun modifySrcL() {
        if (currentSrcNumber <= 0) {
            currentSrcNumber = imgNumber
        }
        currentSrcNumber--
        postInvalidate()
    }

    /**
     * 向右滑动 图片向右移动一个单位
     */
    private fun modifySrcR() {
        if (currentSrcNumber > imgNumber) {
            currentSrcNumber = 1
        }
        currentSrcNumber++
        postInvalidate()
    }

    fun zoomImage(
        bgimage: Bitmap, newWidth: Double,
        newHeight: Double
    ): Bitmap {
        // 获取这个图片的宽和高
        val width = bgimage.width.toFloat()
        val height = bgimage.height.toFloat()
        // 创建操作图片用的matrix对象
        val matrix = Matrix()
        // 计算宽高缩放率
        val scaleWidth = newWidth.toFloat() / width
        val scaleHeight = newHeight.toFloat() / height
        // 缩放图片动作
        matrix.postScale(scaleWidth, scaleHeight)
        return Bitmap.createBitmap(
            bgimage, 0, 0, width.toInt(),
            height.toInt(), matrix, true
        )
    }


}