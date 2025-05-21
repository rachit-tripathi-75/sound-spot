package com.rachit.tripathi75.soundspot.fragments;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.os.Handler;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;

import com.rachit.tripathi75.soundspot.R;
import com.rachit.tripathi75.soundspot.databinding.FragmentLoadingBinding;


public class ImportPlaylistLoadingFragment extends Fragment {

    private FragmentLoadingBinding binding;
    private final Handler handler = new Handler(Looper.getMainLooper());

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    public ImportPlaylistLoadingFragment() {
        // Required empty public constructor
    }

    public static ImportPlaylistLoadingFragment newInstance(String param1, String param2) {
        ImportPlaylistLoadingFragment fragment = new ImportPlaylistLoadingFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentLoadingBinding.inflate(inflater, container, false);



        return binding.getRoot();
    }


    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Start loading animations
        startLoadingAnimations();

        // Set up notify button
        binding.btnNotify.setOnClickListener(v -> {
            v.startAnimation(AnimationUtils.loadAnimation(requireContext(), R.anim.button_press));
            // Would show notification permission or confirmation
        });

        // Auto navigate to playlist selection after delay (simulating API response)
        handler.postDelayed(() -> {
            if (isAdded()) {
                Navigation.findNavController(view).navigate(R.id.action_loadingFragment_to_playlistSelectionFragment);
            }
        }, 3000);
    }


    private void startLoadingAnimations() {
        // Animate the loading rings with different speeds
        AnimatorSet animatorSet = new AnimatorSet();

        ObjectAnimator rotation1 = ObjectAnimator.ofFloat(binding.loadingRing1, "rotation", 0f, 360f);
        rotation1.setDuration(2000);
        rotation1.setRepeatCount(ObjectAnimator.INFINITE);
        rotation1.setInterpolator(new AccelerateDecelerateInterpolator());

        ObjectAnimator rotation2 = ObjectAnimator.ofFloat(binding.loadingRing2, "rotation", 0f, -360f);
        rotation2.setDuration(3000);
        rotation2.setRepeatCount(ObjectAnimator.INFINITE);
        rotation2.setInterpolator(new AccelerateDecelerateInterpolator());

        ObjectAnimator rotation3 = ObjectAnimator.ofFloat(binding.loadingRing3, "rotation", 0f, 360f);
        rotation3.setDuration(1500);
        rotation3.setRepeatCount(ObjectAnimator.INFINITE);
        rotation3.setInterpolator(new AccelerateDecelerateInterpolator());

        animatorSet.playTogether(rotation1, rotation2, rotation3);
        animatorSet.start();

        // Animate the button with a pulse effect
        Animation pulseAnimation = AnimationUtils.loadAnimation(requireContext(), R.anim.pulse);
        binding.btnNotify.startAnimation(pulseAnimation);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        handler.removeCallbacksAndMessages(null);
    }

}