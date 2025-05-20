package com.rachit.tripathi75.soundspot.activities;

import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.animation.AnimationUtils;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.chip.Chip;
import com.rachit.tripathi75.soundspot.R;
import com.rachit.tripathi75.soundspot.adapters.AlbumsAdapter;
import com.rachit.tripathi75.soundspot.adapters.ForYouAdapter;
import com.rachit.tripathi75.soundspot.adapters.SearchResultsAdapter;
import com.rachit.tripathi75.soundspot.adapters.TopSongsAdapter;
import com.rachit.tripathi75.soundspot.databinding.ActivitySearchMusicBinding;
import com.rachit.tripathi75.soundspot.fragments.ChartsFragment;
import com.rachit.tripathi75.soundspot.fragments.DiscoverFragment;
import com.rachit.tripathi75.soundspot.fragments.MoodGenresFragment;
import com.rachit.tripathi75.soundspot.fragments.NewReleasesFragment;
import com.rachit.tripathi75.soundspot.model.NewReleaseAlbum;
import com.rachit.tripathi75.soundspot.model.NewReleaseSong;

import java.util.ArrayList;
import java.util.List;

public class SearchMusicActivity extends AppCompatActivity implements CategoryClickListener {

    private ActivitySearchMusicBinding binding;


    private String activeGenre = null;
    private List<NewReleaseSong> topSongs = new ArrayList<>();
    private List<NewReleaseSong> forYouSongs = new ArrayList<>();
    private List<NewReleaseAlbum> albums = new ArrayList<>();
    private List<SearchResult> searchResults = new ArrayList<>();
    private boolean isInCategoryScreen = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivitySearchMusicBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        // Initialize views

        // Setup animations
        binding.searchEditText.setAnimation(AnimationUtils.loadAnimation(this, R.anim.fade_in));
        binding.genresChipGroup.setAnimation(AnimationUtils.loadAnimation(this, R.anim.slide_up));

        // Setup search functionality
        binding.searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String query = s.toString();
                if (query.isEmpty()) {
                    if (!isInCategoryScreen) {
                        showMainContent();
                    }
                } else {
                    showSearchResults(query);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        });

        // Setup back button
        binding.backButton.setOnClickListener(v -> {
            if (isInCategoryScreen) {
                showMainContent();
                isInCategoryScreen = false;
            }
            // Additional back logic if needed
            binding.cvPickedForYou.setVisibility(View.VISIBLE);
        });

        // Setup genre chips
        setupGenreChips();

        // Initialize data
        initializeData();

        // Setup recycler views
        setupRecyclerViews();

        // Setup category cards
        setupCategoryCards();
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
        topSongs.add(new NewReleaseSong(1, "Greedy", "Tate McRae", R.drawable.baseline_album_24));
        topSongs.add(new NewReleaseSong(2, "One of the Girls", "The Weeknd & Lily Rose", R.drawable.baseline_album_24));
        topSongs.add(new NewReleaseSong(3, "Popular", "The Weeknd, Playboi Carti", R.drawable.baseline_album_24));
        topSongs.add(new NewReleaseSong(4, "I Wanna Be Yours", "Arctic Monkeys", R.drawable.baseline_album_24));

        // Initialize for you songs
        forYouSongs.add(new NewReleaseSong(1, "Half of My Heart", "John Mayer", R.drawable.baseline_album_24));
        forYouSongs.add(new NewReleaseSong(2, "Dance The Night", "Dua Lipa", R.drawable.baseline_album_24));
        forYouSongs.add(new NewReleaseSong(3, "End Game", "Ft. Ed Sheeran", R.drawable.baseline_album_24));

        // Initialize albums
        albums.add(new NewReleaseAlbum("XXL (Stripped)", "Various Artists", R.drawable.baseline_album_24, R.color.blue_500));
        albums.add(new NewReleaseAlbum("23 favorite", "Various Artists", R.drawable.baseline_album_24, R.color.gray_800));
        albums.add(new NewReleaseAlbum("Waiting Back", "Various Artists", R.drawable.baseline_album_24, R.color.orange_300));
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
        AlbumsAdapter albumsAdapter = new AlbumsAdapter(albums);
        binding.albumsRecyclerView.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        binding.albumsRecyclerView.setAdapter(albumsAdapter);

        // Setup search results recycler view
        SearchResultsAdapter searchResultsAdapter = new SearchResultsAdapter(searchResults);
        binding.searchResultsRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        binding.searchResultsRecyclerView.setAdapter(searchResultsAdapter);
    }

    private void showMainContent() {
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

    private void showSearchResults(String query) {
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
        searchResults.add(new SearchResult(SearchResult.TYPE_SONG, query + " Song Result 1", "Artist Name", R.drawable.baseline_album_24));
        searchResults.add(new SearchResult(SearchResult.TYPE_SONG, query + " Song Result 2", "Another Artist", R.drawable.baseline_album_24));
        searchResults.add(new SearchResult(SearchResult.TYPE_ALBUM, query + " Album", "Various Artists", R.drawable.baseline_album_24));
        searchResults.add(new SearchResult(SearchResult.TYPE_ARTIST, query + " Artist", "", R.drawable.baseline_album_24));
        searchResults.add(new SearchResult(SearchResult.TYPE_PLAYLIST, query + " Playlist", "Spotify", R.drawable.baseline_album_24));

        // Notify adapter
        binding.searchResultsRecyclerView.getAdapter().notifyDataSetChanged();

        // Update title
        TextView searchResultsTitle = findViewById(R.id.search_results_title);
        searchResultsTitle.setText("Search Results for \"" + query + "\"");

        isInCategoryScreen = false;
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
        // This is a simple filter for demo purposes
        // In a real app, you would filter based on actual genre metadata

        List<NewReleaseSong> filteredTopSongs = new ArrayList<>();
        List<NewReleaseSong> filteredForYouSongs = new ArrayList<>();

        // Simple filtering logic - in a real app this would be more sophisticated
        for (NewReleaseSong song : topSongs) {
            if (song.id % 2 == 0) { // Just a demo filter
                filteredTopSongs.add(song);
            }
        }

        for (NewReleaseSong song : forYouSongs) {
            if (song.id % 2 == 0) { // Just a demo filter
                filteredForYouSongs.add(song);
            }
        }

        // Update adapters
        ((TopSongsAdapter) binding.topSongsRecyclerView.getAdapter()).updateSongs(filteredTopSongs);
        ((ForYouAdapter) binding.forYouRecyclerView.getAdapter()).updateSongs(filteredForYouSongs);
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
        binding.cvPickedForYou.setVisibility(View.VISIBLE);
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


