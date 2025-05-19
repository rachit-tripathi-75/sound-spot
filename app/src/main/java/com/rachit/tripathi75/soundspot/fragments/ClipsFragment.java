package com.rachit.tripathi75.soundspot.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.rachit.tripathi75.soundspot.R;
import com.rachit.tripathi75.soundspot.databinding.FragmentClipsBinding;


public class ClipsFragment extends Fragment {

    private FragmentClipsBinding binding;
    private static final String ARTIST_ID = "999";
    private static final String CLIPS = "artist_clips";


    private String artistId;
    private String clips;

    public ClipsFragment() {
        // Required empty public constructor
    }
    public static ClipsFragment newInstance(String artistId, Boolean flag) {
        ClipsFragment fragment = new ClipsFragment();
        Bundle args = new Bundle();
        args.putString(ARTIST_ID, artistId);
        args.putBoolean(CLIPS, flag);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            artistId = getArguments().getString(ARTIST_ID);
            clips = String.valueOf(getArguments().getBoolean(CLIPS));
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentClipsBinding.inflate(inflater, container, false);



        return binding.getRoot();
    }
}