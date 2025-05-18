package com.rachit.tripathi75.soundspot.classes;

// AnimationUtils.java

import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;

import java.util.ArrayList;
import java.util.List;

public class AnimationUtils {

    public static void fadeIn(View view, long duration, long delay) {
        view.setAlpha(0f);
        view.setVisibility(View.VISIBLE);

        ObjectAnimator fadeAnimator = ObjectAnimator.ofFloat(view, "alpha", 0f, 1f);
        fadeAnimator.setDuration(duration);
        fadeAnimator.setStartDelay(delay);
        fadeAnimator.start();
    }

    public static void fadeOut(View view, long duration, long delay) {
        ObjectAnimator fadeAnimator = ObjectAnimator.ofFloat(view, "alpha", 1f, 0f);
        fadeAnimator.setDuration(duration);
        fadeAnimator.setStartDelay(delay);
        fadeAnimator.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                view.setVisibility(View.GONE);
            }
        });
        fadeAnimator.start();
    }

    public static void animateChildrenSequentially(ViewGroup viewGroup, long duration, long delay, long staggerDelay) {
        int childCount = viewGroup.getChildCount();
        List<Animator> animators = new ArrayList<>();

        for (int i = 0; i < childCount; i++) {
            View child = viewGroup.getChildAt(i);
            child.setAlpha(0f);
            child.setTranslationY(50f);

            ObjectAnimator alphaAnimator = ObjectAnimator.ofFloat(child, "alpha", 0f, 1f);
            ObjectAnimator translateAnimator = ObjectAnimator.ofFloat(child, "translationY", 50f, 0f);

            AnimatorSet animatorSet = new AnimatorSet();
            animatorSet.playTogether(alphaAnimator, translateAnimator);
            animatorSet.setStartDelay(delay + (i * staggerDelay));
            animatorSet.setDuration(duration);
            animatorSet.setInterpolator(new DecelerateInterpolator());

            animators.add(animatorSet);
        }

        AnimatorSet sequentialSet = new AnimatorSet();
        sequentialSet.playTogether(animators);
        sequentialSet.start();
    }

    public static void pulseAnimation(View view, float scaleFrom, float scaleTo, long duration, int repeatCount) {
        ObjectAnimator scaleX = ObjectAnimator.ofFloat(view, "scaleX", scaleFrom, scaleTo);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(view, "scaleY", scaleFrom, scaleTo);

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(scaleX, scaleY);
        animatorSet.setDuration(duration);
        animatorSet.setInterpolator(new DecelerateInterpolator());
//        animatorSet.setRepeatCount(repeatCount);
//        animatorSet.setRepeatMode(ObjectAnimator.REVERSE);
        animatorSet.start();
    }

    public static void createMusicVisualizer(View[] bars, long duration, Interpolator interpolator) {
        List<Animator> animators = new ArrayList<>();

        for (int i = 0; i < bars.length; i++) {
            View bar = bars[i];
            float originalHeight = bar.getLayoutParams().height;
            float targetHeight = (float) (originalHeight * (1.0 + Math.random()));

            ObjectAnimator heightAnimator = ObjectAnimator.ofFloat(bar, "scaleY", 1f, targetHeight / originalHeight);
            heightAnimator.setDuration(duration);
            heightAnimator.setInterpolator(interpolator);
            heightAnimator.setRepeatCount(ObjectAnimator.INFINITE);
            heightAnimator.setRepeatMode(ObjectAnimator.REVERSE);
            heightAnimator.setStartDelay((long) (Math.random() * 300));

            animators.add(heightAnimator);
        }

        AnimatorSet animatorSet = new AnimatorSet();
        animatorSet.playTogether(animators);
        animatorSet.start();
    }
}