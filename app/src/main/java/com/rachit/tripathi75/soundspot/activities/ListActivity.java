package com.rachit.tripathi75.soundspot.activities;


import android.animation.ArgbEvaluator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.rachit.tripathi75.soundspot.ApplicationClass;
import com.rachit.tripathi75.soundspot.R;
import com.rachit.tripathi75.soundspot.adapters.ActivityListSongsItemAdapter;
import com.rachit.tripathi75.soundspot.adapters.AlbumTrackAdapter;
import com.rachit.tripathi75.soundspot.adapters.UserCreatedSongsListAdapter;
import com.rachit.tripathi75.soundspot.databinding.ActivityListBinding;
import com.rachit.tripathi75.soundspot.databinding.ActivityListMoreInfoBottomSheetBinding;
import com.rachit.tripathi75.soundspot.databinding.UserCreatedListActivityMoreBottomSheetBinding;
import com.rachit.tripathi75.soundspot.model.AlbumItem;
import com.rachit.tripathi75.soundspot.model.AlbumTrack;
import com.rachit.tripathi75.soundspot.model.BasicDataRecord;
import com.rachit.tripathi75.soundspot.network.ApiManager;
import com.rachit.tripathi75.soundspot.network.utility.RequestNetwork;
import com.rachit.tripathi75.soundspot.records.AlbumSearch;
import com.rachit.tripathi75.soundspot.records.PlaylistSearch;
import com.rachit.tripathi75.soundspot.records.SongResponse;
import com.rachit.tripathi75.soundspot.records.sharedpref.SavedLibraries;
import com.rachit.tripathi75.soundspot.utils.SharedPreferenceManager;
import com.rachit.tripathi75.soundspot.utils.customview.BottomSheetItemView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class ListActivity extends AppCompatActivity {



    private static final String PREF_NAME = "ThemePreferences";
    private static final String KEY_THEME = "current_theme";
    private static final String THEME_DARK = "dark";
    private static final String THEME_PURPLE = "purple";


    private AlbumTrackAdapter trackAdapter;

    private String currentTheme = THEME_DARK;
    private float initialAlbumArtSize;
    private float initialAlbumArtX;
    private float initialAlbumArtY;





    private ActivityListBinding binding;

    private final List<String> trackQueue = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // Load saved theme before setting content view
        loadSavedTheme();

        super.onCreate(savedInstanceState);
        binding = ActivityListBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        applyTheme(false);

        initialisers();

        binding.rvAlbumTracks.setLayoutManager(new LinearLayoutManager(this));
//        binding.addMoreSongs.setVisibility(View.GONE);

        Log.i("ListActivity", "onCreate: reached ListActivity");

//        showShimmerData();

        binding.playAllBtn.setOnClickListener(view -> {
            if (!trackQueue.isEmpty()) {
                ((ApplicationClass) getApplicationContext()).setTrackQueue(trackQueue);
                ((ApplicationClass) getApplicationContext()).nextTrack();
                startActivity(new Intent(ListActivity.this, MusicOverviewActivity.class).putExtra("id", ApplicationClass.MUSIC_ID));
            }
        });
        final SharedPreferenceManager sharedPreferenceManager = SharedPreferenceManager.getInstance(ListActivity.this);

//        binding.addToLibrary.setOnClickListener(view -> {
//            if (albumItem == null) return;
//
//            if (isAlbumInLibrary(albumItem, sharedPreferenceManager.getSavedLibrariesData())) {
//
//                new MaterialAlertDialogBuilder(ListActivity.this)
//                        .setTitle("Are you sure?")
//                        .setMessage("Do you want to remove this album from your library?")
//                        .setPositiveButton("Yes", (dialogInterface, i) -> {
//                            int index = getAlbumIndexInLibrary(albumItem, sharedPreferenceManager.getSavedLibrariesData());
//                            if (index == -1) return;
//                            sharedPreferenceManager.removeLibraryFromSavedLibraries(index);
//                            Snackbar.make(binding.getRoot(), "Removed from Library", Snackbar.LENGTH_SHORT).show();
//                            updateAlbumInLibraryStatus();
//                        })
//                        .setNegativeButton("No", (dialogInterface, i) -> {
//
//                        })
//                        .show();
//            } else {
//                SavedLibraries.Library library = new SavedLibraries.Library(
//                        albumItem.id(),
//                        false,
//                        isAlbum,
//                        binding.albumTitle.getText().toString(),
//                        albumItem.albumCover(),
//                        binding.albumSubTitle.getText().toString(),
//                        new ArrayList<>()
//                );
//                sharedPreferenceManager.addLibraryToSavedLibraries(library);
//                Snackbar.make(binding.getRoot(), "Added to Library", Snackbar.LENGTH_SHORT).show();
//            }
//
//            updateAlbumInLibraryStatus();
//        });

//        binding.addMoreSongs.setOnClickListener(view -> {
//            startActivity(new Intent(ListActivity.this, SearchActivity.class));
//        });
//
//        binding.moreIcon.setOnClickListener(view -> onMoreIconClicked());

        showData();
    }

    private void initialisers() {
        initViews();
        setupToolbar();
        setupAlbumArtAnimation();
        setupRecyclerView();
    }

    private void initViews() {


        // Set click listener for play button
        binding.playAllBtn.setOnClickListener(v -> {
            // Implement play functionality
            binding.playAllBtn.animate()
                    .scaleX(0.8f)
                    .scaleY(0.8f)
                    .setDuration(100)
                    .withEndAction(() ->
                            binding.playAllBtn.animate()
                                    .scaleX(1f)
                                    .scaleY(1f)
                                    .setDuration(100)
                                    .start()
                    ).start();
        });
    }

    private void setupToolbar() {
        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setDisplayShowHomeEnabled(true);
        }

        binding.toolbar.setNavigationOnClickListener(v -> onBackPressed());
    }

    private void setupAlbumArtAnimation() {
        // Get initial size for animations
        binding.albumArtCard.post(() -> {
            initialAlbumArtSize = binding.albumArtCard.getWidth();
            initialAlbumArtX = binding.albumArtCard.getX();
            initialAlbumArtY = binding.albumArtCard.getY();
        });

        // Set up scroll listener for animations
        binding.appBar.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            @Override
            public void onOffsetChanged(AppBarLayout appBarLayout, int verticalOffset) {
                // Calculate scroll percentage (0 to 1)
                float scrollPercentage = Math.abs(verticalOffset) / (float) appBarLayout.getTotalScrollRange();

                // Scale album art based on scroll
                float scale = 1f - (0.4f * scrollPercentage); // Scale down to 60% of original size
                binding.albumArtCard.setScaleX(scale);
                binding.albumArtCard.setScaleY(scale);

                // Adjust alpha for fade effect
                float alpha = 1f - (0.2f * scrollPercentage); // Fade to 80% opacity
                binding.albumArtCard.setAlpha(alpha);

                // Move album art up slightly as it shrinks
                float translationY = -40 * scrollPercentage;
                binding.albumArtContainer.setTranslationY(translationY);
            }
        });
    }

    private void setupRecyclerView() {
        binding.rvAlbumTracks.setLayoutManager(new LinearLayoutManager(this));

        // Create sample track data
//        List<AlbumTrack> tracks = new ArrayList<>();
//        tracks.add(new AlbumTrack(1, "2 Much", "Justin Bieber"));
//        tracks.add(new AlbumTrack(2, "Deserve You", "Justin Bieber"));
//        tracks.add(new AlbumTrack(3, "As I Am (feat. Khalid)", "Justin Bieber, Khalid"));
//        tracks.add(new AlbumTrack(4, "Off My Face", "Justin Bieber"));
//        tracks.add(new AlbumTrack(5, "Holy", "Justin Bieber"));
//        tracks.add(new AlbumTrack(6, "Unstable", "Justin Bieber"));
//        tracks.add(new AlbumTrack(7, "MLK Interlude", "Justin Bieber"));
//        tracks.add(new AlbumTrack(8, "Die For You", "Justin Bieber"));
//        tracks.add(new AlbumTrack(9, "Hold On", "Justin Bieber"));
//        tracks.add(new AlbumTrack(10, "Somebody", "Justin Bieber"));
//
//        // Create and set adapter
//        trackAdapter = new AlbumTrackAdapter(this, tracks, currentTheme);
//        binding.rvAlbumTracks.setAdapter(trackAdapter);

        // Add animation to RecyclerView items
        binding.rvAlbumTracks.setLayoutAnimation(
                android.view.animation.AnimationUtils.loadLayoutAnimation(
                        this, R.anim.layout_animation_slide_from_bottom));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_album_details, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.action_theme) {
            toggleTheme();
            return true;
        } else if (item.getItemId() == R.id.action_more) {
            // Handle more options
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void toggleTheme() {
        // Toggle between dark and purple themes
        currentTheme = THEME_DARK.equals(currentTheme) ? THEME_PURPLE : THEME_DARK;

        // Save the new theme preference
        saveThemePreference();

        // Apply the theme with animation
        applyTheme(true);

        // Update the RecyclerView adapter
        if (trackAdapter != null) {
            trackAdapter.updateTheme(currentTheme);
        }
    }

    private void applyTheme(boolean animate) {
        int fromColor, toColor;
        int fromSurfaceColor, toSurfaceColor;
        int fromTextColor, toTextColor;
        int fromSecondaryTextColor, toSecondaryTextColor;

        if (THEME_DARK.equals(currentTheme)) {
            // Switch to dark theme
            setTheme(R.style.Theme_MusicApp_Dark);
            fromColor = ContextCompat.getColor(this, R.color.purple_background);
            toColor = ContextCompat.getColor(this, R.color.dark_background);
            fromSurfaceColor = ContextCompat.getColor(this, R.color.purple_surface);
            toSurfaceColor = ContextCompat.getColor(this, R.color.dark_surface);
            fromTextColor = ContextCompat.getColor(this, R.color.purple_text_primary);
            toTextColor = ContextCompat.getColor(this, R.color.white);
            fromSecondaryTextColor = ContextCompat.getColor(this, R.color.purple_text_secondary);
            toSecondaryTextColor = ContextCompat.getColor(this, R.color.dark_text_secondary);
        } else {
            // Switch to purple theme
            setTheme(R.style.Theme_MusicApp_Purple);
            fromColor = ContextCompat.getColor(this, R.color.dark_background);
            toColor = ContextCompat.getColor(this, R.color.purple_background);
            fromSurfaceColor = ContextCompat.getColor(this, R.color.dark_surface);
            toSurfaceColor = ContextCompat.getColor(this, R.color.purple_surface);
            fromTextColor = ContextCompat.getColor(this, R.color.white);
            toTextColor = ContextCompat.getColor(this, R.color.purple_text_primary);
            fromSecondaryTextColor = ContextCompat.getColor(this, R.color.dark_text_secondary);
            toSecondaryTextColor = ContextCompat.getColor(this, R.color.purple_text_secondary);
        }

        if (animate && binding.main != null) {
            // Animate background color change
            ValueAnimator backgroundColorAnimation = ValueAnimator.ofObject(
                    new ArgbEvaluator(), fromColor, toColor);
            backgroundColorAnimation.setDuration(500);
            backgroundColorAnimation.addUpdateListener(animator ->
                    binding.main.setBackgroundColor((int) animator.getAnimatedValue()));
            backgroundColorAnimation.start();

            // Animate text colors
            ValueAnimator textColorAnimation = ValueAnimator.ofObject(
                    new ArgbEvaluator(), fromTextColor, toTextColor);
            textColorAnimation.setDuration(500);
            textColorAnimation.addUpdateListener(animator -> {
                int color = (int) animator.getAnimatedValue();
                binding.albumTitle.setTextColor(color);
                binding.toolbar.setTitleTextColor(color);
            });
            textColorAnimation.start();

            // Animate secondary text colors
            ValueAnimator secondaryTextColorAnimation = ValueAnimator.ofObject(
                    new ArgbEvaluator(), fromSecondaryTextColor, toSecondaryTextColor);
            secondaryTextColorAnimation.setDuration(500);
            secondaryTextColorAnimation.addUpdateListener(animator -> {
                int color = (int) animator.getAnimatedValue();
                binding.albumArtist.setTextColor(color);
            });
            secondaryTextColorAnimation.start();
        } else if (binding.main != null) {
            // Apply colors immediately without animation
            binding.main.setBackgroundColor(toColor);
            binding.albumTitle.setTextColor(toTextColor);
            binding.albumArtist.setTextColor(toSecondaryTextColor);
            binding.toolbar.setTitleTextColor(toTextColor);
        }
    }

    private void saveThemePreference() {
        SharedPreferences preferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putString(KEY_THEME, currentTheme);
        editor.apply();
    }

    private void loadSavedTheme() {
        SharedPreferences preferences = getSharedPreferences(PREF_NAME, MODE_PRIVATE);
        currentTheme = preferences.getString(KEY_THEME, THEME_DARK);
    }





















    private void onMoreIconClicked() {
        if (albumItem == null) return;

        if (isUserCreated) {
            onMoreIconClickedUserCreated();
            return;
        }

        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(ListActivity.this, R.style.MyBottomSheetDialogTheme);
        final ActivityListMoreInfoBottomSheetBinding _binding = ActivityListMoreInfoBottomSheetBinding.inflate(getLayoutInflater());

        _binding.albumTitle.setText(binding.albumTitle.getText().toString());
        _binding.albumSubTitle.setText(binding.albumArtist.getText().toString());
        Picasso.get().load(Uri.parse(albumItem.albumCover())).into(_binding.coverImage);

        final SharedPreferenceManager sharedPreferenceManager = SharedPreferenceManager.getInstance(ListActivity.this);
        final SavedLibraries savedLibraries = sharedPreferenceManager.getSavedLibrariesData();
        if (savedLibraries == null || savedLibraries.lists() == null){
            _binding.addToLibrary.getTitleTextView().setText("Add to library");
            _binding.addToLibrary.getIconImageView().setImageResource(R.drawable.round_add_24);
        }else{
            if(isAlbumInLibrary(albumItem, savedLibraries)){
                _binding.addToLibrary.getTitleTextView().setText("Remove from library");
                _binding.addToLibrary.getIconImageView().setImageResource(R.drawable.round_close_24);
            }else{
                _binding.addToLibrary.getTitleTextView().setText("Add to library");
                _binding.addToLibrary.getIconImageView().setImageResource(R.drawable.round_add_24);
            }
        }


//        _binding.addToLibrary.setOnClickListener(view -> {
//            bottomSheetDialog.dismiss();
//            binding.addToLibrary.performClick();
//        });

        for (ArtistData artist : artistData) {
            try {
                final String imgUrl = artist.image().isEmpty() ? "" : artist.image();
                BottomSheetItemView bottomSheetItemView = new BottomSheetItemView(ListActivity.this, artist.name(), imgUrl, artist.id());
                bottomSheetItemView.setFocusable(true);
                bottomSheetItemView.setClickable(true);
                bottomSheetItemView.setOnClickListener(view1 -> {
                    Log.i("ListActivity", "BottomSheetItemView: onCLicked!");
                    startActivity(new Intent(ListActivity.this, ArtistProfileActivity.class)
                            .putExtra("data", new Gson().toJson(
                                    new BasicDataRecord(artist.id(), artist.name(), "", imgUrl)))
                    );
                });
                _binding.main.addView(bottomSheetItemView);
            } catch (Exception e) {
                Log.e("ListActivity", "BottomSheetDialog: ", e);
            }
        }

        bottomSheetDialog.setContentView(_binding.getRoot());
        bottomSheetDialog.create();
        bottomSheetDialog.show();

    }

    private void onMoreIconClickedUserCreated() {
        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(ListActivity.this, R.style.MyBottomSheetDialogTheme);
        final UserCreatedListActivityMoreBottomSheetBinding _binding = UserCreatedListActivityMoreBottomSheetBinding.inflate(getLayoutInflater());

        _binding.albumTitle.setText(binding.albumTitle.getText().toString());
        _binding.albumSubTitle.setText(binding.albumArtist.getText().toString());
        Picasso.get().load(Uri.parse(albumItem.albumCover())).into(_binding.coverImage);

        _binding.removeLibrary.setOnClickListener(view -> {
            bottomSheetDialog.dismiss();
//            binding.addToLibrary.performClick();
        });

        bottomSheetDialog.setContentView(_binding.getRoot());
        bottomSheetDialog.create();
        bottomSheetDialog.show();
    }

    private void updateAlbumInLibraryStatus() {
//        SharedPreferenceManager sharedPreferenceManager = SharedPreferenceManager.getInstance(ListActivity.this);
//        if (sharedPreferenceManager.getSavedLibrariesData() == null)
//            binding.addToLibrary.setImageResource(R.drawable.round_add_24);
//        else {
//            final SavedLibraries savedLibraries = sharedPreferenceManager.getSavedLibrariesData();
////            binding.addToLibrary.setImageResource(isAlbumInLibrary(albumItem, savedLibraries) ? R.drawable.round_done_24 : R.drawable.round_add_24);
//        }
    }

    @SuppressLint("NewApi")
    private boolean isAlbumInLibrary(AlbumItem albumItem, SavedLibraries savedLibraries) {
        if (savedLibraries == null || savedLibraries.lists() == null) {
            return false;
        }
        Log.i("ListActivity", "isAlbumInLibrary: " + savedLibraries);
        if (savedLibraries.lists().isEmpty()) return false;
        return savedLibraries.lists().stream().anyMatch(library -> library.id().equals(albumItem.id()));
    }

    @SuppressLint("NewApi")
    private int getAlbumIndexInLibrary(AlbumItem albumItem, SavedLibraries savedLibraries) {
        if (savedLibraries == null || savedLibraries.lists() == null) {
            return -1;
        }
        Log.i("ListActivity", "getAlbumIndexInLibrary: " + savedLibraries);
        if (savedLibraries.lists().isEmpty()) return -1;
        int index = -1;
        for (SavedLibraries.Library library : savedLibraries.lists()) {
            if (library.id().equals(albumItem.id())) {
                index = savedLibraries.lists().indexOf(library);
                break;
            }
        }
        return index;
    }

//    private void showShimmerData() {
//        List<SongResponse.Song> data = new ArrayList<>();
//        for (int i = 0; i < 11; i++) {
//            data.add(new SongResponse.Song(
//                    "<shimmer>",
//                    "",
//                    "",
//                    "",
//                    "",
//                    0.0,
//                    "",
//                    false,
//                    0,
//                    "",
//                    false,
//                    "",
//                    null,
//                    "",
//                    "",
//                    null,
//                    null, null, null
//            ));
//        }
//        binding.recyclerView.setAdapter(new ActivityListSongsItemAdapter(data));
//    }

    private AlbumItem albumItem;
    private boolean isAlbum = false;

    private void showData() {
        if (getIntent().getExtras() == null) return;
        albumItem = new Gson().fromJson(getIntent().getExtras().getString("data"), AlbumItem.class);
        updateAlbumInLibraryStatus();
        binding.albumTitle.setText(albumItem.albumTitle());
        binding.albumArtist.setText(albumItem.albumSubTitle());
        if (!albumItem.albumCover().isBlank())
            Picasso.get().load(Uri.parse(albumItem.albumCover())).into(binding.albumArt);

        final ApiManager apiManager = new ApiManager(this);
        final SharedPreferenceManager sharedPreferenceManager = SharedPreferenceManager.getInstance(this);

        if (getIntent().getExtras().getBoolean("createdByUser", false)) {
            onUserCreatedFetch();
            return;
        }

        if (getIntent().getExtras().getString("type", "").equals("album")) {
            isAlbum = true;
            if (sharedPreferenceManager.getAlbumResponseById(albumItem.id()) != null) {
                onAlbumFetched(sharedPreferenceManager.getAlbumResponseById(albumItem.id()));
                return;
            }
            apiManager.retrieveAlbumById(albumItem.id(), new RequestNetwork.RequestListener() {
                @Override
                public void onResponse(String tag, String response, HashMap<String, Object> responseHeaders) {
                    AlbumSearch albumSearch = new Gson().fromJson(response, AlbumSearch.class);
                    if (albumSearch.success()) {
                        sharedPreferenceManager.setAlbumResponseById(albumItem.id(), albumSearch);
                        onAlbumFetched(albumSearch);
                    }
                }

                @Override
                public void onErrorResponse(String tag, String message) {

                }
            });
            return;
        }

        if (sharedPreferenceManager.getPlaylistResponseById(albumItem.id()) != null) {
            onPlaylistFetched(sharedPreferenceManager.getPlaylistResponseById(albumItem.id()));
            return;
        }
        apiManager.retrievePlaylistById(albumItem.id(), 0, 50, new RequestNetwork.RequestListener() {
            @Override
            public void onResponse(String tag, String response, HashMap<String, Object> responseHeaders) {
                Log.i("API_RESPONSE", "onResponse: " + response);
                PlaylistSearch playlistSearch = new Gson().fromJson(response, PlaylistSearch.class);
                if (playlistSearch.success()) {
                    sharedPreferenceManager.setPlaylistResponseById(albumItem.id(), playlistSearch);
                    onPlaylistFetched(playlistSearch);
                }
            }

            @Override
            public void onErrorResponse(String tag, String message) {

            }
        });
    }

    private boolean isUserCreated = false;

    private void onUserCreatedFetch() {

        isUserCreated = true;

//        binding.shareIcon.setVisibility(View.INVISIBLE);
//        binding.moreIcon.setVisibility(View.INVISIBLE);
//        binding.addToLibrary.setVisibility(View.INVISIBLE);
//        binding.addMoreSongs.setVisibility(View.VISIBLE);

        final SharedPreferenceManager sharedPreferenceManager = SharedPreferenceManager.getInstance(this);
        SavedLibraries savedLibraries = sharedPreferenceManager.getSavedLibrariesData();
        if (savedLibraries == null || savedLibraries.lists().isEmpty()) finish();
        SavedLibraries.Library library = null;
        for (SavedLibraries.Library l : savedLibraries.lists()) {
            if (l.id().equals(albumItem.id())) {
                library = l;
                break;
            }
        }
        if (library == null) finish();
        if (library != null) {
            binding.albumTitle.setText(library.name());
            binding.albumArtist.setText(library.description());
            Picasso.get().load(Uri.parse(library.image())).into(binding.albumArt);
            binding.rvAlbumTracks.setAdapter(new UserCreatedSongsListAdapter(library.songs()));
            for (SavedLibraries.Library.Songs song : library.songs())
                trackQueue.add(song.id());
        }

    }
//
    private void onAlbumFetched(AlbumSearch albumSearch) {
        binding.albumTitle.setText(albumSearch.data().name());
        binding.albumArtist.setText(albumSearch.data().description());
        Picasso.get().load(Uri.parse(albumSearch.data().image().get(albumSearch.data().image().size() - 1).url())).into(binding.albumArt);
        binding.rvAlbumTracks.setAdapter(new AlbumTrackAdapter(this, albumSearch.data().songs(), currentTheme));
        for (SongResponse.Song song : albumSearch.data().songs())
            trackQueue.add(song.id());

        ((ApplicationClass)getApplicationContext()).setTrackQueue(trackQueue);
//        binding.shareIcon.setOnClickListener(view -> {
//            if (albumSearch.data().url().isBlank()) return;
//            Intent sendIntent = new Intent();
//            sendIntent.setAction(Intent.ACTION_SEND);
//            sendIntent.putExtra(Intent.EXTRA_TEXT, albumSearch.data().url());
//            sendIntent.setType("text/plain");
//            startActivity(sendIntent);
//        });

//        for (SongResponse.Artist artist : albumSearch.data().artist().all()) {
//            artistData.add(new ArtistData(artist.name(), artist.id(),
//                    (!artist.image().isEmpty()) ? artist.image().get(artist.image().size() - 1).url()
//                            : artist.image().get(0).url()
//            ));
//        }

        for (SongResponse.Artist artist : albumSearch.data().artist().all()) {
            artistData.add(new ArtistData(
                    artist.name(),
                    artist.id(),
                    (!artist.image().isEmpty())
                            ? artist.image().get(artist.image().size() - 1).url()
                            : null // or a placeholder image URL if needed
            ));
        }
    }
//
    private void onPlaylistFetched(PlaylistSearch playlistSearch) {
        binding.albumTitle.setText(playlistSearch.data().name());
        binding.albumArtist.setText(playlistSearch.data().description());
        Picasso.get().load(Uri.parse(playlistSearch.data().image().get(playlistSearch.data().image().size() - 1).url())).into(binding.albumArt);
        binding.rvAlbumTracks.setAdapter(new AlbumTrackAdapter(this, playlistSearch.data().songs(), currentTheme));
        for (SongResponse.Song song : playlistSearch.data().songs())
            trackQueue.add(song.id());

        ((ApplicationClass)getApplicationContext()).setTrackQueue(trackQueue);
//        binding.shareIcon.setOnClickListener(view -> {
//            if (playlistSearch.data().url().isBlank()) return;
//            Intent sendIntent = new Intent();
//            sendIntent.setAction(Intent.ACTION_SEND);
//            sendIntent.putExtra(Intent.EXTRA_TEXT, playlistSearch.data().url());
//            sendIntent.setType("text/plain");
//            startActivity(sendIntent);
//        });

        for (PlaylistSearch.Data.Artist artist : playlistSearch.data().artists()) {
            artistData.add(new ArtistData(artist.name(), artist.id(),
                    (!artist.image().isEmpty()) ?
                              artist.image().get(artist.image().size() - 1).url()
                            : "https://i.pinimg.com/564x/1d/04/a8/1d04a87b8e6cf2c3829c7af2eccf6813.jpg"
            ));
        }

    }

    private final List<ArtistData> artistData = new ArrayList<>();

    public void backPress(View view) {
        finish();
    }

    private record ArtistData(
            String name,
            String id,
            String image
    ) {
    }

}