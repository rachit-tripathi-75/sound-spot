package com.rachit.tripathi75.soundspot.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;

import com.rachit.tripathi75.soundspot.R;
import com.rachit.tripathi75.soundspot.databinding.FragmentImportPlaylistSelectSourceBinding;
import com.rachit.tripathi75.soundspot.databinding.FragmentSelectSourceBinding;


public class ImportPlaylistSelectSourceFragment extends Fragment {

    private FragmentSelectSourceBinding binding;


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    public ImportPlaylistSelectSourceFragment() {
        // Required empty public constructor
    }

    public static ImportPlaylistSelectSourceFragment newInstance(String param1, String param2) {
        ImportPlaylistSelectSourceFragment fragment = new ImportPlaylistSelectSourceFragment();
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
        binding = FragmentSelectSourceBinding.inflate(inflater, container, false);



        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Set up service cards with animations

        // Apply entrance animations
        binding.cardSpotify.startAnimation(AnimationUtils.loadAnimation(requireContext(), R.anim.scale_in));
        binding.cardAppleMusic.startAnimation(AnimationUtils.loadAnimation(requireContext(), R.anim.scale_in));
        binding.cardDeezer.startAnimation(AnimationUtils.loadAnimation(requireContext(), R.anim.scale_in));
        binding.cardAmazonMusic.startAnimation(AnimationUtils.loadAnimation(requireContext(), R.anim.scale_in));

        // Set up click listeners
        binding.cardSpotify.setOnClickListener(v -> {
            v.startAnimation(AnimationUtils.loadAnimation(requireContext(), R.anim.button_press));
            Navigation.findNavController(view).navigate(R.id.action_selectSourceFragment_to_loadingFragment);
        });

        binding.cardAppleMusic.setOnClickListener(v -> {
            v.startAnimation(AnimationUtils.loadAnimation(requireContext(), R.anim.button_press));
            // Would navigate to Apple Music auth
        });

        binding.cardDeezer.setOnClickListener(v -> {
            v.startAnimation(AnimationUtils.loadAnimation(requireContext(), R.anim.button_press));
            // Would navigate to Deezer auth
        });

        binding.cardAmazonMusic.setOnClickListener(v -> {
            v.startAnimation(AnimationUtils.loadAnimation(requireContext(), R.anim.button_press));
            // Would navigate to Amazon Music auth
        });

        // Set up transfers button
        view.findViewById(R.id.btnTransfers).setOnClickListener(v -> {
            v.startAnimation(AnimationUtils.loadAnimation(requireContext(), R.anim.button_press));
            Navigation.findNavController(view).navigate(R.id.action_selectSourceFragment_to_transferStatusFragment);
        });
    }
}