package com.example.jnidemo.view;

import android.animation.ValueAnimator;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.ColorFilter;
import android.graphics.Paint;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.animation.LinearInterpolator;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.jnidemo.bean.Circle;

import java.util.ArrayList;
import java.util.List;

public class AnimDrawable extends Drawable {
    private static final String TAG = AnimDrawable.class.getName();
    private final ValueAnimator mValueAnimator;
    private Callback mCallback;
    private int currentValue = 0;
    private Paint mPaint;
    private Paint mPointPaint;

    private int mHeight;
    private int mWidth;

    private int mOvalNum;

    private int mGap;

    private int preValue;

    private List<Circle> mOvalList;

    private int mMax = 0;

    public AnimDrawable(int height, int width, int ovalNum, int gap, Callback callback) {
        this.mHeight = height;
        this.mWidth = width;
        this.mOvalNum = ovalNum;
        this.mGap = gap;
        this.mCallback = callback;


        mOvalList = new ArrayList<Circle>(ovalNum);

        setCallback(mCallback);

        initPaint();

        initCircleList();

        mMax = (int) Math.max(mHeight, mWidth);

        preValue = mMax;
        //引擎部分
        mValueAnimator = ValueAnimator.ofInt(mMax, 0);
        mValueAnimator.setDuration(1000);
        mValueAnimator.setInterpolator(new LinearInterpolator());
        mValueAnimator.setRepeatCount(ValueAnimator.INFINITE);
        // mValueAnimator.setRepeatCount(1);
        mValueAnimator.setRepeatMode(ValueAnimator.RESTART);
        mValueAnimator.addUpdateListener(animation -> {
            currentValue = (int) (animation.getAnimatedValue());
            Log.d(TAG, " currentValue = " + currentValue);
            // 根据max对椭圆进行显示和隐藏操作
            for (Circle circle : mOvalList) {
                int value = preValue - currentValue;
                Log.d(TAG, " value = " + value);
                if (value < 0) {
                    value = value + mMax;
                }
                if (circle.getWidth() - 2 * value <= 0) {
                    circle.setWidth(0);
                    circle.setHeight(0);
                    circle.setyPoint(mHeight);
                    circle.setCanShow(false);
                }
                else {
                    circle.setWidth(Math.max(circle.getWidth() - 2 * value, 0));
                    circle.setHeight(Math.max(circle.getHeight() - value, 0));
                    circle.setyPoint(Math.min(circle.getyPoint() + value * (mHeight * 1f / mMax), mHeight));
                }
                Log.d(TAG, circle.toString());
            }
            preValue = currentValue;
            invalidateSelf();
        });
        mValueAnimator.start();
    }

    private void initCircleList() {
        if (mOvalNum > 1) {
            int firstHeight = 2 * (mHeight - (mOvalNum - 1) * mGap) / mOvalNum;
            int diff = firstHeight / (mOvalNum - 1);
            float currentY = 0;
            for (int i = 0; i < mOvalNum; i++) {
                int height = firstHeight - i * diff;
                currentY = currentY + height;
                // 初始y = 前一个
                mOvalList.add(new Circle(i, mWidth / 2f,
                        currentY - height / 2f + i * mGap,
                        currentY - height / 2f + i * mGap,
                        (int) (mWidth - i * (mWidth * 1f / mOvalNum)),
                        (int) (mWidth - i * (mWidth * 1f / mOvalNum)),
                        height, true));
            }
        }
    }

    @Override
    public int getIntrinsicHeight() {
        return mWidth;
    }

    @Override
    public int getIntrinsicWidth() {
        return mHeight;
    }

    private void initPaint() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(Color.RED);
        mPaint.setStrokeWidth(2);
        mPaint.setStyle(Paint.Style.STROKE);

        mPointPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPointPaint.setColor(Color.RED);
        mPointPaint.setStyle(Paint.Style.FILL);
    }

    @Override
    public void draw(@NonNull Canvas canvas) {
        Log.d(TAG, " draw = " + currentValue);
        if (mOvalList.size() > 0) {
            for (Circle circle : mOvalList) {
                canvas.drawOval(circle.getxPoint() - circle.getWidth() / 2f,
                        circle.getyPoint() - circle.getHeight()/2f,
                        circle.getxPoint() + circle.getWidth()/2f,
                        circle.getyPoint() + circle.getHeight()/2f, mPaint);
                canvas.drawCircle(circle.getxPoint(), mHeight - 2, 5, mPointPaint);
            }
        }
    }

    @Override
    public void setAlpha(int alpha) {

    }

    @Override
    public void setColorFilter(@Nullable ColorFilter colorFilter) {

    }

    /**
     * Must be one of: PixelFormat.UNKNOWN, PixelFormat.TRANSLUCENT, PixelFormat.TRANSPARENT, PixelFormat.OPAQUE
     *
     * @return
     */
    @Override
    public int getOpacity() {
        //决定绘制的部分是否遮住Drawable下边的东西，有点抽象，有几种模式
        //PixelFormat.UNKNOWN
        //PixelFormat.TRANSLUCENT 只有绘制的地方才盖住下边
        //PixelFormat.TRANSPARENT 透明，不显示绘制内容
        //PixelFormat.OPAQUE 完全盖住下边内容
        return PixelFormat.TRANSLUCENT;
    }

    public void start() {
        mValueAnimator.start();
    }
}
