package com.rachit.tripathi75.soundspot.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.rachit.tripathi75.soundspot.databinding.RelevantBottomSheetLayoutBinding;

public class RelevantBottomSheetDialog extends BottomSheetDialogFragment {

    private RelevantBottomSheetLayoutBinding binding;

    public static RelevantBottomSheetDialog newInstance() {
        return new RelevantBottomSheetDialog();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = RelevantBottomSheetLayoutBinding.inflate(inflater, container, false);

        return binding.getRoot();
    }
}
