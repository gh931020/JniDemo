package com.example.jnidemo.view;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.BlurMaskFilter;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.os.SystemClock;
import android.text.TextPaint;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;

import com.example.jnidemo.R;

import java.text.NumberFormat;
import com.example.jnidemo.util.BaseBarInterface;

/**
 *
 */
public class ArcSeekBarPlus extends View implements BaseBarInterface {


    private static final String TAG = "ArcSeekBarPlus";
    /**
     * 画笔
     */
    private Paint mPaint;

    /**
     * 文本画笔
     */
    private TextPaint mTextPaint;

    /**
     * 笔画描边的宽度
     */
    private float mStrokeWidth;

    /**
     *
     */
    private Paint.Cap mStrokeCap = Paint.Cap.SQUARE;

    /**
     * 开始角度(默认从右边中间开始)
     */
    private int mStartAngle = 150;
    /**
     * 扫描角度(一个圆)
     */
    private float mSweepAngle = 240;

    /**
     * 圆心坐标x
     */
    private float mCircleCenterX;
    /**
     * 圆心坐标y
     */
    private float mCircleCenterY;

    /**
     * 弧形 正常颜色
     */
    private int mNormalColor = 0xFFC8C8C8;
    /**
     * 进度颜色
     */
    private int mProgressColor = 0xFF4FEAAC;

    /**
     * 是否使用着色器
     */
    private boolean isShader = true;

    /**
     * 着色器
     */
    private Shader mShader;

    /**
     * 着色器颜色
     */
    private int[] mShaderColors = new int[]{0xFF4FEAAC, 0xFFA8DD51, 0xFFE8D30F, 0xFFA8DD51, 0xFF4FEAAC};

    /**
     * 半径
     */
    private float mRadius;

    /**
     * 刻度与弧形的间距
     */
    private float mTickPadding;

    /**
     * 刻度间隔的角度大小
     */
    private float mTickSplitAngle = 5;

    /**
     * 刻度的角度大小
     */
    private float mBlockAngle = 1;

    /**
     * 总刻度数
     */
    private int mTotalTickCount;
    /**
     * 度数画笔宽度
     */
    private float mTickStrokeWidth;

    /**
     * 最大进度
     */
    private int mMax = 100;

    /**
     * 当前进度
     */
    private int mProgress = 0;

    /**
     * 动画持续的时间
     */
    private int mDuration = 500;

    /**
     * 标签内容
     */
    private String mLabelText;

    /**
     * 字体大小
     */
    private float mLabelTextSize;

    /**
     * 字体颜色
     */
    private int mLabelTextColor = 0xFF333333;

    /**
     * Label距离中心位置的内间距
     */
    private float mLabelPaddingTop;
    private float mLabelPaddingBottom;
    private float mLabelPaddingLeft;
    private float mLabelPaddingRight;
    /**
     * 进度百分比
     */
    private float mProgressPercent;

    /**
     * 是否显示标签文字
     */
    private boolean isShowLabel = true;
    /**
     * 是否默认显示百分比为标签文字
     */
    private boolean isShowValueText = true;
    /**
     * 是否显示刻度
     */
    private boolean isShowTick = false;

    /**
     * 拖动按钮的画笔宽度
     */
    private float mThumbStrokeWidth;
    /**
     * 拖动按钮的颜色,0xFFE8D30F
     */
    private int mThumbColor = 0x00000000;
    /**
     * 拖动按钮的半径
     */
    private float mThumbRadius;
    /**
     * 拖动按钮的中心点X坐标
     */
    private float mThumbCenterX;
    /**
     * 拖动按钮的中心点Y坐标
     */
    private float mThumbCenterY;
    /**
     * 触摸时可偏移距离
     */
    private float mAllowableOffsets;
    /**
     * 触摸时按钮半径放大量
     */
    private float mThumbRadiusEnlarges;

    /**
     * 是否显示拖动按钮
     */
    private boolean isShowThumb = true;

    /**
     * 手势，用来处理点击事件
     */
    private GestureDetector mDetector;

    /**
     * 是否可以拖拽
     */
    private boolean isCanDrag = false;

    /**
     * 是否启用拖拽改变进度
     */
    private boolean isEnabledDrag = true;

    /**
     * 是否启用点击改变进度
     */
    private boolean isEnabledSingle = true;

    private boolean isMeasureCircle = false;

    private OnChangeListener mOnChangeListener;
    /**
     * 是否有间隙
     */
    private boolean mHasGap = true;
    /**
     * 间隙个数
     */
    private int mGapNum = 12;
    /**
     * 间隙角度
     */
    private float mGapAngle = 15;
    /**
     * 是否显示最大值和最小值
     */
    private boolean isShowValue = true;
    /**
     * 显示的最小值
     */
    private int mMinValue = 4;
    /**
     * 显示的最大值
     */
    private int mMaxValue = 44;

    private Rect mTextRect;
    /**
     * 最大值和最小值文字大小
     */
    private float mValueTextSize;
    /**
     * 固定点占用的gap个数
     */
    private int mPointNum = 6;
    /**
     * 固定点处的值
     */
    private int mPointValue = 200;
    /**
     * 固定点所占用的角度
     */
    private float mPointAngle = 0;

    /**
     * 字体颜色
     */
    private int mValueTextColor = 0xFF3CDBF9;
    /**
     * 数值单位
     */
    private String mUnitText = "NULL";
    /**
     * 进制
     * 1: 无小数位;
     * 0.1: 一位小数
     * 0.01 两位小数
     */
    private float mDecimal = 1;

    private Paint mGlowPaint;
    private NumberFormat mNumberFormat;
    // private Typeface mTypeface;
    private String mValue;




    public ArcSeekBarPlus(Context context) {
        this(context, null);
    }

    public ArcSeekBarPlus(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ArcSeekBarPlus(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    /**
     * 初始化
     *
     * @param context
     * @param attrs
     */
    private void init(Context context, AttributeSet attrs) {
        Log.e(TAG, "time diff -> init start time 👉 " + SystemClock.uptimeMillis() );
        TypedArray a = context.obtainStyledAttributes(attrs, R.styleable.ArcSeekBarPlus);

        DisplayMetrics displayMetrics = getDisplayMetrics();
        mStrokeWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 12, displayMetrics);

        mLabelTextSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 30, displayMetrics);

        mValueTextSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 25, displayMetrics);

        mTickPadding = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, displayMetrics);

        mTickStrokeWidth = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, displayMetrics);

        mThumbRadius = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 8, displayMetrics);

        mThumbStrokeWidth = mThumbRadius;


        mAllowableOffsets = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 10, displayMetrics);

        mThumbRadiusEnlarges = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, displayMetrics);

        int size = a.getIndexCount();
        for (int i = 0; i < size; i++) {
            int attr = a.getIndex(i);
            if (attr == R.styleable.ArcSeekBarPlus_arcpStrokeWidth) {
                mStrokeWidth = a.getDimension(attr, mStrokeWidth);
            }
            else if (attr == R.styleable.ArcSeekBarPlus_arcpStrokeCap) {
                mStrokeCap = getStrokeCap(a.getInt(attr, 3));
            }
            else if (attr == R.styleable.ArcSeekBarPlus_arcpNormalColor) {
                mNormalColor = a.getColor(attr, mNormalColor);
            }
            else if (attr == R.styleable.ArcSeekBarPlus_arcpProgressColor) {
                mProgressColor = a.getColor(attr, mProgressColor);
                isShader = false;
            }
            else if (attr == R.styleable.ArcSeekBarPlus_arcpStartAngle) {
                mStartAngle = a.getInt(attr, mStartAngle);
            }
            else if (attr == R.styleable.ArcSeekBarPlus_arcpSweepAngle) {
                mSweepAngle = a.getFloat(attr, mSweepAngle);
            }
            else if (attr == R.styleable.ArcSeekBarPlus_arcpMax) {
                int max = a.getInt(attr, mMax);
                if (max > 0) {
                    mMax = max;
                }
            }
            else if (attr == R.styleable.ArcSeekBarPlus_arcpProgress) {
                mProgress = a.getInt(attr, mProgress);
            }
            else if (attr == R.styleable.ArcSeekBarPlus_arcpDuration) {
                mDuration = a.getInt(attr, mDuration);
            }
            else if (attr == R.styleable.ArcSeekBarPlus_arcpLabelText) {
                mLabelText = a.getString(attr);
            }
            else if (attr == R.styleable.ArcSeekBarPlus_arcpLabelTextSize) {
                mLabelTextSize = a.getDimension(attr, mLabelTextSize);
            }
            else if (attr == R.styleable.ArcSeekBarPlus_arcpLabelTextColor) {
                mLabelTextColor = a.getColor(attr, mLabelTextColor);
            }
            else if (attr == R.styleable.ArcSeekBarPlus_arcpLabelPaddingTop) {
                mLabelPaddingTop = a.getDimension(attr, 0);
            }
            else if (attr == R.styleable.ArcSeekBarPlus_arcpLabelPaddingBottom) {
                mLabelPaddingBottom = a.getDimension(attr, 0);
            }
            else if (attr == R.styleable.ArcSeekBarPlus_arcpLabelPaddingLeft) {
                mLabelPaddingLeft = a.getDimension(attr, 0);
            }
            else if (attr == R.styleable.ArcSeekBarPlus_arcpLabelPaddingRight) {
                mLabelPaddingRight = a.getDimension(attr, 0);
            }
            else if (attr == R.styleable.ArcSeekBarPlus_arcpShowLabel) {
                isShowLabel = a.getBoolean(attr, true);
            }
            else if (attr == R.styleable.ArcSeekBarPlus_arcpShowTick) {
                isShowTick = a.getBoolean(attr, true);
            }
            else if (attr == R.styleable.ArcSeekBarPlus_arcpTickStrokeWidth) {
                mTickStrokeWidth = a.getDimension(attr, mTickStrokeWidth);
            }
            else if (attr == R.styleable.ArcSeekBarPlus_arcpTickPadding) {
                mTickPadding = a.getDimension(attr, mTickPadding);
            }
            else if (attr == R.styleable.ArcSeekBarPlus_arcpTickSplitAngle) {
                mTickSplitAngle = a.getInt(attr, 5);
            }
            else if (attr == R.styleable.ArcSeekBarPlus_arcpBlockAngle) {
                mBlockAngle = a.getInt(attr, 1);
            }
            else if (attr == R.styleable.ArcSeekBarPlus_arcpThumbStrokeWidth) {
                mThumbStrokeWidth = a.getDimension(attr, mThumbStrokeWidth);
            }
            else if (attr == R.styleable.ArcSeekBarPlus_arcpThumbColor) {
                mThumbColor = a.getColor(attr, mThumbColor);
            }
            else if (attr == R.styleable.ArcSeekBarPlus_arcpThumbRadius) {
                mThumbRadius = a.getDimension(attr, mThumbRadius);
            }
            else if (attr == R.styleable.ArcSeekBarPlus_arcpThumbRadiusEnlarges) {
                mThumbRadiusEnlarges = a.getDimension(attr, mThumbRadiusEnlarges);
            }
            else if (attr == R.styleable.ArcSeekBarPlus_arcpShowThumb) {
                isShowThumb = a.getBoolean(attr, isShowThumb);
            }
            else if (attr == R.styleable.ArcSeekBarPlus_arcpAllowableOffsets) {
                mAllowableOffsets = a.getDimension(attr, mAllowableOffsets);
            }
            else if (attr == R.styleable.ArcSeekBarPlus_arcpEnabledDrag) {
                isEnabledDrag = a.getBoolean(attr, true);
            }
            else if (attr == R.styleable.ArcSeekBarPlus_arcpEnabledSingle) {
                isEnabledSingle = a.getBoolean(attr, true);
            }
            else if (attr == R.styleable.ArcSeekBarPlus_arcpHasGap) {
                mHasGap = a.getBoolean(attr, false);
            }
            else if (attr == R.styleable.ArcSeekBarPlus_arcpGapNum) {
                mGapNum = a.getInt(attr, mGapNum);
            }
            else if (attr == R.styleable.ArcSeekBarPlus_arcpGapAngle) {
                mGapAngle = a.getFloat(attr, mGapAngle);
            }
            else if (attr == R.styleable.ArcSeekBarPlus_arcpShowValue) {
                isShowValue = a.getBoolean(attr, false);
            }
            else if (attr == R.styleable.ArcSeekBarPlus_arcpMinValue) {
                mMinValue = a.getInt(attr, mMinValue);
            }
            else if (attr == R.styleable.ArcSeekBarPlus_arcpMaxValue) {
                mMaxValue = a.getInt(attr, mMaxValue);
            }
            else if (attr == R.styleable.ArcSeekBarPlus_arcpValueTextSize) {
                mValueTextSize = a.getDimension(attr, mValueTextSize);
            }
            else if (attr == R.styleable.ArcSeekBarPlus_arcpValueTextColor) {
                mValueTextColor = a.getColor(attr, mValueTextColor);
            }
            else if (attr == R.styleable.ArcSeekBarPlus_arcpUnitText) {
                String unitText = a.getString(attr);
                mUnitText = TextUtils.isEmpty(unitText) ? mUnitText: unitText;
            }
            else if (attr == R.styleable.ArcSeekBarPlus_arcpDecimal) {
                mDecimal = a.getFloat(attr, mDecimal);
            }
            else if (attr == R.styleable.ArcSeekBarPlus_arcpPointNum) {
                mPointNum = a.getInt(attr, mPointNum);
            }
            else if (attr == R.styleable.ArcSeekBarPlus_arcpPointValue) {
                mPointValue = a.getInt(attr, mPointValue);
            }
        }
        a.recycle();

        Log.e(TAG, "time diff -> recycle end time 👉 " + SystemClock.uptimeMillis() );
        isShowValueText = TextUtils.isEmpty(mLabelText);

        caulMax();


        /*
        计算固定点占用的角度
         */
        mPointAngle = mPointNum * 1f / (mGapNum + 1) * mSweepAngle;

        calcPercentByProgress();

        // mProgressPercent = (int) (mProgress * 100.0f / mMax);

        mNumberFormat = NumberFormat.getNumberInstance();
        mNumberFormat.setMaximumFractionDigits(getNumDecimal(mDecimal));

        mPaint = new Paint();

        mTextPaint = new TextPaint();

        mTotalTickCount = (int) (mSweepAngle / (mTickSplitAngle + mBlockAngle));

        mDetector = new GestureDetector(getContext(), new GestureDetector.SimpleOnGestureListener() {
            @Override
            public boolean onSingleTapUp(MotionEvent event) {

                if (isInArc(event.getX(), event.getY())) {
                    updateDragThumb(event.getX(), event.getY(), true);
                    if (mOnChangeListener != null) {
                        mOnChangeListener.onSingleTapUp();
                    }
                    return true;
                }

                return super.onSingleTapUp(event);
            }
        });

        mTextRect = new Rect();
        Log.e(TAG, "time diff -> createFromAsset start time 👉 " + SystemClock.uptimeMillis() );
        // mTypeface = getResources().getFont(R.font.sourcecn);
        // mTypeface = Typeface.createFromAsset(getContext().getAssets(),"fonts/sourcecn.otf");
        Log.e(TAG, "time diff -> createFromAsset end time 👉 " + SystemClock.uptimeMillis() );

        Log.e(TAG, "time diff -> init end time 👉 " + SystemClock.uptimeMillis() );
        // 设置边缘发光
        // initGlowPaint();
    }

    /**
     * 通过进度值计算进度百分比
     */
    private void calcPercentByProgress() {
        if (mProgress <= mPointValue){
            mProgressPercent = mProgress * 1.0f / mPointValue * mPointAngle / mSweepAngle;
        }else{
            mProgressPercent = (mPointAngle + (mProgress - mPointValue) * 1.0f / (mMaxValue - mPointValue) * (mSweepAngle - mPointAngle)) / mSweepAngle;
        }
        Log.e(TAG, "getProgressForAngle: mProgressPercent = " + mProgressPercent);
    }

    private void caulMax() {
        if (mMinValue >= 0 && mMaxValue >mMinValue){
            mMax = mMaxValue - mMinValue;
        }
    }

    private void initGlowPaint() {
        mGlowPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mGlowPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        // mGlowPaint.setTypeface(mTypeface);
        // 设置字体为粗体
        // mGlowPaint.setTypeface(Typeface.DEFAULT_BOLD);
        // 设置字符间距
        // mGlowPaint.setLetterSpacing(0.1f);
        mGlowPaint.setTextAlign(Paint.Align.CENTER);
        mGlowPaint.setTextSize(mLabelTextSize);
        mGlowPaint.setMaskFilter(new BlurMaskFilter(5, BlurMaskFilter.Blur.OUTER));
        mGlowPaint.setColor(0xcc909090);
    }

    private Paint.Cap getStrokeCap(int value) {
        switch (value) {
            case 1:
                return Paint.Cap.BUTT;
            case 2:
                return Paint.Cap.SQUARE;
            default:
                return Paint.Cap.ROUND;
        }
    }


    private DisplayMetrics getDisplayMetrics() {
        return getResources().getDisplayMetrics();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        Log.e(TAG, "time diff -> onMeasure start time 👉 " + SystemClock.uptimeMillis() );
        int defaultValue = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 200, getDisplayMetrics());

        int width = measureHandler(widthMeasureSpec, defaultValue);
        int height = measureHandler(heightMeasureSpec, defaultValue);

        //圆心坐标
        mCircleCenterX = (width + getPaddingLeft() - getPaddingRight()) / 2.0f;
        mCircleCenterY = (height + getPaddingTop() - getPaddingBottom()) / 2.0f;
        //计算间距
        int padding = Math.max(getPaddingLeft() + getPaddingRight(), getPaddingTop() + getPaddingBottom());
        //半径=视图宽度-横向或纵向内间距值 - 画笔宽度
        mRadius = (width - padding - Math.max(mStrokeWidth, mThumbStrokeWidth)) / 2.0f - mThumbRadius;

        //默认着色器
        mShader = new SweepGradient(mCircleCenterX, mCircleCenterX, mShaderColors, null);
        isMeasureCircle = true;

        setMeasuredDimension(width, height);
        Log.e(TAG, "time diff -> onMeasure end time 👉 " + SystemClock.uptimeMillis() );
    }

    /**
     * 测量
     *
     * @param measureSpec
     * @param defaultSize
     * @return
     */
    private int measureHandler(int measureSpec, int defaultSize) {

        int result = defaultSize;
        int measureMode = MeasureSpec.getMode(measureSpec);
        int measureSize = MeasureSpec.getSize(measureSpec);
        if (measureMode == MeasureSpec.EXACTLY) {
            result = measureSize;
        }
        else if (measureMode == MeasureSpec.AT_MOST) {
            result = Math.min(defaultSize, measureSize);
        }
        return result;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        Log.e(TAG, "time diff -> onDraw start time 👉 " + SystemClock.uptimeMillis() );
        drawArc(canvas);
        drawThumb(canvas);
        drawText(canvas);
        drawValue(canvas);
        Log.e(TAG, "time diff -> onDraw end time 👉 " + SystemClock.uptimeMillis() );
    }


    /**
     * 绘制弧形(默认为一个圆)
     *
     * @param canvas
     */
    private void drawArc(Canvas canvas) {
        Log.e(TAG, "time diff -> drawArc start time 👉 " + SystemClock.uptimeMillis() );
        mPaint.reset();
        mPaint.setAntiAlias(true);
        mPaint.setStyle(Paint.Style.STROKE);
        //是否显示刻度
        if (isShowTick) {
            mPaint.setStrokeWidth(mTickStrokeWidth);
            float circleRadius = mRadius - mTickPadding - mStrokeWidth;
            float tickDiameter = circleRadius * 2;
            float tickStartX = mCircleCenterX - circleRadius;
            float tickStartY = mCircleCenterY - circleRadius;
            RectF rectF = new RectF(tickStartX, tickStartY, tickStartX + tickDiameter, tickStartY + tickDiameter);

            final int currentBlockIndex = (int) (mProgressPercent / 100f * mTotalTickCount);

            for (int i = 0; i < mTotalTickCount; i++) {
                if (i < currentBlockIndex) {
                    //已选中的刻度
                    if (isShader && mShader != null) {
                        mPaint.setShader(mShader);
                    }
                    else {
                        mPaint.setColor(mProgressColor);
                    }
                    //绘制刻度
                    canvas.drawArc(rectF, i * (mBlockAngle + mTickSplitAngle) + mStartAngle, mBlockAngle, false, mPaint);
                }
                else {
                    if (mNormalColor != 0) {
                        //未选中的刻度
                        mPaint.setShader(null);
                        mPaint.setColor(mNormalColor);
                        //绘制刻度
                        canvas.drawArc(rectF, i * (mBlockAngle + mTickSplitAngle) + mStartAngle, mBlockAngle, false, mPaint);
                    }
                }
            }

        }

        mPaint.setStrokeWidth(mStrokeWidth);
        mPaint.setShader(null);
        mPaint.setStrokeCap(mStrokeCap);

        //进度圆半径
        float diameter = mRadius * 2;
        float startX = mCircleCenterX - mRadius;
        float startY = mCircleCenterY - mRadius;
        RectF rectF1 = new RectF(startX, startY, startX + diameter, startY + diameter);

        if (mNormalColor != 0) {
            mPaint.setColor(mNormalColor);
            if (mHasGap && mGapNum > 0 && mGapAngle > 0) {
                //计算每块背景的角度
                float singleAngle = (mSweepAngle - mGapNum * mGapAngle) / (mGapNum + 1);
                float tempStartAngle = 0;
                for (int i = 0; i <= mGapNum; i++) {
                    canvas.drawArc(rectF1, mStartAngle + tempStartAngle, singleAngle, false, mPaint);
                    tempStartAngle += (mGapAngle + singleAngle);
                }

            }
            else {
                //绘制底层弧形
                canvas.drawArc(rectF1, mStartAngle, mSweepAngle, false, mPaint);
            }
        }

        //着色器不为空则设置着色器，反之用纯色
        if (isShader && mShader != null) {
            mPaint.setShader(mShader);
        }
        else {
            mPaint.setColor(mProgressColor);
        }

        float ratio = getRatio();
        if (ratio != 0) {

            if (mHasGap && mGapNum > 0 && mGapAngle > 0) {
                //计算进度条的角度
                float singleAngle = (mSweepAngle - mGapNum * mGapAngle) / (mGapNum + 1);

                float sweepAngle = mSweepAngle * ratio;

                float tempStartAngle = 0;
                int count = (int) (sweepAngle / (singleAngle + mGapAngle));
                if ((sweepAngle % (singleAngle + mGapAngle)) > 0) {
                    count += 1;
                }

                for (int i = 0; i < count; i++) {
                    canvas.drawArc(rectF1, mStartAngle + tempStartAngle, singleAngle, false, mPaint);
                    tempStartAngle += (mGapAngle + singleAngle);
                }

            }
            else {
                //绘制当前进度弧形
                canvas.drawArc(rectF1, mStartAngle, mSweepAngle * ratio, false, mPaint);
            }
        }
        Log.e(TAG, "time diff -> drawArc end time 👉 " + SystemClock.uptimeMillis() );
    }

    private void drawThumb(Canvas canvas) {
        Log.e(TAG, "time diff -> drawThumb start time 👉 " + SystemClock.uptimeMillis() );
        if (isShowThumb) {
            mPaint.reset();
            mPaint.setAntiAlias(true);
            mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
            mPaint.setStrokeWidth(mThumbStrokeWidth);
            mPaint.setColor(mThumbColor);
            float thumbAngle = mStartAngle + mSweepAngle * getRatio();
            //已知圆心，半径，角度，求圆上的点坐标
            mThumbCenterX = (float) (mCircleCenterX + mRadius * Math.cos(Math.toRadians(thumbAngle)));
            mThumbCenterY = (float) (mCircleCenterY + mRadius * Math.sin(Math.toRadians(thumbAngle)));
            if (isCanDrag) {
                canvas.drawCircle(mThumbCenterX, mThumbCenterY, mThumbRadius + mThumbRadiusEnlarges, mPaint);
            }
            else {
                canvas.drawCircle(mThumbCenterX, mThumbCenterY, mThumbRadius, mPaint);
            }
        }
        Log.e(TAG, "time diff -> drawThumb end time 👉 " + SystemClock.uptimeMillis() );
    }

    /**
     * 绘制中间的文本
     *
     * @param canvas
     */
    private void drawText(Canvas canvas) {
        Log.e(TAG, "time diff -> drawText start time 👉 " + SystemClock.uptimeMillis() );
        if (!isShowLabel) {
            return;
        }
        mTextPaint.reset();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        // 设置字体为粗体
        // mTextPaint.setTypeface(Typeface.DEFAULT_BOLD);
        // 设置字符间距
        // mTextPaint.setLetterSpacing(0.1f);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTextPaint.setTextSize(mLabelTextSize);
        mTextPaint.setColor(mLabelTextColor);

        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        // 计算文字高度 
        float fontHeight = fontMetrics.bottom - fontMetrics.top;
        // 计算文字baseline 
        float textBaseX = getWidth() / 2f + mLabelPaddingLeft - mLabelPaddingRight;
        float textBaseY = getHeight() - (getHeight() - fontHeight) / 2f - fontMetrics.bottom + mLabelPaddingTop - mLabelPaddingBottom;
        //是否显示数值
        if (isShowValueText) {
            // 显示值大小, 当前值 * 进制位
            mValue = mNumberFormat.format(getCurrentValue() * mDecimal);
            mValue = mValue.replace(",", "");
            Log.e(TAG, "drawText: value = " + getCurrentValue());
            // canvas.drawText(value + mUnitText, textBaseX, textBaseY, mGlowPaint);
            mTextPaint.getTextBounds(mValue, 0, mValue.length(), mTextRect);
            Log.e(TAG, "current text width = " + mTextRect.width());
            // 根据value长度改变字体大小
            // if (mTextRect.width() > 150){
            //     mTextPaint.setTextSize(mLabelTextSize - 25);
            // }
            // else if (mTextRect.width() > 100 ){
            //     mTextPaint.setTextSize(mLabelTextSize - 10);
            // }
            // canvas.drawText(mValue,  textBaseX - 5, textBaseY, mTextPaint);
            // if (mTextRect.width() > 150 ){
            //     mTextPaint.setTextSize(mLabelTextSize - 10);
            // }
            canvas.drawText(mValue,  textBaseX, textBaseY, mTextPaint);

            // 绘制单位
            // mTextPaint.setTextSize(40);
            // 不同字母的width不一致,为了统一右边间隔,固定测量"W"
            // mTextPaint.getTextBounds("ss", 0, "ss".length(), mTextRect);
            // canvas.drawText(mUnitText, getWidth() - getPaddingEnd() - mTextRect.width()- mStrokeWidth, textBaseY, mTextPaint);
        }
        else if (!TextUtils.isEmpty(mLabelText)) {
            //显示自定义文本
            canvas.drawText(mLabelText, textBaseX, textBaseY, mTextPaint);
        }
        Log.e(TAG, "time diff -> drawText end time 👉 " + SystemClock.uptimeMillis() );
    }

    /**
     * 绘制最大值,最小值
     *
     * @param canvas
     */
    private void drawValue(Canvas canvas) {
        Log.e(TAG, "time diff -> drawValue start time 👉 " + SystemClock.uptimeMillis() );
        if (!isShowValue) {
            return;
        }
        mTextPaint.reset();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setStyle(Paint.Style.FILL);
        mTextPaint.setTextSize(mValueTextSize);
        mTextPaint.setColor(mValueTextColor);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        // Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        // 计算文字高度 
        // float fontHeight = fontMetrics.bottom - fontMetrics.top;

        // 绘制最小值
        // 计算最小值x.y位置  String.valueOf((int)mMinValue)
        String minValue = mNumberFormat.format(mMinValue * mDecimal);
        String minValueMeasure = "0.0";
        mTextPaint.getTextBounds(minValueMeasure, 0, minValueMeasure.length(), mTextRect);
        float textMinBaseX =
                (float) (getWidth() / 2f + mRadius * Math.cos(Math.toRadians(mStartAngle)) + mStrokeWidth);
        float textMinBaseY =
                (float) (getHeight()/2f + mRadius * Math.sin(Math.toRadians(mStartAngle)) + mTextRect.height() * 1.5f);
        canvas.drawText(minValue, textMinBaseX, textMinBaseY, mTextPaint);
        // 绘制最大值
        // String maxValue = DecimalFormat.getIntegerInstance().format(mMaxValue);
        String maxValue = mNumberFormat.format(mMaxValue * mDecimal);
        maxValue = maxValue.replace(",", "");

        String maxValueMeasure = "00.0";
        mTextPaint.getTextBounds(maxValueMeasure, 0, maxValueMeasure.length(), mTextRect);
        float textMaxBaseX =
                (float) (getWidth()/2f + mRadius * Math.cos(Math.toRadians(mStartAngle + mSweepAngle - 360))) - mTextRect.width() * 0.5f;
        // float textMaxBaseY =
        //         (float) (getHeight()/2 - mRadius * Math.sin(mStartAngle + mSweepAngle - 360) + mTextRect.height());
        canvas.drawText(maxValue, textMaxBaseX, textMinBaseY, mTextPaint);
        Log.e(TAG, "time diff -> drawValue end time 👉 " + SystemClock.uptimeMillis() );
    }

    @Override
    public boolean onTouchEvent(MotionEvent event) {

        if (isEnabledDrag) {
            switch (event.getAction()) {
                case MotionEvent.ACTION_DOWN:
                    checkCanDrag(event.getX(), event.getY());
                    break;
                case MotionEvent.ACTION_MOVE:
                    if (isCanDrag) {
                        updateDragThumb(event.getX(), event.getY(), false);
                    }
                    break;
                case MotionEvent.ACTION_UP:
                case MotionEvent.ACTION_CANCEL:
                    getParent().requestDisallowInterceptTouchEvent(false);
                    if (mOnChangeListener != null) {
                        mOnChangeListener.onStopTrackingTouch(isCanDrag);
                    }
                    isCanDrag = false;
                    invalidate();
                    break;
                default:
                    break;
            }
        }

        if (isEnabledSingle) {
            mDetector.onTouchEvent(event);
        }

        return isEnabledSingle || isEnabledDrag || super.onTouchEvent(event);
    }

    /**
     * 判断坐标点是否在弧形上
     *
     * @param x
     * @param y
     * @return
     */
    private boolean isInArc(float x, float y) {
        float distance = getDistance(mCircleCenterX, mCircleCenterY, x, y);
        if (Math.abs(distance - mRadius) <= mStrokeWidth / 2f + mAllowableOffsets) {
            if (mSweepAngle < 360) {
                float angle = (getTouchDegrees(x, y) + mStartAngle) % 360;
                if (mStartAngle + mSweepAngle <= 360) {
                    return angle >= mStartAngle && angle <= mStartAngle + mSweepAngle;
                }
                else {
                    return angle >= mStartAngle || angle <= (mStartAngle + mSweepAngle) % 360;
                }
            }

            return true;

        }
        return false;
    }

    /**
     * 获取两点间距离
     *
     * @param x1
     * @param y1
     * @param x2
     * @param y2
     * @return
     */
    private float getDistance(float x1, float y1, float x2, float y2) {
        return (float) Math.sqrt(Math.pow(x1 - x2, 2) + Math.pow(y1 - y2, 2));
    }

    /**
     * 更新拖拽进度
     *
     * @param x
     * @param y
     * @param isSingle
     */
    private void updateDragThumb(float x, float y, boolean isSingle) {
        Log.e(TAG, "time diff -> updateDragThumb start time 👉 " + SystemClock.uptimeMillis() );
        int progress = getProgressForAngle(getTouchDegrees(x, y));
        if (!isSingle) {
            int tempProgressPercent = (int) (progress * 100.0f / mMax);
            //当滑动至至边界值时，增加进度校准机制
            if (mProgressPercent < 0.1 && tempProgressPercent > 90) {
                progress = 0;
            }
            else if (mProgressPercent > 0.9 && tempProgressPercent < 10) {
                progress = mMax;
            }
            // int progressPercent = (int) (progress * 100.0f / mMax);
            // //拖动进度突变不允许超过30%
            // if (Math.abs(progressPercent - mProgressPercent) > 30) {
            //     return;
            // }
            //
        }

        setProgress(progress, true);
        Log.e(TAG, "time diff -> updateDragThumb end time 👉 " + SystemClock.uptimeMillis() );
    }

    /**s
     * 通过弧度换算得到当前进度
     *
     * @param angle
     * @return     */
    private int getProgressForAngle(float angle) {
        mProgressPercent = angle / mSweepAngle;
        Log.e(TAG, "getProgressForAngle: angle = " + angle);
        Log.e(TAG, "getProgressForAngle: mPointAngle = " + mPointAngle);
        Log.e(TAG, "getProgressForAngle: mProgressPercent = " + mProgressPercent);
        if (angle <= mPointAngle){
            return Math.round(mPointValue / mPointAngle * angle);
        }else{
            return mPointValue + Math.round((mMax - mPointValue) / (mSweepAngle - mPointAngle) * (angle - mPointAngle));
        }
    }

    /**
     * 获取触摸坐标的夹角度数
     *
     * @param x
     * @param y
     * @return
     */
    private float getTouchDegrees(float x, float y) {
        float x1 = x - mCircleCenterX;
        float y1 = y - mCircleCenterY;
        //求触摸点弧形的夹角度数
        float angle = (float) (Math.atan2(y1, x1) * 180 / Math.PI);
        angle -= mStartAngle;
        while (angle < 0) {
            angle += 360;
        }
        return angle;
    }

    /**
     * 检测是否可拖拽
     *
     * @param x
     * @param y
     */
    private void checkCanDrag(float x, float y) {
        float distance = getDistance(mThumbCenterX, mThumbCenterY, x, y);
        isCanDrag = distance <= mThumbRadius + mAllowableOffsets;
        if (mOnChangeListener != null) {
            mOnChangeListener.onStartTrackingTouch(isCanDrag);
        }
        invalidate();
    }

    /**
     * 显示进度动画效果（根据当前已有进度开始）
     *
     * @param progress
     */
    public void showAppendAnimation(int progress) {
        showAnimation(mProgress, progress, mDuration);
    }

    /**
     * 显示进度动画效果
     *
     * @param progress
     */
    public void showAnimation(int progress) {
        showAnimation(progress, mDuration);
    }

    /**
     * 显示进度动画效果
     *
     * @param progress
     * @param duration 动画时长
     */
    public void showAnimation(int progress, int duration) {
        showAnimation(0, progress, duration);
    }

    /**
     * 显示进度动画效果，从from到to变化
     *
     * @param from
     * @param to
     * @param duration 动画时长
     */
    public void showAnimation(int from, int to, int duration) {
        showAnimation(from, to, duration, null);
    }

    /**
     * 显示进度动画效果，从from到to变化
     *
     * @param from
     * @param to
     * @param duration 动画时长
     * @param listener
     */
    public void showAnimation(int from, int to, int duration, Animator.AnimatorListener listener) {
        Log.e(TAG, "time diff -> showAnimation start time 👉 " + SystemClock.uptimeMillis() );
        this.mDuration = duration;
        this.mProgress = from;
        ValueAnimator valueAnimator = ValueAnimator.ofFloat(from, to);
        valueAnimator.setDuration(duration);

        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                setProgress((int) animation.getAnimatedValue());
            }
        });

        if (listener != null) {
            valueAnimator.removeAllUpdateListeners();
            valueAnimator.addListener(listener);
        }

        valueAnimator.start();
        Log.e(TAG, "time diff -> showAnimation end time 👉 " + SystemClock.uptimeMillis() );
    }

    /**
     * 进度比例
     *
     * @return
     */
    private float getRatio() {
        return mProgressPercent;
        // return mProgress * 1f / mMax;
    }

    /**
     * 设置最大进度
     *
     * @param max
     */
    @Override
    public void setMax(int max) {
        if (max > 0) {
            this.mMax = max;

            invalidate();
        }
    }

    /**
     * 设置当前进度
     *
     * @param progress
     */
    public void setProgress(int progress) {
        setProgress(progress, false);
    }

    private void setProgress(int progress, boolean fromUser) {
        if (progress < 0) {
            progress = 0;
        }
        else if (progress > mMax) {
            progress = (int) mMax;
        }
        this.mProgress = progress;
        if (!fromUser){
            calcPercentByProgress();
            // mProgressPercent = (int) (mProgress * 100.0f / mMax);
        }
        Log.e(TAG, "setProgress: mProgressPercent = " + mProgressPercent);
        Log.e(TAG, "setProgress: getCurrentValue() = " + getCurrentValue());
        // //  如果当前progress不等于0,因为float类型可能为很小的小数,强制设置为0
        // if (progress != 0 && getCurrentValue() == mMinValue){
        //     this.mProgress = 0;
        //     mProgressPercent = 0;
        // }
        // if (!new BigDecimal(progress).equals(new BigDecimal(mMax)) && getCurrentValue() == mMaxValue){
        //     this.mProgress = mMax;
        //     mProgressPercent = 100;
        // }
        invalidate();

        if (mOnChangeListener != null) {
            mOnChangeListener.onProgressChanged(mProgress, mMax, getCurrentValue(), fromUser);
        }
    }

    /**
     * 设置正常颜色
     *
     * @param color
     */
    public void setNormalColor(int color) {
        this.mNormalColor = color;
        invalidate();
    }


    /**
     * 设置着色器
     *
     * @param shader
     */
    public void setShader(Shader shader) {
        isShader = true;
        this.mShader = shader;
        invalidate();
    }

    /**
     * 设置进度颜色（通过着色器实现渐变色）
     *
     * @param colors
     */
    public void setProgressColor(int... colors) {
        if (isMeasureCircle) {
            Shader shader = new SweepGradient(mCircleCenterX, mCircleCenterX, colors, null);
            setShader(shader);
        }
        else {
            mShaderColors = colors;
            isShader = true;
        }
    }

    /**
     * 设置进度颜色（纯色）
     *
     * @param color
     */
    public void setProgressColor(int color) {
        isShader = false;
        this.mProgressColor = color;
        invalidate();
    }

    /**
     * 设置进度颜色
     *
     * @param resId
     */
    public void setProgressColorResource(int resId) {
        int color = getResources().getColor(resId);
        setProgressColor(color);
    }

    /**
     * 设置是否显示外环刻度
     *
     * @param isShowTick
     */
    public void setShowTick(boolean isShowTick) {
        this.isShowTick = isShowTick;
        invalidate();
    }

    public int getStartAngle() {
        return mStartAngle;
    }

    public float getSweepAngle() {
        return mSweepAngle;
    }

    public float getCircleCenterX() {
        return mCircleCenterX;
    }

    public float getCircleCenterY() {
        return mCircleCenterY;
    }

    public float getRadius() {
        return mRadius;
    }

    /**
     * 获取当前实际值
     *
     * @return int
     */
    private int getCurrentValue(){
        return mProgress + mMinValue;
    }

    @Override
    public void setStrMinMax(String min, String max) {

    }

    @Override
    public int getProcess() {
        return (int) getProgress();
    }

    @Override
    public void setProcess(int process) {
        setProgress(process);
    }

    @Override
    public void reduceProcess() {
        reduceProcess(false, 1);
    }

    @Override
    public void addProcess() {
        addProcess(false, 1);
    }

    @Override
    public void addProcess(int idx) {

    }

    /**
     * 根据值设置进度
     * @param value 实际值
     */
    public void setProgressByValue(int value){
        if (value < mMinValue || value >mMaxValue) {
            return;
        }
        // float progress = (value - mMinValue)/ (mMaxValue - mMinValue) * mMax;
        int progress = value - mMinValue;

        setProgress(progress);
    }

    private void reduceProcess(boolean flag, int stepValue) {
        if (mProgress > 0) {
            // float progress = Math.max(0, mProgress - mMax / (mMaxValue - mMinValue));
            int progress = Math.max(0, mProgress - stepValue);
            Log.e(TAG, "reduceProcess: progress = " + progress);
            setProgress(progress);
        }
    }

    private void addProcess(boolean flag, int stepValue) {
        if (mProgress < mMax) {
            int progress = Math.min(mMax, mProgress + stepValue);
            Log.e(TAG, "addProcess: progress = " + progress);
            setProgress(progress);
        }
    }

    @Override
    public int getMax() {
        return (int) mMax;
    }

    @Override
    public void setIntValue(int baseValue, int unit, int max) {

    }

    @Override
    public int getIntValue() {
        return getCurrentValue();
    }

    @Override
    public void setFloatValue(float unit_f, int baseValue, int unit, int max) {

    }

    @Override
    public float getFloatValue() {
        return 0;
    }

    public float getProgress() {
        return mProgress;
    }

    public String getLabelText() {
        return mLabelText;
    }

    public float getThumbRadius() {
        return mThumbRadius;
    }

    public float getThumbCenterX() {
        return mThumbCenterX;
    }

    public float getThumbCenterY() {
        return mThumbCenterY;
    }


    public float getAllowableOffsets() {
        return mAllowableOffsets;
    }

    public boolean isEnabledDrag() {
        return isEnabledDrag;
    }

    /**
     * 触摸时可偏移距离：偏移量越大，触摸精度越小
     *
     * @param allowableOffsets
     */
    public void setAllowableOffsets(float allowableOffsets) {
        this.mAllowableOffsets = allowableOffsets;
    }

    /**
     * 是否启用拖拽
     *
     * @param enabledDrag 默认为true，为false时 相当于{@link android.widget.ProgressBar}
     */
    public void setEnabledDrag(boolean enabledDrag) {
        isEnabledDrag = enabledDrag;
    }

    /**
     * 设置中间文本标签内间距
     *
     * @param left
     * @param top
     * @param right
     * @param bottom
     */
    public void setLabelPadding(float left, float top, float right, float bottom) {
        this.mLabelPaddingLeft = left;
        this.mLabelPaddingTop = top;
        this.mLabelPaddingRight = right;
        this.mLabelPaddingBottom = bottom;
        invalidate();
    }

    /**
     * 设置标签文本
     *
     * @param labelText
     */
    public void setLabelText(String labelText) {
        this.mLabelText = labelText;
        this.isShowValueText = TextUtils.isEmpty(labelText);
        invalidate();
    }

    /**
     * 进度百分比
     *
     * @return
     */
    public float getProgressPercent() {
        return mProgressPercent;
    }

    /**
     * 如果自定义设置过{@link #setLabelText(String)} 或通过xml设置过{@code app:labelText}则
     * 返回{@link #mLabelText}，反之默认返回百分比{@link #mProgressPercent}
     *
     * @return
     */
    public String getText() {
        if (isShowValueText) {
            return mProgressPercent + "%";
        }

        return mLabelText;
    }

    public int getLabelTextColor() {
        return mLabelTextColor;
    }

    /**
     * 设置文本颜色
     *
     * @param color
     */
    public void setLabelTextColor(int color) {
        this.mLabelTextColor = color;
        invalidate();
    }

    /**
     * 设置文本颜色
     *
     * @param resId 颜色资源id
     */
    public void setLabelTextColorResource(int resId) {
        int color = getResources().getColor(resId);
        setLabelTextColor(color);
    }

    /**
     * 设置文本标签字体大小
     *
     * @param textSize 单位：sp
     */
    public void setLabelTextSize(float textSize) {
        setLabelTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
    }

    /**
     * 设置文本标签字体大小
     *
     * @param unit     单位 一般使用{@link TypedValue#COMPLEX_UNIT_SP}
     * @param textSize
     */
    public void setLabelTextSize(int unit, float textSize) {
        float size = TypedValue.applyDimension(unit, textSize, getDisplayMetrics());
        if (mLabelTextSize != size) {
            this.mLabelTextSize = size;
            invalidate();
        }

    }

    public boolean isHasGap() {
        return mHasGap;
    }

    public void setHasGap(boolean hasGap) {
        mHasGap = hasGap;
        invalidate();
    }

    public int getGapNum() {
        return mGapNum;
    }

    public void setGapNum(int gapNum) {
        mGapNum = gapNum;
        invalidate();
    }

    public float getGapAngle() {
        return mGapAngle;
    }

    public void setGapAngle(float gapAngle) {
        mGapAngle = gapAngle;
        invalidate();
    }

    public boolean isShowValue() {
        return isShowValue;
    }

    public void setShowValue(boolean showValue) {
        isShowValue = showValue;
        invalidate();
    }

    public float getMinValue() {
        return mMinValue;
    }

    public void setMinValue(int minValue) {
        mMinValue = minValue;
        caulMax();
        invalidate();
    }

    public float getMaxValue() {
        return mMaxValue;
    }

    public void setMaxValue(int maxValue) {
        mMaxValue = maxValue;
        caulMax();
        invalidate();
    }

    public float getValueTextSize() {
        return mValueTextSize;
    }

    public void setValueTextSize(float textSize) {
        setValueTextSize(TypedValue.COMPLEX_UNIT_SP, textSize);
    }

    public void setValueTextSize(int unit, float textSize) {
        mValueTextSize = textSize;

        float size = TypedValue.applyDimension(unit, textSize, getDisplayMetrics());
        if (mValueTextSize != size) {
            this.mValueTextSize = size;
            invalidate();
        }
    }

    public int getValueTextColor() {
        return mValueTextColor;
    }

    /**
     * 设置文本颜色
     *
     * @param color
     */
    public void setValueTextColor(int color) {
        this.mValueTextColor = color;
        invalidate();
    }

    /**
     * 设置文本颜色
     *
     * @param resId 颜色资源id
     */
    public void setValueTextColorResource(int resId) {
        int color = getResources().getColor(resId);
        setValueTextColor(color);
    }

    /**
     * 设置进度改变监听
     *
     * @param onChangeListener
     */
    public void setOnChangeListener(OnChangeListener onChangeListener) {
        this.mOnChangeListener = onChangeListener;
    }


    public interface OnChangeListener {
        void onStartTrackingTouch(boolean isCanDrag);

        void onProgressChanged(float progress, float max, int currentValue, boolean fromUser);

        void onStopTrackingTouch(boolean isCanDrag);

        void onSingleTapUp();
    }

    public abstract class OnSimpleChangeListener implements OnChangeListener {
        @Override
        public void onStartTrackingTouch(boolean isCanDrag) {

        }

        @Override
        public void onStopTrackingTouch(boolean isCanDrag) {

        }

        @Override
        public void onSingleTapUp() {

        }
    }

    /**
     * 获取小数位
     * @param decimal
     * @return
     */
    private int getNumDecimal(float decimal){
        if (decimal == 1){
            return 0;
        }else if (decimal == 0.1){
            return 1;
        }else if (decimal == 0.01){
            return 2;
        }else{
            return 0;
        }

    }
}

