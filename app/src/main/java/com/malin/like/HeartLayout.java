package com.malin.like;

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.PointF;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.support.v4.content.ContextCompat;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.AccelerateInterpolator;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import java.util.Random;

public class HeartLayout extends RelativeLayout {

    private static final String TAG = HeartLayout.class.getSimpleName();

    private Interpolator line = new LinearInterpolator();//线性
    private Interpolator acc = new AccelerateInterpolator();//加速
    private Interpolator dce = new DecelerateInterpolator();//减速
    private Interpolator accdec = new AccelerateDecelerateInterpolator();//先加速后减速
    private Interpolator[] interpolators;

    private int mHeartWidth;
    private int mHeartHeight;

    private int mLayoutWidth;
    private int mLayoutHeight;

    private Drawable[] mDrawableArray;

    private Random mRandom = new Random();

    //定义一个LayoutParams 用它来控制子view的位置
    private LayoutParams mLayoutParams;


    private void init() {

        Drawable redDrawable = ContextCompat.getDrawable(getContext(), R.drawable.xin_green);
        Drawable blueDrawable = ContextCompat.getDrawable(getContext(), R.drawable.xin_purple);
        Drawable yellowDrawable = ContextCompat.getDrawable(getContext(), R.drawable.xin_yellow);
        mDrawableArray = new Drawable[3];
        mDrawableArray[0] = redDrawable;
        mDrawableArray[1] = blueDrawable;
        mDrawableArray[2] = yellowDrawable;
       

        //获取图的宽高 用于后面的计算
        //注意 我这里3张图片的大小都是一样的,所以我只取了一个
        mHeartWidth = redDrawable.getIntrinsicHeight();
        mHeartHeight = redDrawable.getIntrinsicWidth();

        //定义一个LayoutParams 用它来控制子view的位置
        //底部 并且 水平居中
        //TODO:宽度和高度
        mLayoutParams = new LayoutParams(mHeartWidth, mHeartHeight);
        mLayoutParams.addRule(CENTER_HORIZONTAL, TRUE);
        mLayoutParams.addRule(ALIGN_PARENT_BOTTOM, TRUE);

        // 初始化插补器
        interpolators = new Interpolator[4];
        interpolators[0] = line;
        interpolators[1] = acc;
        interpolators[2] = dce;
        interpolators[3] = accdec;
    }

    public HeartLayout(Context context) {
        super(context);
        init();
    }

    public HeartLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public HeartLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    public HeartLayout(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
        init();
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        // 获取本身的宽高 需要在测量之后才有宽高
        mLayoutWidth = getMeasuredWidth();
        mLayoutHeight = getMeasuredHeight();


    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);


    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
    }


    public void addHeart() {
        ImageView mImageView = new ImageView(getContext());
        mImageView.setImageDrawable(mDrawableArray[mRandom.nextInt(3)]);
        mImageView.setLayoutParams(mLayoutParams);
        //TODO:需要addView进去
        addView(mImageView);
        Log.v(TAG, "add后子view数:" + getChildCount());
        AnimatorSet set = getEnterAnimtor(mImageView);
        set.start();
    }
    private ImageView mImageView;
    public void addAnimationHeart(){
        mImageView  = new ImageView(getContext());
        mImageView.setImageDrawable(mDrawableArray[mRandom.nextInt(3)]);
        mImageView.setLayoutParams(mLayoutParams);
        addView(mImageView);
        
        Log.v(TAG, "+++add后子view数:" + getChildCount());
        
        Animator animator = getAnimator(mImageView);
        animator.addListener(new AnimEndListener(mImageView));
        animator.start();
    }
    /**
     * 利用ObjectAnimator AnimatorSet来实现 alpha以及x,y轴的缩放功能
     *
     * @param view:爱心
     * @return
     */
    private AnimatorSet getEnterAnimtor(View view) {
        ObjectAnimator alpha = ObjectAnimator.ofFloat(view, View.ALPHA, 0.2f, 1.0f);
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, View.SCALE_X, 0.2f, 1.0f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, View.SCALE_Y, 0.2f, 1.0f);
        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.setDuration(500);
        animatorSet.setInterpolator(new LinearInterpolator());
        animatorSet.playTogether(alpha, scaleX, scaleY);
        animatorSet.setTarget(view);
        return animatorSet;
    }

    private Animator getAnimator(View view){
        AnimatorSet set = getEnterAnimtor(view);
        ValueAnimator bezierValueAnimator = getBezierValueAnimator(view);

        AnimatorSet finalSet = new AnimatorSet();
        finalSet.playSequentially(set);
        finalSet.playSequentially(set,bezierValueAnimator);
        finalSet.setInterpolator(interpolators[mRandom.nextInt(4)]);
        finalSet.setTarget(view);
        return finalSet;
    }
    /**
     * 获取贝尔曲线动画效果
     * @param view
     * @return
     */
    private ValueAnimator getBezierValueAnimator(View view) {
        //初始化一个BezierEvaluator

        BezierEvaluator evaluator = new BezierEvaluator(getPointF(2), getPointF(1));
        ValueAnimator animator = ValueAnimator.ofObject(
                evaluator,
                new PointF((mLayoutWidth-mHeartWidth)/2, mLayoutHeight-mHeartHeight),
                new PointF(mRandom.nextInt(getWidth()),0));//随机
        animator.addUpdateListener(new BezierListener(view));
        animator.setTarget(view);
        animator.setDuration(3000);

        return animator;
    }

    /**
     * 这里涉及到另外一个方法:getPointF(),这个是我用来获取途径的两个点
     * 这里的取值可以随意调整,调整到你希望的样子就好
     * 获取中间的两个 点
     *
     * @param scale
     */
    private PointF getPointF(int scale) {
        PointF pointF = new PointF();
        pointF.x = mRandom.nextInt((mLayoutWidth - 100));//减去100 是为了控制 x轴活动范围,看效果 随意~~
        //再Y轴上 为了确保第二个点 在第一个点之上,我把Y分成了上下两半 这样动画效果好一些  也可以用其他方法
        pointF.y = mRandom.nextInt((mLayoutHeight - 100)) / scale;
        return pointF;
    }

    public class BezierListener implements ValueAnimator.AnimatorUpdateListener {
        private View view;

        public BezierListener(View view) {
            this.view = view;
        }

        @Override
        public void onAnimationUpdate(ValueAnimator valueAnimator) {
            // 这里获取到贝塞尔曲线计算出来的的x y值 赋值给view 这样就能让爱心随着曲线走啦
            PointF pointF = (PointF) valueAnimator.getAnimatedValue();
            view.setX(pointF.x);
            view.setY(pointF.y);
            // 一个alpha动画,这样alpha渐变也完成啦
            view.setAlpha(1 - valueAnimator.getAnimatedFraction());
        }
    }

    private class AnimEndListener extends AnimatorListenerAdapter {
        private View view;

        public AnimEndListener(View view) {
            this.view = view;
        }

        @Override
        public void onAnimationEnd(Animator animation) {
            super.onAnimationEnd(animation);
            //因为不停的add 导致子view数量只增不减,所以在view动画结束后remove掉
            ImageView imageView = (ImageView) view;
            imageView.setImageDrawable(null);
            imageView.removeCallbacks(null);
            imageView.clearAnimation();
            removeView((view));
        }
    }
}
