package com.example.android_draw.view

import android.content.Context
import android.util.AttributeSet

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
 * 创建日期：5/28/21
 * 描述：Android_Draw
 * E-mail : 1276998208@qq.com
 * CSDN:https://blog.csdn.net/m0_37667770/article
 * GitHub:https://github.com/luhenchang
 */
import android.animation.AnimatorSet
import android.animation.ValueAnimator
import android.app.Activity
import android.graphics.*
import android.os.Handler
import android.os.Looper
import android.os.Message
import android.view.View
import android.view.animation.AccelerateDecelerateInterpolator
import com.example.android_draw.R
import com.example.android_draw.view.soundwava.Source
import com.example.android_draw.view.soundwava.SourceMock
import java.util.*

class AnimBar @JvmOverloads constructor(
    context: Context,
    attributeSet: AttributeSet? = null,
    defStyleAttr: Int = 0,
    defStyleRes: Int = 0
) : View(context, attributeSet, defStyleAttr, defStyleRes) {
    private val paint = Paint().apply {
        strokeWidth = 20f
        color = Color.GRAY
    }
    private var mTimer: Timer? = null
    private val source: Source<Int> = SourceMock()
    private val mHandler: Handler = object : Handler(Looper.myLooper() ?: Looper.getMainLooper()) {
        override fun handleMessage(msg: Message) {
            super.handleMessage(msg)
            when (msg.what) {
                1->{
                    source.begin(0)
                }
            }
        }
    }
    init {
        source.setOnDataFeedListener {
            (context as Activity).runOnUiThread {
                setData(it)
            }
        }
    }
    private val shader by lazy {
        val res = context.resources
        LinearGradient(
            0f,
            0f,
            measuredWidth.toFloat(),
            measuredHeight.toFloat(),
            intArrayOf(
                res.getColor(R.color.voice_color1,null),
                res.getColor(R.color.voice_color2,null),
                res.getColor(R.color.voice_color3,null),
                res.getColor(R.color.voice_color4,null),
                res.getColor(R.color.voice_color5,null)
            ),
            null,
            Shader.TileMode.CLAMP
        )
    }

    private var data: MutableList<Int> = mutableListOf()

    override fun onDraw(canvas: Canvas) {

        val barWidth = 4
        val spaceWidth = 9
        val max = 100

        val clipPath = Path()

        val startX = (measuredWidth - data.size * (barWidth + spaceWidth) - 1f) / 2
        val middleY = measuredHeight / 2f

        data.forEachIndexed { index, value ->
            val heightFactor = value.toFloat() / max
            val barOffset = heightFactor * measuredHeight.toFloat() / 2f
            val start = startX + index * (barWidth + spaceWidth)
            clipPath.addRoundRect(
                start,
                middleY - barOffset,
                start + barWidth,
                middleY + barOffset,
                5f, 5f, Path.Direction.CW
            )
        }
        canvas.clipPath(clipPath)

        setupBgPaint()
        canvas.drawPaint(paint)
    }

    private fun setupBgPaint() {
        paint.shader = shader
    }

    fun setData(list: List<Int>) {
        if (list.isEmpty()) {
            return
        }
        val new = list.toList()
        val old =
            if (this.data.isEmpty()) MutableList(new.size) { 0 }
            else this.data.toList()

        data = MutableList(new.size) { 0 }

        val a = anim
        if (a == null || !a.isRunning) {
            anim = buildAnimator(new, old, 250).apply { start() }
        }
    }

    private var anim: AnimatorSet? = null

    private fun buildAnimator(new: List<Int>, old: List<Int>, time: Long): AnimatorSet {
        return AnimatorSet().apply {
            for (i in new.indices) {
                val delta = new[i] - old[i]
                val childAnim = ValueAnimator.ofInt(old[i], new[i]).apply {
                    addUpdateListener {
                        data[i] = (old[i] + delta * it.animatedFraction).toInt()
                    }
                }
                play(childAnim)
            }

            val progressAnim = ValueAnimator.ofFloat(0f, 1f).apply {
                addUpdateListener {
                    invalidate()
                }
            }
            play(progressAnim)

            duration = time
            interpolator = AccelerateDecelerateInterpolator()
        }
    }

    fun startVoice(nextInt: Int) {
        (source as SourceMock).setState(true)
        source.begin(nextInt)
    }

    fun stopVoice() {
        (source as SourceMock).setState(false)
        source.begin(5)
    }

    fun starSimple(b: Boolean) {
        mTimer = Timer()
        mTimer?.schedule(object : TimerTask() {
            override fun run() {
                source.begin(Random().nextInt(94))
            }
        }, 0, 10)
    }

    fun stopSimple(b: Boolean) {
        if (mTimer!=null){
            mTimer?.cancel()
            mTimer=null
        }
    }

}
