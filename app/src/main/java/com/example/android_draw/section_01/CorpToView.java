package com.example.android_draw.section_01;
import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.pm.ApplicationInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DiscretePathEffect;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.Paint.Style;
import android.graphics.PointF;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.MotionEvent;
import android.widget.ImageView;
@SuppressLint("AppCompatCustomView")
public class CorpToView extends ImageView {
    /**
     * 按下的点不在裁剪框上
     */
    private static final int POS_OUT_OF_CLIP_RECT = -1;
    private static final int POS_TOP_LEFT = 0;
    private static final int POS_TOP_RIGHT = 1;
    private static final int POS_BOTTOM_LEFT = 2;
    private static final int POS_BOTTOM_RIGHT = 3;
    private static final int POS_TOP = 4;
    private static final int POS_BOTTOM = 5;
    private static final int POS_LEFT = 6;
    private static final int POS_RIGHT = 7;

    /**
     * 拖拽的是剪裁框里面
     */
    private static final int POS_CENTER = 8;
    private static final float BORDER_CORNER_LENGTH = 30f;
    private static final float TOUCH_FIELD = 10f;

    /**
     * 当前裁剪框是拖拽还是缩放
     */
    private int mood = NONE;

    /**
     * 裁剪框既不拖拽也不缩放
     */
    private static final int NONE = 0;
    /**
     * 拖拽裁剪框
     */
    private static final int DRAG = 1;
    /**
     * 缩放裁剪框
     */
    private static final int ZOOM = 2;

    private Matrix matrix = new Matrix();
    private Matrix currentMatrix = new Matrix();

    /**
     * 手指最开始按下时的点的坐标
     */
    private PointF startPoint = new PointF();
    private PointF lastPoint = new PointF();

    private PointF centerPointForZoom;

    private float twoFingerDistanceBeforeZoom;

    /**
     * 放置图片的容器View
     */
    private RectF viewRectF;

    /**
     * 要裁剪的原图的路径
     */
    private String mImagePath;

    /**
     * 存放图片的Bitmap，后面所有的图片的操作，都市通过这个对象
     */
    private Bitmap mBmpToCrop;

    /**
     * 绘制图片上面蒙版的画笔
     */
    private Paint maskLayoutPaint;

    /**
     * 裁剪框位置
     */
    private RectF clipRect;
    
    /**
     * 九宫图裁剪区外边框
     */
    private Paint clipRectBorderPaint;

    /**
     * 四个角小圆圈
     */
    private Paint clipRectCornerPaint;

    /**
     *裁剪框4条边框画笔
     */
    private Paint clipRectGuidelinePaint;

    private int touchPosition;

    /**
     * 是否点击了裁剪按钮
     */
    private boolean isClip = false;

    /**
     * 只画一次，初始化view中图片，裁剪框等的位置
     */
    private boolean isFirstDraw = true;

    public CorpToView(Context context) {
        super(context);
        init(context);
    }

    public CorpToView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    private void init(Context context) {
        Paint mBmpPaint;
        mBmpPaint = new Paint();
        // 防止边缘的锯齿
        mBmpPaint.setAntiAlias(true);
        // 对位图进行滤波处理
        mBmpPaint.setFilterBitmap(true);

        // 初始化裁剪框边框的画笔
        clipRectBorderPaint = new Paint();
        clipRectBorderPaint.setStyle(Style.STROKE);
        clipRectBorderPaint.setColor(Color.parseColor("#AA12ACBA"));//#AAFFFFFF
        clipRectBorderPaint.setStrokeWidth(6f);

        // 初始化裁剪框辅助线的画笔
        clipRectGuidelinePaint = new Paint();
        clipRectGuidelinePaint.setColor(Color.parseColor("#AA12ACBA"));//#AAFFFFFF
        clipRectGuidelinePaint.setStrokeWidth(1f);

        // 初始化裁剪框边框四个角圆圈的画笔
        clipRectCornerPaint = new Paint();
        clipRectCornerPaint.setColor(Color.parseColor("#AA12ACBA"));

        // 初始化整个View遮罩层的画笔
        maskLayoutPaint = new Paint();
        maskLayoutPaint.setColor(Color.parseColor("#AAFFFFFF"));
        maskLayoutPaint.setAlpha(150);

    }

    @Override
    protected void onDraw(Canvas canvas) {
        // 获取自定义view宽高，设置第一次绘图是裁剪框和图片在中心显示。
        // 如果是通过intent打开相册选择图片，这个地方需要自行修改，就不能使用这个判断了，需要把下面的几个方法放到其他的位置
        if(isFirstDraw) {
            setViewRectF();
            setClipRectDefaultPosition();
            setPictureDefaultPosition();
            isFirstDraw = false;
        }

        // 有图片才画
        if (mImagePath != null) {
            // 画原图
            canvas.drawBitmap(mBmpToCrop, matrix, null);
            if(!isClip) {
                // 画裁剪框
                canvas.drawRect(clipRect.left, clipRect.top, clipRect.right, clipRect.bottom, clipRectBorderPaint);
                // 画裁剪框4个角上的圆圈
                draw4RoundCorner(canvas);
                // 画裁剪框的辅助线
                drawGuideLines(canvas);
                // 画遮罩层
                drawMaskLayer(canvas);
            }
        }
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction() & MotionEvent.ACTION_MASK){
            // 手指按下
            case MotionEvent.ACTION_DOWN:
                mood = DRAG;

                // 手指按下的点
                startPoint.set(event.getX(), event.getY());
                lastPoint.set(event.getX(),event.getY());

                getParent().requestDisallowInterceptTouchEvent(true);
                touchPosition = detectTouchPosition(event.getX(), event.getY());

                if(touchPosition == POS_OUT_OF_CLIP_RECT ) {
                    currentMatrix.set(matrix);
                }

                break;
            // 手指进行移动
            case MotionEvent.ACTION_MOVE :
                if (mood == DRAG){
                    if(touchPosition == POS_OUT_OF_CLIP_RECT) {
                        moveImage(event);
                    } else {
                        moveOrChangeClipRect(event);
                    }
                }else if (mood == ZOOM){
                    float twoFingerDistanceAfterZoom = calculateFingersSlideDistance(event);
                    if (twoFingerDistanceAfterZoom > 10f) {
                        // 缩放比例
                        float scale = twoFingerDistanceAfterZoom / twoFingerDistanceBeforeZoom;

                        matrix.set(currentMatrix);
                        matrix.postScale(scale, scale, centerPointForZoom.x, centerPointForZoom.y);
                    }
                }

                lastPoint.set(event.getX(),event.getY());
                break;
            // 手指抬起
            case MotionEvent.ACTION_UP:
                mood = NONE;
                break;
            case MotionEvent.ACTION_POINTER_UP:
                mood = NONE;
                break;
            // 屏幕上已经有一根手指,再放下一根
            case MotionEvent.ACTION_POINTER_DOWN:
                mood = ZOOM;
                twoFingerDistanceBeforeZoom = calculateFingersSlideDistance(event);
                if (twoFingerDistanceBeforeZoom > 10f) {
                    centerPointForZoom = calculateCenterPointForZoom(event);
                    currentMatrix.set(matrix);
                }
                break;
        }
        // 调用 onDraw方法
        invalidate();
        // 这里一定要是return true 不然也是无效的
        return true;
    }
    int imWidth;
    int imHeigth;
    /**
     * 按比例缩放图片
     *
     * @param origin 原图
     * @param wratio  比例
     * @param  hratio 比例
     * @return 新的bitmap
     */
    private Bitmap scaleBitmap(Context context,Bitmap origin, float wratio,float hratio,int alpha) {
        if (origin == null) {
            return null;
        }
        int width = origin.getWidth();
        int height = origin.getHeight();

        int origWidth= px2dip(context,origin.getWidth());
        int origHeight=px2dip(context,origin.getHeight());
        imWidth=origWidth;
        imHeigth=origHeight;
        Matrix matrix = new Matrix();
        matrix.preScale(origWidth*1.0f/(width), origHeight*1.0f/(height));


        Bitmap newBM = Bitmap.createBitmap(origin, 0, 0, width, height, matrix, false);
        if (newBM.equals(origin)) {
            return newBM;
        }
        origin.recycle();

        return rotateBitmap(newBM,alpha);

    }
    /**
     * 选择变换
     *
     * @param origin 原图
     * @param alpha  旋转角度，可正可负
     * @return 旋转后的图片
     */
    private Bitmap rotateBitmap(Bitmap origin, float alpha) {
        if (origin == null) {
            return null;
        }
        int width = origin.getWidth();
        int height = origin.getHeight();
        Matrix matrix = new Matrix();
        matrix.setRotate(alpha);
        // 围绕原地进行旋转
        Bitmap newBM = Bitmap.createBitmap(origin, 0, 0, width, height, matrix, false);
        if (newBM.equals(origin)) {
            return newBM;
        }
        origin.recycle();
        return newBM;
    }

    /**
     * 在View中显示原图
     * @param picPath 原图的路径
     */
    public void showImage(Context context,ApplicationInfo applicationInfo, String picPath) {
        this.mImagePath = picPath;
        //本地和其手机内部的
        int resID = getResources().getIdentifier("path_imagw", "drawable", applicationInfo.packageName);
        DisplayMetrics displayMetrics= getResources().getDisplayMetrics();
        int height=displayMetrics.heightPixels;
        int width=displayMetrics.widthPixels;
        // mBmpToCrop =  BitmapFactory.decodeResource(getResources(), resID);//BitmapFactory.decodeFile(mImagePath);
        Bitmap bitmap=  BitmapFactory.decodeResource(getResources(), resID);
        int origWidth= px2dip(context,bitmap.getWidth());
        int origHeight=px2dip(context,bitmap.getHeight());
        mBmpToCrop = scaleBitmap(context,bitmap,1, 1,0);
//        if ((bitmap.getHeight()*1.5f<bitmap.getWidth())&&bitmap.getWidth()>width) {
//            mBmpToCrop = scaleBitmap(bitmap, ((width) / (origHeight * 1.0f)), width / (origHeight * 1.0f),90);
//        }else if(((bitmap.getHeight()*1.5f>=bitmap.getWidth())&&bitmap.getWidth()>width)||(bitmap.getWidth()*1.5f<bitmap.getHeight())){
//            mBmpToCrop = scaleBitmap(bitmap, ((width) / (origWidth * 1.0f)), width / (origWidth * 1.0f),0);
//        }else if(((bitmap.getHeight()*1.5f>=bitmap.getWidth())&&bitmap.getWidth()<width)||((bitmap.getWidth()*1.5f>=bitmap.getHeight())&&bitmap.getHeight()<height)){
//            mBmpToCrop = scaleBitmap(bitmap,1, 1,0);
//        }else{
//            mBmpToCrop = scaleBitmap(bitmap, ((width) / (origWidth * 1.0f)), width / (origWidth * 1.0f),0);
//
//        }
        invalidate();
    }
    public static int px2dip(Context context, float pxValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (pxValue / scale + 0.5f);
    }
    /**
     * 根据手机的分辨率从 dp 的单位 转成为 px(像素)
     */
    public static int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }
    /**
     * 让图片旋转90度
     */
    public void rotate90(){
        matrix.preRotate(90,mBmpToCrop.getWidth()/2,mBmpToCrop.getHeight()/2);
        invalidate();
    }

    private void setViewRectF() {
        viewRectF = new RectF();
        viewRectF.left = 0;
        viewRectF.top = 0;
        viewRectF.right =  getWidth();
        viewRectF.bottom = getHeight();
    }

    /**
     * 设置裁剪框默认的大小和在view上的位置
     */
    private void setClipRectDefaultPosition() {
        // 裁剪框默任宽高
        final float CLIP_RECT_WIDTH = imWidth;
        final float CLIP_RECT_HEIGHT = imHeigth;

        // 使裁剪框一开始出现在图片的中心位置
        clipRect = new RectF();
        clipRect.left = (viewRectF.width() - CLIP_RECT_WIDTH) / 2;
        clipRect.top = (viewRectF.height() - CLIP_RECT_HEIGHT) / 2;
        clipRect.right = clipRect.left + CLIP_RECT_WIDTH;;
        clipRect.bottom = clipRect.top + CLIP_RECT_HEIGHT;

    }
    /**
     * 显示原图时，设置图片显示在view中的位置
     */
    private void setPictureDefaultPosition () {
        float dx = 0;
        float dy = 0;

        if(mBmpToCrop.getWidth() < viewRectF.width()) {
            dx = (viewRectF.width() - mBmpToCrop.getWidth()) / 2;
        }

        if(mBmpToCrop.getHeight() < viewRectF.height()) {
            dy = (viewRectF.height() - mBmpToCrop.getHeight()) / 2;
        }

        // 平移缩放模式,相对于当前位置，移动多少距离
        matrix.postTranslate(dx,dy);

    }
    /**
     * 获取裁剪框区域的图片
     * @return
     */
    public Bitmap getClipRectImage() {
        isClip = true;
        destroyDrawingCache();
        setDrawingCacheEnabled(true);
        buildDrawingCache();
        Bitmap mBitmap  = getDrawingCache();
        isClip = false;
        return Bitmap.createBitmap(mBitmap, (int) clipRect.left, (int) clipRect.top, (int) clipRect.width(), (int) clipRect.height());
    }


    /**
     * 自测用，在自定义view中显示裁剪的图片，覆盖之前的图片。可以忽略此方法
     */
    public void showCilpRectImage() {

        isClip = true;
        destroyDrawingCache();
        setDrawingCacheEnabled(true);
        buildDrawingCache();
        Bitmap mBitmap  = getDrawingCache();

        mBmpToCrop =Bitmap.createBitmap(mBitmap, (int) clipRect.left, (int) clipRect.top, (int) clipRect.width(), (int) clipRect.height());
        matrix = new Matrix();
        isClip = false;
        invalidate();
    }

    /**
     * 拖拽图片是，移动图片
     * @param event
     */
    private void moveImage (MotionEvent event) {
        // 图片要移动的距离
        float dx = event.getX() - startPoint.x;
        float dy = event.getY() - startPoint.y;

        // 平移缩放模式,相对于当前位置，移动多少距离
        matrix.set(currentMatrix);
        matrix.postTranslate(dx,dy);
    }

    /**
     * 移动裁剪框或者改变裁剪框大小
     * @param event
     */
    private void moveOrChangeClipRect(MotionEvent event) {
        float deltaX = event.getX() - lastPoint.x;
        float deltaY = event.getY() - lastPoint.y;

        switch (touchPosition) {
            case POS_CENTER:
               moveWholeClipRect(deltaX,deltaY);
                break;
            case POS_TOP:
                changeClipRectTop(deltaY);
                break;
            case POS_BOTTOM:
                changeClipRectBottom(deltaY);
                break;
            case POS_LEFT:
                changeClipRectLeft(deltaX);
                break;
            case POS_RIGHT:
                changeClipRectRight(deltaX);
                break;
            case POS_TOP_LEFT:
                changeClipRectTop(deltaY);
                changeClipRectLeft(deltaX);
                break;
            case POS_TOP_RIGHT:
                changeClipRectTop(deltaY);
                changeClipRectRight(deltaX);
                break;
            case POS_BOTTOM_LEFT:
                changeClipRectBottom(deltaY);
                changeClipRectLeft(deltaX);
                break;
            case POS_BOTTOM_RIGHT:
                changeClipRectBottom(deltaY);
                changeClipRectRight(deltaX);
                break;
            default:

                break;
        }
    }

    /**
     * 获取缩放时的中心位置
     */
    private PointF calculateCenterPointForZoom(MotionEvent event) {
        float midx = event.getX(1) + event.getX(0);
        float midy = event.getY(1) + event.getY(0);
        return new PointF(midx/2,midy/2);
    }

    /**
     * 计算手指滑动距离
     * @param event
     * @return
     */
    private float calculateFingersSlideDistance(MotionEvent event) {
        float dx = event.getX(1) - event.getX(0);
        float dy = event.getY(1) - event.getY(0);
        return (float)Math.sqrt(dx * dx + dy * dy);
    }

    /**
     * 绘制剪切的框四周半透明的遮罩层
     * @param canvas
     */
    private void drawMaskLayer(Canvas canvas) {

        /*-
          -------------------------------------
          |              遮罩层top             |
          -------------------------------------
          |  遮  |                     |  遮   |
          |  罩  |                     |  罩   |
          |  层  |                     |  层   |
          | left |        可见层       | right |
          |      |                    |       |
          -------------------------------------
          |            遮罩层bottom            |
          -------------------------------------
         */

        PointF p1 = new PointF();
        PointF p2 = new PointF();

        // 上阴影
        p1.set(0,0);
        p2.set(viewRectF.width(),clipRect.top);
        canvas.drawRect(p1.x,p1.y,p2.x,p2.y, maskLayoutPaint);

        // 下阴影
        p1.set(0,clipRect.bottom);
        p2.set(viewRectF.width(),viewRectF.height());
        canvas.drawRect(p1.x,p1.y,p2.x,p2.y, maskLayoutPaint);

        // 左阴影
        p1.set(0,clipRect.top);
        p2.set(clipRect.left,clipRect.bottom);
        canvas.drawRect(p1.x,p1.y,p2.x,p2.y, maskLayoutPaint);

        //右阴影
        p1.set(clipRect.right,clipRect.top);
        p2.set(viewRectF.width(),clipRect.bottom);
        canvas.drawRect(p1.x,p1.y,p2.x,p2.y, maskLayoutPaint);
    }

    /**
     * 画裁剪区域中间的参考线
     * @param canvas
     */
    private void drawGuideLines(Canvas canvas) {
        // Draw vertical guidelines.
        final float oneThirdCropWidth = clipRect.width() / 3;

        final float x1 = clipRect.left + oneThirdCropWidth;
        canvas.drawLine(x1, clipRect.top, x1, clipRect.bottom, clipRectGuidelinePaint);
        final float x2 = clipRect.right - oneThirdCropWidth;
        canvas.drawLine(x2, clipRect.top, x2, clipRect.bottom, clipRectGuidelinePaint);

        // Draw horizontal guidelines.
        final float oneThirdCropHeight = clipRect.height() / 3;

        final float y1 = clipRect.top + oneThirdCropHeight;
        canvas.drawLine(clipRect.left, y1, clipRect.right, y1, clipRectGuidelinePaint);
        final float y2 = clipRect.bottom - oneThirdCropHeight;
        canvas.drawLine(clipRect.left, y2, clipRect.right, y2, clipRectGuidelinePaint);
    }

    /**
     * 裁剪框四个角的圆圈
     * @param canvas
     */
    private void draw4RoundCorner(Canvas canvas) {
        canvas.drawCircle(clipRect.left,clipRect.top,12,clipRectCornerPaint);
        canvas.drawCircle(clipRect.right,clipRect.top,12,clipRectCornerPaint);
        canvas.drawCircle(clipRect.left,clipRect.bottom,12,clipRectCornerPaint);
        canvas.drawCircle(clipRect.right,clipRect.bottom,12,clipRectCornerPaint);
    }

    /**
     * 检测按下触碰的点在裁剪框上的哪个位置上，或者没在裁剪框上
     * @param x
     * @param y
     * @return
     */
    private int detectTouchPosition(float x, float y) {
        if (x > clipRect.left + TOUCH_FIELD && x < clipRect.right - TOUCH_FIELD
                && y > clipRect.top + TOUCH_FIELD && y < clipRect.bottom - TOUCH_FIELD)
            return POS_CENTER;

        if (x > clipRect.left + BORDER_CORNER_LENGTH && x < clipRect.right - BORDER_CORNER_LENGTH) {
            if (y > clipRect.top - TOUCH_FIELD && y < clipRect.top + TOUCH_FIELD)
                return POS_TOP;
            if (y > clipRect.bottom - TOUCH_FIELD && y < clipRect.bottom + TOUCH_FIELD)
                return POS_BOTTOM;
        }

        if (y > clipRect.top + BORDER_CORNER_LENGTH && y < clipRect.bottom - BORDER_CORNER_LENGTH) {
            if (x > clipRect.left - TOUCH_FIELD && x < clipRect.left + TOUCH_FIELD)
                return POS_LEFT;
            if (x > clipRect.right - TOUCH_FIELD && x < clipRect.right + TOUCH_FIELD)
                return POS_RIGHT;
        }

        // 前面的逻辑已经排除掉了几种情况 所以后面的 ┏ ┓ ┗ ┛ 边角就按照所占区域的方形来判断就可以了
        if (x > clipRect.left - TOUCH_FIELD && x < clipRect.left + BORDER_CORNER_LENGTH) {
            if (y > clipRect.top - TOUCH_FIELD && y < clipRect.top + BORDER_CORNER_LENGTH)
                return POS_TOP_LEFT;
            if (y > clipRect.bottom - BORDER_CORNER_LENGTH && y < clipRect.bottom + TOUCH_FIELD)
                return POS_BOTTOM_LEFT;
        }

        if (x > clipRect.right - BORDER_CORNER_LENGTH && x < clipRect.right + TOUCH_FIELD) {
            if (y > clipRect.top - TOUCH_FIELD && y < clipRect.top + BORDER_CORNER_LENGTH)
                return POS_TOP_RIGHT;
            if (y > clipRect.bottom - BORDER_CORNER_LENGTH && y < clipRect.bottom + TOUCH_FIELD)
                return POS_BOTTOM_RIGHT;
        }

        return POS_OUT_OF_CLIP_RECT;
    }

    /**
     * 改变裁剪框左边框与view左边框的距离
     * @param delta
     */
    private void changeClipRectLeft(float delta) {
        clipRect.left += delta;
        preventClipRectLeftOverView();
    }

    /**
     * 不改变大小，移动裁剪框位置
     * @param deltaX
     * @param deltaY
     */
    private void moveWholeClipRect(float deltaX,float deltaY) {

        float width = clipRect.width();
        float height = clipRect.height();

        clipRect.left += deltaX;

        if (clipRect.left < viewRectF.left) {
            clipRect.left = viewRectF.left;
        }

        if (clipRect.left > viewRectF.right - clipRect.width()) {
            clipRect.left = viewRectF.right - clipRect.width();
        }

        clipRect.top += deltaY;
        if (clipRect.top < viewRectF.top) {
            clipRect.top = viewRectF.top;
        }

        if (clipRect.top > viewRectF.bottom - clipRect.height()) {
            clipRect.top = viewRectF.bottom - clipRect.height();
        }

        clipRect.right = clipRect.left + width;
        clipRect.bottom = clipRect.top + height;
    }

    /**
     * 改变裁剪框上边框与view上边框的距离
     * @param delta
     */
    private void changeClipRectTop(float delta) {
        clipRect.top += delta;
        preventClipRectTopOverView();
    }

    /**
     * 改变裁剪框右边框与view右边框的距离
     * @param delta
     */
    private void changeClipRectRight(float delta) {
        clipRect.right += delta;
        preventClipRectRightOverView();

    }

    /**
     * 改变裁剪框下边框与view下边框的距离
     * @param delta
     */
    private void changeClipRectBottom(float delta) {
        clipRect.bottom += delta;
        preventClipRectBottomOverView();
    }

    /**
     * 防止裁剪框左侧超出View
     */
    private void preventClipRectLeftOverView() {
        if (clipRect.left < viewRectF.left) {
            clipRect.left = viewRectF.left;
        }

        if (clipRect.width() < 2 * BORDER_CORNER_LENGTH) {
            clipRect.left = clipRect.right - 2 * BORDER_CORNER_LENGTH;
        }
    }

    /**
     * 防止裁剪框顶部超出View
     */
    private void preventClipRectTopOverView() {
        if (clipRect.top < viewRectF.top) {
            clipRect.top = viewRectF.top;
        }

        if (clipRect.height() < 2 * BORDER_CORNER_LENGTH) {
            clipRect.top = clipRect.bottom - 2 * BORDER_CORNER_LENGTH;
        }
    }

    /**
     * 防止裁剪框右侧超出View
     */
    private void preventClipRectRightOverView() {
        if (clipRect.right > viewRectF.right){
            clipRect.right = viewRectF.right;
        }

        if (clipRect.width() < 2 * BORDER_CORNER_LENGTH) {
            clipRect.right = clipRect.left + 2 * BORDER_CORNER_LENGTH;
        }
    }

    /**
     * 防止裁剪框底部超出View
     */
    private void preventClipRectBottomOverView() {
        if (clipRect.bottom > viewRectF.bottom) {
            clipRect.bottom = viewRectF.bottom;
        }

        if (clipRect.height() < 2 * BORDER_CORNER_LENGTH) {
            clipRect.bottom = clipRect.top + 2 * BORDER_CORNER_LENGTH;
        }
    }
}