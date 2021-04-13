package com.example.jnidemo.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.View.MeasureSpec;

import androidx.annotation.Nullable;

import com.example.jnidemo.R;

public class TemperatureView extends View {

    private static final String TAG = TemperatureView.class.getName();
    private int mRectWidth;
    private int mRectHeight;
    private Paint mRectPaint;
    private Paint mCirclePaint;
    private int color = Color.parseColor("#00000000");
    private boolean mReverse;

    public TemperatureView(Context context) {
        this(context, null);
    }

    public TemperatureView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public TemperatureView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initAttrs(context, attrs, defStyleAttr);
        initPaints();

    }

    private void initAttrs(Context context, AttributeSet attrs, int defStyleAttr) {
        TypedArray typedArray = context.getTheme().obtainStyledAttributes(attrs, R.styleable.TemperatureView, defStyleAttr, 0);

        mRectWidth = typedArray.getDimensionPixelSize(R.styleable.TemperatureView_rect_width, 10);
        mRectHeight = typedArray.getDimensionPixelSize(R.styleable.TemperatureView_rect_height, 10);
        mReverse = typedArray.getBoolean(R.styleable.TemperatureView_reverse, false);
        typedArray.recycle();

    }

    private void initPaints() {
        mRectPaint = new Paint();
        mRectPaint.setAntiAlias(true);
        mRectPaint.setColor(color);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);

        switch (widthMode) {
            case MeasureSpec.UNSPECIFIED:
            case MeasureSpec.AT_MOST:
                //矩形宽度
                widthSize = mRectWidth;
                break;
            default:
                break;
        }
        switch (heightMode) {
            case MeasureSpec.UNSPECIFIED:
            case MeasureSpec.AT_MOST:
                // 矩形高度 + 矩形宽度(半圆)
                heightSize = mRectHeight + mRectWidth;
                break;
            default:
                break;
        }
        setMeasuredDimension(widthSize, heightSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mReverse){
            canvas.drawCircle(mRectWidth / 2f, mRectWidth / 2f, mRectWidth/2f, mRectPaint);
            canvas.drawRect(0, mRectWidth / 2f, mRectWidth, mRectHeight + mRectWidth / 2f, mRectPaint);
        }else {
            canvas.drawRect(0, 0, mRectWidth, mRectHeight, mRectPaint);
            canvas.drawCircle(mRectWidth / 2f, mRectHeight, mRectWidth/2f, mRectPaint);
        }
    }

    /**
     * 设置画笔颜色
     * @param color
     */
    public void setColor(int color) {
        Log.i(TAG, "setColor: " + color);
        this.color = color;
        mRectPaint.setColor(color);
        invalidate();
    }
}
