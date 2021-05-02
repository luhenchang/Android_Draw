package com.example.android_draw.view.paint;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

import javax.crypto.Mac;

/**
 * author : chong.huang
 * e-mail : chong.huang@ecarx.com.cn
 * create : 3/24/21
 * desc   :
 */
public class CanvasView extends View {

    List<Path> mPaths = new ArrayList<>();
    Path mPath;
    Paint mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);

    public CanvasView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);

        init();
    }

    private void init() {
        mPaint.setColor(Color.WHITE);
        mPaint.setStrokeWidth(2);
        mPaint.setStyle(Paint.Style.STROKE);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        for (int i = 0; i < mPaths.size(); i++) {
            Path path = mPaths.get(i);
            mPaint.setAlpha(writingDown(i, mPaths.size()));
            canvas.drawPath(path, mPaint);
        }
    }

    private int writingDown(int index, int count){
        if (count <= 3){
            return 255;
        }
        int alpha = 255 - (count - index);
        return Math.max(alpha, 0);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        float x = event.getX();
        float y = event.getY();

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mPath = new Path();
                mPath.moveTo(x, y);
                mPaths.add(mPath);


                break;
            case MotionEvent.ACTION_MOVE:
                mPath.lineTo(x, y);
                break;
            case MotionEvent.ACTION_UP:
                break;
        }

        invalidate();
        return true;
    }
}
