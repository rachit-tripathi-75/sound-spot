package com.rachit.tripathi75.soundspot.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.LinearLayoutManager;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;

import com.rachit.tripathi75.soundspot.R;
import com.rachit.tripathi75.soundspot.adapters.ImportedPlaylistAdapter;
import com.rachit.tripathi75.soundspot.classes.ImportedPlaylist;
import com.rachit.tripathi75.soundspot.databinding.FragmentPlaylistSelectionBinding;

import java.util.ArrayList;
import java.util.List;


public class ImportPlaylistPlaylistSelectionFragment extends Fragment implements ImportedPlaylistAdapter.OnPlaylistSelectionListener{

    private FragmentPlaylistSelectionBinding binding;
    private ImportedPlaylistAdapter adapter;
    private final List<ImportedPlaylist> playlists = new ArrayList<>();
    private final List<Integer> selectedPlaylists = new ArrayList<>();
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public ImportPlaylistPlaylistSelectionFragment() {
        // Required empty public constructor
    }


    public static ImportPlaylistPlaylistSelectionFragment newInstance(String param1, String param2) {
        ImportPlaylistPlaylistSelectionFragment fragment = new ImportPlaylistPlaylistSelectionFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentPlaylistSelectionBinding.inflate(inflater, container, false);



        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


        // Set up playlist data (mock data)
        setupMockData();

        // Set up RecyclerView
        adapter = new ImportedPlaylistAdapter(playlists, this);
        binding.rvPlaylists.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.rvPlaylists.setAdapter(adapter);

        // Apply animation to RecyclerView items
        binding.rvPlaylists.setLayoutAnimation(AnimationUtils.loadLayoutAnimation(requireContext(), R.anim.layout_animation_slide_from_bottom));

        // Set up select all button
        binding.btnSelectAll.setOnClickListener(v -> {
            v.startAnimation(AnimationUtils.loadAnimation(requireContext(), R.anim.button_press));
            toggleSelectAll();
        });

        // Set up add to library button
        binding.btnAddToLibrary.setOnClickListener(v -> {
            if (!selectedPlaylists.isEmpty()) {
                v.startAnimation(AnimationUtils.loadAnimation(requireContext(), R.anim.button_press));
                Navigation.findNavController(view).navigate(R.id.action_playlistSelectionFragment_to_transferStatusFragment);
            }
        });

        // Set up search button
        view.findViewById(R.id.btnSearch).setOnClickListener(v -> {
            v.startAnimation(AnimationUtils.loadAnimation(requireContext(), R.anim.button_press));
            // Would show search UI
        });

        // Update UI state
        updateSelectionState();

    }



    private void setupMockData() {
        playlists.add(new ImportedPlaylist(1, "Chill Vibes", 42));
        playlists.add(new ImportedPlaylist(2, "Workout Mix", 28));
        playlists.add(new ImportedPlaylist(3, "Focus Flow", 35));
        playlists.add(new ImportedPlaylist(4, "Evening Jazz", 18));
    }

    private void toggleSelectAll() {
        if (selectedPlaylists.size() == playlists.size()) {
            // Deselect all
            selectedPlaylists.clear();
            binding.btnSelectAll.setText(R.string.select_all);
        } else {
            // Select all
            selectedPlaylists.clear();
            for (ImportedPlaylist playlist : playlists) {
                selectedPlaylists.add(playlist.getId());
            }
            binding.btnSelectAll.setText("Deselect All");
        }
        adapter.notifyDataSetChanged();
        updateSelectionState();
    }

    private void updateSelectionState() {
        int count = selectedPlaylists.size();
        binding.tvSelectedCount.setText(count + (count == 1 ? " Playlist Selected" : " Playlists Selected"));
        binding.btnAddToLibrary.setEnabled(count > 0);
        binding.btnAddToLibrary.setAlpha(count > 0 ? 1.0f : 0.5f);
    }

    @Override
    public void onPlaylistSelected(int playlistId, boolean isSelected) {
        if (isSelected) {
            if (!selectedPlaylists.contains(playlistId)) {
                selectedPlaylists.add(playlistId);
            }
        } else {
            selectedPlaylists.remove(Integer.valueOf(playlistId));
        }
        updateSelectionState();
    }

    @Override
    public boolean isPlaylistSelected(int playlistId) {
        return selectedPlaylists.contains(playlistId);
    }

}