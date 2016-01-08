package com.zy.sualianwb.util;

import android.animation.ValueAnimator;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.LinearLayout;

import com.zy.sualianwb.Constants;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by zz on 15/12/25.
 */
public class BlindLayout_BackUp2 extends LinearLayout {
    boolean isClose = false;
    ValueAnimator valueAnimator;
    float winWidth, winHeight;
    private ScaleAnimation openAnim;
    private ScaleAnimation closeAnim;


    Paint mPaint;
    private int cutTime= 1;
    private int paintColor;

    float left, top, right, bottom;
    private List<View> childView = new ArrayList<>(5);

    public BlindLayout_BackUp2(Context context) {
        super(context);
        init();
    }

    public BlindLayout_BackUp2(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BlindLayout_BackUp2(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        post(new Runnable() {
            @Override
            public void run() {
                setOrientation(LinearLayout.HORIZONTAL);
                initChild();
//
//                final int childCount = getChildCount();
//                mPaint = new Paint();
//                if (childCount == 0) {
//                    return;
//                }
//                for (int i = 0; i < childCount; i++) {
//                    View childAt = getChildAt(i);
//                    childAt.setBackgroundColor(Color.parseColor("#000000"));
//                    childView.add(childAt);
//                }


                winWidth = DensityUtil.getWindowWidthHeight(getContext())[0];
                winHeight = DensityUtil.getWindowWidthHeight(getContext())[1];
//                valueAnimator = ValueAnimator.ofFloat(0, winWidth / childCount);
//                valueAnimator.setDuration(1000);


                getOpenAnim();

                getCloseAnim();


//
//                valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//                    @Override
//                    public void onAnimationUpdate(ValueAnimator animation) {
//                        Float floatV = (Float) animation.getAnimatedValue();
//                        left = 0;
//                        top = 0;
//                        right = floatV;
//                        bottom = winHeight;
//
//                        for (View child : childView) {
//
//                            if (!isClose) {
////                                child.getMatrix().setScale((1 - floatV / (winWidth / childCount)), 1, 1f * 0f, 1f * 0.5f);
//                                child.setScaleX((1 - floatV / (winWidth / childCount)));
//
//                            } else {
////                                child.getMatrix().setScale(( floatV / (winWidth / childCount)), 1, 1f* 1f, 1f* 0.5f);
//                                child.setScaleX((floatV / (winWidth / childCount)));
//                            }
//                            child.requestLayout();
//                        }
//                        invalidate();
//                    }
//                });

            }
        });
    }

    private void initChild() {
        for(int i=0;i<cutTime;i++) {
            View view=new View(getContext());
            LayoutParams params=new LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT,1);
            view.setLayoutParams(params);
            childView.add(view);
            view.setBackgroundColor(Color.parseColor("#000000"));
            addView(view);
        }
    }

    private void getCloseAnim() {
        closeAnim = new ScaleAnimation(1f, 1f, 0f, 1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0F);
        closeAnim.setFillAfter(true);
        closeAnim.setDuration(Constants.ANIM_DURATION_OUT);
        closeAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if(onAnimEnd!=null){
                    onAnimEnd.onCloseAnimEnd();
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    private void getOpenAnim() {
        openAnim = new ScaleAnimation(1f, 1f, 1f, 0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 1F);
        openAnim.setFillAfter(true);
        openAnim.setDuration(Constants.ANIM_DURATION_OUT);
        openAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if(onAnimEnd!=null){
                    onAnimEnd.onOpenAnimEnd();
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }


    public void setClose(boolean isClose) {
        this.isClose = isClose;
        if (isClose) {
            paintColor = Color.parseColor("#000000");
//            setBackgroundColor(Color.parseColor("#000000"));
        } else {
            paintColor = Color.parseColor("#00000000");
            
//            setBackgroundColor(Color.parseColor("#00000000"));
        }
//        mPaint.setColor(paintColor);
//        valueAnimator.start();
        startAnim();
    }

    public void startAnim() {
        for (View child : childView) {

            if (!isClose) {
//                child.setScaleX((1 - floatV / (winWidth / childCount)));
                child.startAnimation(openAnim);

            } else {
//                child.setScaleX((floatV / (winWidth / childCount)));
                child.startAnimation(closeAnim);
            }
            child.requestLayout();
        }
        invalidate();

    }

    private OnAnimEnd onAnimEnd;

    public void setOnAnimEnd(OnAnimEnd onAnimEnd) {
        this.onAnimEnd = onAnimEnd;
    }

    public interface OnAnimEnd{
        void onOpenAnimEnd();
        void onCloseAnimEnd();
    }
}
