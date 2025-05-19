package com.rachit.tripathi75.soundspot.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.Gson;
import com.rachit.tripathi75.soundspot.adapters.ActivityArtistProfileTopSongsAdapter;
import com.rachit.tripathi75.soundspot.databinding.FragmentTopSongsBinding;
import com.rachit.tripathi75.soundspot.records.SongResponse;

import java.util.ArrayList;
import java.util.List;


public class TopSongsFragment extends Fragment {

    private FragmentTopSongsBinding binding;
    private static final String ARTIST_ID = "999";
    private static final String TOP_SONGS = "top_songs";

    private String artistId;
    private List<SongResponse.Song> topSongs;

    public TopSongsFragment() {
        // Required empty public constructor
    }

    public static TopSongsFragment newInstance(String artistId, List<SongResponse.Song> topSongs) {
        TopSongsFragment fragment = new TopSongsFragment();
        Bundle args = new Bundle();
        args.putString(ARTIST_ID, artistId);
        args.putSerializable(TOP_SONGS, new ArrayList<>(topSongs));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            artistId = getArguments().getString(ARTIST_ID);
            topSongs = (List<SongResponse.Song>) getArguments().getSerializable(TOP_SONGS);
        }
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentTopSongsBinding.inflate(inflater, container, false);

        initialisers();


        return binding.getRoot();
    }

    private void initialisers() {
        binding.topSongsRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));

        Gson gson = new Gson();
        Log.d("TopSongsData", gson.toJson(topSongs));
        binding.topSongsRecyclerView.setAdapter(new ActivityArtistProfileTopSongsAdapter(topSongs));
    }

}