package com.rachit.tripathi75.soundspot.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;

import com.rachit.tripathi75.soundspot.R;
import com.rachit.tripathi75.soundspot.adapters.ChartSongsAdapter;
import com.rachit.tripathi75.soundspot.adapters.TrendingAdapter;
import com.rachit.tripathi75.soundspot.model.ChartSong;
import com.rachit.tripathi75.soundspot.model.TrendingItem;

import java.util.ArrayList;
import java.util.List;


public class ChartsFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_charts, container, false);

        // Initialize header
        View headerView = view.findViewById(R.id.category_header);
        headerView.setBackgroundResource(R.color.red_400);
        headerView.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.fade_in));

        // Setup recycler views
        RecyclerView topGlobalRecyclerView = view.findViewById(R.id.top_global_recycler_view);
        RecyclerView trendingRecyclerView = view.findViewById(R.id.trending_recycler_view);

        // Create data
        List<ChartSong> topGlobalSongs = new ArrayList<>();
        topGlobalSongs.add(new ChartSong(1, "Blinding Lights", "The Weeknd", R.drawable.baseline_album_24));
        topGlobalSongs.add(new ChartSong(2, "Shape of You", "Ed Sheeran", R.drawable.baseline_album_24));
        topGlobalSongs.add(new ChartSong(3, "Dance Monkey", "Tones and I", R.drawable.baseline_album_24));
        topGlobalSongs.add(new ChartSong(4, "Someone You Loved", "Lewis Capaldi", R.drawable.baseline_album_24));
        topGlobalSongs.add(new ChartSong(5, "Sunflower", "Post Malone, Swae Lee", R.drawable.baseline_album_24));

        List<TrendingItem> trendingItems = new ArrayList<>();
        trendingItems.add(new TrendingItem("As It Was", "Harry Styles", R.drawable.baseline_album_24, R.color.purple_500));
        trendingItems.add(new TrendingItem("About Damn Time", "Lizzo", R.drawable.baseline_album_24, R.color.green_500));
        trendingItems.add(new TrendingItem("First Class", "Jack Harlow", R.drawable.baseline_album_24, R.color.orange_500));

        // Setup adapters
        ChartSongsAdapter chartSongsAdapter = new ChartSongsAdapter(requireContext(), topGlobalSongs);
        topGlobalRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        topGlobalRecyclerView.setAdapter(chartSongsAdapter);

        TrendingAdapter trendingAdapter = new TrendingAdapter(trendingItems);
        trendingRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        trendingRecyclerView.setAdapter(trendingAdapter);

        // Apply animations
        topGlobalRecyclerView.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.slide_up));
        trendingRecyclerView.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.slide_up));

        return view;
    }
}