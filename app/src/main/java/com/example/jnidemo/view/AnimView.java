package com.example.jnidemo.view;

import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.jnidemo.R;
import com.example.jnidemo.bean.Circle;
import com.example.jnidemo.util.Util;

import java.util.ArrayList;
import java.util.List;

public class AnimView extends View {

    private static final String TAG = AnimView.class.getName();

    private static final int MAX_VALUE = 50;

    private Paint mPaint;
    private Paint mPointPaint;

    private List<List<Circle>> mCircleList;

    private int mMax = 0;

    private int mCurrentShowIndex = -1;
    private int mOvalTotalHeight = Util.dip2px(50);
    private int mOvalTotalWidth = Util.dip2px(200);
    private int mOvalWidth = Util.dip2px(60);
    private int mOvalGap = Util.dip2px(10);
    private int mOvalNumPerGroup = 4;
    private int mOvalGroupNum = 13;
    private int mAnimDuration = 500;
    private int mOvalColor = Color.RED;

    private Drawable mBackgroud;


    public AnimView(Context context) {
        this(context, null);
    }

    public AnimView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public AnimView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.AnimView);
        mOvalTotalHeight = typedArray.getDimensionPixelSize(R.styleable.AnimView_avOvalTotalHeight, mOvalTotalHeight);
        mOvalTotalWidth = typedArray.getDimensionPixelSize(R.styleable.AnimView_avOvalTotalWidth, mOvalTotalWidth);
        mOvalWidth = typedArray.getDimensionPixelSize(R.styleable.AnimView_avOvalWidth, mOvalWidth);
        mOvalGap = typedArray.getDimensionPixelSize(R.styleable.AnimView_avOvalGap, mOvalGap);
        mOvalNumPerGroup = typedArray.getInt(R.styleable.AnimView_avOvalNumPerGroup, mOvalNumPerGroup);
        mOvalGroupNum = typedArray.getInt(R.styleable.AnimView_avOvalGroupNum, mOvalGroupNum);
        mAnimDuration = typedArray.getInt(R.styleable.AnimView_avAnimDuration, mAnimDuration);
        mOvalColor = typedArray.getColor(R.styleable.AnimView_avOvalColor, mOvalColor);
        typedArray.recycle();

        if (getBackground() != null){
            mBackgroud = getBackground();
        }else{
            mBackgroud = getResources().getDrawable(R.mipmap.bg_hand_45);
        }
        mOvalTotalWidth = Math.min(mBackgroud.getIntrinsicWidth()/2, mOvalTotalWidth);

        mCircleList = new ArrayList<>(mOvalGroupNum);

        initPaint();

        mMax = Math.max(mOvalTotalHeight, mOvalWidth);

        initCircle();
    }

    private void initPaint() {
        mPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPaint.setColor(mOvalColor);
        mPaint.setTextSize(18);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(2);

        mPointPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        mPointPaint.setColor(mOvalColor);
        mPointPaint.setStyle(Paint.Style.FILL);
    }

    private void initCircle() {
        for (int i = 0; i < mOvalGroupNum; i++) {
            List<Circle> mOvalList = new ArrayList<>(mOvalNumPerGroup);
            for (int j = 0; j < mOvalNumPerGroup; j++) {
                mOvalList.add(new Circle());
            }
            mCircleList.add(mOvalList);
        }
    }

    private boolean isRunning = false;
    private boolean isStop = false;

    private void startAnim() {
        isStop = false;
    }

    public void stopAnim() {
        Log.d(TAG, " stopAnim = ");
        mCurrentShowIndex = -1;
    }

    public void addShow() {
        invalidate();
        mCurrentShowIndex++;
        mCurrentShowIndex %= mOvalGroupNum;
        if (mCurrentShowIndex >= 0) {
            List<Circle> circles = mCircleList.get(mCurrentShowIndex);
            circles.clear();
            circles.addAll(getCircleGroup((getWidth() - mOvalTotalWidth + mOvalWidth) / 2f + mCurrentShowIndex * mOvalTotalWidth * 1f / mOvalGroupNum));
            postDelayed(new OvalRunnable(circles), 0);
        }
    }

    private class OvalRunnable implements Runnable{

        private final List<Circle> ovalList;

        public OvalRunnable(List<Circle> ovalList) {
            this.ovalList = ovalList;
        }

        @Override
        public void run() {
            if (isStop){
                return;
            }
            int value = 20;
            Log.d(TAG, "run: ");
            // for (List<Circle> ovalList : mCircleList) {
                for (Circle circle : ovalList) {
                    if (circle.isCanShow()) {
                        if (circle.getWidth() - value <= 0) {
                            circle.setWidth(0);
                            circle.setHeight(0);
                            circle.setyPoint(finalYpoint);
                            circle.setCanShow(false);
                        }
                        else {
                            float diff = value * circle.getFixWidth() / mMax;
                            circle.setWidth(Math.max(circle.getWidth() - diff, 0));
                            circle.setHeight(circle.getWidth() / 4);
                            circle.setyPoint(Math.min(circle.getyPoint() + value * ((finalYpoint - circle.getStartYPoint()) * 1f / mMax),
                                    finalYpoint));
                        }
                        postInvalidate();
                    }
                }
            // }
            postDelayed(this, mAnimDuration);
        }
    }

    // private final Runnable mRunnable = new Runnable() {
    //     @Override
    //     public void run() {
    //         if (isStop){
    //             return;
    //         }
    //         int value = 20;
    //         Log.d(TAG, "run: ");
    //         for (List<Circle> ovalList : mCircleList) {
    //             for (Circle circle : ovalList) {
    //                 if (circle.isCanShow()) {
    //                     if (circle.getWidth() - value <= 0) {
    //                         circle.setWidth(0);
    //                         circle.setHeight(0);
    //                         circle.setyPoint(finalYpoint);
    //                         circle.setCanShow(false);
    //                     }
    //                     else {
    //                         float diff = value * circle.getFixWidth() / mMax;
    //                         circle.setWidth(Math.max(circle.getWidth() - diff, 0));
    //                         circle.setHeight(circle.getWidth() / 4);
    //                         circle.setyPoint(Math.min(circle.getyPoint() + value * ((finalYpoint - circle.getStartYPoint()) * 1f / mMax),
    //                                 finalYpoint));
    //                     }
    //                     postInvalidate();
    //                 }
    //             }
    //         }
    //         postDelayed(mRunnable, mAnimDuration);
    //     }
    // };

    private final Runnable mStopRunnable = new Runnable() {
        @Override
        public void run() {
            isStop = true;
            // removeCallbacks(mRunnable);
            isRunning = false;
        }
    };

    private float finalYpoint;

    private List<Circle> getCircleGroup(float xPoint) {
        List<Circle> circles = new ArrayList<>(mOvalNumPerGroup);
        if (mOvalNumPerGroup > 1) {
            float currentY = getHeight() / 3f;
            for (int j = 0; j < mOvalNumPerGroup; j++) {
                float width = mOvalWidth - j * (mOvalWidth * 1f / mOvalNumPerGroup);
                float height = width / 4;
                // 初始y = 前一个
                circles.add(new Circle(j, xPoint,
                        currentY + height / 2f,
                        currentY + height / 2f,
                        width,
                        width,
                        height, true));
                if (j == mOvalNumPerGroup - 1) {
                    finalYpoint = currentY + height;
                }
                else {
                    currentY = currentY + height + mOvalGap;
                }
                Log.d(TAG, " circle height = " + height);
            }
        }
        return circles;
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int width = MeasureSpec.getSize(widthMeasureSpec);
        int height = MeasureSpec.getSize(heightMeasureSpec);

        setMeasuredDimension(mBackgroud.getIntrinsicWidth(), mBackgroud.getIntrinsicHeight());
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        removeCallbacks(mStopRunnable);
        if (!isRunning) {
            isRunning = true;
            startAnim();
        }
        for (List<Circle> ovalList : mCircleList) {
            for (Circle circle : ovalList) {
                if (circle.isCanShow()) {
                    canvas.drawOval(circle.getxPoint() - circle.getWidth() / 2f,
                            circle.getyPoint() - circle.getHeight() / 2f,
                            circle.getxPoint() + circle.getWidth() / 2f,
                            circle.getyPoint() + circle.getHeight() / 2f, mPaint);
                }
            }
            // canvas.drawCircle(ovalList.get(0).getxPoint(), finalYpoint, 5, mPointPaint);
        }
        // 3秒无绘制,停止动画
        postDelayed(mStopRunnable, 3000);
    }


}
