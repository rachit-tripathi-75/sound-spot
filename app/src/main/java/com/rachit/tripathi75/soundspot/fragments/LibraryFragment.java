package com.rachit.tripathi75.soundspot.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.bumptech.glide.Glide;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.rachit.tripathi75.soundspot.ApplicationClass;
import com.rachit.tripathi75.soundspot.R;
import com.rachit.tripathi75.soundspot.adapters.LibraryItemAdapter;
import com.rachit.tripathi75.soundspot.adapters.RecentPlayAdapter;
import com.rachit.tripathi75.soundspot.adapters.SavedLibrariesAdapter;
import com.rachit.tripathi75.soundspot.databinding.AddNewLibraryBottomSheetBinding;
import com.rachit.tripathi75.soundspot.databinding.FragmentLibraryBinding;
import com.rachit.tripathi75.soundspot.model.MusicItem;
import com.rachit.tripathi75.soundspot.records.sharedpref.SavedLibraries;
import com.rachit.tripathi75.soundspot.utils.SharedPreferenceManager;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;


public class LibraryFragment extends Fragment {

    private FragmentLibraryBinding binding;
    SavedLibraries savedLibraries;
    private RecentPlayAdapter recentPlayAdapter;
    private LibraryItemAdapter libraryItemAdapter;

    public LibraryFragment() {
        // Required empty public constructor

    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentLibraryBinding.inflate(inflater, container, false);

        initialisers();


//        binding.recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
//        OverScrollDecoratorHelper.setUpOverScroll(binding.recyclerView, OverScrollDecoratorHelper.ORIENTATION_VERTICAL);
//
//        binding.addNewLibrary.setOnClickListener(view -> {
//            AddNewLibraryBottomSheetBinding addNewLibraryBottomSheetBinding = AddNewLibraryBottomSheetBinding.inflate(getLayoutInflater());
//            BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(requireContext(), R.style.MyBottomSheetDialogTheme);
//            bottomSheetDialog.setContentView(addNewLibraryBottomSheetBinding.getRoot());
//            addNewLibraryBottomSheetBinding.cancel.setOnClickListener(view1 -> {
//                bottomSheetDialog.dismiss();
//            });
//            addNewLibraryBottomSheetBinding.create.setOnClickListener(view1 -> {
//                final String name = addNewLibraryBottomSheetBinding.edittext.getText().toString();
//                if (name.isEmpty()) {
//                    addNewLibraryBottomSheetBinding.edittext.setError("Name cannot be empty");
//                    return;
//                }
//                addNewLibraryBottomSheetBinding.edittext.setError(null);
//                Log.i("SavedLibrariesActivity", "BottomSheetDialog_create: " + name);
//
//                final String currentTime = String.valueOf(System.currentTimeMillis());
//
//                SavedLibraries.Library library = new SavedLibraries.Library(
//                        "#" + currentTime,
//                        true,
//                        false,
//                        name,
//                        "",
//                        "Created on :- " + formatMillis(Long.parseLong(currentTime)),
//                        new ArrayList<>()
//                );
//
//                final SharedPreferenceManager sharedPreferenceManager = SharedPreferenceManager.getInstance(requireContext());
//                sharedPreferenceManager.addLibraryToSavedLibraries(library);
//                Snackbar.make(binding.getRoot(), "Library added successfully", Snackbar.LENGTH_SHORT).show();
//
//
//                bottomSheetDialog.dismiss();
//
//                showData(sharedPreferenceManager);
//            });
//            bottomSheetDialog.show();
//        });
//
//        showData();

        return binding.getRoot();
    }

    private void initialisers() {

        // Set up bottom navigation

        // Set up filter chips

        setupProfilePic();

        setupFilterChips();

        // Set up recent plays recycler view
        setupRecentPlaysRecycler();

        // Set up library items recycler view
        setupLibraryItemsRecycler();

    }

    private void setupProfilePic() {
        FirebaseAuth firebaseAuth = ApplicationClass.getFirebaseAuth();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        assert user != null;
        Glide.with(requireContext()).load(user.getPhotoUrl()).placeholder(R.drawable.user).into(binding.ivProfilePic);
    }


    private void setupFilterChips() {
        // Set default selected chip
        binding.chipPlaylists.setChecked(true);


        // Set chip click listeners
        binding.filterChipGroup.setOnCheckedStateChangeListener((group, checkedIds) -> {
            if (checkedIds.size() > 0) {
                int chipId = checkedIds.get(0);
                // Handle filter selection
                if (chipId == R.id.chip_playlists) {
                    filterLibraryItems("Playlist");
                } else if (chipId == R.id.chip_albums) {
                    filterLibraryItems("Album");
                } else if (chipId == R.id.chip_artists) {
                    filterLibraryItems("Artist");
                } else if (chipId == R.id.chip_podcasts) {
                    filterLibraryItems("Podcast");
                } else if (chipId == R.id.chip_downloaded) {
                    // Filter downloaded items
                } else if (chipId == R.id.chip_following) {
                    // Filter following items
                }
            }
        });
    }

    private void setupRecentPlaysRecycler() {
        // Create sample data
        List<MusicItem> recentPlays = getSampleRecentPlays();

        // Set up adapter
        recentPlayAdapter = new RecentPlayAdapter(requireContext(), recentPlays);
        binding.recentPlaysRecycler.setAdapter(recentPlayAdapter);

        // Set layout manager
        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
        binding.recentPlaysRecycler.setLayoutManager(layoutManager);

        // Add animation
        animateRecentPlays();
    }

    private void setupLibraryItemsRecycler() {
        // Create sample data
        List<MusicItem> libraryItems = getSampleLibraryItems();

        // Set up adapter
        libraryItemAdapter = new LibraryItemAdapter(requireContext(), libraryItems);
        binding.libraryItemsRecycler.setAdapter(libraryItemAdapter);

        // Set layout manager
        binding.libraryItemsRecycler.setLayoutManager(new LinearLayoutManager(requireContext()));
    }

    private void animateRecentPlays() {
        // Apply animation to recent plays items
        binding.recentPlaysRecycler.setLayoutAnimation(AnimationUtils.loadLayoutAnimation(requireContext(), R.anim.layout_animation_slide_from_bottom));
        binding.recentPlaysRecycler.scheduleLayoutAnimation();
    }

    private void filterLibraryItems(String type) {
        // Filter library items by type
        List<MusicItem> filteredItems = new ArrayList<>();
        for (MusicItem item : getSampleLibraryItems()) {
            if (item.getType().equals(type) || type.equals("All")) {
                filteredItems.add(item);
            }
        }

        // Update adapter with filtered items
        libraryItemAdapter.updateItems(filteredItems);
    }

    private List<MusicItem> getSampleRecentPlays() {
        List<MusicItem> items = new ArrayList<>();
        items.add(new MusicItem(1, "Cosmic Vibes", "Playlist", "You", R.drawable.baseline_album_24));
        items.add(new MusicItem(2, "Purple Haze", "Playlist", "You", R.drawable.baseline_album_24));
        items.add(new MusicItem(3, "Midnight Dreams", "Album", "Luna Eclipse", R.drawable.baseline_album_24));
        items.add(new MusicItem(4, "Synthwave Nights", "Playlist", "You", R.drawable.baseline_album_24));
        items.add(new MusicItem(5, "Neon Beats", "Album", "Cyber Pulse", R.drawable.baseline_album_24));
        items.add(new MusicItem(6, "Astral Journey", "Playlist", "You", R.drawable.baseline_album_24));
        return items;
    }

    private List<MusicItem> getSampleLibraryItems() {
        List<MusicItem> items = new ArrayList<>();
        items.add(new MusicItem(1, "My Favorites ✨", "Playlist", "You", R.drawable.baseline_album_24));
        items.add(new MusicItem(2, "Discover Weekly", "Playlist", "Made for You", R.drawable.baseline_album_24));
        items.add(new MusicItem(3, "After Hours", "Album", "The Weeknd", R.drawable.baseline_album_24));
        items.add(new MusicItem(4, "Dawn FM", "Album", "The Weeknd", R.drawable.baseline_album_24));
        items.add(new MusicItem(5, "Stellar Sounds", "Artist", "", R.drawable.baseline_album_24));
        items.add(new MusicItem(6, "Cosmic Harmony", "Single", "Stellar Sounds", R.drawable.baseline_album_24));
        items.add(new MusicItem(7, "Purple Rain", "Single", "Prince", R.drawable.baseline_album_24));
        return items;
    }


//    private String formatMillis(long millis) {
//        Date date = new Date(millis);
//        @SuppressLint("SimpleDateFormat")
//        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("MM-dd-yyyy HH:mm a");
//        return simpleDateFormat.format(date);
//    }
//
//
//    private void showData(SharedPreferenceManager sharedPreferenceManager) {
//        savedLibraries = sharedPreferenceManager.getSavedLibrariesData();
//        binding.emptyListTv.setVisibility(savedLibraries == null ? View.VISIBLE : View.GONE);
//        if (savedLibraries != null)
//            binding.recyclerView.setAdapter(new SavedLibrariesAdapter(savedLibraries.lists()));
//    }
//
//    private void showData() {
//        showData(SharedPreferenceManager.getInstance(requireContext()));
//    }


}