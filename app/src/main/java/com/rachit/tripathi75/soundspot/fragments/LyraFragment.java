package com.rachit.tripathi75.soundspot.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.rachit.tripathi75.soundspot.R;
import com.rachit.tripathi75.soundspot.databinding.FragmentLyraBinding;


public class LyraFragment extends Fragment {

    private FragmentLyraBinding binding;
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    public LyraFragment() {
        // Required empty public constructor
    }

    public static LyraFragment newInstance(String param1, String param2) {
        LyraFragment fragment = new LyraFragment();
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
        binding = FragmentLyraBinding.inflate(inflater, container, false);



        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        listeners();
    }

    private void listeners() {


        binding.btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String prompt = binding.etPrompt.getText().toString().trim();
                if (!prompt.isEmpty()) {
                    processPrompt(prompt);
                } else {
                    Toast.makeText(requireContext(), "Please enter a prompt", Toast.LENGTH_SHORT).show();
                }
            }
        });

        binding.btnCalmMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processPrompt("Calm Music for relaxation");
            }
        });

        binding.btnFocusStudyMusic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processPrompt("Focus & Study Music");
            }
        });

        binding.btnCommercialSoundtracks.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                processPrompt("Commercial Soundtracks");
            }
        });
    }

    private void processPrompt(String prompt) {
        // Check if this is an artist query
        if (prompt.toLowerCase().contains("taylor swift") ||
                prompt.toLowerCase().contains("artist")) {
            // Navigate to artist result
//            Intent intent = new Intent(requireContext(), ArtistResultActivity.class);
//            intent.putExtra("PROMPT", prompt);
//            startActivity(intent);
        } else {
            // Navigate to playlist result
//            Intent intent = new Intent(AIHelperActivity.this, PlaylistResultActivity.class);
//            intent.putExtra("PROMPT", prompt);
//            startActivity(intent);
        }
    }


}