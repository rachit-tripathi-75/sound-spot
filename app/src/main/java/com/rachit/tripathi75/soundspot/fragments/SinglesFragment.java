package com.rachit.tripathi75.soundspot.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.rachit.tripathi75.soundspot.adapters.ActivityArtistProfileTopSinglesAdapter;
import com.rachit.tripathi75.soundspot.databinding.FragmentSinglesBinding;
import com.rachit.tripathi75.soundspot.records.AlbumsSearch;

import java.util.ArrayList;
import java.util.List;


public class SinglesFragment extends Fragment {
    private FragmentSinglesBinding binding;

    private static final String ARTIST_ID = "999";
    private static final String SINGLES = "artist_singles";
    private String artistId;
    private List<AlbumsSearch.Data.Results> singles;


    public SinglesFragment() {
        // Required empty public constructor
    }

    public static SinglesFragment newInstance(String artistId, List<AlbumsSearch.Data.Results> singles) {
        SinglesFragment fragment = new SinglesFragment();
        Bundle args = new Bundle();
        args.putString(ARTIST_ID, artistId);
        args.putSerializable(SINGLES, new ArrayList<>(singles));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            artistId = getArguments().getString(ARTIST_ID);
            singles = (List<AlbumsSearch.Data.Results>) getArguments().getSerializable(SINGLES);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentSinglesBinding.inflate(inflater, container, false);

        binding.topSinglesRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.topSinglesRecyclerView.setAdapter(new ActivityArtistProfileTopSinglesAdapter(singles));


        return binding.getRoot();
    }
}