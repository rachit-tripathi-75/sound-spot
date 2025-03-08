package com.rachit.tripathi75.soundspot.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.res.ColorStateList;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.rachit.tripathi75.soundspot.ApplicationClass;
import com.rachit.tripathi75.soundspot.R;
import com.rachit.tripathi75.soundspot.activities.AboutActivity;
import com.rachit.tripathi75.soundspot.activities.HostActivity;
import com.rachit.tripathi75.soundspot.activities.MusicOverviewActivity;
import com.rachit.tripathi75.soundspot.activities.SavedLibrariesActivity;
import com.rachit.tripathi75.soundspot.activities.SettingsActivity;
import com.rachit.tripathi75.soundspot.adapters.ActivityMainAlbumItemAdapter;
import com.rachit.tripathi75.soundspot.adapters.ActivityMainArtistsItemAdapter;
import com.rachit.tripathi75.soundspot.adapters.ActivityMainPlaylistAdapter;
import com.rachit.tripathi75.soundspot.adapters.ActivityMainPopularSongs;
import com.rachit.tripathi75.soundspot.adapters.SavedLibrariesAdapter;
import com.rachit.tripathi75.soundspot.databinding.FragmentHomeBinding;
import com.rachit.tripathi75.soundspot.model.AlbumItem;
import com.rachit.tripathi75.soundspot.network.ApiManager;
import com.rachit.tripathi75.soundspot.network.NetworkChangeReceiver;
import com.rachit.tripathi75.soundspot.network.utility.RequestNetwork;
import com.rachit.tripathi75.soundspot.records.AlbumsSearch;
import com.rachit.tripathi75.soundspot.records.ArtistsSearch;
import com.rachit.tripathi75.soundspot.records.PlaylistsSearch;
import com.rachit.tripathi75.soundspot.records.SongSearch;
import com.rachit.tripathi75.soundspot.records.sharedpref.SavedLibraries;
import com.rachit.tripathi75.soundspot.utils.SharedPreferenceManager;
import com.squareup.picasso.Picasso;
import com.yarolegovich.slidingrootnav.SlidingRootNav;
import com.yarolegovich.slidingrootnav.SlidingRootNavBuilder;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;


public class HomeFragment extends Fragment {

    private FragmentHomeBinding binding;

    private final String TAG = "MainActivity";
    private ApplicationClass applicationClass;
    private final List<AlbumItem> songs = new ArrayList<>();
    private final List<ArtistsSearch.Data.Results> artists = new ArrayList<>();
    private final List<AlbumItem> albums = new ArrayList<>();
    private final List<AlbumItem> playlists = new ArrayList<>();

    private Context context;
    private SlidingRootNav slidingRootNavBuilder;

    Handler handler = new Handler();
    Runnable runnable = this::showPlayBarData;

    public HomeFragment(Context context) {
        // Required empty public constructor
        this.context = context;
    }

    NetworkChangeReceiver networkChangeReceiver = new NetworkChangeReceiver(new NetworkChangeReceiver.NetworkStatusListener() {
        @Override
        public void onNetworkConnected() {
            if (songs.isEmpty() || artists.isEmpty() || albums.isEmpty() || playlists.isEmpty())
                showData();
        }

        @Override
        public void onNetworkDisconnected() {
            if (songs.isEmpty() || artists.isEmpty() || albums.isEmpty() || playlists.isEmpty())
                showOfflineData();
            Snackbar.make(binding.getRoot(), "No Internet Connection", Snackbar.LENGTH_LONG).show();
        }
    });

    public static int calculateNoOfColumns(Context context, float columnWidthDp) { // For example columnWidthDp=180
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float screenWidthDp = displayMetrics.widthPixels / displayMetrics.density;
        return (int) (screenWidthDp / columnWidthDp + 0.5); // +0.5 for correct rounding to int.
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        binding = FragmentHomeBinding.inflate(inflater, container, false);


        // ------------------------------------------------------

        applicationClass = (ApplicationClass) requireActivity().getApplication();
        ApplicationClass.setCurrentActivity(requireActivity());
        ApplicationClass.updateTheme();

        slidingRootNavBuilder = new SlidingRootNavBuilder(requireActivity())
                .withMenuLayout(R.layout.main_drawer_layout)
                .withContentClickableWhenMenuOpened(false)
                .withDragDistance(250)
                .inject();

        onDrawerItemsClicked();

//        binding.profileIcon.setOnClickListener(view -> slidingRootNavBuilder.openMenu(true));

        int span = calculateNoOfColumns(context, 200);
        binding.playlistRecyclerView.setLayoutManager(new GridLayoutManager(context, span));

        binding.popularSongsRecyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        binding.popularArtistsRecyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        binding.popularAlbumsRecyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));
        binding.savedRecyclerView.setLayoutManager(new LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false));

        OverScrollDecoratorHelper.setUpOverScroll(binding.popularSongsRecyclerView, OverScrollDecoratorHelper.ORIENTATION_HORIZONTAL);
        OverScrollDecoratorHelper.setUpOverScroll(binding.popularArtistsRecyclerView, OverScrollDecoratorHelper.ORIENTATION_HORIZONTAL);
        OverScrollDecoratorHelper.setUpOverScroll(binding.popularAlbumsRecyclerView, OverScrollDecoratorHelper.ORIENTATION_HORIZONTAL);
        OverScrollDecoratorHelper.setUpOverScroll(binding.savedRecyclerView, OverScrollDecoratorHelper.ORIENTATION_HORIZONTAL);


        binding.refreshLayout.setOnRefreshListener(() -> {
            showShimmerData();
            showData();
            binding.refreshLayout.setRefreshing(false);
        });

        binding.playBarPlayPauseIcon.setOnClickListener(view -> {
            ApplicationClass applicationClass = (ApplicationClass) requireActivity().getApplication();
            ApplicationClass.setCurrentActivity(requireActivity());

            applicationClass.togglePlayPause();
            applicationClass.showNotification(ApplicationClass.player.isPlaying() ? R.drawable.baseline_pause_24 : R.drawable.play_arrow_24px);
            binding.playBarPlayPauseIcon.setImageResource(ApplicationClass.player.isPlaying() ? R.drawable.baseline_pause_24 : R.drawable.play_arrow_24px);
        });

        binding.playBarBackground.setOnClickListener(view -> {
            if (!ApplicationClass.MUSIC_ID.isBlank())
                startActivity(new Intent(requireContext(), MusicOverviewActivity.class).putExtra("id", ApplicationClass.MUSIC_ID));
        });

        binding.playBarPrevIcon.setOnClickListener(view -> {
            applicationClass.prevTrack();
        });

        binding.playBarNextIcon.setOnClickListener(view -> {
            applicationClass.nextTrack();
        });

        showShimmerData();
        showOfflineData();

        //showData();
        showPlayBarData();

        showSavedLibrariesData();

        // ----------------------------------------------


        return binding.getRoot();
    }

    private void showSavedLibrariesData() {
        SavedLibraries savedLibraries = SharedPreferenceManager.getInstance(context).getSavedLibrariesData();
        binding.savedLibrariesSection.setVisibility(savedLibraries != null && !(savedLibraries.lists().isEmpty()) ? View.VISIBLE : View.GONE);
        if (savedLibraries != null)
            binding.savedRecyclerView.setAdapter(new SavedLibrariesAdapter(savedLibraries.lists()));
    }

    void showPlayBarData() {
        binding.playBarMusicTitle.setText(ApplicationClass.MUSIC_TITLE);
        binding.playBarMusicDesc.setText(ApplicationClass.MUSIC_DESCRIPTION);
        Picasso.get().load(Uri.parse(ApplicationClass.IMAGE_URL)).into(binding.playBarCoverImage);
        if (ApplicationClass.player.isPlaying()) {
            binding.playBarPlayPauseIcon.setImageResource(R.drawable.baseline_pause_24);
        } else {
            binding.playBarPlayPauseIcon.setImageResource(R.drawable.play_arrow_24px);
        }

        GradientDrawable gradientDrawable = new GradientDrawable();
        gradientDrawable.setColor(ApplicationClass.IMAGE_BG_COLOR);
        gradientDrawable.setCornerRadius(18);

        binding.playBarBackground.setBackground(gradientDrawable);

        binding.playBarMusicTitle.setTextColor(ApplicationClass.TEXT_ON_IMAGE_COLOR1);
        binding.playBarMusicDesc.setTextColor(ApplicationClass.TEXT_ON_IMAGE_COLOR1);

        binding.playBarPlayPauseIcon.setImageTintList(ColorStateList.valueOf(ApplicationClass.TEXT_ON_IMAGE_COLOR));
        binding.playBarPrevIcon.setImageTintList(ColorStateList.valueOf(ApplicationClass.TEXT_ON_IMAGE_COLOR));
        binding.playBarNextIcon.setImageTintList(ColorStateList.valueOf(ApplicationClass.TEXT_ON_IMAGE_COLOR));

        OverScrollDecoratorHelper.setUpStaticOverScroll(binding.getRoot(), OverScrollDecoratorHelper.ORIENTATION_VERTICAL);

        handler.postDelayed(runnable, 1000);
    }

    private void showShimmerData() {
        final List<AlbumItem> data_shimmer = new ArrayList<>();
        final List<ArtistsSearch.Data.Results> artists_shimmer = new ArrayList<>();
        for (int i = 0; i < 11; i++) {
            data_shimmer.add(new AlbumItem("<shimmer>", "<shimmer>", "<shimmer>", "<shimmer>"));
            artists_shimmer.add(new ArtistsSearch.Data.Results(
                    "<shimmer>",
                    "<shimmer>",
                    "<shimmer>",
                    "<shimmer>",
                    "<shimmer>",
                    null
            ));
        }
        binding.popularSongsRecyclerView.setAdapter(new ActivityMainAlbumItemAdapter(data_shimmer));
        binding.popularAlbumsRecyclerView.setAdapter(new ActivityMainAlbumItemAdapter(data_shimmer));
        binding.popularArtistsRecyclerView.setAdapter(new ActivityMainArtistsItemAdapter(artists_shimmer));
        binding.playlistRecyclerView.setAdapter(new ActivityMainPlaylistAdapter(data_shimmer));

    }

    private void onDrawerItemsClicked() {
        slidingRootNavBuilder.getLayout().findViewById(R.id.settings).setOnClickListener(v -> {
            startActivity(new Intent(context, SettingsActivity.class));
            slidingRootNavBuilder.closeMenu();
        });

        slidingRootNavBuilder.getLayout().findViewById(R.id.logo).setOnClickListener(view -> slidingRootNavBuilder.closeMenu());

        slidingRootNavBuilder.getLayout().findViewById(R.id.library).setOnClickListener(view -> {
            startActivity(new Intent(context, SavedLibrariesActivity.class));
            slidingRootNavBuilder.closeMenu();
        });

        slidingRootNavBuilder.getLayout().findViewById(R.id.about).setOnClickListener(view -> {
            startActivity(new Intent(context, AboutActivity.class));
            slidingRootNavBuilder.closeMenu();
        });

    }

    private void showData() {
        songs.clear();
        artists.clear();
        albums.clear();
        playlists.clear();

        final ApiManager apiManager = new ApiManager(requireActivity());

        apiManager.searchSongs(" ", 0, 15, new RequestNetwork.RequestListener() {
            @Override
            public void onResponse(String tag, String response, HashMap<String, Object> responseHeaders) {
                SongSearch songSearch = new Gson().fromJson(response, SongSearch.class);
                Log.i(TAG, "onResponse: " + response);
                if (songSearch.success()) {
                    songSearch.data().results().forEach(results -> {
                        songs.add(new AlbumItem(results.name(), results.language() + " " + results.year(), results.image().get(results.image().size() - 1).url(), results.id()));
                        ActivityMainPopularSongs adapter = new ActivityMainPopularSongs(songs);
                        binding.popularSongsRecyclerView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    });
                    ApplicationClass.sharedPreferenceManager.setHomeSongsRecommended(songSearch);
                } else {
                    try {
                        showOfflineData();
                        Toast.makeText(context, new JSONObject(response).getString("message"), Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        Log.e(TAG, "onResponse: ", e);
                    }
                }
            }

            @Override
            public void onErrorResponse(String tag, String message) {
                showOfflineData();
            }
        });

        apiManager.searchArtists(" ", 0, 15, new RequestNetwork.RequestListener() {
            @Override
            public void onResponse(String tag, String response, HashMap<String, Object> responseHeaders) {
                ArtistsSearch artistSearch = new Gson().fromJson(response, ArtistsSearch.class);
                Log.i(TAG, "onResponse: " + response);
                if (artistSearch.success()) {
                    artistSearch.data().results().forEach(results -> {
                        artists.add(results);
                        ActivityMainArtistsItemAdapter adapter = new ActivityMainArtistsItemAdapter(artists);
                        binding.popularArtistsRecyclerView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    });
                    ApplicationClass.sharedPreferenceManager.setHomeArtistsRecommended(artistSearch);
                } else {
                    try {
                        showOfflineData();
                        Toast.makeText(context, new JSONObject(response).getString("message"), Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        Log.e(TAG, "onResponse: ", e);
                    }
                }
            }

            @Override
            public void onErrorResponse(String tag, String message) {
                showOfflineData();
            }
        });

        apiManager.searchAlbums(" ", 0, 15, new RequestNetwork.RequestListener() {
            @Override
            public void onResponse(String tag, String response, HashMap<String, Object> responseHeaders) {
                AlbumsSearch albumsSearch = new Gson().fromJson(response, AlbumsSearch.class);
                Log.i(TAG, "onResponse: " + response);
                if (albumsSearch.success()) {
                    albumsSearch.data().results().forEach(results -> {
                        albums.add(new AlbumItem(results.name(), results.language() + " " + results.year(), results.image().get(results.image().size() - 1).url(), results.id()));
                        ActivityMainAlbumItemAdapter adapter = new ActivityMainAlbumItemAdapter(albums);
                        binding.popularAlbumsRecyclerView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    });
                    ApplicationClass.sharedPreferenceManager.setHomeAlbumsRecommended(albumsSearch);
                } else {
                    try {
                        Toast.makeText(context, new JSONObject(response).getString("message"), Toast.LENGTH_SHORT).show();
                        showOfflineData();
                    } catch (JSONException e) {
                        Log.e(TAG, "onResponse: ", e);
                    }
                }
            }

            @Override
            public void onErrorResponse(String tag, String message) {
                showOfflineData();
            }
        });

        // String.valueOf(Calendar.getInstance().get(Calendar.YEAR))
        apiManager.searchPlaylists("2024", 0, 15, new RequestNetwork.RequestListener() {
            @Override
            public void onResponse(String tag, String response, HashMap<String, Object> responseHeaders) {
                PlaylistsSearch playlistsSearch = new Gson().fromJson(response, PlaylistsSearch.class);
                Log.i(TAG, "onResponse: " + response);
                if (playlistsSearch.success()) {
                    playlistsSearch.data().results().forEach(results -> {
                        playlists.add(new AlbumItem(results.name(), "", results.image().get(results.image().size() - 1).url(), results.id()));
                        //binding.playlistRecyclerView.setAdapter(new ActivityMainPlaylistAdapter(playlists));

                        ActivityMainPlaylistAdapter adapter = new ActivityMainPlaylistAdapter(playlists);
                        binding.playlistRecyclerView.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    });
                    ApplicationClass.sharedPreferenceManager.setHomePlaylistRecommended(playlistsSearch);
                } else {
                    try {
                        Toast.makeText(context, new JSONObject(response).getString("message"), Toast.LENGTH_SHORT).show();
                        showOfflineData();
                    } catch (JSONException e) {
                        Log.e(TAG, "onResponse: ", e);
                    }
                }
            }

            @Override
            public void onErrorResponse(String tag, String message) {
                showOfflineData();
            }
        });
    }

    private void showOfflineData() {
        if (ApplicationClass.sharedPreferenceManager.getHomeSongsRecommended() != null) {
            SongSearch songSearch = ApplicationClass.sharedPreferenceManager.getHomeSongsRecommended();
            songSearch.data().results().forEach(results -> {
                songs.add(new AlbumItem(results.name(), results.language() + " " + results.year(), results.image().get(results.image().size() - 1).url(), results.id()));
                ActivityMainPopularSongs adapter = new ActivityMainPopularSongs(songs);
                binding.popularSongsRecyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            });
        }

        if (ApplicationClass.sharedPreferenceManager.getHomeArtistsRecommended() != null) {
            ArtistsSearch artistsSearch = ApplicationClass.sharedPreferenceManager.getHomeArtistsRecommended();
            artistsSearch.data().results().forEach(results -> {
                artists.add(results);
                ActivityMainArtistsItemAdapter adapter = new ActivityMainArtistsItemAdapter(artists);
                binding.popularArtistsRecyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            });
        }

        if (ApplicationClass.sharedPreferenceManager.getHomeAlbumsRecommended() != null) {
            AlbumsSearch albumsSearch = ApplicationClass.sharedPreferenceManager.getHomeAlbumsRecommended();
            albumsSearch.data().results().forEach(results -> {
                albums.add(new AlbumItem(results.name(), results.language() + " " + results.year(), results.image().get(results.image().size() - 1).url(), results.id()));
                ActivityMainAlbumItemAdapter adapter = new ActivityMainAlbumItemAdapter(albums);
                binding.popularAlbumsRecyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            });
        }

        if (ApplicationClass.sharedPreferenceManager.getHomePlaylistRecommended() != null) {
            PlaylistsSearch playlistsSearch = ApplicationClass.sharedPreferenceManager.getHomePlaylistRecommended();
            playlistsSearch.data().results().forEach(results -> {
                playlists.add(new AlbumItem(results.name(), "", results.image().get(results.image().size() - 1).url(), results.id()));
                //binding.playlistRecyclerView.setAdapter(new ActivityMainPlaylistAdapter(playlists));

                ActivityMainPlaylistAdapter adapter = new ActivityMainPlaylistAdapter(playlists);
                binding.playlistRecyclerView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            });
        }

        //showData(); //TODO: showData if new data is available
    }

}