package com.zy.sualianwb.util;

import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.LinearLayout;

import com.zy.sualianwb.Constants;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * Created by zz on 15/12/25.
 */
public class BlindLayout extends LinearLayout {
    private static final String TAG = "BlindLayout";
    boolean isClose = false;
    //    ValueAnimator valueAnimator;
    float winWidth, winHeight;
    //    private ScaleAnimation openAnim;
//    private ScaleAnimation closeAnim;
    private int offsetSeed = Constants.OFFSET_TIME_SEED;
    private static final int HANG = 4;
    private int playedCount = 0;
    private int LIE = 3;
    private Thread allCloseNotifyThread;
//    private int cutTime = 1;

    private List<View> childView = new ArrayList<>(12);

    public BlindLayout(Context context) {
        super(context);
        init();
    }

    public BlindLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public BlindLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private Handler mAllPlayedHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            playedCount = 0;
            if (null != onAnimEnd) {
                onAnimEnd.onCloseAnimEnd();
            }
        }
    };

    private void init() {

        post(new Runnable() {
            @Override
            public void run() {
                setOrientation(LinearLayout.VERTICAL);
                setBackgroundColor(Color.parseColor("#00000000"));
//                initChild();
                initChild2();

                winWidth = DensityUtil.getWindowWidthHeight(getContext())[0];
                winHeight = DensityUtil.getWindowWidthHeight(getContext())[1];


//                getOpenAnim();
//
//                getCloseAnim();


            }
        });
    }

    private void initChild2() {
        for (int i = 0; i < HANG; i++) {
            LinearLayout hangLayout = getHangLayoutContainChild();
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, 0, 1);
            hangLayout.setLayoutParams(params);
            hangLayout.setBackgroundColor(Color.parseColor("#00000000"));
            addView(hangLayout);
        }

    }

    /**
     * 每一个线性布局都有LIE个子View获得带有子view的线性布局
     *
     * @return
     */
    private LinearLayout getHangLayoutContainChild() {
        LinearLayout linearLayout = new LinearLayout(getContext());
        linearLayout.setOrientation(HORIZONTAL);
        for (int i = 0; i < LIE; i++) {
            View view = new View(getContext());
            LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1);
            view.setLayoutParams(params);
            view.setBackgroundColor(Color.parseColor("#000000"));
            view.setVisibility(INVISIBLE);
            childView.add(view);
            linearLayout.addView(view);
        }
        return linearLayout;
    }

//    private void initChild() {
//        for (int i = 0; i < cutTime; i++) {
//            View view = new View(getContext());
//            LayoutParams params = new LayoutParams(0, ViewGroup.LayoutParams.MATCH_PARENT, 1);
//            view.setLayoutParams(params);
//            childView.add(view);
//            view.setBackgroundColor(Color.parseColor("#000000"));
//            addView(view);
//        }
//    }

//    private void getCloseAnim() {
//        closeAnim = new ScaleAnimation(1f, 1f, 0f, 1f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0F);
//        closeAnim.setFillAfter(true);
//        closeAnim.setDuration(Constants.ANIM_DURATION_OUT);
//        closeAnim.setAnimationListener(new Animation.AnimationListener() {
//            @Override
//            public void onAnimationStart(Animation animation) {
//
//            }
//
//            @Override
//            public void onAnimationEnd(Animation animation) {
//                if (onAnimEnd != null) {
//                    onAnimEnd.onCloseAnimEnd();
//                }
//            }
//
//            @Override
//            public void onAnimationRepeat(Animation animation) {
//
//            }
//        });
//    }

//    private void getOpenAnim() {
//        openAnim = new ScaleAnimation(1f, 1f, 1f, 0f, Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 1F);
//        openAnim.setFillAfter(true);
//        openAnim.setDuration(Constants.ANIM_DURATION_OUT);
//        openAnim.setAnimationListener(new Animation.AnimationListener() {
//            @Override
//            public void onAnimationStart(Animation animation) {
//
//            }
//
//            @Override
//            public void onAnimationEnd(Animation animation) {
//                if (onAnimEnd != null) {
//                    onAnimEnd.onOpenAnimEnd();
//                }
//            }
//
//            @Override
//            public void onAnimationRepeat(Animation animation) {
//
//            }
//        });
//    }


    public void setClose(boolean isClose) {
        this.isClose = isClose;


//        startAnim();
        startAnim2();
    }

    private void startAnim2() {
        List<Integer> randomSeedList = getRandomSeed();
        for (int i = 0; i < childView.size(); i++) {
            View view = childView.get(i);
            view.setVisibility(VISIBLE);
            if (isClose) {
                Animation alphaCloseAnim = getCloseAnim(randomSeedList.get(i));
                view.startAnimation(alphaCloseAnim);
                startAllCloseNotifyThread();
            } else {
                Animation alphaOpenAnim = getOpenAnim(randomSeedList.get(i));
                view.startAnimation(alphaOpenAnim);
            }
        }
    }

    private void startAllCloseNotifyThread() {
        if (null == allCloseNotifyThread) {
            allCloseNotifyThread = new Thread() {
                @Override
                public void run() {
                    super.run();
                    //如果播放完成的数量小于子集数，代表未播放完成，循环判断
                    while (playedCount < childView.size()) {
                        Log.i(TAG, "playedCount" + playedCount);
                        try {
                            Thread.sleep(50);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                    //播放完成

                    Log.i(TAG, "played finish");
                    mAllPlayedHandler.obtainMessage(1).sendToTarget();
                    allCloseNotifyThread = null;
                }
            };
        }


        if (null != allCloseNotifyThread && !allCloseNotifyThread.isAlive()) {
            L.i(TAG, "thread start");
            allCloseNotifyThread.start();
        }
        L.i(TAG, "thread has already start");

    }

    /**
     * 打开帷幕
     *
     * @param integer
     * @return
     */
    private Animation getOpenAnim(Integer integer) {
        AlphaAnimation animation = new AlphaAnimation(1, 0);
        animation.setFillAfter(true);
        animation.setDuration(Constants.ANIM_DURATION_IN);
        animation.setStartOffset(integer * offsetSeed);
        Log.i(TAG, "startOffset openAnim " + offsetSeed);
        return animation;
    }

    /**
     * 关闭帷幕
     *
     * @param integer
     * @return
     */
    private Animation getCloseAnim(Integer integer) {
        AlphaAnimation animation = new AlphaAnimation(0, 1);
        animation.setFillAfter(true);
        animation.setDuration(Constants.ANIM_DURATION_OUT);
        animation.setStartOffset(integer * offsetSeed);
        Log.i(TAG, "startOffset closeAnim " + offsetSeed);
        animation.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                playedCount++;
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });
        return animation;
    }

    private List<Integer> getRandomSeed() {
        List<Integer> randomSeedLis = new ArrayList<>(childView.size());
        for (int i = 0; i < childView.size(); i++) {
            Random random = new Random();
            int randomSeed = random.nextInt(Constants.RANDOM_SEED) + 1;
            randomSeedLis.add(randomSeed);
        }
        return randomSeedLis;
    }

//
//    public void startAnim() {
//        for (View child : childView) {
//            if (!isClose) {
//                child.startAnimation(openAnim);
//            } else {
//                child.startAnimation(closeAnim);
//            }
//            child.requestLayout();
//        }
//    }

    private OnAnimEnd onAnimEnd;

    public void setOnAnimEnd(OnAnimEnd onAnimEnd) {
        this.onAnimEnd = onAnimEnd;
    }

    public interface OnAnimEnd {
        void onOpenAnimEnd();

        void onCloseAnimEnd();
    }
}
