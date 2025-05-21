package com.rachit.tripathi75.soundspot.activities;

import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.chip.Chip;
import com.google.gson.Gson;
import com.rachit.tripathi75.soundspot.R;
import com.rachit.tripathi75.soundspot.adapters.ActivitySearchListItemAdapter;
import com.rachit.tripathi75.soundspot.adapters.AlbumsAdapter;
import com.rachit.tripathi75.soundspot.adapters.ForYouAdapter;
import com.rachit.tripathi75.soundspot.adapters.SearchResultsAdapter;
import com.rachit.tripathi75.soundspot.adapters.TopSongsAdapter;
import com.rachit.tripathi75.soundspot.databinding.ActivitySearchMusicBinding;
import com.rachit.tripathi75.soundspot.fragments.ChartsFragment;
import com.rachit.tripathi75.soundspot.fragments.DiscoverFragment;
import com.rachit.tripathi75.soundspot.fragments.MoodGenresFragment;
import com.rachit.tripathi75.soundspot.fragments.NewReleasesFragment;
import com.rachit.tripathi75.soundspot.model.NewReleaseSong;
import com.rachit.tripathi75.soundspot.model.SearchListItem;
import com.rachit.tripathi75.soundspot.network.ApiManager;
import com.rachit.tripathi75.soundspot.network.utility.RequestNetwork;
import com.rachit.tripathi75.soundspot.records.GlobalSearch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SearchMusicActivity extends AppCompatActivity implements CategoryClickListener {

    private ActivitySearchMusicBinding binding;
    private final String TAG = "SearchMusicActivity";
    private GlobalSearch globalSearch;


    private String activeGenre = null;
    private List<NewReleaseSong> topSongs = new ArrayList<>();
    private List<NewReleaseSong> forYouSongs = new ArrayList<>();
    private List<NewReleaseSong> newAlbumsAndSingles = new ArrayList<>();
    private List<SearchResult> searchResults = new ArrayList<>();
    private boolean isInCategoryScreen = false;

    private Handler handler = new Handler();
    private Runnable searchRunnable;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySearchMusicBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize views

        // Setup animations
        binding.searchEditText.setAnimation(AnimationUtils.loadAnimation(this, R.anim.fade_in));
        binding.genresChipGroup.setAnimation(AnimationUtils.loadAnimation(this, R.anim.slide_up));

        // Setup back button
        binding.backButton.setOnClickListener(v -> {
            if (isInCategoryScreen) {
                showMainContent();
                isInCategoryScreen = false;
            }
            // Additional back logic if needed
            onBackPressed();
        });

        // Setup genre chips
//        setupGenreChips();

        // Initialize data
        initializeData();

        // Setup recycler views
        setupRecyclerViews();

        // Setup category cards
        setupCategoryCards();

        showDataToJustForYou("songs 2024");
        showDataToTopSongs("top songs 2025 hindi");
        showDataToNewAlbumsAndSingles("punjabi hits");

        // setup search functionality, watcher on search edit text, for debounce search result.........
        setUpSearchFunctionality();

    }


    private void setupGenreChips() {
        String[] genres = {"Dance", "Pop", "Jazz", "Classical", "Indie", "Latin", "Electronic", "Rock", "Acoustic"};
        int[] colors = {R.color.pink_500, R.color.blue_500, R.color.amber_500, R.color.purple_500,
                R.color.red_500, R.color.green_500, R.color.cyan_500, R.color.orange_500, R.color.violet_500};

        for (int i = 0; i < genres.length; i++) {
            final String genre = genres[i];
            final int color = colors[i];

            Chip chip = new Chip(this);
            chip.setText(genre);
            chip.setCheckable(true);
            chip.setClickable(true);

            chip.setOnClickListener(v -> {
                if (activeGenre != null && activeGenre.equals(genre)) {
                    chip.setChipBackgroundColorResource(R.color.gray_800);
                    activeGenre = null;
                    resetSongLists();
                } else {
                    // Reset all chips to default color
                    for (int j = 0; j < binding.genresChipGroup.getChildCount(); j++) {
                        View child = binding.genresChipGroup.getChildAt(j);
                        if (child instanceof Chip) {
                            ((Chip) child).setChipBackgroundColorResource(R.color.gray_800);
                        }
                    }

                    chip.setChipBackgroundColorResource(color);
                    activeGenre = genre;
                    filterSongsByGenre(genre);
                }

                // Update section titles to show active genre
                updateSectionTitles();
            });

            // Add animation to each chip
            chip.setAnimation(AnimationUtils.loadAnimation(this, R.anim.scale_in));

            binding.genresChipGroup.addView(chip);
        }
    }

    private void setupCategoryCards() {
        // Find category card views
        View newReleasesCard = findViewById(R.id.new_releases_card);
        View chartsCard = findViewById(R.id.charts_card);
        View moodGenresCard = findViewById(R.id.mood_genres_card);
        View discoverCard = findViewById(R.id.discover_card);

        // Set click listeners
        newReleasesCard.setOnClickListener(v -> {
            showCategoryScreen("New Releases");
            binding.cvPickedForYou.setVisibility(View.GONE);
        });
//        chartsCard.setOnClickListener(v -> showCategoryScreen("Charts"));
        moodGenresCard.setOnClickListener(v -> {
            showCategoryScreen("Mood & Genres");
            binding.cvPickedForYou.setVisibility(View.GONE);
        });
        discoverCard.setOnClickListener(v -> {
            showCategoryScreen("Discover");
            binding.cvPickedForYou.setVisibility(View.GONE);
        });
    }

    private void initializeData() {
        // Initialize top songs
        topSongs.add(new NewReleaseSong("1", "Greedy", "Tate McRae", "imgUrl"));
        topSongs.add(new NewReleaseSong("2", "One of the Girls", "The Weeknd & Lily Rose", "imgUrl"));
        topSongs.add(new NewReleaseSong("3", "Popular", "The Weeknd, Playboi Carti", "imgUrl"));
        topSongs.add(new NewReleaseSong("4", "I Wanna Be Yours", "Arctic Monkeys", "imgUrl"));

        // Initialize for you songs
        forYouSongs.add(new NewReleaseSong("1", "Half of My Heart", "John Mayer", "imgUrl"));
        forYouSongs.add(new NewReleaseSong("1", "Dance The Night", "Dua Lipa", "imgUrl"));
        forYouSongs.add(new NewReleaseSong("1", "End Game", "Ft. Ed Sheeran", "imgUrl"));

        // Initialize albums
        newAlbumsAndSingles.add(new NewReleaseSong("1", "XXL (Stripped)", "Various Artists", "imgUrl"));
        newAlbumsAndSingles.add(new NewReleaseSong("2", "23 favorite", "Various Artists", "imgUrl"));
        newAlbumsAndSingles.add(new NewReleaseSong("3", "Waiting Back", "Various Artists", "imgUrl"));
    }

    private void setupRecyclerViews() {
        // Setup top songs recycler view
        TopSongsAdapter topSongsAdapter = new TopSongsAdapter(topSongs);
        binding.topSongsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.topSongsRecyclerView.setAdapter(topSongsAdapter);

        // Setup for you recycler view
        ForYouAdapter forYouAdapter = new ForYouAdapter(forYouSongs);
        binding.forYouRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        binding.forYouRecyclerView.setAdapter(forYouAdapter);

        // Setup albums recycler view
        AlbumsAdapter albumsAdapter = new AlbumsAdapter(newAlbumsAndSingles);
        binding.albumsRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        binding.albumsRecyclerView.setAdapter(albumsAdapter);

        // Setup search results recycler view
        SearchResultsAdapter searchResultsAdapter = new SearchResultsAdapter(searchResults);
        binding.searchResultsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.searchResultsRecyclerView.setAdapter(searchResultsAdapter);
    }

    private void showMainContent() {
//        binding.cvPickedForYou.setVisibility(View.GONE);
        binding.genresChipGroup.setVisibility(View.VISIBLE);
        binding.categoriesLayout.setVisibility(View.VISIBLE);
        binding.forYouLayout.setVisibility(View.VISIBLE);
        binding.topSongsLayout.setVisibility(View.VISIBLE);
        binding.albumsLayout.setVisibility(View.VISIBLE);
        binding.searchResultsLayout.setVisibility(View.GONE);
        binding.categoryContainer.setVisibility(View.GONE);

        // Apply animations
        binding.categoriesLayout.startAnimation(AnimationUtils.loadAnimation(this, R.anim.fade_in));
        binding.forYouLayout.startAnimation(AnimationUtils.loadAnimation(this, R.anim.slide_up));
        binding.topSongsLayout.startAnimation(AnimationUtils.loadAnimation(this, R.anim.slide_up));
        binding.albumsLayout.startAnimation(AnimationUtils.loadAnimation(this, R.anim.slide_up));
    }


    private void setUpSearchFunctionality() {

        SearchResultsAdapter adapter = new SearchResultsAdapter(new ArrayList<>());
        binding.searchResultsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.searchResultsRecyclerView.setAdapter(adapter);

        binding.searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

                if (searchRunnable != null)
                    handler.removeCallbacks(searchRunnable);

                searchRunnable = () -> showSearchResults(s.toString());
                handler.postDelayed(searchRunnable, 500); // Debounce delay

            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });
    }

    private void showSearchResults(String query) {

        if (query.isEmpty()) {
            if (!isInCategoryScreen) {
                showMainContent();
                return;
            }
        }

        binding.cvPickedForYou.setVisibility(View.GONE);
        binding.genresChipGroup.setVisibility(View.GONE);
        binding.categoriesLayout.setVisibility(View.GONE);
        binding.forYouLayout.setVisibility(View.GONE);
        binding.topSongsLayout.setVisibility(View.GONE);
        binding.albumsLayout.setVisibility(View.GONE);
        binding.categoryContainer.setVisibility(View.GONE);
        binding.searchResultsLayout.setVisibility(View.VISIBLE);

        // Apply animation
        binding.searchResultsLayout.startAnimation(AnimationUtils.loadAnimation(this, R.anim.fade_in));

        // Update search results
        searchResults.clear();


        showShimmerData();

        final ApiManager apiManager = new ApiManager(this);
        apiManager.globalSearch(query, new RequestNetwork.RequestListener() {
            @Override
            public void onResponse(String tag, String response, HashMap<String, Object> responseHeaders) {
                globalSearch = new Gson().fromJson(response, GlobalSearch.class);
                if (globalSearch.success()) {
                    refreshData(query);
                }
                Log.i(TAG, "onResponse: " + response);
            }

            @Override
            public void onErrorResponse(String tag, String message) {
                Log.e(TAG, "onErrorResponse: " + message);
            }
        });


//        searchResults.add(new SearchResult(SearchResult.TYPE_SONG, query + " Song Result 1", "Artist Name", R.drawable.baseline_album_24));
//        searchResults.add(new SearchResult(SearchResult.TYPE_SONG, query + " Song Result 2", "Another Artist", R.drawable.baseline_album_24));
//        searchResults.add(new SearchResult(SearchResult.TYPE_ALBUM, query + " Album", "Various Artists", R.drawable.baseline_album_24));
//        searchResults.add(new SearchResult(SearchResult.TYPE_ARTIST, query + " Artist", "", R.drawable.baseline_album_24));
//        searchResults.add(new SearchResult(SearchResult.TYPE_PLAYLIST, query + " Playlist", "Spotify", R.drawable.baseline_album_24));

        // Notify adapter
//        binding.searchResultsRecyclerView.getAdapter().notifyDataSetChanged();

        // Update title
//        TextView searchResultsTitle = findViewById(R.id.search_results_title);
//        searchResultsTitle.setText("Search Results for \"" + query + "\"");
//
//        isInCategoryScreen = false;
    }


    private void refreshData(String query) {
        final List<SearchListItem> data = new ArrayList<>();

        globalSearch.data().topQuery().results().forEach(item -> {
            if (!(item.type().equals("song") || item.type().equals("album") || item.type().equals("playlist") || item.type().equals("artist")))
                return;
            data.add(
                    new SearchListItem(
                            item.id(),
                            item.title(),
                            item.description(),
                            item.image().get(item.image().size() - 1).url(),
                            SearchListItem.Type.valueOf(item.type().toUpperCase())
                    )
            );
        });
        addSongsData(data);
        addAlbumsData(data);
        addPlaylistsData(data);
        addArtistsData(data);

        if (!data.isEmpty()){
            binding.searchResultsRecyclerView.setAdapter(new ActivitySearchListItemAdapter(data));
            binding.searchResultsRecyclerView.getAdapter().notifyDataSetChanged();
            TextView searchResultsTitle = findViewById(R.id.search_results_title);
            searchResultsTitle.setText("Search Results for \"" + query + "\"");

            isInCategoryScreen = false;
        }

    }


    private void addSongsData(List<SearchListItem> data) {
        globalSearch.data().songs().results().forEach(item -> {
            data.add(
                    new SearchListItem(
                            item.id(),
                            item.title(),
                            item.description(),
                            item.image().get(item.image().size() - 1).url(),
                            SearchListItem.Type.SONG
                    )
            );
        });
    }

    private void addAlbumsData(List<SearchListItem> data) {
        globalSearch.data().albums().results().forEach(item -> {
            data.add(
                    new SearchListItem(
                            item.id(),
                            item.title(),
                            item.description(),
                            item.image().get(item.image().size() - 1).url(),
                            SearchListItem.Type.ALBUM
                    )
            );
        });
    }

    private void addPlaylistsData(List<SearchListItem> data) {
        globalSearch.data().playlists().results().forEach(item -> {
            data.add(
                    new SearchListItem(
                            item.id(),
                            item.title(),
                            item.description(),
                            item.image().get(item.image().size() - 1).url(),
                            SearchListItem.Type.PLAYLIST
                    )
            );
        });
    }

    private void addArtistsData(List<SearchListItem> data) {
        globalSearch.data().artists().results().forEach(item -> {
            data.add(
                    new SearchListItem(
                            item.id(),
                            item.title(),
                            item.description(),
                            item.image().get(item.image().size() - 1).url(),
                            SearchListItem.Type.ARTIST
                    )
            );
        });
    }


    private void showCategoryScreen(String category) {
        binding.genresChipGroup.setVisibility(View.GONE);
        binding.categoriesLayout.setVisibility(View.GONE);
        binding.forYouLayout.setVisibility(View.GONE);
        binding.topSongsLayout.setVisibility(View.GONE);
        binding.albumsLayout.setVisibility(View.GONE);
        binding.searchResultsLayout.setVisibility(View.GONE);
        binding.categoryContainer.setVisibility(View.VISIBLE);

        // Create and show the appropriate fragment
        Fragment fragment;
        switch (category) {
            case "New Releases":
                fragment = new NewReleasesFragment();
                break;
            case "Charts":
                fragment = new ChartsFragment();
                break;
            case "Mood & Genres":
                fragment = new MoodGenresFragment();
                break;
            case "Discover":
                fragment = new DiscoverFragment();
                break;
            default:
                fragment = new NewReleasesFragment();
                break;
        }

        // Apply animation to container
        binding.categoryContainer.startAnimation(AnimationUtils.loadAnimation(this, R.anim.slide_in_right));

        // Replace fragment with animation
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.setCustomAnimations(R.anim.slide_in_right, R.anim.slide_out_left);
        transaction.replace(R.id.category_container, fragment);
        transaction.commit();

        isInCategoryScreen = true;
    }

    private void filterSongsByGenre(String genre) {
//        // This is a simple filter for demo purposes
//        // In a real app, you would filter based on actual genre metadata
//
//        List<NewReleaseSong> filteredTopSongs = new ArrayList<>();
//        List<NewReleaseSong> filteredForYouSongs = new ArrayList<>();
//
//        // Simple filtering logic - in a real app this would be more sophisticated
//        for (NewReleaseSong song : topSongs) {
//            if (song.id % 2 == 0) { // Just a demo filter
//                filteredTopSongs.add(song);
//            }
//        }
//
//        for (NewReleaseSong song : forYouSongs) {
//            if (song.id % 2 == 0) { // Just a demo filter
//                filteredForYouSongs.add(song);
//            }
//        }

        // Update adapters
//        ((TopSongsAdapter) binding.topSongsRecyclerView.getAdapter()).updateSongs(filteredTopSongs);
//        ((ForYouAdapter) binding.forYouRecyclerView.getAdapter()).updateSongs(filteredForYouSongs);
    }

    private void resetSongLists() {
        // Reset to original lists
        ((TopSongsAdapter) binding.topSongsRecyclerView.getAdapter()).updateSongs(topSongs);
        ((ForYouAdapter) binding.forYouRecyclerView.getAdapter()).updateSongs(forYouSongs);
    }

    private void updateSectionTitles() {
        TextView forYouTitle = findViewById(R.id.for_you_title);
        TextView topSongsTitle = findViewById(R.id.top_songs_title);

        if (activeGenre != null) {
            forYouTitle.setText("Just For You • " + activeGenre);
            topSongsTitle.setText("Top Songs • " + activeGenre);
        } else {
            forYouTitle.setText("Just For You");
            topSongsTitle.setText("Top Songs");
        }
    }

    @Override
    public void onCategoryClick(String category) {
        showCategoryScreen(category);
    }

    @Override
    public void onBackPressed() {
        if (isInCategoryScreen) {
            showMainContent();
            isInCategoryScreen = false;
        } else if (!binding.searchEditText.getText().toString().isEmpty()) {
            binding.searchEditText.setText("");
        } else {
            super.onBackPressed();
        }
    }


    private void showDataToJustForYou(String query) {
        final ApiManager apiManager = new ApiManager(this);
        apiManager.globalSearch(query, new RequestNetwork.RequestListener() {
            @Override
            public void onResponse(String tag, String response, HashMap<String, Object> responseHeaders) {
                globalSearch = new Gson().fromJson(response, GlobalSearch.class);
                if (globalSearch.success()) {
                    refreshDataForJustForYou();
                    Gson gson = new Gson();
                    Log.d("justForYouTAG", "onResponse: " + gson.toJson(globalSearch.data().songs().results()));
                }
                Log.i(TAG, "onResponse: " + response);
            }

            @Override
            public void onErrorResponse(String tag, String message) {
                Log.e(TAG, "onErrorResponse: " + message);
            }
        });
    }


    private void showDataToTopSongs(String query) {
//        showShimmerData();

        final ApiManager apiManager = new ApiManager(this);
        apiManager.globalSearch(query, new RequestNetwork.RequestListener() {
            @Override
            public void onResponse(String tag, String response, HashMap<String, Object> responseHeaders) {
                globalSearch = new Gson().fromJson(response, GlobalSearch.class);
                if (globalSearch.success()) {
                    refreshDataForTopSongs();
                    Gson gson = new Gson();
                    Log.d("topSongsTAG", "onResponse: " + gson.toJson(globalSearch.data().songs().results()));
                }
                Log.i(TAG, "onResponse: " + response);
            }

            @Override
            public void onErrorResponse(String tag, String message) {
                Log.e(TAG, "onErrorResponse: " + message);
            }
        });
    }


    private void showDataToNewAlbumsAndSingles(String query) {
        final ApiManager apiManager = new ApiManager(this);
        apiManager.globalSearch(query, new RequestNetwork.RequestListener() {
            @Override
            public void onResponse(String tag, String response, HashMap<String, Object> responseHeaders) {
                globalSearch = new Gson().fromJson(response, GlobalSearch.class);
                if (globalSearch.success()) {
                    refreshDataForNewAlbumsAndSingles();
                    Gson gson = new Gson();
                    Log.d("newAlbumsAndSinglesTAG", "onResponse: " + gson.toJson(globalSearch.data().songs().results()));
                }
                Log.i(TAG, "onResponse: " + response);
            }

            @Override
            public void onErrorResponse(String tag, String message) {
                Log.e(TAG, "onErrorResponse: " + message);
            }
        });
    }


// ------------------ refreshed data results -------------------------------------------


    private void refreshDataForJustForYou() {
        forYouSongs.clear();

        globalSearch.data().songs().results().forEach(item -> {
            Gson gson = new Gson();
            if (!(item.type().equals("song") || item.type().equals("album") || item.type().equals("playlist") || item.type().equals("artist")))
                return;
            if (item.type().equalsIgnoreCase("song")) {
                Log.d("justForYouTAGx", gson.toJson(item));
                Log.d("justForYouDetailsTAG", "id: " + item.id() + " title: " + item.title() + " description: " + item.description() + " url: " + item.image().get(2).url());
                forYouSongs.add(new NewReleaseSong(item.id(), item.title(), item.description(), item.image().get(2).url()));
            }


        });

        for (int i = 0; i < forYouSongs.size(); i++) {
            NewReleaseSong s = forYouSongs.get(i);
            Log.d("justForYouLoopTAG", "id: " + s.getId() + " --- title: " + s.getTitle() + " --- description: " + s.getArtist() + " --- url: " + s.getImageUrl());
        }


        if (!forYouSongs.isEmpty())
            binding.forYouRecyclerView.setAdapter(new ForYouAdapter(forYouSongs));
        else {
            Toast.makeText(this, "Data is empty for 'Just for you' ", Toast.LENGTH_SHORT).show();
        }
    }


    private void refreshDataForTopSongs() {
        topSongs.clear();

        globalSearch.data().songs().results().forEach(item -> {
            Gson gson = new Gson();
            if (!(item.type().equals("song") || item.type().equals("album") || item.type().equals("playlist") || item.type().equals("artist")))
                return;
            if (item.type().equalsIgnoreCase("song")) {
                Log.d("topSongsTAGx", gson.toJson(item));
                Log.d("topSongDetailsTAG", "id: " + item.id() + " title: " + item.title() + " description: " + item.description() + " url: " + item.image().get(2).url());
                topSongs.add(new NewReleaseSong(item.id(), item.title(), item.description(), item.image().get(2).url()));
            }


        });

        for (int i = 0; i < topSongs.size(); i++) {
            NewReleaseSong s = topSongs.get(i);
            Log.d("topSongsDataLoopTAG", "id: " + s.getId() + " --- title: " + s.getTitle() + " --- description: " + s.getArtist() + " --- url: " + s.getImageUrl());
        }


        if (!topSongs.isEmpty())
            binding.topSongsRecyclerView.setAdapter(new TopSongsAdapter(topSongs));
        else {
            Toast.makeText(this, "Data is empty for 'Top Songs' ", Toast.LENGTH_SHORT).show();
        }
    }

    private void refreshDataForNewAlbumsAndSingles() {
        newAlbumsAndSingles.clear();

        globalSearch.data().songs().results().forEach(item -> {
            Gson gson = new Gson();
            if (!(item.type().equals("song") || item.type().equals("album") || item.type().equals("playlist") || item.type().equals("artist")))
                return;
//            if (item.type().equalsIgnoreCase("album")) {
            Log.d("newAlbumsAndSinglesTAGx", gson.toJson(item));
            Log.d("newAlbumsAndSinglesDetailsTAG", "id: " + item.id() + " title: " + item.title() + " description: " + item.description() + " url: " + item.image().get(2).url());
            newAlbumsAndSingles.add(new NewReleaseSong(item.id(), item.title(), item.description(), item.image().get(2).url()));
//            }


        });

        for (int i = 0; i < newAlbumsAndSingles.size(); i++) {
            NewReleaseSong s = newAlbumsAndSingles.get(i);
            Log.d("newAlbumsAndSinglesDataLoopTAG", "id: " + s.getId() + " --- title: " + s.getTitle() + " --- description: " + s.getArtist() + " --- url: " + s.getImageUrl());
        }


        if (!newAlbumsAndSingles.isEmpty())
            binding.albumsRecyclerView.setAdapter(new AlbumsAdapter(newAlbumsAndSingles));
        else {
            Toast.makeText(this, "Data is empty for 'NewAlbumsAndSingles' ", Toast.LENGTH_SHORT).show();
        }

    }



    private void showShimmerData() {
//        List<SearchListItem> data = new ArrayList<>();
//        for (int i = 0; i < 11; i++) {
//            data.add(new SearchListItem(
//                    "<shimmer>",
//                    "",
//                    "",
//                    "",
//                    SearchListItem.Type.SONG
//            ));
//        }
//        binding.topSongsRecyclerView.setAdapter(new TopSongsAdapter(data));
    }


    // Model classes


    public static class Album {
        public String title;
        public String artist;
        public int imageResId;
        public int colorResId;

        Album(String title, String artist, int imageResId, int colorResId) {
            this.title = title;
            this.artist = artist;
            this.imageResId = imageResId;
            this.colorResId = colorResId;
        }
    }

    public static class SearchResult {
        public static final int TYPE_SONG = 0;
        public static final int TYPE_ALBUM = 1;
        public static final int TYPE_ARTIST = 2;
        public static final int TYPE_PLAYLIST = 3;

        public int type;
        public String title;
        public String subtitle;
        public int imageResId;

        SearchResult(int type, String title, String subtitle, int imageResId) {
            this.type = type;
            this.title = title;
            this.subtitle = subtitle;
            this.imageResId = imageResId;
        }
    }


}

interface CategoryClickListener {
    void onCategoryClick(String category);
}


