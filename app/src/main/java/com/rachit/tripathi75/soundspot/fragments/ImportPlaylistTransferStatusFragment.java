package com.rachit.tripathi75.soundspot.fragments;

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
import android.view.animation.AnimationUtils;

import com.rachit.tripathi75.soundspot.R;
import com.rachit.tripathi75.soundspot.databinding.FragmentTransferStatusBinding;


public class ImportPlaylistTransferStatusFragment extends Fragment {

    private FragmentTransferStatusBinding binding;
    private final Handler handler = new Handler(Looper.getMainLooper());
    private boolean isTransferComplete = false;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public ImportPlaylistTransferStatusFragment() {
        // Required empty public constructor
    }

    public static ImportPlaylistTransferStatusFragment newInstance(String param1, String param2) {
        ImportPlaylistTransferStatusFragment fragment = new ImportPlaylistTransferStatusFragment();
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
        binding = FragmentTransferStatusBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        // Set up refresh button
        view.findViewById(R.id.btnRefresh).setOnClickListener(v -> {
            v.startAnimation(AnimationUtils.loadAnimation(requireContext(), R.anim.rotate_animation));
            // Would refresh transfer status
        });

        // Set up navigation buttons
        binding.btnGoToLibrary.setOnClickListener(v -> {
            v.startAnimation(AnimationUtils.loadAnimation(requireContext(), R.anim.button_press));
            // Would navigate to library
        });

        binding.btnImportMore.setOnClickListener(v -> {
            v.startAnimation(AnimationUtils.loadAnimation(requireContext(), R.anim.button_press));
            Navigation.findNavController(view).navigate(R.id.action_transferStatusFragment_to_selectSourceFragment);
        });

        // Check if we should show ongoing or completed state
        Bundle args = getArguments();
        if (args != null && args.getBoolean("isComplete", false)) {
            showCompletedState();
        } else {
            showOngoingState();
            // Simulate transfer completion after delay
            handler.postDelayed(this::showCompletedState, 3000);
        }

        // Apply animations
        view.findViewById(R.id.cardStatus).startAnimation(AnimationUtils.loadAnimation(requireContext(), R.anim.slide_in_right));
        view.findViewById(R.id.cardTransfer).startAnimation(AnimationUtils.loadAnimation(requireContext(), R.anim.slide_in_right));

    }



    private void showOngoingState() {
        isTransferComplete = false;
        binding.ivStatusIcon.setBackgroundTintList(requireContext().getColorStateList(R.color.info_blue));
        binding.ivStatusIcon.setImageResource(R.drawable.animated_loading);
        binding.tvStatusTitle.setText(R.string.ongoing);
        binding.tvStatusDescription.setText(R.string.playlists_being_imported);
        binding.progressTransfer.setVisibility(View.VISIBLE);
        binding.tvTransferDate.setVisibility(View.GONE);
    }

    private void showCompletedState() {
        if (isAdded()) {
            isTransferComplete = true;
            binding.ivStatusIcon.setBackgroundTintList(requireContext().getColorStateList(R.color.success_green));
            binding.ivStatusIcon.setImageResource(R.drawable.ic_check);
            binding.tvStatusTitle.setText(R.string.completed);
            binding.tvStatusDescription.setText(R.string.import_completed_message);
            binding.progressTransfer.setVisibility(View.GONE);
            binding.tvTransferDate.setVisibility(View.VISIBLE);

            // Apply completion animation
            binding.ivStatusIcon.startAnimation(AnimationUtils.loadAnimation(requireContext(), R.anim.bounce));
        }
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        handler.removeCallbacksAndMessages(null);
    }

}