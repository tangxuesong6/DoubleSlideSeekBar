package com.example.txs.doubleslideseekbar;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.MotionEvent;
import android.view.View;
import android.widget.TextView;

/**
 * @author txs
 * @date 2018/01/29
 */

public class DoubleSlideSeekBar extends View {
    /**
     * 线条（进度条）的宽度
     */
    private int lineWidth;
    /**
     * 线条（进度条）的长度
     */
    private int lineLength = 400;
    /**
     * 字所在的高度 100$
     */
    private int textHeight;
    /**
     * 游标 图片宽度
     */
    private int imageWidth;
    /**
     * 游标 图片高度
     */
    private int imageHeight;
    /**
     * 是否有刻度线
     */
    private boolean hasRule;
    /**
     * 左边的游标是否在动
     */
    private boolean isLowerMoving;
    /**
     * 右边的游标是否在动
     */
    private boolean isUpperMoving;
    /**
     * 字的大小 100$
     */
    private int textSize;
    /**
     * 字的颜色 100$
     */
    private int textColor;
    /**
     * 两个游标内部 线（进度条）的颜色
     */
    private int inColor = Color.BLUE;
    /**
     * 两个游标外部 线（进度条）的颜色
     */
    private int outColor = Color.BLUE;
    /**
     * 刻度的颜色
     */
    private int ruleColor = Color.BLUE;
    /**
     * 刻度上边的字 的颜色
     */
    private int ruleTextColor = Color.BLUE;
    /**
     * 左边图标的图片
     */
    private Bitmap bitmapLow;
    /**
     * 右边图标 的图片
     */
    private Bitmap bitmapBig;
    /**
     * 左边图标所在X轴的位置
     */
    private int slideLowX;
    /**
     * 右边图标所在X轴的位置
     */
    private int slideBigX;
    /**
     * 图标（游标） 高度
     */
    private int bitmapHeight;
    /**
     * 图标（游标） 宽度
     */
    private int bitmapWidth;
    /**
     * 加一些padding 大小酌情考虑 为了我们的自定义view可以显示完整
     */
    private int paddingLeft = 100;
    private int paddingRight = 100;
    private int paddingTop = 50;
    private int paddingBottom = 10;
    /**
     * 线（进度条） 开始的位置
     */
    private int lineStart = paddingLeft;
    /**
     * 线的Y轴位置
     */
    private int lineY;
    /**
     * 线（进度条）的结束位置
     */
    private int lineEnd = lineLength + paddingLeft;
    /**
     * 选择器的最大值
     */
    private int bigValue = 100;
    /**
     * 选择器的最小值
     */
    private int smallValue = 0;
    /**
     * 选择器的当前最小值
     */
    private float smallRange;
    /**
     * 选择器的当前最大值
     */
    private float bigRange;
    /**
     * 单位 元
     */
    private String unit = " ";
    /**
     * 单位份数
     */
    private int equal = 20;
    /**
     * 刻度单位 $
     */
    private String ruleUnit = " ";
    /**
     * 刻度上边文字的size
     */
    private int ruleTextSize = 20;
    /**
     * 刻度线的高度
     */
    private int ruleLineHeight = 20;
    private Paint linePaint;
    private Paint bitmapPaint;
    private Paint textPaint;
    private Paint paintRule;

    public DoubleSlideSeekBar(Context context) {
        this(context, null);
    }

    public DoubleSlideSeekBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DoubleSlideSeekBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.DoubleSlideSeekBar, defStyleAttr, 0);
        int size = typedArray.getIndexCount();
        for (int i = 0; i < size; i++) {
            int type = typedArray.getIndex(i);
            switch (type) {
                case R.styleable.DoubleSlideSeekBar_inColor:
                    inColor = typedArray.getColor(type, Color.BLACK);
                    break;
                case R.styleable.DoubleSlideSeekBar_lineHeight:
                    lineWidth = (int) typedArray.getDimension(type, dip2px(getContext(), 10));
                    break;
                case R.styleable.DoubleSlideSeekBar_outColor:
                    outColor = typedArray.getColor(type, Color.YELLOW);
                    break;
                case R.styleable.DoubleSlideSeekBar_textColor:
                    textColor = typedArray.getColor(type, Color.BLUE);
                    break;
                case R.styleable.DoubleSlideSeekBar_textSize:
                    textSize = typedArray.getDimensionPixelSize(type, (int) TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_SP, 16, getResources().getDisplayMetrics()));
                    break;
                case R.styleable.DoubleSlideSeekBar_imageLow:
                    bitmapLow = BitmapFactory.decodeResource(getResources(), typedArray.getResourceId(type, 0));
                    break;
                case R.styleable.DoubleSlideSeekBar_imageBig:
                    bitmapBig = BitmapFactory.decodeResource(getResources(), typedArray.getResourceId(type, 0));
                    break;
                case R.styleable.DoubleSlideSeekBar_imageheight:
                    imageHeight = (int) typedArray.getDimension(type, dip2px(getContext(), 20));
                    break;
                case R.styleable.DoubleSlideSeekBar_imagewidth:
                    imageWidth = (int) typedArray.getDimension(type, dip2px(getContext(), 20));
                    break;
                case R.styleable.DoubleSlideSeekBar_hasRule:
                    hasRule = typedArray.getBoolean(type, false);
                    break;
                case R.styleable.DoubleSlideSeekBar_ruleColor:
                    ruleColor = typedArray.getColor(type, Color.BLUE);
                    break;
                case R.styleable.DoubleSlideSeekBar_ruleTextColor:
                    ruleTextColor = typedArray.getColor(type, Color.BLUE);
                    break;
                case R.styleable.DoubleSlideSeekBar_unit:
                    unit = typedArray.getString(type);
                    break;
                case R.styleable.DoubleSlideSeekBar_equal:
                    equal = typedArray.getInt(type, 10);
                    break;
                case R.styleable.DoubleSlideSeekBar_ruleUnit:
                    ruleUnit = typedArray.getString(type);
                    break;
                case R.styleable.DoubleSlideSeekBar_ruleTextSize:
                    ruleTextSize = typedArray.getDimensionPixelSize(type, (int) TypedValue.applyDimension(
                            TypedValue.COMPLEX_UNIT_SP, 16, getResources().getDisplayMetrics()));
                    break;
                case R.styleable.DoubleSlideSeekBar_ruleLineHeight:
                    ruleLineHeight = (int) typedArray.getDimension(type, dip2px(getContext(), 10));
                    break;
                case R.styleable.DoubleSlideSeekBar_bigValue:
                    bigValue = typedArray.getInteger(type, 100);
                    break;
                case R.styleable.DoubleSlideSeekBar_smallValue:
                    smallValue = typedArray.getInteger(type, 100);
                    break;


                default:
                    break;
            }
        }
        typedArray.recycle();
        init();
    }

    private void init() {
        /**游标的默认图*/
        if (bitmapLow == null) {
            bitmapLow = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        }
        if (bitmapBig == null) {
            bitmapBig = BitmapFactory.decodeResource(getResources(), R.mipmap.ic_launcher);
        }
        /**游标图片的真实高度 之后通过缩放比例可以把图片设置成想要的大小*/
        bitmapHeight = bitmapLow.getHeight();
        bitmapWidth = bitmapLow.getWidth();
        // 设置想要的大小
        int newWidth = imageWidth;
        int newHeight = imageHeight;
        // 计算缩放比例
        float scaleWidth = ((float) newWidth) / bitmapWidth;
        float scaleHeight = ((float) newHeight) / bitmapHeight;
        Matrix matrix = new Matrix();
        matrix.postScale(scaleWidth, scaleHeight);
        /**缩放图片*/
        bitmapLow = Bitmap.createBitmap(bitmapLow, 0, 0, bitmapWidth, bitmapHeight, matrix, true);
        bitmapBig = Bitmap.createBitmap(bitmapBig, 0, 0, bitmapWidth, bitmapHeight, matrix, true);
        /**重新获取游标图片的宽高*/
        bitmapHeight = bitmapLow.getHeight();
        bitmapWidth = bitmapLow.getWidth();
        /**初始化两个游标的位置*/
        slideLowX = lineStart;
        slideBigX = lineEnd;
        smallRange = smallValue;
        bigRange = bigValue;
        if (hasRule) {
            //有刻度时 paddingTop 要加上（text高度）和（刻度线高度加刻度线上边文字的高度和） 之间的最大值
            paddingTop = paddingTop + Math.max(textSize, ruleLineHeight + ruleTextSize);
        } else {
            //没有刻度时 paddingTop 加上 text的高度
            paddingTop = paddingTop + textSize;
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int width = getMyMeasureWidth(widthMeasureSpec);
        int height = getMyMeasureHeight(heightMeasureSpec);
        setMeasuredDimension(width, height);
    }

    private int getMyMeasureHeight(int heightMeasureSpec) {
        int mode = MeasureSpec.getMode(heightMeasureSpec);
        int size = MeasureSpec.getSize(heightMeasureSpec);
        if (mode == MeasureSpec.EXACTLY) {
            // matchparent 或者 固定大小 view最小应为 paddingBottom + paddingTop + bitmapHeight + 10 否则显示不全
            size = Math.max(size, paddingBottom + paddingTop + bitmapHeight + 10);
        } else {
            //wrap content
            int height = paddingBottom + paddingTop + bitmapHeight + 10;
            size = Math.min(size, height);
        }
        return size;
    }

    private int getMyMeasureWidth(int widthMeasureSpec) {
        int mode = MeasureSpec.getMode(widthMeasureSpec);
        int size = MeasureSpec.getSize(widthMeasureSpec);
        if (mode == MeasureSpec.EXACTLY) {
            // match parent 或者 固定大小 此时可以获取线（进度条）的长度
            lineLength = size - paddingLeft - paddingRight - bitmapWidth;
            //线（进度条）的结束位置
            lineEnd = lineLength + paddingLeft + bitmapWidth / 2;
            //线（进度条）的开始位置
            lineStart = paddingLeft + bitmapWidth / 2;
            //初始化 游标位置
            slideBigX = lineEnd;
            slideLowX = lineStart;
            size = Math.max(size, paddingLeft + paddingRight + bitmapWidth * 2);

        } else {
            //wrap content
            int width = paddingLeft + paddingRight + bitmapWidth * 2;
            size = Math.min(size, width);
        }
        return size;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        // Y轴 坐标
        lineY = getHeight() - paddingBottom - bitmapHeight / 2;
        // 字所在高度 100$
        textHeight = lineY - bitmapHeight / 2 - 10;
        //是否画刻度
        if (hasRule) {
            drawRule(canvas);
        }
        if (linePaint == null) {
            linePaint = new Paint();
        }
        //画内部线
        linePaint.setAntiAlias(true);
        linePaint.setStrokeWidth(lineWidth);
        linePaint.setColor(inColor);
        linePaint.setStrokeCap(Paint.Cap.ROUND);
        canvas.drawLine(slideLowX, lineY, slideBigX, lineY, linePaint);
        linePaint.setColor(outColor);
        linePaint.setStrokeCap(Paint.Cap.ROUND);
        //画 外部线
        canvas.drawLine(lineStart, lineY, slideLowX, lineY, linePaint);
        canvas.drawLine(slideBigX, lineY, lineEnd, lineY, linePaint);
        //画游标
        if (bitmapPaint == null) {
            bitmapPaint = new Paint();
        }
        canvas.drawBitmap(bitmapLow, slideLowX - bitmapWidth / 2, lineY - bitmapHeight / 2, bitmapPaint);
        canvas.drawBitmap(bitmapBig, slideBigX - bitmapWidth / 2, lineY - bitmapHeight / 2, bitmapPaint);
        //画 游标上边的字
        if (textPaint == null) {
            textPaint = new Paint();
        }
        textPaint.setColor(textColor);
        textPaint.setTextSize(textSize);
        textPaint.setAntiAlias(true);
        canvas.drawText(String.format("%.0f" + unit, smallRange), slideLowX - bitmapWidth / 2, textHeight, textPaint);
        canvas.drawText(String.format("%.0f" + unit, bigRange), slideBigX - bitmapWidth / 2, textHeight, textPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        //事件机制
        super.onTouchEvent(event);
        float nowX = event.getX();
        float nowY = event.getY();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                //按下 在线（进度条）范围上
                boolean rightY = Math.abs(nowY - lineY) < bitmapHeight / 2;
                //按下 在左边游标上
                boolean lowSlide = Math.abs(nowX - slideLowX) < bitmapWidth / 2;
                //按下 在右边游标上
                boolean bigSlide = Math.abs(nowX - slideBigX) < bitmapWidth / 2;
                if (rightY && lowSlide) {
                    isLowerMoving = true;
                } else if (rightY && bigSlide) {
                    isUpperMoving = true;
                    //点击了游标外部 的线上
                } else if (nowX >= lineStart && nowX <= slideLowX - bitmapWidth / 2 && rightY) {
                    slideLowX = (int) nowX;
                    updateRange();
                    postInvalidate();
                } else if (nowX <= lineEnd && nowX >= slideBigX + bitmapWidth / 2 && rightY) {
                    slideBigX = (int) nowX;
                    updateRange();
                    postInvalidate();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                //左边游标是运动状态
                if (isLowerMoving) {
                    //当前 X坐标在线上 且在右边游标的左边
                    if (nowX <= slideBigX - bitmapWidth && nowX >= lineStart - bitmapWidth / 2) {
                        slideLowX = (int) nowX;
                        if (slideLowX < lineStart) {
                            slideLowX = lineStart;
                        }
                        //更新进度
                        updateRange();
                        postInvalidate();
                    }

                } else if (isUpperMoving) {
                    //当前 X坐标在线上 且在左边游标的右边
                    if (nowX >= slideLowX + bitmapWidth && nowX <= lineEnd + bitmapWidth / 2) {
                        slideBigX = (int) nowX;
                        if (slideBigX > lineEnd) {
                            slideBigX = lineEnd;
                        }
                        //更新进度
                        updateRange();
                        postInvalidate();

                    }
                }
                break;
            //手指抬起
            case MotionEvent.ACTION_UP:
                isUpperMoving = false;
                isLowerMoving = false;
                break;
            default:
                break;
        }

        return true;
    }

    private void updateRange() {
        //当前 左边游标数值
        smallRange = computRange(slideLowX);
        //当前 右边游标数值
        bigRange = computRange(slideBigX);
        //接口 实现值的传递
        if (onRangeListener != null) {
            onRangeListener.onRange(smallRange, bigRange);
        }
    }

    /**
     * 获取当前值
     */
    private float computRange(float range) {
        return (range - lineStart) * (bigValue - smallValue) / lineLength + smallValue;
    }

    public int dip2px(Context context, float dpValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dpValue * scale + 0.5f);
    }

    /**
     * 画刻度
     */
    protected void drawRule(Canvas canvas) {
        if (paintRule == null) {
            paintRule = new Paint();
        }
        paintRule.setStrokeWidth(1);
        paintRule.setTextSize(ruleTextSize);
        paintRule.setTextAlign(Paint.Align.CENTER);
        paintRule.setAntiAlias(true);
        //遍历 equal份,画刻度
        for (int i = smallValue; i <= bigValue; i += (bigValue - smallValue) / equal) {
            float degX = lineStart + i * lineLength / (bigValue - smallValue);
            int degY = lineY - ruleLineHeight;
            paintRule.setColor(ruleColor);
            canvas.drawLine(degX, lineY, degX, degY, paintRule);
            paintRule.setColor(ruleTextColor);
            canvas.drawText(String.valueOf(i) + ruleUnit, degX, degY, paintRule);
        }
    }

    /**
     * 写个接口 用来传递最大最小值
     */
    public interface onRangeListener {
        void onRange(float low, float big);
    }

    private onRangeListener onRangeListener;

    public void setOnRangeListener(DoubleSlideSeekBar.onRangeListener onRangeListener) {
        this.onRangeListener = onRangeListener;
    }
}
