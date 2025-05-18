package com.rachit.tripathi75.soundspot.adapters;

// TabPagerAdapter.java

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import com.rachit.tripathi75.soundspot.fragments.DummyFragment;

public class TabPageAdapter extends FragmentStateAdapter {

    public TabPageAdapter(@NonNull FragmentActivity fragmentActivity) {
        super(fragmentActivity);
    }

    @NonNull
    @Override
    public Fragment createFragment(int position) {
        switch (position) {
            case 0:
                // TopTracksFragment()
                return new DummyFragment();
            case 1:
                // ClipsFragment()
                return new DummyFragment();
            case 2:
                // SinglesFragment()
                return new DummyFragment();
            case 3:
                // AlbumsFragment()
                return new DummyFragment();
            case 4:
                // AboutFragment()
                return new DummyFragment();
            case 5:
                // EventsFragment()
                return new DummyFragment();
            default:
                // TopTracksFragment()
                return new DummyFragment();
        }
    }

    @Override
    public int getItemCount() {
        return 6;
    }
}