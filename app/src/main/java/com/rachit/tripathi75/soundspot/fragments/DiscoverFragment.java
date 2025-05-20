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
import com.rachit.tripathi75.soundspot.adapters.PlaylistsAdapter;
import com.rachit.tripathi75.soundspot.adapters.SimilarArtistsAdapter;
import com.rachit.tripathi75.soundspot.model.PlaylistItem;
import com.rachit.tripathi75.soundspot.model.SimilarArtistItem;

import java.util.ArrayList;
import java.util.List;

public class DiscoverFragment extends Fragment {

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_discover, container, false);

        // Initialize header
        View headerView = view.findViewById(R.id.category_header);
        headerView.setBackgroundResource(R.color.yellow_400);
        headerView.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.fade_in));

        // Setup recycler views
        RecyclerView madeForYouRecyclerView = view.findViewById(R.id.made_for_you_recycler_view);
        RecyclerView similarArtistsRecyclerView = view.findViewById(R.id.similar_artists_recycler_view);

        // Create data
        List<PlaylistItem> madeForYouItems = new ArrayList<>();
        madeForYouItems.add(new PlaylistItem("Daily Mix 1", "Based on your listening", R.drawable.baseline_album_24, "bg-gradient-to-br from-blue-500 to-purple-500"));
        madeForYouItems.add(new PlaylistItem("Discover Weekly", "New discoveries every Monday", R.drawable.baseline_album_24, "bg-gradient-to-br from-green-500 to-teal-500"));
        madeForYouItems.add(new PlaylistItem("Release Radar", "New releases from artists you follow", R.drawable.baseline_album_24, "bg-gradient-to-br from-red-500 to-orange-500"));

        List<SimilarArtistItem> similarArtistItems = new ArrayList<>();
        similarArtistItems.add(new SimilarArtistItem("If you like Drake", "Check out J. Cole", R.drawable.baseline_album_24));
        similarArtistItems.add(new SimilarArtistItem("If you like Billie Eilish", "Check out Halsey", R.drawable.baseline_album_24));
        similarArtistItems.add(new SimilarArtistItem("If you like The Weeknd", "Check out Daft Punk", R.drawable.baseline_album_24));
        similarArtistItems.add(new SimilarArtistItem("If you like Taylor Swift", "Check out Olivia Rodrigo", R.drawable.baseline_album_24));

        // Setup adapters
        PlaylistsAdapter playlistsAdapter = new PlaylistsAdapter(requireContext(), madeForYouItems);
        madeForYouRecyclerView.setLayoutManager(new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false));
        madeForYouRecyclerView.setAdapter(playlistsAdapter);

        SimilarArtistsAdapter similarArtistsAdapter = new SimilarArtistsAdapter(requireContext(), similarArtistItems);
        similarArtistsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        similarArtistsRecyclerView.setAdapter(similarArtistsAdapter);

        // Apply animations
        madeForYouRecyclerView.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.slide_up));
        similarArtistsRecyclerView.startAnimation(AnimationUtils.loadAnimation(getContext(), R.anim.slide_up));

        return view;
    }

}