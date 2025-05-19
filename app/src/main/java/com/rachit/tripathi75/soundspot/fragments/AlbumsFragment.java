package com.rachit.tripathi75.soundspot.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;

import com.rachit.tripathi75.soundspot.R;
import com.rachit.tripathi75.soundspot.adapters.ActivityArtistProfileTopAlbumsAdapter;
import com.rachit.tripathi75.soundspot.classes.GridSpacingItemDecoration;
import com.rachit.tripathi75.soundspot.databinding.FragmentAlbumsBinding;
import com.rachit.tripathi75.soundspot.records.AlbumsSearch;

import java.util.ArrayList;
import java.util.List;


public class AlbumsFragment extends Fragment {

    private FragmentAlbumsBinding binding;

    private static final String ARTIST_ID = "999";
    private static final String ALBUMS = "albums";

    private String artistId;
    private List<AlbumsSearch.Data.Results> albums;


    public AlbumsFragment() {
        // Required empty public constructor
    }

    public static AlbumsFragment newInstance(String artistId, List<AlbumsSearch.Data.Results> albums) {
        AlbumsFragment fragment = new AlbumsFragment();
        Bundle args = new Bundle();
        args.putString(ARTIST_ID, artistId);
        args.putSerializable(ALBUMS, new ArrayList<>(albums));
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            artistId = getArguments().getString(ARTIST_ID);
            albums = (List<AlbumsSearch.Data.Results>) getArguments().getSerializable(ALBUMS);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentAlbumsBinding.inflate(inflater, container, false);
        GridLayoutManager layoutManager = new GridLayoutManager(requireContext(), 2);
        binding.topAlbumsRecyclerView.setLayoutManager(layoutManager);

        binding.topAlbumsRecyclerView.setHasFixedSize(true);

        // Add spacing between grid items
//        int spacing = getResources().getDimensionPixelSize(R.dimen.grid_spacing); // define in dimens.xml
//        binding.topAlbumsRecyclerView.addItemDecoration(new GridSpacingItemDecoration(1, spacing, true));

        binding.topAlbumsRecyclerView.setAdapter(new ActivityArtistProfileTopAlbumsAdapter(albums));


        return binding.getRoot();
    }
}