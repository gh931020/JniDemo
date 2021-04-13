package com.example.jnidemo.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.PointF;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.jnidemo.R;
import com.example.jnidemo.util.CommonUtils;

import java.text.DecimalFormat;

/**
 * @author : zhenjieZhang
 * @date : 2021/4/8
 * @apiNote : 弧形可拖动进度条
 **/
public class CircleProgressView extends View {
    private static final String TAG = CircleProgressView.class.getName();
    /**
     * 圆环的宽度
     */
    private int mCicleWidth;
    /**
     * 圆环扫过的角度
     */
    private int mSweepAngle;
    /**
     * 进度显示的颜色
     */
    private int mCicleSlideBgColor;
    /**
     * 圆环背景色
     */
    private int mCicleBgColor;
    /**
     * 圆环外圆的半径
     */
    private int mCircleRadius;
    /**
     * 最大进度值
     */
    private int mMaxProgress;
    /**
     * 最小进度值
     */
    private int mMinProgress;
    /**
     * 当前进度值
     */
    private int mCurrentProgress;
    /**
     * 滑块图标
     */
    private Drawable mThumb;
    /**
     * 滑块图标的尺寸
     */
    private int mThumbSize;
    /**
     * 进度值文本颜色
     */
    private int mTextColor;
    /**
     * 进度值文本字体大小
     */
    private int mTextSize;
    /**
     * 标题文本
     */
    private String mTitleText;
    /**
     * 标题文本颜色
     */
    private int mTitleTextColor;
    /**
     * 标题文本字体大小
     */
    private int mTitleTextSize;
    /**
     * 显示的文本内容,如温度,功率值
     */
    private String mDisplayText;
    /**
     * 显示的文本内容颜色
     */
    private int mDisplayTextColor;
    /**
     * 显示的文本字体大小
     */
    private int mDisplayTextSize;
    /**
     * 单位
     */
    private String mDisplayTextUnit;
    /**
     * 单位文本的大小
     */
    private int mDisplayTextUnitSize;

    private Bitmap mThumbBitmap;
    /**
     * 可触发滑动的范围
     */
    private int mSlideAbleLocation;
    /**
     * 保留1位小数
     */
    private DecimalFormat mDecimalFormat;
    /**
     * 当前进度对应的值
     */
    private double mCurrentValue;
    private Paint mThumbPaint;
    private Paint mCicleBgPaint;
    private Paint mCicleSlidePaint;
    private Paint mDisplayPaint;
    private Rect mDisplayRect;
    private Paint mTitlePaint;
    private Rect mTitleRect;
    private Paint mTextPaint;
    private Rect mTextRect;
    private Paint mDisplayUnitPaint;
    private Rect mDisplayUnitRect;
    private float mBeginLocation;
    /**
     * 每次增加或减少的数值
     */
    private double addOrReduce = 1;
    private PointF mThumbPoint;
    private Drawable mBackground;
    private Paint mBgPaint;
    private Bitmap mBackgroundBitmap;


    public CircleProgressView(Context context) {
        this(context, null);
    }

    public CircleProgressView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CircleProgressView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        initAttr(context, attrs, defStyleAttr);
        initPaint(context);

    }


    /**
     * 初始化属性值
     *
     * @param context
     * @param attrs
     * @param defStyleAttr
     */
    private void initAttr(Context context, AttributeSet attrs, int defStyleAttr) {
        //获取属性集
        TypedArray array = context.getTheme().obtainStyledAttributes(attrs, R.styleable.CircleProgressView, defStyleAttr, 0);
        //获取属性值
        mCicleWidth = array.getDimensionPixelSize(R.styleable.CircleProgressView_cicleWidth, 10);
        mSweepAngle = array.getInt(R.styleable.CircleProgressView_sweepAngle, 270);
        mBeginLocation = 360 - mSweepAngle + (mSweepAngle - 180) / 2f;
        mCicleSlideBgColor = array.getColor(R.styleable.CircleProgressView_cicleSlideBgColor, Color.parseColor("#32A9F1"));
        mCicleBgColor = array.getColor(R.styleable.CircleProgressView_cicleBgColor, Color.parseColor("#98A2B3"));
        mCircleRadius = array.getDimensionPixelSize(R.styleable.CircleProgressView_cicleRadius, 80);
        mMaxProgress = array.getInt(R.styleable.CircleProgressView_maxProgress, 100);
        mMinProgress = array.getInt(R.styleable.CircleProgressView_minProgress, 0);
        mCurrentProgress = array.getInt(R.styleable.CircleProgressView_currentProgress, 0);
        mThumb = array.getDrawable(R.styleable.CircleProgressView_thumb);
        if (mThumb == null) {
            mThumb = getResources().getDrawable(R.mipmap.isbthumb);
        }
        mThumbSize = array.getDimensionPixelSize(R.styleable.CircleProgressView_thumbSize, 35);
        //进度值文本属性
        mTextColor = array.getColor(R.styleable.CircleProgressView_textColor, Color.BLACK);
        mTextSize = array.getDimensionPixelSize(R.styleable.CircleProgressView_textSize, 18);
        //标题文本属性
        mTitleText = array.getString(R.styleable.CircleProgressView_titleText);
        if (TextUtils.isEmpty(mTitleText)) {
            mTitleText = "标题";
        }

        mTitleTextColor = array.getColor(R.styleable.CircleProgressView_titleTextColor, Color.BLACK);
        mTitleTextSize = array.getDimensionPixelSize(R.styleable.CircleProgressView_titleTextSize, 18);
        //显示内容文本属性
        mDisplayText = array.getString(R.styleable.CircleProgressView_displayText);
        if (TextUtils.isEmpty(mDisplayText)) {
            mDisplayText = "";
        }
        mDisplayTextColor = array.getColor(R.styleable.CircleProgressView_displayTextColor, Color.BLACK);
        mDisplayTextSize = array.getDimensionPixelSize(R.styleable.CircleProgressView_displayTextSize, 24);

        mDisplayTextUnit = array.getString(R.styleable.CircleProgressView_displayTextUnit);
        if (TextUtils.isEmpty(mDisplayTextUnit)) {
            mDisplayTextUnit = "";
        }
        mDisplayTextUnitSize = array.getDimensionPixelSize(R.styleable.CircleProgressView_displayTextUnitSize, 18);


        //回收工作
        array.recycle();
//        mBackgroundBitmap = CommonUtils.drawableToBitmap(mBackground);
        //根据尺寸设置滑块大小
        mThumbBitmap = CommonUtils.conversionBitmap(CommonUtils.drawableToBitmap(mThumb), mThumbSize, mThumbSize);
        mSlideAbleLocation = CommonUtils.dip2px(context, 30);

        mDecimalFormat = new DecimalFormat("#.0");
        mCurrentValue = getCurrentValue(mCurrentProgress);

    }

    /**
     * 初始化画笔
     *
     * @param context
     */
    private void initPaint(Context context) {

        //设置thumb画笔
        mBgPaint = new Paint();
        //设置防抖动
        mBgPaint.setDither(true);
        //对bitmap进行滤波处理
        mBgPaint.setFilterBitmap(true);
        //设置抗锯齿
        mBgPaint.setAntiAlias(true);

        //设置thumb画笔
        mThumbPaint = new Paint();
        //设置防抖动
        mThumbPaint.setDither(true);
        //对bitmap进行滤波处理
        mThumbPaint.setFilterBitmap(true);
        //设置抗锯齿
        mThumbPaint.setAntiAlias(true);

        //设置圆环画笔
        mCicleBgPaint = new Paint();
        mCicleBgPaint.setColor(mCicleBgColor);
        mCicleBgPaint.setAntiAlias(true);
        // 设置空心
        mCicleBgPaint.setStyle(Paint.Style.STROKE);
        // 线宽度，即环宽
        mCicleBgPaint.setStrokeWidth(mCicleWidth);
        // 圆形笔头
        mCicleBgPaint.setStrokeCap(Paint.Cap.ROUND);

        //设置进度cicle画笔
        mCicleSlidePaint = new Paint();
        mCicleSlidePaint.setAntiAlias(true);
        mCicleSlidePaint.setStyle(Paint.Style.STROKE);
        mCicleSlidePaint.setColor(mCicleSlideBgColor);
        mCicleSlidePaint.setStrokeWidth(mCicleWidth);
        mCicleSlidePaint.setStrokeCap(Paint.Cap.ROUND);

        //设置显示值paint
        mDisplayPaint = new Paint();
        mDisplayPaint.setColor(mDisplayTextColor);
        mDisplayPaint.setTypeface(Typeface.DEFAULT_BOLD);
        mDisplayPaint.setTextSize(mDisplayTextSize);
        mDisplayRect = new Rect();
        //获取最大进度值对应的value值作为测算文字边界的值
        String str = getCurrentValue(mMaxProgress) + "";
        mDisplayPaint.getTextBounds(str, 0, str.length(), mDisplayRect);

        //设置显示单位值paint
        mDisplayUnitPaint = new Paint();
        mDisplayUnitPaint.setColor(mDisplayTextColor);
        mDisplayUnitPaint.setTextSize(mDisplayTextUnitSize);
        mDisplayUnitRect = new Rect();
        mDisplayPaint.getTextBounds(mDisplayTextUnit, 0, mDisplayTextUnit.length(), mDisplayUnitRect);

        //设置titlePaint
        mTitlePaint = new Paint();
        mTitlePaint.setColor(mTitleTextColor);
        mTitlePaint.setTextSize(mTitleTextSize);
        mTitleRect = new Rect();
        mTitlePaint.getTextBounds(mTitleText, 0, mTitleText.length(), mTitleRect);

        //设置进度的最大,最小值文本paint
        mTextPaint = new Paint();
        mTextPaint.setColor(mTextColor);
        mTextPaint.setTextSize(mTextSize);
        mTextRect = new Rect();
        String progress = String.valueOf(mMaxProgress);
        mTextPaint.getTextBounds(progress, 0, progress.length(), mTextRect);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);
        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        switch (widthMode) {
            case MeasureSpec.UNSPECIFIED:
            case MeasureSpec.AT_MOST:
                //自适应宽度的时候,width = 圆环直径 + 左右内边距 + 两边进度文字的宽度
                widthSize = 2 * mCircleRadius
                        + getPaddingLeft() + getPaddingRight()
                        + 2 * mTextRect.width();
                break;
            default:
                break;
        }
        switch (heightMode) {
            case MeasureSpec.UNSPECIFIED:
            case MeasureSpec.AT_MOST:
                //自适应高度的时候,height = 圆环直径 + 上下内边距 + 两边进度文字的宽度
                heightSize = 2 * mCircleRadius
                        + getPaddingTop() + getPaddingBottom()
                        + 2 * mTextRect.width();
                break;
            default:
                break;
        }
        //设置测量后的值
        setMeasuredDimension(widthSize, heightSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        //圆环绘制的左边距
        int leftBlock = getPaddingLeft() + mTextRect.width();
        int topBlock = getPaddingTop() + mTextRect.width();

        //绘制背景圆环

        canvas.drawArc(leftBlock + mCicleWidth / 2,
                topBlock + mCicleWidth / 2f,
                leftBlock + mCircleRadius * 2 - mCicleWidth / 2f,
                topBlock + mCircleRadius * 2 - mCicleWidth / 2f,
                mBeginLocation,
                mSweepAngle,
                false,
                mCicleBgPaint);

        //绘制进度圆环
        canvas.drawArc(leftBlock + mCicleWidth / 2f,
                topBlock + mCicleWidth / 2f,
                leftBlock + mCircleRadius * 2 - mCicleWidth / 2f,
                topBlock + mCircleRadius * 2 - mCicleWidth / 2f,
                mBeginLocation,
                mCurrentProgress * mSweepAngle / 100f,
                false,
                mCicleSlidePaint);

        //绘制滑块
        mThumbPoint = CommonUtils.calcArcEndPointXY(mCircleRadius + leftBlock
                , mCircleRadius + topBlock
                , mCircleRadius - mCicleWidth / 2f
                , mCurrentProgress * mSweepAngle / 100f
                , mBeginLocation);
        //滑块的起点:计算坐标的x-滑块宽度的一半
        int left = (int) (mThumbPoint.x - mThumbBitmap.getWidth() / 2);
        int top = (int) (mThumbPoint.y - mThumbBitmap.getHeight() / 2);
        canvas.drawBitmap(mThumbBitmap, left, top, mThumbPaint);


        //绘制当前的显示值
        String value = String.valueOf((int) Math.floor(mCurrentValue));
        //重新获取文字边界
        mDisplayPaint.getTextBounds(value, 0, value.length(), mDisplayRect);
        canvas.drawText(value, leftBlock + mCircleRadius - mDisplayRect.width() / 2f,
                topBlock + mCircleRadius, mDisplayPaint);

        //绘制单位
        canvas.drawText(mDisplayTextUnit,
                leftBlock + mCircleRadius - mDisplayRect.width() / 2f + mDisplayRect.width(),
                topBlock + mCircleRadius, mDisplayUnitPaint);

        //绘制标题
        canvas.drawText(mTitleText,
                leftBlock + mCircleRadius - mTitleRect.width() / 2f,
                topBlock + mCircleRadius + 2 * mDisplayRect.height(),
                mTitlePaint);


        //绘制最大值,最小值
        //最小值
        String minValue = String.valueOf(mMinProgress);
        mTextPaint.getTextBounds(minValue, 0, minValue.length(), mTextRect);
        canvas.drawText(minValue,
                //左边距+半径 - 起始点到原点的X轴距离 - 圆环宽度 - 文字宽度
                (float) (getPaddingLeft() + mCircleRadius - mCircleRadius * Math.cos((mSweepAngle - 180) / 2f)) - mTextRect.width() - mCicleWidth,
                //半径+文字高度+ 内边距+半径 * 起始角度与x轴夹角的sin值
                (float) (mCircleRadius + mTextRect.height() + getPaddingTop() + mCircleRadius * Math.sin((mSweepAngle - 180) / 2f)),
                mTextPaint);

        //最大值
        String maxValue = String.valueOf(mMaxProgress);
        mTextPaint.getTextBounds(maxValue, 0, maxValue.length(), mTextRect);
        canvas.drawText(maxValue,
                (float) (leftBlock + mCicleWidth + mTextRect.width() + mCircleRadius + mCircleRadius * (1 - Math.cos((mSweepAngle - 180) / 2f))),
                //半径+文字高度+ 内边距+半径 * 起始角度与x轴夹角的sin值
                (float) (mCircleRadius + mTextRect.height() + getPaddingTop() + mCircleRadius * Math.sin((mSweepAngle - 180) / 2f)),
                mTextPaint);

    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        int action = event.getAction();
        int x = (int) event.getX();
        int y = (int) event.getY();

        switch (action) {
            case MotionEvent.ACTION_DOWN:
                if (isOnRing(x, y) && y <= 2 * mCircleRadius + getPaddingTop() + mTextRect.height()) {
                    updateProgress(x, y);
                    return true;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if (y <= 2 * mCircleRadius + getPaddingTop() + mTextRect.height()) {
                    updateProgress(x, y);
                }
                return true;
            case MotionEvent.ACTION_UP:
                invalidate();
                break;
            default:
                break;
        }
        return super.onTouchEvent(event);
    }

    /**
     * 根据当前点的位置求角度,再换算成当前的进度
     *
     * @param eventX X位置
     * @param eventY Y位置
     */
    private void updateProgress(int eventX, int eventY) {
        // atan2 方法返回一个弧度值，[-1,1]
        //X轴正方向为0,顺时针0~1, 逆时针0~-1
        //一周的弧度数为2πr/r=2π，360°角=2π弧度
        //得到的值是  x* PI
        double angle = Math.atan2(eventY - (mCircleRadius + getPaddingLeft() + mTextRect.width()),
                eventX - (mCircleRadius + getPaddingLeft() + mTextRect.width())) / Math.PI;
        Log.i(TAG, "updateProgress: first -->" + angle);
        //弧度 = (度数/180) * PI,前面angle已经将PI约去,因此这里只需要/180即可
        if (180f * angle >= mBeginLocation) {
            angle = (180f * angle - mBeginLocation) / mSweepAngle;
        } else {
            angle = (360 - (-180f * angle + mBeginLocation)) / mSweepAngle;
        }
        Log.i(TAG, "updateProgress: then -v->" + angle);
        int round = (int) Math.round(angle * 100);
        if (round >= 0 && round <= 100) {
            mCurrentProgress = round;
            mCurrentValue = getCurrentValue(mCurrentProgress);
            //限制最大值
            if (mCurrentValue > mMaxProgress) {
                mCurrentProgress = mMaxProgress;
            }
            Log.i(TAG, "mCurrentProgress: - - ->" + mCurrentProgress);
        }
        invalidate();
    }

    /**
     * 判断当前手指位置是否在可以滑动的范围内
     * 判断标准是是否在thumb的30dp矩形内范围
     *
     * @param eventX 手指按下的x坐标
     * @param eventY 手指按下的y坐标
     * @return boolean
     */
    private boolean isOnRing(int eventX, int eventY) {
        boolean result = false;
//        int currleft = getPaddingLeft();
//        double distance = Math.sqrt(Math.pow(eventX - (mCircleRadius + currleft), 2)
//                + Math.pow(eventY - (mCircleRadius + currleft), 2));
//        if (distance < (2 * mCircleRadius + currleft + getPaddingRight())
//        && distance > mCircleRadius - mSlideAbleLocation){
//            result = true;
//        }

        int minX = (int) (mThumbPoint.x - mSlideAbleLocation);
        int maxX = (int) (mThumbPoint.x + mSlideAbleLocation);
        int minY = (int) (mThumbPoint.y - mSlideAbleLocation);
        int maxY = (int) (mThumbPoint.y + mSlideAbleLocation);

        if (eventX >= minX && eventX <= maxX && eventY <= maxY && eventY >= minY) {
            result = true;
        }
        return result;
    }

    /**
     * 根据当前的进度获取当前的value值,因为进度和value不一定是一致的
     *
     * @param currentProgress 当前进度值
     */
    private double getCurrentValue(int currentProgress) {
        return Double.parseDouble(mDecimalFormat.format((mMaxProgress - mMinProgress) / 100.0 * currentProgress + mMinProgress));
    }

    /**
     * 定位到指定刻度。
     */
    public void setProgress(int p) {
        if (p <= mMaxProgress && p >= mMinProgress) {
            mCurrentValue = p;
            mCurrentProgress = (int) ((mCurrentValue - mMinProgress) * 100.0 / (mMaxProgress - mMinProgress));
        }
        invalidate();
    }

    /**
     * 点击增加刻度值。
     */
    public void addProgress() {
        if (mCurrentValue < mMaxProgress) {
            synchronized (CircleProgressView.class) {
                mCurrentValue = Double.parseDouble(mDecimalFormat.format(mCurrentValue + addOrReduce));
                mCurrentProgress = (int) ((mCurrentValue - mMinProgress) * 100.0 / (mMaxProgress - mMinProgress));
            }
            invalidate();
        }
    }

    /**
     * 点击减少刻度值。
     */
    public void reduceProgress() {
        if (mCurrentValue > mMinProgress) {
            synchronized (CircleProgressView.class) {
                mCurrentValue = Double.parseDouble(mDecimalFormat.format(mCurrentValue - addOrReduce));
                mCurrentProgress = (int) ((mCurrentValue - mMinProgress) * 100.0 / (mMaxProgress - mMinProgress));
            }
            invalidate();
        }
    }
}
