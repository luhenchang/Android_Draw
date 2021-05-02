package com.example.android_draw.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.example.android_draw.R;

import static android.view.MotionEvent.ACTION_DOWN;

/**
 * ┌─────────────────────────────────────────────────────────────┐
 * │┌───┬───┬───┬───┬───┬───┬───┬───┬───┬───┬───┬───┬───┬───┬───┐│
 * ││Esc│!1 │@2 │#3 │$4 │%5 │^6 │&7 │*8 │(9 │)0 │_- │+= │|\ │`~ ││
 * │├───┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴───┤│
 * ││ Tab │ Q │ W │ E │ R │ T │ Y │ U │ I │ O │ P │{[ │}] │ BS  ││
 * │├─────┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴┬──┴─────┤│
 * ││ Ctrl │ A │ S │ D │ F │ G │ H │ J │ K │ L │: ;│" '│ Enter  ││
 * │├──────┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴─┬─┴────┬───┤│
 * ││ Shift  │ Z │ X │ C │ V │ B │ N │ M │< ,│> .│? /│Shift │Fn ││
 * │└─────┬──┴┬──┴──┬┴───┴───┴───┴───┴───┴──┬┴───┴┬──┴┬─────┴───┘│
 * │      │Fn │ Alt │         Space         │ Alt │Win│   HHKB   │
 * │      └───┴─────┴───────────────────────┴─────┴───┘          │
 * └─────────────────────────────────────────────────────────────┘
 * 版权：渤海新能 版权所有
 *
 * @author feiWang
 * 版本：1.5
 * 创建日期：4/5/21
 * 描述：Android_Draw
 * E-mail : 1276998208@qq.com
 * CSDN:https://blog.csdn.net/m0_37667770/article
 * GitHub:https://github.com/luhenchang
 */
public class LHC_MaskView extends View {
    private Paint paint;
    private Bitmap bitmap, bitmpa1;
    private float centerX;//圆心坐标
    private float centerY;
    private float radius;//圆半径
    private final int mDuration = 5000;
    private ValueAnimator animator;

    public LHC_MaskView(Context context) {
        this(context, null);
    }

    public LHC_MaskView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public LHC_MaskView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        paint = new Paint(Paint.ANTI_ALIAS_FLAG);//抗锯齿
        paint.setStyle(Paint.Style.FILL);//设置画笔样式为填充
        bitmap = BitmapFactory.decodeResource(context.getResources(), R.drawable.csmr);//获取被遮罩的图片
        bitmpa1 = BitmapFactory.decodeResource(context.getResources(), R.drawable.hbmr);//获取被遮罩的图片


        radius = 0;//没有水波纹动画的情况下设置为40 如果做动画 从0开始

        animator = ValueAnimator.ofFloat(0, (float) (Math.hypot(bitmap.getWidth(), bitmap.getHeight()) / 2));
        animator.setInterpolator(new LinearInterpolator());
        animator.setDuration(mDuration);
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                radius = (float) animation.getAnimatedValue();
                invalidate();
            }
        });


    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int width = metrics.widthPixels;
        int height = metrics.heightPixels;
        bitmap = Bitmap.createScaledBitmap(bitmap, width, height, false);
        bitmpa1 = Bitmap.createScaledBitmap(bitmpa1, width, height, false);
        setLayerType(View.LAYER_TYPE_SOFTWARE, null);//关闭硬件加速
        canvas.drawBitmap(bitmap, 0, 0, paint);//绘制图片
        int layerId = canvas.saveLayer(0, 0, getWidth(), getHeight(), paint, Canvas.ALL_SAVE_FLAG);//保存图层
        canvas.drawBitmap(bitmpa1, 0, 0, paint);
        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.DST_OUT));//PorterDuffXfermode 设置画笔的图形混合模式
        canvas.drawCircle(centerX, centerY, radius, paint);//画圆
        paint.setXfermode(null);
        canvas.restoreToCount(layerId);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case ACTION_DOWN: {
                centerX = event.getX();//设置圆心位置
                centerY = event.getY();
                animator.start();
            }
        }
        return true;
    }
}