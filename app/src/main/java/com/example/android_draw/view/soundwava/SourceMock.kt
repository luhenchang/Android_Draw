package com.example.android_draw.view.soundwava

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
import java.util.*

class SourceMock : Source<Int> {
    companion object {
        private const val LIMIT = 60
    }

    private var listener: ((List<Int>) -> Unit)? = null

    private val queue = LinkedList<Int>()
    private val random = Random()

    private var state = true
    public fun setState(state:Boolean){
        this.state=state
    }

    override fun begin(data: Int) {
        val g = (data - 30)
        val voice = when {
            g > 30 -> {
                data * 1.2
            }
            g < 10 -> {
                15
            }
            else -> {
                data * 1.1
            }
        }.toInt()

        if (queue.size > LIMIT) {
            queue.clear()
        }
        for (index in 0 until LIMIT) {
           if(state){
               queue.add(random.nextInt(voice))
           }else{
               queue.add(5)
           }
        }
        if (queue.size == LIMIT) {
            listener?.invoke(queue.toList())
        }
    }

    init {
        if (queue.isEmpty()) {
            for (index in 0 until LIMIT) {
                queue.add(random.nextInt(30))
            }
        }
    }

    override fun close() {
    }

    override fun setOnDataFeedListener(listener: (List<Int>) -> Unit) {
        this.listener = listener
    }
}