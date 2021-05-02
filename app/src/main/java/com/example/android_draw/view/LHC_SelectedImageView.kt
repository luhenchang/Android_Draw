package com.example.android_draw.view
import android.animation.Animator
import android.animation.ObjectAnimator
import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.content.res.TypedArray
import android.graphics.drawable.Drawable
import android.util.AttributeSet
import android.util.Log
import android.view.MotionEvent
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
 * 创建日期：1/19/21
 * 描述：OsmDroid
 * E-mail : 1276998208@qq.com
 * CSDN:https://blog.csdn.net/m0_37667770/article
 * GitHub:https://github.com/luhenchang
 */
@Suppress("UNREACHABLE_CODE")
class LHC_SelectedImageView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0) : androidx.appcompat.widget.AppCompatImageView(context, attrs, defStyle) {
    private var defaultImg: Drawable? = null
    private var seletedImg: Drawable? = null
    private var cendefaultImg: Drawable? = null
    private var flag = false
    private var down = false
    private var animalDuration = 0
    private var animalType: Int?

    /**
     * 缩放的动画插值
     */
    private var scaleValueStart=1f
    private var scaleValueCenter=1f
    private var scaleValueEnd=1f

    /**
     * 旋转的动画间隔值
     */
    var rotaion_value_star=0f
    var rotaion_value_center=0f
    var rotaion_value_end=0f

    /**
     * 动画定义
     */
    var valueAnimator: ValueAnimator?=null

    init {
        val array: TypedArray = context.obtainStyledAttributes(attrs, R.styleable.LHC_SelectedImageView)
        defaultImg = array.getDrawable(R.styleable.LHC_SelectedImageView_defaultImag)
        cendefaultImg = array.getDrawable(R.styleable.LHC_SelectedImageView_cendefault_img)
        seletedImg = array.getDrawable(R.styleable.LHC_SelectedImageView_selectedImg)
        animalDuration = array.getInt(R.styleable.LHC_SelectedImageView_animal_duration, 300)
        animalType = array.getInt(R.styleable.LHC_SelectedImageView_animal_type, 0)
        //旋转动画值
        rotaion_value_star  =array.getFloat(R.styleable.LHC_SelectedImageView_animal_rotaion_value_start,0f)
        rotaion_value_center=array.getFloat(R.styleable.LHC_SelectedImageView_animal_rotaion_value_center,0f)
        rotaion_value_end   =array.getFloat(R.styleable.LHC_SelectedImageView_animal_rotaion_value_end,0f)

        //缩放
        scaleValueStart=array.getFloat(R.styleable.LHC_SelectedImageView_animal_scale_value_start,0f)
        scaleValueCenter=array.getFloat(R.styleable.LHC_SelectedImageView_animal_scale_value_center,0f)
        scaleValueEnd=array.getFloat(R.styleable.LHC_SelectedImageView_animal_scale_value_end,0f)
        //设置默认背景
        setDefaultImage()
    }

    /***
     * //设置默认背景
     */
    private fun setDefaultImage() {
        this.background = defaultImg
    }

    //在自己点击事件执行之前-》拦截点击事件来进行背景的修改。
    @SuppressLint("ClickableViewAccessibility")
    override fun onTouchEvent(event: MotionEvent): Boolean {
        Log.e("onTouchEvent", "onTouchEvent=" + event.action.toString())
        if (event.action == MotionEvent.ACTION_DOWN) {
            down = true
        }
        if (event.action == MotionEvent.ACTION_UP && down) {
            //按下的时候设置图片
            setBackgroundImag()
            //如果没有动画
            if (animalType == 0) {
                setPostBackgroundImage()
            }
            down = false
        }
        return super.onTouchEvent(event)
    }
    //设置延迟图片默认背景
    private fun setPostBackgroundImage() {
        postDelayed({
            setEndBackground()
        }, animalDuration.toLong())
    }


    //修改背景
    private fun setBackgroundImag() {
        if (cendefaultImg == null)
            return
        if (animalType == 0) {//表示没动画
            this.background = cendefaultImg
        } else {//表示有动画
            if (valueAnimator == null && animalType == 2) {
                valueAnimator = ObjectAnimator.ofFloat(this.scaleValueStart,scaleValueCenter,scaleValueEnd)
            } else if(valueAnimator == null&&animalType == 1){
                valueAnimator = ObjectAnimator.ofFloat(rotaion_value_star,rotaion_value_center,rotaion_value_end)
            }
            valueAnimator?.duration = (animalDuration).toLong()
            valueAnimator?.addUpdateListener { animation ->
                this.background = cendefaultImg
                //1.旋转动画
                if (animalType == 1) {
                    this.rotation = animation.animatedValue as Float
                } else {
                    //2.缩放动画
                    this.scaleX = animation.animatedValue as Float
                    this.scaleY = animation.animatedValue as Float
                }
            }
            //添加监听动画
            addAnimalListenner(valueAnimator)
            valueAnimator?.start()
        }
    }

    private fun addAnimalListenner(valueAnimator: ValueAnimator?) {
        //监听动画结束且设置最后的背景
        valueAnimator?.addListener(object : Animator.AnimatorListener {
            override fun onAnimationStart(animation: Animator?) {

            }

            override fun onAnimationEnd(animation: Animator?) {
                //3.动画结束时候
                setEndBackground()
            }

            override fun onAnimationCancel(animation: Animator?) {
            }

            override fun onAnimationRepeat(animation: Animator?) {
            }
        })
    }

    private fun setEndBackground() {
        if (!flag) {
            this.background = seletedImg
        } else {
            this.background = defaultImg
        }
        flag = !flag
    }

}
//@Suppress("UNREACHABLE_CODE")
//class LHC_SelectedImageView @JvmOverloads constructor(context: Context, attrs: AttributeSet? = null, defStyle: Int = 0) : androidx.appcompat.widget.AppCompatImageView(context, attrs, defStyle) {
//    var default_img: Drawable? = null
//    var seleted_img: Drawable? = null
//    var flag = false
//    var down = false
//    init {
//        val array: TypedArray = context.obtainStyledAttributes(attrs, R.styleable.LHC_SelectedImageView)
//        default_img = array.getDrawable(R.styleable.LHC_SelectedImageView_defaultImag)
//        seleted_img = array.getDrawable(R.styleable.LHC_SelectedImageView_selectedImg)
//        setDefaultImage()
//    }
//    private fun setDefaultImage() {
//        if (default_img!=null)
//        this.background = default_img
//    }
//
//    //在自己点击事件执行之前-》拦截点击事件来进行背景的修改。
//    @SuppressLint("ClickableViewAccessibility")
//    override fun onTouchEvent(event: MotionEvent): Boolean {
//        Log.e("onTouchEvent", "onTouchEvent=" + event.action.toString())
//        if (event.action == MotionEvent.ACTION_DOWN) {
//            down = true
//        }
//        if (event.action == MotionEvent.ACTION_UP && down) {
//            //按下的时候设置图片
//            setBackgroundImag()
//            down = false
//        }
//        return super.onTouchEvent(event)
//
//    }
//
//    //修改背景
//    private fun setBackgroundImag() {
//        if (!flag) {
//            this.background =seleted_img
//        } else {
//            this.background =default_img
//        }
//        flag = !flag
//    }
//
//}