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
import com.rachit.tripathi75.soundspot.adapters.AlbumsAdapter;
import com.rachit.tripathi75.soundspot.adapters.ForYouAdapter;
import com.rachit.tripathi75.soundspot.adapters.TopSongsAdapter;
import com.rachit.tripathi75.soundspot.model.NewReleaseAlbum;
import com.rachit.tripathi75.soundspot.model.NewReleaseSong;

import java.util.ArrayList;
import java.util.List;


public class NewReleasesFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_new_releases, container, false);

        // Initialize header
        View headerView = view.findViewById(R.id.category_header);
        headerView.setBackgroundResource(R.color.teal_500);
        headerView.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.fade_in));

        // Setup recycler views
        RecyclerView newAlbumsRecyclerView = view.findViewById(R.id.new_albums_recycler_view);
        RecyclerView newSinglesRecyclerView = view.findViewById(R.id.new_singles_recycler_view);

        // Create data
        List<NewReleaseAlbum> newAlbums = new ArrayList<>();
        newAlbums.add(new NewReleaseAlbum("Renaissance", "Beyoncé", R.drawable.baseline_album_24, R.color.pink_500));
        newAlbums.add(new NewReleaseAlbum("Midnights", "Taylor Swift", R.drawable.baseline_album_24, R.color.blue_500));
        newAlbums.add(new NewReleaseAlbum("Harry's House", "Harry Styles", R.drawable.baseline_album_24, R.color.amber_500));

        List<NewReleaseSong> newSingles = new ArrayList<>();
        newSingles.add(new NewReleaseSong(1, "Flowers", "Miley Cyrus", R.drawable.baseline_album_24));
        newSingles.add(new NewReleaseSong(2, "Kill Bill", "SZA", R.drawable.baseline_album_24));
        newSingles.add(new NewReleaseSong(3, "Anti-Hero", "Taylor Swift", R.drawable.baseline_album_24));
        newSingles.add(new NewReleaseSong(4, "Unholy", "Sam Smith & Kim Petras", R.drawable.baseline_album_24));

        // Setup adapters
        AlbumsAdapter albumsAdapter = new AlbumsAdapter(newAlbums);
        newAlbumsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        newAlbumsRecyclerView.setAdapter(albumsAdapter);

        TopSongsAdapter songsAdapter = new TopSongsAdapter(newSingles);
        newSinglesRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        newSinglesRecyclerView.setAdapter(songsAdapter);

        // Apply animations
        newAlbumsRecyclerView.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.slide_up));
        newSinglesRecyclerView.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.slide_up));

        return view;
    }


}