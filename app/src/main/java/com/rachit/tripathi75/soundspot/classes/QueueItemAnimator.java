package com.rachit.tripathi75.soundspot.classes;


import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.ObjectAnimator;
import android.view.View;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.RecyclerView;

public class QueueItemAnimator extends DefaultItemAnimator {

    @Override
    public boolean animateAdd(RecyclerView.ViewHolder holder) {
        // Initial state for the added item: fully transparent and below its final position
        holder.itemView.setAlpha(0f);
        holder.itemView.setTranslationY((float) holder.itemView.getHeight());

        // Create an animation to fade in the item
        ObjectAnimator fadeIn = ObjectAnimator.ofFloat(holder.itemView, View.ALPHA, 0f, 1f);
        fadeIn.setDuration(300);

        // Create an animation to translate the item upwards to its final position
        ObjectAnimator translateUp = ObjectAnimator.ofFloat(holder.itemView, View.TRANSLATION_Y,
                (float) holder.itemView.getHeight(), 0f);
        translateUp.setDuration(300);

        // Add a listener to the translation animation to dispatch events when it ends
        translateUp.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                // Notify the RecyclerView that the add animation for this holder is finished
                dispatchAddFinished(holder);
                // Check if all animations are done and dispatch the overall finished event
                dispatchFinishedWhenDone();
            }
        });

        // Start both animations concurrently
        translateUp.start();
        fadeIn.start();

        // Return true to indicate that we are handling the animation
        return true;
    }

    @Override
    public boolean animateRemove(RecyclerView.ViewHolder holder) {
        // Create an animation to fade out the item
        ObjectAnimator fadeOut = ObjectAnimator.ofFloat(holder.itemView, View.ALPHA, 1f, 0f);
        fadeOut.setDuration(300);

        // Create an animation to translate the item downwards as it disappears
        ObjectAnimator translateDown = ObjectAnimator.ofFloat(holder.itemView, View.TRANSLATION_Y,
                0f, (float) holder.itemView.getHeight());
        translateDown.setDuration(300);

        // Add a listener to the translation animation to dispatch events when it ends
        translateDown.addListener(new AnimatorListenerAdapter() {
            @Override
            public void onAnimationEnd(Animator animation) {
                // Notify the RecyclerView that the remove animation for this holder is finished
                dispatchRemoveFinished(holder);
                // Check if all animations are done and dispatch the overall finished event
                dispatchFinishedWhenDone();
            }
        });

        // Start both animations concurrently
        translateDown.start();
        fadeOut.start();

        // Return true to indicate that we are handling the animation
        return true;
    }

    /**
     * Helper method to dispatch the overall animations finished event
     * only when no other animations are currently running.
     */
    private void dispatchFinishedWhenDone() {
        // isRunning() is a method from DefaultItemAnimator that checks if any animations are active.
        if (!isRunning()) {
            dispatchAnimationsFinished();
        }
    }
}