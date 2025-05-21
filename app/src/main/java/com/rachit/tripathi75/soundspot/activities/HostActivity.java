package com.rachit.tripathi75.soundspot.activities;

import android.app.ActivityOptions;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Pair;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.rachit.tripathi75.soundspot.ApplicationClass;
import com.rachit.tripathi75.soundspot.R;
import com.rachit.tripathi75.soundspot.databinding.ActivityHostBinding;
import com.rachit.tripathi75.soundspot.fragments.HomeFragment;
import com.rachit.tripathi75.soundspot.fragments.ImportPlaylistFragment;
import com.rachit.tripathi75.soundspot.fragments.LibraryFragment;
import com.rachit.tripathi75.soundspot.fragments.SearchFragment;
import com.squareup.picasso.Picasso;

import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;
import me.ibrahimsn.lib.OnItemSelectedListener;

public class HostActivity extends AppCompatActivity {

    private ActivityHostBinding binding;
    private Handler handler = new Handler();
    private Runnable runnable = this::showPlayBarData;
    private ApplicationClass applicationClass;

    private Fragment homeFragment, searchFragment, libraryFragment, importPlaylistFragment;
    private Fragment activeFragment;
    private FragmentManager fragmentManager;

    private boolean isPlaying = false;
    private int progressStatus = 0;
    private final int MAX_PROGRESS = 100;
    private Runnable progressRunnable;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHostBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        fragmentManager = getSupportFragmentManager();

        homeFragment = new HomeFragment();
        searchFragment = new SearchFragment();
        importPlaylistFragment = new ImportPlaylistFragment();
        libraryFragment = new LibraryFragment();
        activeFragment = homeFragment;


        fragmentManager.beginTransaction()
                .add(R.id.fragment_container, homeFragment, "Home")
//                .add(R.id.fragment_container, searchFragment, "Search").hide(searchFragment)
                .add(R.id.fragment_container, importPlaylistFragment, "Import Playlist").hide(importPlaylistFragment)
                .add(R.id.fragment_container, libraryFragment, "Library").hide(libraryFragment)
                .commit();


        binding.ibPlayBarPlayPauseIcon.setOnClickListener(view -> {
            applicationClass = (ApplicationClass) getApplication();
            ApplicationClass.setCurrentActivity(HostActivity.this);

            applicationClass.togglePlayPause();
            applicationClass.showNotification(ApplicationClass.player.isPlaying() ? R.drawable.baseline_pause_24 : R.drawable.play_arrow_24px);
            binding.ibPlayBarPlayPauseIcon.setImageResource(ApplicationClass.player.isPlaying() ? R.drawable.baseline_pause_24 : R.drawable.play_arrow_24px);
        });

        binding.playerContainer.setOnClickListener(view -> {
            if (!ApplicationClass.MUSIC_ID.isBlank())
                openFullPlayer();
        });

        binding.ibPlayBarPreviousIcon.setOnClickListener(view -> {
            applicationClass.prevTrack();
        });

        binding.ibPlayBarNextIcon.setOnClickListener(view -> {
            applicationClass.nextTrack();
        });

        showPlayBarData();
        setUpAnimation();
        setupClickListeners();


        binding.bottomBar.setOnItemSelectedListener((OnItemSelectedListener) position -> {
            if (position == 0) {
                switchFragment(homeFragment);
            } else if (position == 1) {
//                switchFragment(searchFragment);
//                switchFragment(searchFragment);
                startActivity(new Intent(this, ImportPlaylistActivity.class));
            } else if (position == 2) {
                switchFragment(libraryFragment);
            }
            return true;
        });
    }

    private void openFullPlayer() {

        // Create the transition animation pairs
        Pair<View, String> albumArtPair = Pair.create(binding.ivPlayBarAlbumArt, "album_art_transition");
        Pair<View, String> titlePair = Pair.create(binding.tvPlayBarMusicTitle, "title_transition");
        Pair<View, String> artistPair = Pair.create(binding.tvPlayBarArtistName, "artist_transition");
        Pair<View, String> playButtonPair = Pair.create(binding.ibPlayBarPlayPauseIcon, "play_button_transition");

        // Create the activity options with the transitions
        ActivityOptions options = ActivityOptions.makeSceneTransitionAnimation(
                this, albumArtPair, titlePair, artistPair, playButtonPair);

        startActivity(new Intent(this, MusicOverviewActivity.class).putExtra("id", ApplicationClass.MUSIC_ID), options.toBundle());
    }

    private void setUpAnimation() {
        // Animate player container
        Animation slideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up);
        slideUp.setStartOffset(500);
        binding.playerContainer.startAnimation(slideUp);
    }

    private void setupClickListeners() {
        binding.ibPlayBarPlayPauseIcon.setOnClickListener(v -> togglePlayPause());

        // Add click listeners for other buttons
    }

    private void togglePlayPause() {
        isPlaying = !isPlaying;

        // Change play/pause icon with animation
        binding.ibPlayBarPlayPauseIcon.animate()
                .scaleX(0.8f)
                .scaleY(0.8f)
                .setDuration(100)
                .withEndAction(() -> {
                    binding.ibPlayBarPlayPauseIcon.setImageResource(isPlaying ? R.drawable.ic_pause : R.drawable.ic_play);
                    binding.ibPlayBarPlayPauseIcon.animate()
                            .scaleX(1f)
                            .scaleY(1f)
                            .setDuration(100)
                            .start();
                })
                .start();

        // Show/hide and animate progress bar
        if (isPlaying) {
            binding.progressBar.setVisibility(View.VISIBLE);
            startProgressAnimation();
        } else {
            handler.removeCallbacks(progressRunnable);
        }
    }

    private void startProgressAnimation() {
        progressRunnable = new Runnable() {
            @Override
            public void run() {
                if (progressStatus < MAX_PROGRESS) {
                    progressStatus++;
                    binding.progressBar.setProgress(progressStatus);
                    handler.postDelayed(this, 300); // Update every 300ms
                } else {
                    progressStatus = 0;
                    binding.progressBar.setProgress(progressStatus);
                    isPlaying = false;
                    binding.ibPlayBarPlayPauseIcon.setImageResource(R.drawable.ic_play);
                }
            }
        };
        handler.post(progressRunnable);
    }

    private void switchFragment(Fragment fragment) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.hide(activeFragment).show(fragment).commit();
        activeFragment = fragment;
    }


    void showPlayBarData() {
        binding.tvPlayBarMusicTitle.setText(ApplicationClass.MUSIC_TITLE);
        binding.tvPlayBarArtistName.setText(ApplicationClass.MUSIC_DESCRIPTION);
        Picasso.get().load(Uri.parse(ApplicationClass.IMAGE_URL)).into(binding.ivPlayBarAlbumArt);

        if (ApplicationClass.player.isPlaying()) {
            binding.ibPlayBarPlayPauseIcon.setImageResource(R.drawable.baseline_pause_24);
        } else {
            binding.ibPlayBarPlayPauseIcon.setImageResource(R.drawable.play_arrow_24px);
        }

        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setColor(ApplicationClass.IMAGE_BG_COLOR);
        gradientDrawable.setCornerRadius(18);
        binding.playerContainer.setBackground(gradientDrawable);

        binding.tvPlayBarMusicTitle.setTextColor(ApplicationClass.TEXT_ON_IMAGE_COLOR1);
        binding.tvPlayBarArtistName.setTextColor(ApplicationClass.TEXT_ON_IMAGE_COLOR1);

//        binding.ibPlayBarPlayPauseIcon.setImageTintList(ColorStateList.valueOf(ApplicationClass.TEXT_ON_IMAGE_COLOR));
        binding.ibPlayBarPreviousIcon.setImageTintList(ColorStateList.valueOf(ApplicationClass.TEXT_ON_IMAGE_COLOR));
        binding.ibPlayBarNextIcon.setImageTintList(ColorStateList.valueOf(ApplicationClass.TEXT_ON_IMAGE_COLOR));

        OverScrollDecoratorHelper.setUpStaticOverScroll(binding.getRoot(), OverScrollDecoratorHelper.ORIENTATION_VERTICAL);

        handler.postDelayed(runnable, 1000);
    }

}
