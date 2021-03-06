package com.sscl.baselibrary.widget;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.animation.LinearInterpolator;

import androidx.annotation.Nullable;

/**
 * 自定义的圆弧形进度条
 *
 * @author pengh
 */
public class ArcProgressBar extends View {

    /**
     * 根据数据显示的圆弧Paint
     */
    private Paint mArcPaint;
    /**
     * 文字描述的paint
     */
    private Paint mTextPaint;
    /**
     * 圆弧开始的角度
     */
    private final float startAngle = 135;
    /**
     * 圆弧结束的角度
     */
    private final float endAngle = 45;
    /**
     * 圆弧背景的开始和结束间的夹角大小
     */
    private final float mAngle = 270;
    /**
     * 当前进度夹角大小
     */
    private float mIncludedAngle = 0;
    /**
     * 圆弧的画笔的宽度
     */
    private final float mStrokeWith = 10;
    /**
     * 中心的文字描述
     */
    private String mDes = "";
    /**
     * 动画效果的数据及最大/小值
     */
    private int mAnimatorValue, mMinValue, mMaxValue;
    /**
     * 中心点的XY坐标
     */
    private float centerX, centerY;
    /**
     * 文本的颜色
     */
    private int textColor = Color.parseColor("#999999");

    public ArcProgressBar(Context context) {
        this(context, null);
    }

    public ArcProgressBar(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ArcProgressBar(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    /**
     * <p>
     * Measure the view and its content to determine the measured width and the
     * measured height. This method is invoked by {@link #measure(int, int)} and
     * should be overridden by subclasses to provide accurate and efficient
     * measurement of their contents.
     * </p>
     *
     * <p>
     * <strong>CONTRACT:</strong> When overriding this method, you
     * <em>must</em> call {@link #setMeasuredDimension(int, int)} to store the
     * measured width and height of this view. Failure to do so will trigger an
     * <code>IllegalStateException</code>, thrown by
     * {@link #measure(int, int)}. Calling the superclass'
     * {@link #onMeasure(int, int)} is a valid use.
     * </p>
     *
     * <p>
     * The base class implementation of measure defaults to the background size,
     * unless a larger size is allowed by the MeasureSpec. Subclasses should
     * override {@link #onMeasure(int, int)} to provide better measurements of
     * their content.
     * </p>
     *
     * <p>
     * If this method is overridden, it is the subclass's responsibility to make
     * sure the measured height and width are at least the view's minimum height
     * and width ({@link #getSuggestedMinimumHeight()} and
     * {@link #getSuggestedMinimumWidth()}).
     * </p>
     *
     * @param widthMeasureSpec  horizontal space requirements as imposed by the parent.
     *                          The requirements are encoded with
     *                          {@link MeasureSpec}.
     * @param heightMeasureSpec vertical space requirements as imposed by the parent.
     *                          The requirements are encoded with
     *                          {@link MeasureSpec}.
     * @see #getMeasuredWidth()
     * @see #getMeasuredHeight()
     * @see #setMeasuredDimension(int, int)
     * @see #getSuggestedMinimumHeight()
     * @see #getSuggestedMinimumWidth()
     * @see MeasureSpec#getMode(int)
     * @see MeasureSpec#getSize(int)
     */
    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int xSize = MeasureSpec.getSize(widthMeasureSpec);
        int ySize = MeasureSpec.getSize(heightMeasureSpec);
        if (xSize < ySize){
            ySize = xSize;
        }else {
            ySize = xSize;
        }

        widthMeasureSpec = MeasureSpec.makeMeasureSpec(xSize,MeasureSpec.EXACTLY);
        heightMeasureSpec = MeasureSpec.makeMeasureSpec(ySize,MeasureSpec.EXACTLY);
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

    }

    private void initPaint() {
        //圆弧的paint
        mArcPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        //抗锯齿
        mArcPaint.setAntiAlias(true);
        mArcPaint.setColor(Color.parseColor("#666666"));
        //设置透明度（数值为0-255）
        mArcPaint.setAlpha(100);
        //设置画笔的画出的形状
        mArcPaint.setStrokeJoin(Paint.Join.ROUND);
        mArcPaint.setStrokeCap(Paint.Cap.ROUND);
        //设置画笔类型
        mArcPaint.setStyle(Paint.Style.STROKE);
        mArcPaint.setStrokeWidth(dp2px(mStrokeWith));
        //中心文字的paint
        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setColor(Color.parseColor("#FF4A40"));
        //设置文本的对齐方式
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        mTextPaint.setTextSize(dp2px(25));
    }

    @SuppressLint("DrawAllocation")
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        centerX = getWidth() / 2;
        centerY = getHeight() / 2;
        //初始化paint
        initPaint();
        //绘制弧度
        drawArc(canvas);
        //绘制文本
        drawText(canvas);
    }

    /**
     * 绘制文本
     *
     * @param canvas 画布
     */
    private void drawText(Canvas canvas) {
        Rect mRect = new Rect();
        String mValue = String.valueOf(mAnimatorValue);
        //绘制中心的数值
        mTextPaint.getTextBounds(mValue, 0, mValue.length(), mRect);
        canvas.drawText(String.valueOf(mAnimatorValue), centerX, centerY + mRect.height(), mTextPaint);
        //绘制中心文字描述
        mTextPaint.setColor(textColor);
        mTextPaint.setTextSize(dp2px(12));
        mTextPaint.getTextBounds(mDes, 0, mDes.length(), mRect);
        //绘制最小值
        canvas.drawText(mDes, centerX, centerY + 2 * mRect.height() + dp2px(10), mTextPaint);
        String minValue = String.valueOf(mMinValue);
        String maxValue = String.valueOf(mMaxValue);
        mTextPaint.setTextSize(dp2px(18));
        mTextPaint.getTextBounds(minValue, 0, minValue.length(), mRect);
        canvas.drawText(minValue, (float) (centerX - 0.6 * centerX - dp2px(5)), (float) (centerY + 0.75 * centerY + mRect.height() + dp2px(5)), mTextPaint);
        //绘制最大指
        mTextPaint.getTextBounds(maxValue, 0, maxValue.length(), mRect);
        canvas.drawText(maxValue, (float) (centerX + 0.6 * centerX + dp2px(5)), (float) (centerY + 0.75 * centerY + mRect.height() + dp2px(5)), mTextPaint);
    }

    /**
     * 绘制圆弧
     *
     * @param canvas 画布
     */
    private void drawArc(Canvas canvas) {
        //绘制圆弧背景
        RectF mRectF = new RectF(mStrokeWith + dp2px(5), mStrokeWith + dp2px(5), getWidth() - mStrokeWith - dp2px(5), getHeight() - mStrokeWith);
        canvas.drawArc(mRectF, startAngle, mAngle, false, mArcPaint);
        //绘制当前数值对应的圆弧
        mArcPaint.setColor(Color.parseColor("#FF4A40"));
        //根据当前数据绘制对应的圆弧
        canvas.drawArc(mRectF, startAngle, mIncludedAngle, false, mArcPaint);
    }

    /**
     * 为绘制弧度及数据设置动画
     *
     * @param startAngle   开始的弧度
     * @param currentAngle 需要绘制的弧度
     * @param currentValue 需要绘制的数据
     * @param time         动画执行的时长
     */
    private void setAnimation(float startAngle, float currentAngle, int currentValue, int time) {
        //绘制当前数据对应的圆弧的动画效果
        ValueAnimator progressAnimator = ValueAnimator.ofFloat(startAngle, currentAngle);
        progressAnimator.setDuration(time);
        progressAnimator.setTarget(mIncludedAngle);
        progressAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mIncludedAngle = (float) animation.getAnimatedValue();
                //重新绘制，不然不会出现效果
                postInvalidate();
            }
        });
        //开始执行动画
        progressAnimator.start();    //中心数据的动画效果
        ValueAnimator valueAnimator = ValueAnimator.ofInt(mAnimatorValue, currentValue);
        valueAnimator.setDuration(2500);
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator valueAnimator) {
                mAnimatorValue = (int) valueAnimator.getAnimatedValue();
                postInvalidate();
            }
        });
        valueAnimator.start();
    }

    public void setTextColor(int textColor) {
        this.textColor = textColor;
    }

    /**
     * 设置数据
     *
     * @param minValue     最小值
     * @param maxValue     最大值
     * @param currentValue 当前绘制的值
     * @param des          描述信息
     */
    public void setValues(int minValue, int maxValue, int currentValue, String des) {
        mDes = des;
        mMaxValue = maxValue;
        mMinValue = minValue;
        //完全覆盖背景弧度
        if (currentValue > maxValue) {
            currentValue = maxValue;
        }
        //计算弧度比重
        float scale = (float) currentValue / maxValue;
        //计算弧度
        float currentAngle = scale * mAngle;
        //开始执行动画
        setAnimation(0, currentAngle, currentValue, 2500);
    }


    public float dp2px(float dp) {
        DisplayMetrics metrics = Resources.getSystem().getDisplayMetrics();
        return dp * metrics.density;
    }
}