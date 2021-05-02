package com.example.android_draw.view

import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.content.Context
import android.content.res.TypedArray
import android.util.AttributeSet
import android.view.View
import com.example.android_draw.R
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
 * 创建日期：1/21/21
 * 描述：OsmDroid
 * E-mail : 1276998208@qq.com
 * CSDN:https://blog.csdn.net/m0_37667770/article
 * GitHub:https://github.com/luhenchang
 */
class LHC_ExpandLinearLayout @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0) : androidx.appcompat.widget.LinearLayoutCompat(context, attrs, defStyle) {
    //初始化第一次宽度默认不写为0

    private var childRota: Boolean=false

    /**
     * lt_width
     * LinearLayout总共的宽度
     */
    var lt_width=0

    /**
     * 可点击关闭和打开的子view
     * if gravaty is left that firstChild is oneChildView
     * if gravaty is right that endingChild is oneChildView
     * if top
     * if end
     */
    lateinit var oneChildView:View
    var oneChildViewWidth=0
    /**
     * 选择状态
     */
    var animalStar=false

    /**
     * 动画定义
     */
    var scaleX :ValueAnimator?=null

    /**
     * 动画值跟新布局长度
     */
    var animalValue=0
    /**
     *
     */
    var animalDuration=0

    /**
     * 标记来控制动画的执行方向
     */
    var selecteFlag=false

    /**
     * gragvity
     */
    var gravityOfParent=0x1
    init {
        val array: TypedArray = context.obtainStyledAttributes(attrs, R.styleable.LHC_ExpandLinearLayout)
        gravityOfParent = array.getInt(R.styleable.LHC_ExpandLinearLayout_gravity,0x1)
        childRota=array.getBoolean(R.styleable.LHC_ExpandLinearLayout_child_rotation,false)
        animalDuration=array.getInt(R.styleable.LHC_ExpandLinearLayout_duration,300)
        viewTreeObserver.addOnGlobalLayoutListener {
            setLayout()
        }
    }
    private fun setLayout() {

        oneChildView.setOnClickListener {
            if(scaleX==null) {
                scaleX = ObjectAnimator.ofFloat(oneChildViewWidth.toFloat(), lt_width.toFloat())
            }
            scaleX?.duration = animalDuration.toLong()
            scaleX?.addUpdateListener { animation ->
                animalValue  = MeasureSpec.makeMeasureSpec((animation.animatedValue as Float).toInt(), MeasureSpec.EXACTLY)
                requestLayout()
            }
            if(!selecteFlag){
                scaleX?.start()
            }else{
                scaleX?.reverse()
            }
            selecteFlag=!selecteFlag
            animalStar=true
        }
    }


    override fun onLayout(changed: Boolean, l: Int, t: Int, r: Int, b: Int) {
        val starchild=getChildAt(0)
        val endchild=getChildAt(childCount-1)
        super.onLayout(changed, l, t, r, b)
        if(selecteFlag&&(gravityOfParent==0x2)) {//如果
            if(childRota){//旋转的子布局都需要重新排序一下
                //首先进行摆放
                for (index in 0 until childCount){
                    val childViewWidth=getChildAt(index).measuredWidth
                    val childViewHeight=getChildAt(index).measuredHeight
                    val diagonalLength= sqrt(childViewWidth.toDouble().pow(2.0) + childViewHeight.toDouble().pow(2.0))
                    getChildAt(index).layout(diagonalLength.toInt()*index+((diagonalLength-getChildAt(1).measuredWidth)/2).toInt(),((diagonalLength-starchild.measuredHeight)/2).toInt(),diagonalLength.toInt()*index+((diagonalLength-starchild.measuredWidth)/2).toInt()+starchild.measuredWidth,((diagonalLength-starchild.measuredHeight)/2).toInt()+starchild.measuredHeight)

                }
                //进行交换位置
                val starchild_m=getChildAt(0)
                val endchild_m=getChildAt(childCount-1)
                //附值避免执行完成{@see #onMeasure()}被修改
                val startLeft=endchild_m.left
                val startTop=endchild_m.top
                val startRight=endchild_m.right
                val startBootom=endchild_m.bottom
                getChildAt(childCount-1).layout(starchild_m.left,starchild_m.top,starchild_m.right,starchild_m.bottom)
                getChildAt(0).layout(startLeft,startTop,startRight,startBootom)
            }else{
                val startRight=starchild.right
                val measuredHeight=endchild.measuredHeight
                starchild.layout(endchild.left, 0,endchild.right, starchild.measuredHeight)
                endchild.layout(0, 0,startRight,measuredHeight)
            }

        }else{
            if(childRota)
            for (index in 0 until childCount){
                val childViewWidth=getChildAt(index).measuredWidth
                val childViewHeight=getChildAt(index).measuredHeight
                val diagonalLength1= sqrt(childViewWidth.toDouble().pow(2.0) + childViewHeight.toDouble().pow(2.0))
                getChildAt(index).layout(diagonalLength1.toInt()*index+((diagonalLength1-getChildAt(1).measuredWidth)/2).toInt(),((diagonalLength1-starchild.measuredHeight)/2).toInt(),diagonalLength1.toInt()*index+((diagonalLength1-starchild.measuredWidth)/2).toInt()+starchild.measuredWidth,((diagonalLength1-starchild.measuredHeight)/2).toInt()+starchild.measuredHeight)

            }
        }
    }
    private var heightMeasureSpec: Int = 0
    private var widthMeasureSpec:Int=0

    override fun onMeasure(widthMeasureSpecs: Int, heightMeasureSpecs: Int) {
        lt_width=0
        oneChildView=getChildAt(0)
        for (index in 0 until childCount) {
            if(!childRota) {
                val childView: View = getChildAt(index)
                val marginChildView = childView.layoutParams as MarginLayoutParams
                lt_width += childView.measuredWidth + childView.paddingLeft + childView.paddingRight + marginChildView.leftMargin + marginChildView.rightMargin
            }else{
                val childView:View=getChildAt(index)
                val childViewWidth=childView.measuredWidth
                val childViewHeight=childView.measuredHeight
                val diagonalLength= sqrt(childViewWidth.toDouble().pow(2.0) + childViewHeight.toDouble().pow(2.0))
                lt_width +=diagonalLength.toInt()
            }
        }

        val childViewWidth=oneChildView.measuredWidth
        val childViewHeight=oneChildView.measuredHeight
        val diagonalLength= sqrt(childViewWidth.toDouble().pow(2.0) + childViewHeight.toDouble().pow(2.0))
        if(childRota){
            oneChildViewWidth=diagonalLength.toInt()

        }else{
            oneChildViewWidth=oneChildView.measuredWidth
        }
        if (childRota) {
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(diagonalLength.toInt(), MeasureSpec.EXACTLY)
        }else{
            //这里简化应该求各个子View中最高的一个
            heightMeasureSpec=MeasureSpec.makeMeasureSpec(childViewHeight, MeasureSpec.AT_MOST)
        }
        if(!animalStar){
            //如果子View有旋转
            if(childRota){
                widthMeasureSpec = MeasureSpec.makeMeasureSpec(diagonalLength.toInt(), MeasureSpec.EXACTLY)
            }else{
                widthMeasureSpec = MeasureSpec.makeMeasureSpec(oneChildView.measuredWidth, MeasureSpec.EXACTLY)

            }
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)
        }else{
            widthMeasureSpec = MeasureSpec.makeMeasureSpec(animalValue, MeasureSpec.EXACTLY)
            super.onMeasure(widthMeasureSpec, heightMeasureSpec)

        }
    }
}




