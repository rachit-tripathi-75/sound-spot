package com.rachit.tripathi75.soundspot.activities;

import static androidx.core.content.ContentProviderCompat.requireContext;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.viewpager2.adapter.FragmentStateAdapter;

import com.google.android.material.snackbar.Snackbar;
import com.google.android.material.tabs.TabLayoutMediator;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.gson.Gson;
import com.rachit.tripathi75.soundspot.R;
import com.rachit.tripathi75.soundspot.classes.PrefsManager;
import com.rachit.tripathi75.soundspot.databinding.ActivityArtistProfileBinding;
import com.rachit.tripathi75.soundspot.fragments.AlbumsFragment;
import com.rachit.tripathi75.soundspot.fragments.ClipsFragment;
import com.rachit.tripathi75.soundspot.fragments.SinglesFragment;
import com.rachit.tripathi75.soundspot.fragments.TopSongsFragment;
import com.rachit.tripathi75.soundspot.model.BasicDataRecord;
import com.rachit.tripathi75.soundspot.network.ApiManager;
import com.rachit.tripathi75.soundspot.network.NetworkChangeReceiver;
import com.rachit.tripathi75.soundspot.network.utility.RequestNetwork;
import com.rachit.tripathi75.soundspot.records.ArtistSearch;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

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
    static String artistId = "9999";
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

        setupToolbar();
        setupAnimations();
        setupFollowingButton();
        setupListeners();
        showData();


//        setSupportActionBar(binding.collapsingToolbar);
//        if (getSupportActionBar() != null) getSupportActionBar().setDisplayShowTitleEnabled(false);
//        binding.collapsingToolbarLayout.setExpandedTitleColor(getResources().getColor(android.R.color.transparent));
//
//        getWindow().getDecorView().setSystemUiVisibility(
//                View.SYSTEM_UI_FLAG_LAYOUT_STABLE | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
//        getWindow().setStatusBarColor(Color.TRANSPARENT);
//
//        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
//        getSupportActionBar().setDisplayShowHomeEnabled(true);
//
////        binding.collapsingToolbarLayout.setTitle("Artist Name");
//
//        binding.collapsingToolbarAppbarlayout.addOnOffsetChangedListener((appBarLayout, verticalOffset) -> {
//            if (verticalOffset == 0) {
//            } else {
//            }
//        });
//
//        binding.topSongsRecyclerview.setLayoutManager(new LinearLayoutManager(this));
//        binding.topAlbumsRecyclerview.setLayoutManager(new LinearLayoutManager(this));
//        binding.topSinglesRecyclerview.setLayoutManager(new LinearLayoutManager(this));
//
//        binding.topSongsSeeMore.setOnClickListener(v -> {
//            startActivity(new Intent(ArtistProfileActivity.this, SeeMoreActivity.class)
//                    .putExtra("id", artistId)
//                    .putExtra("type", ActivitySeeMoreListAdapter.Mode.TOP_SONGS.name())
//                    .putExtra("artist_name", binding.artistName.getText().toString()));
//        });
//        binding.topAlbumsSeeMore.setOnClickListener(v -> {
//            startActivity(new Intent(ArtistProfileActivity.this, SeeMoreActivity.class)
//                    .putExtra("id", artistId)
//                    .putExtra("type", ActivitySeeMoreListAdapter.Mode.TOP_ALBUMS.name())
//                    .putExtra("artist_name", binding.artistName.getText().toString()));
//        });
//        binding.topSinglesSeeMore.setOnClickListener(v -> {
//
//        });
//
//        showShimmerData();
//        showData();
    }

    private void setupFollowingButton() {
        DatabaseReference db = FirebaseDatabase.getInstance().getReference();
        db.child("users").child(PrefsManager.getUserDetails(this).getId()).child("followedArtistsId").addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                boolean found = false;
                for (DataSnapshot artistSnapshot : snapshot.getChildren()) {
                    String id = artistSnapshot.getValue(String.class);
                    if (id != null && id.equals(artistId)) {
                        found = true;
                        break;
                    }
                }

                if (found) {
                    Log.d("checkArtist", "Artist ID " + artistId + " is followed.");
                    binding.followButton.setText(getString(R.string.following));
                } else {
                    Log.d("checkArtist", "Artist ID " + artistId + " is NOT followed.");
                    binding.followButton.setText(getString(R.string.follow));
                }

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Log.e("FirebaseError", "Error reading data", error.toException());
            }
        });
    }


    private void setupToolbar() {
        setSupportActionBar(binding.toolbar);
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayShowTitleEnabled(false);
        }
    }

    private void setupViewPager(ArtistSearch artistSearch) {
        ArtistPagerAdapter pagerAdapter = new ArtistPagerAdapter(this, artistSearch);
        binding.viewPager.setAdapter(pagerAdapter);

        // Connect TabLayout with ViewPager2
        new TabLayoutMediator(binding.tabLayout, binding.viewPager, (tab, position) -> {
            switch (position) {
                case 0:
                    tab.setText(getString(R.string.top_10));
                    break;
                case 1:
                    tab.setText(getString(R.string.clips));
                    break;
                case 2:
                    tab.setText(getString(R.string.singles));
                    break;
                case 3:
                    tab.setText(getString(R.string.albums));
                    break;
                default:
                    tab.setText(null);
            }
        }).attach();
    }

    private void setupAnimations() {
        // Animate artist image
        Animation fadeIn = AnimationUtils.loadAnimation(this, R.anim.fade_in);
        binding.ivArtist.startAnimation(fadeIn);

        // Animate artist info
        Animation slideUp = AnimationUtils.loadAnimation(this, R.anim.slide_up);
        binding.artistName.startAnimation(slideUp);
        binding.monthlyListeners.startAnimation(slideUp);

        // Animate buttons with delay
        binding.followButton.setAlpha(0f);
        binding.shuffleButton.setAlpha(0f);
        binding.playButton.setAlpha(0f);

        binding.followButton.animate().alpha(1f).setDuration(300).setStartDelay(300).start();
        binding.shuffleButton.animate().alpha(1f).setDuration(300).setStartDelay(400).start();
        binding.playButton.animate().alpha(1f).setDuration(300).setStartDelay(500).start();

        // Scale animation for play button
        Animation scaleIn = AnimationUtils.loadAnimation(this, R.anim.scale_in);
        binding.playButton.startAnimation(scaleIn);
    }

    private void setupListeners() {
        binding.backButton.setOnClickListener(v -> onBackPressed());

        binding.followButton.setOnClickListener(v -> {
            // Toggle follow state
            boolean isFollowing = binding.followButton.getText().equals(getString(R.string.following));
            if (isFollowing) {
                binding.followButton.setText(getString(R.string.follow));
            } else {
                binding.followButton.setText(getString(R.string.following));
                // Show a quick scale animation
                Animation scaleIn = AnimationUtils.loadAnimation(this, R.anim.scale_in);
                binding.followButton.startAnimation(scaleIn);
            }
        });

        binding.playButton.setOnClickListener(v -> {
            // Play animation
            binding.playButton.animate()
                    .scaleX(0.85f)
                    .scaleY(0.85f)
                    .setDuration(100)
                    .withEndAction(() -> binding.playButton.animate()
                            .scaleX(1f)
                            .scaleY(1f)
                            .setDuration(100)
                            .start())
                    .start();

            // Start playing music logic would go here
        });
    }

    static class ArtistPagerAdapter extends FragmentStateAdapter {
        private ArtistSearch artistSearch;

        public ArtistPagerAdapter(@NonNull FragmentActivity fragmentActivity, ArtistSearch artistSearch) {
            super(fragmentActivity);
            this.artistSearch = artistSearch;
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            // Return the appropriate fragment for each tab
            return switch (position) {
                case 0 -> TopSongsFragment.newInstance(artistId, artistSearch.data().topSongs());
                case 1 -> ClipsFragment.newInstance(artistId, artistSearch.success());
                case 2 -> SinglesFragment.newInstance(artistId, artistSearch.data().singles());
                case 3 -> AlbumsFragment.newInstance(artistId, artistSearch.data().topAlbums());
                default -> TopSongsFragment.newInstance(artistId, artistSearch.data().topSongs());
            };
        }

        @Override
        public int getItemCount() {
            return 4;
        }
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


    void showData() {
        if (getIntent().getExtras() == null) return;
        final String artist = getIntent().getExtras().getString("data", "null");
        final BasicDataRecord artistItem = new Gson().fromJson(artist, BasicDataRecord.class);
        if (artistItem == null) return;
        artistId = artistItem.id();

        Picasso.get().load(Uri.parse(artistItem.image())).into(binding.ivArtist);
        binding.artistName.setText(artistItem.title());
//        binding.collapsingToolbarLayout.setTitle(artistItem.title());

        final ApiManager apiManager = new ApiManager(this);
        apiManager.retrieveArtistById(artistId, new RequestNetwork.RequestListener() {
            @Override
            public void onResponse(String tag, String response, HashMap<String, Object> responseHeaders) {
                ArtistSearch artistSearch = new Gson().fromJson(response, ArtistSearch.class);
                Log.i(TAG, "onResponse: " + response);
                if (artistSearch.success()) {
                    Picasso.get().load(Uri.parse(artistSearch.data().image().get(artistSearch.data().image().size() - 1).url())).into(binding.ivArtist);
                    binding.artistName.setText(artistSearch.data().name());
//                    binding.collapsingToolbarLayout.setTitle(artistSearch.data().name());
                    setupViewPager(artistSearch);
//                    binding.topSongsRecyclerview.setAdapter(new ActivityArtistProfileTopSongsAdapter(artistSearch.data().topSongs()));
//                    binding.topAlbumsRecyclerview.setAdapter(new ActivityArtistProfileTopAlbumsAdapter(artistSearch.data().topAlbums()));
//                    binding.topSinglesRecyclerview.setAdapter(new ActivityArtistProfileTopAlbumsAdapter(artistSearch.data().singles()));
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

//    @NonNull
//    private static List<SongResponse.Song> getShimmerData() {
//        final List<SongResponse.Song> shimmerData = new ArrayList<>();
//        for (int i = 0; i < 11; i++) {
//            shimmerData.add(new SongResponse.Song(
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
//                    new SongResponse.Lyrics("", "", ""),
//                    "",
//                    "",
//                    new SongResponse.Album("", "", ""),
//                    new SongResponse.Artists(new ArrayList<>(), new ArrayList<>(), new ArrayList<>()),
//                    new ArrayList<>(),
//                    new ArrayList<>()
//            ));
//        }
//        return shimmerData;
//    }
//
//
//    void showShimmerData() {
//        final List<SongResponse.Song> shimmerData = getShimmerData();
//        final List<AlbumsSearch.Data.Results> shimmerDataAlbum = new ArrayList<>();
//        for (int i = 0; i < 11; i++) {
//            shimmerDataAlbum.add(new AlbumsSearch.Data.Results(
//                    "<shimmer>",
//                    null,
//                    null,
//                    null,
//                    0,
//                    null,
//                    0,
//                    null,
//                    false,
//                    null,
//                    null
//            ));
//        }
//
//
//        binding.topSongsRecyclerview.setAdapter(new ActivityArtistProfileTopSongsAdapter(shimmerData));
//        binding.topAlbumsRecyclerview.setAdapter(new ActivityArtistProfileTopAlbumsAdapter(shimmerDataAlbum));
//        binding.topSinglesRecyclerview.setAdapter(new ActivityArtistProfileTopAlbumsAdapter(shimmerDataAlbum));
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        if (item.getItemId() == android.R.id.home) {
//            onBackPressed();
//            return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }
}