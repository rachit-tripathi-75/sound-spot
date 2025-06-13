package com.rachit.tripathi75.soundspot.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.gson.Gson;
import com.rachit.tripathi75.soundspot.databinding.LyricsBottomSheetLayoutBinding;
import com.rachit.tripathi75.soundspot.network.ApiClient;
import com.rachit.tripathi75.soundspot.network.ApiServices;
import com.rachit.tripathi75.soundspot.responses.GeniusApiSearchResponse;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.nodes.Node;
import org.jsoup.nodes.TextNode;
import org.jsoup.select.Elements;

import java.io.IOException;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LyricsBottomSheetDialog extends BottomSheetDialogFragment {

    private LyricsBottomSheetLayoutBinding binding;

    private static final String ARG_SONG_TITLE = "song_title";
    private static final String ARG_ARTIST = "artist";

    private String receivedSongTitle;
    private String receivedArtist;


    public static LyricsBottomSheetDialog newInstance(String songTitle, String artist) {
        LyricsBottomSheetDialog fragment = new LyricsBottomSheetDialog();
        Bundle args = new Bundle();
        args.putString(ARG_SONG_TITLE, songTitle);
        args.putString(ARG_ARTIST, artist);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // Retrieve the arguments passed to the fragment
        if (getArguments() != null) {
            receivedSongTitle = getArguments().getString(ARG_SONG_TITLE);
            receivedArtist = getArguments().getString(ARG_ARTIST);
        } else {
            // Handle cases where arguments might be null (e.g., fragment recreated by system)
            // You might want to log an error or set default values
            receivedSongTitle = "Unknown Song";
            receivedArtist = "Unknown Artist";
        }
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = LyricsBottomSheetLayoutBinding.inflate(inflater, container, false);


        binding.tvLyricsSongTitle.setText(receivedSongTitle);
        binding.tvLyricsSongArtist.setText(receivedArtist);

        fetchLyrics(receivedSongTitle + receivedArtist);

        return binding.getRoot();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }

    private String getSongTitle(String songTitle) {
        if (songTitle == null) return null;
        String[] parts = songTitle.split("\\(", 2);
        return parts[0].trim();
    }

    private String getPrimaryArtistOfTheSong(String artists) {
        if (artists == null) {
            return null;
        }

        int commaIndex = artists.indexOf(',');
        if (commaIndex != -1) {
            return artists.substring(0, commaIndex).trim();
        } else {
            return artists;
        }
    }

    private void fetchLyrics(String query) {

        binding.progressBar.setVisibility(View.VISIBLE);
        binding.tvLyrics.setVisibility(View.GONE);

        ApiServices.GeniusLyricsApiService geniusLyricsApiService = ApiClient.getClient().create(ApiServices.GeniusLyricsApiService.class);
        geniusLyricsApiService.searchLyrics(
                "Bearer u_fIy2Ec7caSXucMz98ZeJeMqg3ofkhF6QpZfeKauACrJsF408F2cLmHvSCm6C33",
                "application/json",
                query
        ).enqueue(new Callback<GeniusApiSearchResponse>() {
            @Override
            public void onResponse(Call<GeniusApiSearchResponse> call, Response<GeniusApiSearchResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body() != null) {
                        if (!response.body().getResponse().getHits().isEmpty()) {
                            GeniusApiSearchResponse s = response.body();
                            Gson gson = new Gson();
                            Log.d("geniusLyricsTAG", gson.toJson(s.getResponse().getHits().get(0).getResult().getUrl()));
                            scrapLyrics(s.getResponse().getHits().get(0).getResult().getUrl());
                        } else {

                        }

                    }
                } else {
                    binding.progressBar.setVisibility(View.GONE);
                    Log.d("geniusLyricsTAG", "onResponse, response: " + new Gson().toJson(response.body()));
                }
            }

            @Override
            public void onFailure(Call<GeniusApiSearchResponse> call, Throwable t) {
                binding.progressBar.setVisibility(View.GONE);
                Log.d("geniusLyricsTAG", "onFailure, error: " + t.getMessage());
            }
        });
    }

    private void scrapLyrics(String songLyricsUrl) {
        new Thread(() -> {
            try {
                Document doc = Jsoup.connect(songLyricsUrl)
                        .userAgent("Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 " +
                                "(KHTML, like Gecko) Chrome/113.0.0.0 Safari/537.36")
                        .referrer("http://www.google.com")
                        .get();

                Elements containers = doc.select("div[class^=Lyrics__Container]");

                StringBuilder lyricsBuilder = new StringBuilder();
                boolean startCollecting = false;

                for (Element container : containers) {
                    for (Node node : container.childNodes()) {
                        String text;
                        if (node instanceof TextNode) {
                            text = ((TextNode) node).text();
                        } else if (node.nodeName().equals("br")) {
                            lyricsBuilder.append("\n");
                            continue;
                        } else if (node instanceof Element) {
                            text = ((Element) node).text();
                        } else {
                            continue;
                        }

                        // Trigger collection only when we find something like [Intro], [Verse 1], etc.
                        if (!startCollecting && text.matches("\\[.*\\]")) {
                            startCollecting = true;
                        }

                        if (startCollecting) {
                            lyricsBuilder.append(text).append("\n");
                        }
                    }

                    if (startCollecting) {
                        lyricsBuilder.append("\n\n");
                    }
                }

                String lyrics = lyricsBuilder.toString().trim();

                requireActivity().runOnUiThread(() -> {
                    binding.progressBar.setVisibility(View.GONE);
                    if (lyrics.isEmpty()) {
                        binding.tvLyrics.setVisibility(View.GONE);
                        binding.llNoLyrics.setVisibility(View.VISIBLE);
                    } else {
                        binding.tvLyrics.setVisibility(View.VISIBLE);
                        binding.tvLyrics.setText(lyrics);
                    }
                });

            } catch (IOException e) {
                e.printStackTrace();
                binding.progressBar.setVisibility(View.GONE);
                binding.tvLyrics.setVisibility(View.GONE);
                binding.llNoLyrics.setVisibility(View.VISIBLE);
                Log.d("geniusLyricsTAG", "catchBlock: " + e.getMessage());
            }
        }).start();
    }
}