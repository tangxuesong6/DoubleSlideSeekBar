package com.example.txs.doubleslideseekbar;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * @author txs
 * @date 2018/01/29
 */

public class RangeSeekBar extends View {


    private float lineWidth = 5.0f;
    private float textSize = 25.0f;

    private int inRangeColor = 0xffff0000;
    private int outRangeColor = 0xff777777;
    private int textColor = 0xff0000ff;

    private int textMarginBottom = 10;

    private int lowerCenterX;
    private int upperCenterX;

    private int bmpWidth;
    private int bmpHeight;

    private Bitmap lowerBmp;
    private Bitmap upperBmp;

    private Paint inRangePaint;
    private Paint outRangePaint;
    private Paint bmpPaint;
    private Paint textPaint;

    private boolean isLowerMoving = false;
    private boolean isUpperMoving = false;

    private OnRangeChangedListener onRangeChangedListener;

    private int paddingLeft = 50;
    private int paddingRight = 50;
    private int paddingTop = 50;
    private int paddingBottom = 10;

    private int lineHeight;
    private int lineLength = 400;
    private int lineStart = paddingLeft;
    private int lineEnd = lineLength + paddingLeft;

    private float smallValue = 0.0f;
    private float bigValue = 100.0f;

    private float smallRange = smallValue;
    private float bigRange = bigValue;

    private int textHeight;

    public RangeSeekBar(Context context) {
        super(context);
        init();
    }

    public RangeSeekBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RangeSeekBar(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        lowerBmp = BitmapFactory.decodeResource(getResources(),
                R.mipmap.ic_launcher);
        upperBmp = BitmapFactory.decodeResource(getResources(),
                R.mipmap.ic_launcher);
        bmpWidth = upperBmp.getWidth();
        bmpHeight = upperBmp.getHeight();

        lowerCenterX = lineStart;
        upperCenterX = lineEnd;

        lineHeight = getHeight() - paddingBottom - lowerBmp.getHeight() / 2;
        textHeight = lineHeight + lowerBmp.getHeight() / 2 + 10;

    }

    private void initPaint() {
        // 绘制范围内的线条
        inRangePaint = new Paint();
        inRangePaint.setAntiAlias(true);
        inRangePaint.setStrokeWidth(lineWidth);
        inRangePaint.setColor(inRangeColor);

        // 绘制范围外的线条
        outRangePaint = new Paint();
        outRangePaint.setAntiAlias(true);
        outRangePaint.setStrokeWidth(lineWidth);
        outRangePaint.setColor(outRangeColor);

        // 画图片滑块
        bmpPaint = new Paint();

        // 画范围文字
        textPaint = new Paint();
        textPaint.setColor(textColor);
        textPaint.setTextSize(textSize);
        textPaint.setAntiAlias(true);
        textPaint.setStrokeWidth(lineWidth);
    }

    private int measureWidth(int measureSpec) {
        int result = 0;

        int specMode = MeasureSpec.getMode(measureSpec);
        int specSize = MeasureSpec.getSize(measureSpec);

        if (specMode == MeasureSpec.EXACTLY) {
            result = specSize;
            lineLength = specSize - paddingLeft - paddingRight - bmpWidth;
            lineEnd = lineLength + paddingLeft + bmpWidth/2;
            lineStart = paddingLeft + bmpWidth/2;
            upperCenterX = lineEnd;
            lowerCenterX = lineStart;
        } else {
            result = paddingLeft + paddingRight + bmpWidth * 2;

            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }

        return result;
    }

    private int measureHeight(int measureHeight) {
        int result = 0;

        int specMode = MeasureSpec.getMode(measureHeight);
        int specSize = MeasureSpec.getSize(measureHeight);

        if (specMode == MeasureSpec.EXACTLY) {
            result = bmpHeight * 2;
        } else {
            result = bmpHeight + paddingTop;

            if (specMode == MeasureSpec.AT_MOST) {
                result = Math.min(result, specSize);
            }
        }

        return result;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        widthMeasureSpec = measureWidth(widthMeasureSpec);
        heightMeasureSpec = measureHeight(heightMeasureSpec);

        setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        bmpWidth = upperBmp.getWidth();
        bmpHeight = upperBmp.getHeight();

        lineHeight = getHeight() - paddingBottom - lowerBmp.getHeight() / 2;
        textHeight = lineHeight - bmpHeight / 2 - textMarginBottom;

        // 画线
        Paint linePaint = new Paint();
        linePaint.setAntiAlias(true);
        linePaint.setStrokeWidth(lineWidth);

        // 绘制处于图片滑块之间线段
        linePaint.setColor(inRangeColor);
        canvas.drawLine(lowerCenterX, lineHeight, upperCenterX, lineHeight,
                linePaint);

        // 绘制处于图片滑块两端的线段
        linePaint.setColor(outRangeColor);
        canvas.drawLine(lineStart, lineHeight, lowerCenterX, lineHeight,
                linePaint);
        canvas.drawLine(upperCenterX, lineHeight, lineEnd, lineHeight,
                linePaint);

        // 画图片滑块
        Paint bmpPaint = new Paint();
        canvas.drawBitmap(lowerBmp, lowerCenterX - bmpWidth / 2, lineHeight
                - bmpHeight / 2, bmpPaint);
        canvas.drawBitmap(lowerBmp, upperCenterX - bmpWidth / 2, lineHeight
                - bmpHeight / 2, bmpPaint);

        // 画范围文字
        Paint textPaint = new Paint();
        textPaint.setColor(textColor);
        textPaint.setTextSize(textSize);
        textPaint.setAntiAlias(true);
        textPaint.setStrokeWidth(lineWidth);
        canvas.drawText(String.format("￥%.0f", smallRange), lowerCenterX
                - bmpWidth / 2, textHeight, textPaint);
        canvas.drawText(String.format("￥%.0f", bigRange), upperCenterX
                - bmpWidth / 2, textHeight, textPaint);
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        super.onTouchEvent(event);

        float xPos = event.getX();
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                // 如果按下的位置在垂直方向没有与图片接触，则不会滑动滑块
                float yPos = event.getY();
                if (Math.abs(yPos - lineHeight) > bmpHeight / 2) {
                    return false;
                }

                // 表示当前按下的滑块是左边的滑块
                if (Math.abs(xPos - lowerCenterX) < bmpWidth / 2) {
                    isLowerMoving = true;
                }

                // //表示当前按下的滑块是右边的滑块
                if (Math.abs(xPos - upperCenterX) < bmpWidth / 2) {
                    isUpperMoving = true;
                }

                // 单击左边滑块的左边线条时，左边滑块滑动到对应的位置
                if (xPos >= lineStart && xPos <= lowerCenterX - bmpWidth / 2) {
                    lowerCenterX = (int) xPos;
                    updateRange();
                    postInvalidate();
                }

                // 单击右边滑块的右边线条时， 右边滑块滑动到对应的位置
                if (xPos <= lineEnd && xPos >= upperCenterX + bmpWidth / 2) {
                    upperCenterX = (int) xPos;
                    updateRange();
                    postInvalidate();
                }
                break;
            case MotionEvent.ACTION_MOVE:
                // 滑动左边滑块时
                if (isLowerMoving) {
                    if (xPos >= lineStart && xPos < upperCenterX - bmpWidth) {
                        lowerCenterX = (int) xPos;
                        updateRange();
                        postInvalidate();
                    }
                }

                // 滑动右边滑块时
                if (isUpperMoving) {
                    if (xPos > lowerCenterX + bmpWidth && xPos < lineEnd + bmpWidth/2) {
                        upperCenterX = (int) xPos;
                        if (upperCenterX >= lineEnd){
                            upperCenterX = lineEnd;
                        }
                        updateRange();
                        postInvalidate();
                    }
                }

                break;
            case MotionEvent.ACTION_UP:
                // 修改滑块的滑动状态为不再滑动
                isLowerMoving = false;
                isUpperMoving = false;
                break;
            default:
                break;
        }

        return true;
    }

    // 计算指定滑块对应的范围值
    private float computRange(float range) {
        return (range - lineStart) * (bigValue - smallValue) / lineLength
                + smallValue;
    }

    // 滑动滑块的过程中，更新滑块上方的范围标识
    private void updateRange() {
        smallRange = computRange(lowerCenterX);
        bigRange = computRange(upperCenterX);

        if (null != onRangeChangedListener) {
            onRangeChangedListener.onRangeChanged(smallRange, bigRange);
        }
    }

    // 注册滑块范围值改变事件的监听
    public void setOnRangeChangedListener(
            OnRangeChangedListener onRangeChangedListener) {
        this.onRangeChangedListener = onRangeChangedListener;
    }

    // 公共接口，用户回调接口范围值的改变
    public interface OnRangeChangedListener {

        public void onRangeChanged(float lowerRange, float upperRange);

    }

}