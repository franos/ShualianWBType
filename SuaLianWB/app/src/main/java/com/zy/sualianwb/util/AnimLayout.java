package com.zy.sualianwb.util;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.animation.Animation;


/**
 * Created by zz on 15/12/30.
 */
public class AnimLayout extends View {
    Animation openAnim, closeAnim;

    public AnimLayout(Context context) {
        this(context, null, null);
    }

    public AnimLayout(Context context, Animation openAnim, Animation closeAnim) {
        super(context);
        this.openAnim = openAnim;
        this.closeAnim = closeAnim;
        initAnim();
    }

    public AnimLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        initAnim();
    }

    public AnimLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initAnim();
    }


    private void initAnim() {
        if (openAnim == null) {
            return;
        }
        openAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (onAnim != null) {
                    onAnim.onOpenEnd();
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        if (closeAnim == null) {
            return;
        }
        closeAnim.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                if (onAnim != null) {
                    onAnim.onCloseEnd();
                }
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
    }

    public void startOpenAnim() {
        clearAnimation();
        startAnimation(openAnim);
    }

    public void startOpenAnim(long offset) {
        openAnim.setStartOffset(offset);
        clearAnimation();
        startAnimation(openAnim);
    }


    public void startCloseAnim() {
        clearAnimation();
        startAnimation(closeAnim);
    }

    public void startCloseAnim(long offset) {
        closeAnim.setStartOffset(offset);
        clearAnimation();
        startAnimation(closeAnim);
    }

    private OnAnim onAnim;

    public void setOnAnim(OnAnim onAnim) {
        this.onAnim = onAnim;
    }

    public interface OnAnim {
        void onOpenEnd();

        void onCloseEnd();
    }
}
