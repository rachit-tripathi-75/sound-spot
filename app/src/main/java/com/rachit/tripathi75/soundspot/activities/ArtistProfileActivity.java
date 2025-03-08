package com.rachit.tripathi75.soundspot.activities;

import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.rachit.tripathi75.soundspot.adapters.ActivityArtistProfileTopAlbumsAdapter;
import com.rachit.tripathi75.soundspot.adapters.ActivityArtistProfileTopSongsAdapter;
import com.rachit.tripathi75.soundspot.adapters.ActivitySeeMoreListAdapter;
import com.rachit.tripathi75.soundspot.databinding.ActivityArtistProfileBinding;
import com.rachit.tripathi75.soundspot.model.BasicDataRecord;
import com.rachit.tripathi75.soundspot.network.ApiManager;
import com.rachit.tripathi75.soundspot.network.NetworkChangeReceiver;
import com.rachit.tripathi75.soundspot.network.utility.RequestNetwork;
import com.rachit.tripathi75.soundspot.records.AlbumsSearch;
import com.rachit.tripathi75.soundspot.records.ArtistSearch;
import com.rachit.tripathi75.soundspot.records.SongResponse;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * The `ArtistProfileActivity` class displays the profile information of an artist,
 * including their name, image, top songs, top albums, and singles.
 * It fetches the artist's data from a remote API and handles network connectivity changes.
 *
 * <p>
 * This activity uses a collapsing toolbar layout to provide a visually appealing
 * header that expands and collapses as the user scrolls.
 * It also utilizes Shimmer effect as placeholder while data is loading.
 * </p>
 */
public class ArtistProfileActivity extends AppCompatActivity {

    private final String TAG = "ArtistProfileActivity";
    ActivityArtistProfileBinding binding;

    NetworkChangeReceiver networkChangeReceiver = new NetworkChangeReceiver(new NetworkChangeReceiver.NetworkStatusListener() {
        @Override
        public void onNetworkConnected() {
            showData();
        }

        @Override
        public void onNetworkDisconnected() {
            Snackbar.make(binding.getRoot(), "No Internet Connection", Snackbar.LENGTH_LONG).show();
        }
    });

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityArtistProfileBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        setSupportActionBar(binding.collapsingToolbar);
        if (getSupportActionBar() != null) getSupportActionBar().setDisplayShowTitleEnabled(false);
        binding.collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));

        getWindow().getDecorView().setSystemUiVisibility(
                View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        getWindow().setStatusBarColor(Color.TRANSPARENT);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);

//        binding.collapsingToolbarLayout.setTitle("Artist Name");

        binding.collapsingToolbarAppbarlayout.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> {
            if (verticalOffset == 0) {
            } else {
            }
        });

        binding.topSongsRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        binding.topAlbumsRecyclerview.setLayoutManager(new LinearLayoutManager(this));
        binding.topSinglesRecyclerview.setLayoutManager(new LinearLayoutManager(this));

        binding.topSongsSeeMore.setOnClickListener(v -> {
            startActivity(new Intent(ArtistProfileActivity.this, SeeMoreActivity.class)
                    .putExtra("id", artistId)
                    .putExtra("type", ActivitySeeMoreListAdapter.Mode.TOP_SONGS.name())
                    .putExtra("artist_name", binding.artistName.getText().toString()));
        });
        binding.topAlbumsSeeMore.setOnClickListener(v -> {
            startActivity(new Intent(ArtistProfileActivity.this, SeeMoreActivity.class)
                    .putExtra("id", artistId)
                    .putExtra("type", ActivitySeeMoreListAdapter.Mode.TOP_ALBUMS.name())
                    .putExtra("artist_name", binding.artistName.getText().toString()));
        });
        binding.topSinglesSeeMore.setOnClickListener(v -> {

        });

        showShimmerData();
        //showData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        NetworkChangeReceiver.registerReceiver(this, networkChangeReceiver);
    }

    @Override
    protected void onPause() {
        super.onPause();
        NetworkChangeReceiver.unregisterReceiver(this, networkChangeReceiver);
    }

    private String artistId = "9999";

    void showData() {
        if (getIntent().getExtras() == null) return;
        final String artist = getIntent().getExtras().getString("data", "null");
        final BasicDataRecord artistItem = new Gson().fromJson(artist, BasicDataRecord.class);
        if (artistItem == null) return;
        artistId = artistItem.id();

        Picasso.get().load(Uri.parse(artistItem.image())).into(binding.artistImg);
        binding.artistName.setText(artistItem.title());
        binding.collapsingToolbarLayout.setTitle(artistItem.title());

        final ApiManager apiManager = new ApiManager(this);
        apiManager.retrieveArtistById(artistId, new RequestNetwork.RequestListener() {
            @Override
            public void onResponse(String tag, String response, HashMap<String, Object> responseHeaders) {
                ArtistSearch artistSearch = new Gson().fromJson(response, ArtistSearch.class);
                Log.i(TAG, "onResponse: " + response);
                if (artistSearch.success()) {
                    Picasso.get().load(Uri.parse(artistSearch.data().image().get(artistSearch.data().image().size() - 1).url())).into(binding.artistImg);
                    binding.artistName.setText(artistSearch.data().name());
                    binding.collapsingToolbarLayout.setTitle(artistSearch.data().name());
                    binding.topSongsRecyclerview.setAdapter(new ActivityArtistProfileTopSongsAdapter(artistSearch.data().topSongs()));
                    binding.topAlbumsRecyclerview.setAdapter(new ActivityArtistProfileTopAlbumsAdapter(artistSearch.data().topAlbums()));
                    binding.topSinglesRecyclerview.setAdapter(new ActivityArtistProfileTopAlbumsAdapter(artistSearch.data().singles()));
                }
            }

            @Override
            public void onErrorResponse(String tag, String message) {
                Log.i(TAG, "onErrorResponse: " + message);
//                if (!NetworkUtil.isNetworkAvailable(ArtistProfileActivity.this)) {
//                    try {
//                        Thread.sleep(2000);
//                        showData();
//                    } catch (InterruptedException e) {
//                        Log.e(TAG, "onErrorResponse: ", e);
//                    }
//                }
            }
        });
    }

    @NonNull
    private static List<SongResponse.Song> getShimmerData() {
        final List<SongResponse.Song> shimmerData = new ArrayList<>();
        for (int i = 0; i < 11; i++) {
            shimmerData.add(new SongResponse.Song(
                    "<shimmer>",
                    "",
                    "",
                    "",
                    "",
                    0.0,
                    "",
                    false,
                    0,
                    "",
                    false,
                    "",
                    new SongResponse.Lyrics("", "", ""),
                    "",
                    "",
                    new SongResponse.Album("", "", ""),
                    new SongResponse.Artists(new ArrayList<>(), new ArrayList<>(), new ArrayList<>()),
                    new ArrayList<>(),
                    new ArrayList<>()
            ));
        }
        return shimmerData;
    }


    void showShimmerData() {
        final List<SongResponse.Song> shimmerData = getShimmerData();
        final List<AlbumsSearch.Data.Results> shimmerDataAlbum = new ArrayList<>();
        for (int i = 0; i < 11; i++) {
            shimmerDataAlbum.add(new AlbumsSearch.Data.Results(
                    "<shimmer>",
                    null,
                    null,
                    null,
                    0,
                    null,
                    0,
                    null,
                    false,
                    null,
                    null
            ));
        }


        binding.topSongsRecyclerview.setAdapter(new ActivityArtistProfileTopSongsAdapter(shimmerData));
        binding.topAlbumsRecyclerview.setAdapter(new ActivityArtistProfileTopAlbumsAdapter(shimmerDataAlbum));
        binding.topSinglesRecyclerview.setAdapter(new ActivityArtistProfileTopAlbumsAdapter(shimmerDataAlbum));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            onBackPressed();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
}