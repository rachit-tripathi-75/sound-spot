package com.rachit.tripathi75.soundspot.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.snackbar.Snackbar;
import com.rachit.tripathi75.soundspot.R;
import com.rachit.tripathi75.soundspot.adapters.SavedLibrariesAdapter;
import com.rachit.tripathi75.soundspot.databinding.AddNewLibraryBottomSheetBinding;
import com.rachit.tripathi75.soundspot.databinding.FragmentLibraryBinding;
import com.rachit.tripathi75.soundspot.records.sharedpref.SavedLibraries;
import com.rachit.tripathi75.soundspot.utils.SharedPreferenceManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;


public class LibraryFragment extends Fragment {

    private FragmentLibraryBinding binding;
    SavedLibraries savedLibraries;

    public LibraryFragment() {
        // Required empty public constructor

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentLibraryBinding.inflate(inflater, container, false);


        binding.recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        OverScrollDecoratorHelper.setUpOverScroll(binding.recyclerView, OverScrollDecoratorHelper.ORIENTATION_VERTICAL);

        binding.addNewLibrary.setOnClickListener(view -> {
            AddNewLibraryBottomSheetBinding addNewLibraryBottomSheetBinding = AddNewLibraryBottomSheetBinding.inflate(getLayoutInflater());
            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(requireContext(), R.style.MyBottomSheetDialogTheme);
            bottomSheetDialog.setContentView(addNewLibraryBottomSheetBinding.getRoot());
            addNewLibraryBottomSheetBinding.cancel.setOnClickListener(view1 -> {
                bottomSheetDialog.dismiss();
            });
            addNewLibraryBottomSheetBinding.create.setOnClickListener(view1 -> {
                final String name = addNewLibraryBottomSheetBinding.edittext.getText().toString();
                if (name.isEmpty()) {
                    addNewLibraryBottomSheetBinding.edittext.setError("Name cannot be empty");
                    return;
                }
                addNewLibraryBottomSheetBinding.edittext.setError(null);
                Log.i("SavedLibrariesActivity", "BottomSheetDialog_create: " + name);

                final String currentTime = String.valueOf(System.currentTimeMillis());

                SavedLibraries.Library library = new SavedLibraries.Library(
                        "#" + currentTime,
                        true,
                        false,
                        name,
                        "",
                        "Created on :- " + formatMillis(Long.parseLong(currentTime)),
                        new ArrayList<>()
                );

                final SharedPreferenceManager sharedPreferenceManager = SharedPreferenceManager.getInstance(requireContext());
                sharedPreferenceManager.addLibraryToSavedLibraries(library);
                Snackbar.make(binding.getRoot(), "Library added successfully", Snackbar.LENGTH_SHORT).show();


                bottomSheetDialog.dismiss();

                showData(sharedPreferenceManager);
            });
            bottomSheetDialog.show();
        });

        showData();

        return binding.getRoot();
    }

    private String formatMillis(long millis) {
        Date date = new Date(millis);
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd-yyyy HH:mm a");
        return simpleDateFormat.format(date);
    }


    private void showData(SharedPreferenceManager sharedPreferenceManager) {
        savedLibraries = sharedPreferenceManager.getSavedLibrariesData();
        binding.emptyListTv.setVisibility(savedLibraries == null ? View.VISIBLE : View.GONE);
        if (savedLibraries != null)
            binding.recyclerView.setAdapter(new SavedLibrariesAdapter(savedLibraries.lists()));
    }

    private void showData() {
        showData(SharedPreferenceManager.getInstance(requireContext()));
    }


}