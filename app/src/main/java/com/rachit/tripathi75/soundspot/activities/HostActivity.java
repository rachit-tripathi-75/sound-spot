package com.rachit.tripathi75.soundspot.activities;

import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.rachit.tripathi75.soundspot.ApplicationClass;
import com.rachit.tripathi75.soundspot.R;
import com.rachit.tripathi75.soundspot.databinding.ActivityHostBinding;
import com.rachit.tripathi75.soundspot.fragments.HomeFragment;
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

    private Fragment homeFragment, searchFragment, libraryFragment;
    private Fragment activeFragment;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityHostBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        fragmentManager = getSupportFragmentManager();

        homeFragment = new HomeFragment(HostActivity.this);
        searchFragment = new SearchFragment(HostActivity.this);
        libraryFragment = new LibraryFragment(HostActivity.this);
        activeFragment = homeFragment;


        fragmentManager.beginTransaction()
                .add(R.id.fragment_container, homeFragment, "Home")
                .add(R.id.fragment_container, searchFragment, "Search").hide(searchFragment)
                .add(R.id.fragment_container, libraryFragment, "Library").hide(libraryFragment)
                .commit();


        binding.playBarPlayPauseIcon.setOnClickListener(view -> {
            applicationClass = (ApplicationClass) getApplication();
            ApplicationClass.setCurrentActivity(HostActivity.this);

            applicationClass.togglePlayPause();
            applicationClass.showNotification(ApplicationClass.player.isPlaying() ? R.drawable.baseline_pause_24 : R.drawable.play_arrow_24px);
            binding.playBarPlayPauseIcon.setImageResource(ApplicationClass.player.isPlaying() ? R.drawable.baseline_pause_24 : R.drawable.play_arrow_24px);
        });

        binding.playBarBackground.setOnClickListener(view -> {
            if (!ApplicationClass.MUSIC_ID.isBlank())
                startActivity(new Intent(this, MusicOverviewActivity.class).putExtra("id", ApplicationClass.MUSIC_ID));
        });

        binding.playBarPrevIcon.setOnClickListener(view -> {
            applicationClass.prevTrack();
        });

        binding.playBarNextIcon.setOnClickListener(view -> {
            applicationClass.nextTrack();
        });

        showPlayBarData();



        binding.bottomBar.setOnItemSelectedListener((OnItemSelectedListener) position -> {
            if (position == 0) {
                switchFragment(homeFragment);
            } else if (position == 1) {
                switchFragment(searchFragment);
            } else if (position == 2) {
                switchFragment(libraryFragment);
            }
            return true;
        });
    }

    private void switchFragment(Fragment fragment) {
        if (fragment == activeFragment) return;

        FragmentTransaction transaction = fragmentManager.beginTransaction();
        transaction.hide(activeFragment);
        transaction.show(fragment);
        transaction.commit();

        activeFragment = fragment;
    }

    void showPlayBarData() {
        binding.playBarMusicTitle.setText(ApplicationClass.MUSIC_TITLE);
        binding.playBarMusicDesc.setText(ApplicationClass.MUSIC_DESCRIPTION);
        Picasso.get().load(Uri.parse(ApplicationClass.IMAGE_URL)).into(binding.playBarCoverImage);

        if (ApplicationClass.player.isPlaying()) {
            binding.playBarPlayPauseIcon.setImageResource(R.drawable.baseline_pause_24);
        } else {
            binding.playBarPlayPauseIcon.setImageResource(R.drawable.play_arrow_24px);
        }

        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setColor(ApplicationClass.IMAGE_BG_COLOR);
        gradientDrawable.setCornerRadius(18);
        binding.playBarBackground.setBackground(gradientDrawable);

        binding.playBarMusicTitle.setTextColor(ApplicationClass.TEXT_ON_IMAGE_COLOR1);
        binding.playBarMusicDesc.setTextColor(ApplicationClass.TEXT_ON_IMAGE_COLOR1);

        binding.playBarPlayPauseIcon.setImageTintList(ColorStateList.valueOf(ApplicationClass.TEXT_ON_IMAGE_COLOR));
        binding.playBarPrevIcon.setImageTintList(ColorStateList.valueOf(ApplicationClass.TEXT_ON_IMAGE_COLOR));
        binding.playBarNextIcon.setImageTintList(ColorStateList.valueOf(ApplicationClass.TEXT_ON_IMAGE_COLOR));

        OverScrollDecoratorHelper.setUpStaticOverScroll(binding.getRoot(), OverScrollDecoratorHelper.ORIENTATION_VERTICAL);

        handler.postDelayed(runnable, 1000);
    }
}
