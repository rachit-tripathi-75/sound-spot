package com.rachit.tripathi75.soundspot.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;

import com.rachit.tripathi75.soundspot.R;
import com.rachit.tripathi75.soundspot.adapters.GenresAdapter;
import com.rachit.tripathi75.soundspot.adapters.MoodsAdapter;
import com.rachit.tripathi75.soundspot.model.GenreItem;
import com.rachit.tripathi75.soundspot.model.MoodItem;

import java.util.ArrayList;
import java.util.List;


public class MoodGenresFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_mood_genres, container, false);

        // Initialize header
        View headerView = view.findViewById(R.id.category_header);
        headerView.setBackgroundResource(R.color.purple_400);
        headerView.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.fade_in));

        // Setup recycler views
        RecyclerView moodsRecyclerView = view.findViewById(R.id.moods_recycler_view);
        RecyclerView genresRecyclerView = view.findViewById(R.id.genres_recycler_view);

        // Create data
        List<MoodItem> moods = new ArrayList<>();
        moods.add(new MoodItem("Happy", "Upbeat tunes to lift your mood", R.drawable.baseline_album_24, R.color.yellow_400));
        moods.add(new MoodItem("Chill", "Relaxing beats for downtime", R.drawable.baseline_album_24, R.color.blue_400));
        moods.add(new MoodItem("Focus", "Music to help you concentrate", R.drawable.baseline_album_24, R.color.green_400));
        moods.add(new MoodItem("Workout", "Energetic tracks for exercise", R.drawable.baseline_album_24, R.color.red_400));

        List<GenreItem> genres = new ArrayList<>();
        genres.add(new GenreItem("Pop", "Chart-topping hits", R.drawable.baseline_album_24));
        genres.add(new GenreItem("Hip Hop", "Rap and trap beats", R.drawable.baseline_album_24));
        genres.add(new GenreItem("Rock", "Classic and modern rock", R.drawable.baseline_album_24));
        genres.add(new GenreItem("R&B", "Rhythm and blues", R.drawable.baseline_album_24));
        genres.add(new GenreItem("Electronic", "EDM and dance music", R.drawable.baseline_album_24));

        // Setup adapters
        MoodsAdapter moodsAdapter = new MoodsAdapter(requireContext(), moods);
        moodsRecyclerView.setLayoutManager(new GridLayoutManager(getContext(), 2));
        moodsRecyclerView.setAdapter(moodsAdapter);

        GenresAdapter genresAdapter = new GenresAdapter(requireContext(), genres);
        genresRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        genresRecyclerView.setAdapter(genresAdapter);

        // Apply animations
        moodsRecyclerView.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.slide_up));
        genresRecyclerView.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.slide_up));

        return view;
    }
}