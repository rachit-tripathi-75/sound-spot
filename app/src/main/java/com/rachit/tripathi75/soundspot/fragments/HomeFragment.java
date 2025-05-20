package com.rachit.tripathi75.soundspot.fragments;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.app.ActivityOptionsCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.facebook.login.LoginManager;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.gson.Gson;
import com.rachit.tripathi75.soundspot.ApplicationClass;
import com.rachit.tripathi75.soundspot.R;
import com.rachit.tripathi75.soundspot.activities.AboutActivity;
import com.rachit.tripathi75.soundspot.activities.SearchMusicActivity;
import com.rachit.tripathi75.soundspot.activities.SettingsActivity;
import com.rachit.tripathi75.soundspot.activities.SplashScreenActivity;
import com.rachit.tripathi75.soundspot.classes.FollowingArtist;
import com.rachit.tripathi75.soundspot.classes.FollowingArtistAdapter;
import com.rachit.tripathi75.soundspot.classes.Playlist;
import com.rachit.tripathi75.soundspot.classes.PlaylistAdapter;
import com.rachit.tripathi75.soundspot.classes.PrefsManager;
import com.rachit.tripathi75.soundspot.databinding.FragmentHomeBinding;
import com.rachit.tripathi75.soundspot.model.AlbumItem;
import com.rachit.tripathi75.soundspot.network.ApiManager;
import com.rachit.tripathi75.soundspot.network.NetworkChangeReceiver;
import com.rachit.tripathi75.soundspot.network.utility.RequestNetwork;
import com.rachit.tripathi75.soundspot.records.AlbumsSearch;
import com.rachit.tripathi75.soundspot.records.ArtistsSearch;
import com.skydoves.powermenu.MenuAnimation;
import com.skydoves.powermenu.OnMenuItemClickListener;
import com.skydoves.powermenu.PowerMenu;
import com.skydoves.powermenu.PowerMenuItem;

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

    private final List<Playlist> playlists = new ArrayList<>();
//    private final List<AlbumItem> playlists = new ArrayList<>();

    //    private SlidingRootNav slidingRootNavBuilder;
//
    Handler handler = new Handler();
//    Runnable runnable = this::showPlayBarData;

    private PowerMenu powerMenu;
    private ActivityOptionsCompat options;


    private FollowingArtistAdapter artistAdapter;
    private PlaylistAdapter playlistAdapter;
    private boolean isPlaying = false;
    //    private Handler handler = new Handler();
    private int progressStatus = 0;
    private final int MAX_PROGRESS = 100;
    private Runnable progressRunnable;


    public HomeFragment() {
        // Required empty public constructor
    }

    NetworkChangeReceiver networkChangeReceiver = new NetworkChangeReceiver(new NetworkChangeReceiver.NetworkStatusListener() {
        @Override
        public void onNetworkConnected() {
//            if (songs.isEmpty() || artists.isEmpty() || albums.isEmpty() || playlists.isEmpty())
//                showData();
        }

        @Override
        public void onNetworkDisconnected() {
//            if (songs.isEmpty() || artists.isEmpty() || albums.isEmpty() || playlists.isEmpty())
//                showOfflineData();
//            Snackbar.make(binding.getRoot(), "No Internet Connection", Snackbar.LENGTH_LONG).show();
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
        options = ActivityOptionsCompat.makeCustomAnimation(requireContext(), R.anim.fade_in, R.anim.fade_out);

//        slidingRootNavBuilder = new SlidingRootNavBuilder(requireActivity())
//                .withMenuLayout(R.layout.main_drawer_layout)
//                .withContentClickableWhenMenuOpened(false)
//                .withDragDistance(250)
//                .inject();
//
//        onDrawerItemsClicked();

//        binding.profileIcon.setOnClickListener(view -> slidingRootNavBuilder.openMenu(true));

        int span = calculateNoOfColumns(requireContext(), 200);
//        binding.playlistRecyclerView.setLayoutManager(new GridLayoutManager(requireContext(), span));
//
//        binding.popularSongsRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        binding.rvFollowedArtists.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
//        binding.popularAlbumsRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
//        binding.savedRecyclerView.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
//
//        OverScrollDecoratorHelper.setUpOverScroll(binding.popularSongsRecyclerView, OverScrollDecoratorHelper.ORIENTATION_HORIZONTAL);
        OverScrollDecoratorHelper.setUpOverScroll(binding.rvFollowedArtists, OverScrollDecoratorHelper.ORIENTATION_HORIZONTAL);
//        OverScrollDecoratorHelper.setUpOverScroll(binding.popularAlbumsRecyclerView, OverScrollDecoratorHelper.ORIENTATION_HORIZONTAL);
//        OverScrollDecoratorHelper.setUpOverScroll(binding.savedRecyclerView, OverScrollDecoratorHelper.ORIENTATION_HORIZONTAL);

        FirebaseAuth firebaseAuth = ApplicationClass.getFirebaseAuth();
        FirebaseUser user = firebaseAuth.getCurrentUser();


//        binding.refreshLayout.setOnRefreshListener(() -> {
//            showShimmerData();
//            showData();
//            binding.refreshLayout.setRefreshing(false);
//        });

//        binding.playBarPlayPauseIcon.setOnClickListener(view -> {
//            ApplicationClass applicationClass = (ApplicationClass) requireActivity().getApplication();
//            ApplicationClass.setCurrentActivity(requireActivity());
//
//            applicationClass.togglePlayPause();
//            applicationClass.showNotification(ApplicationClass.player.isPlaying() ? R.drawable.baseline_pause_24 : R.drawable.play_arrow_24px);
//            binding.playBarPlayPauseIcon.setImageResource(ApplicationClass.player.isPlaying() ? R.drawable.baseline_pause_24 : R.drawable.play_arrow_24px);
//        });
//
//        binding.playBarBackground.setOnClickListener(view -> {
//            if (!ApplicationClass.MUSIC_ID.isBlank())
//                startActivity(new Intent(requireContext(), MusicOverviewActivity.class).putExtra("id", ApplicationClass.MUSIC_ID));
//        });
//
//        binding.playBarPrevIcon.setOnClickListener(view -> {
//            applicationClass.prevTrack();
//        });
//
//        binding.playBarNextIcon.setOnClickListener(view -> {
//            applicationClass.nextTrack();
//        });

        listeners();
//        showShimmerData();
//        showOfflineData();

        showData();
//        showPlayBarData();

        showSavedLibrariesData();

        // ----------------------------------------------


        // -------------- add the PowerMenu dialog -----------------------
        powerMenu = new PowerMenu.Builder(requireContext())
//                .addItem(new PowerMenuItem("Profile", false, R.drawable.user3))
                .addItem(new PowerMenuItem("Settings", false, R.drawable.settings_24px))
                .addItem(new PowerMenuItem("About Developer", false, R.drawable.round_info_outline_24))
                .addItem(new PowerMenuItem("Sign Out", false, R.drawable.logout))
                .setAnimation(MenuAnimation.DROP_DOWN)
                .setMenuRadius(10f) // sets the corner radius.
                .setMenuShadow(10f) // sets the shadow.
                .setTextColor(ContextCompat.getColor(requireContext(), R.color.white))
                .setTextGravity(Gravity.CENTER)
                .setTextTypeface(Typeface.create("sans-serif-medium", Typeface.BOLD))
                .setSelectedTextColor(Color.BLACK)
                .setMenuColor(Color.BLACK)
                .setSelectedMenuColor(ContextCompat.getColor(requireContext(), R.color.button_dark))
                .setOnMenuItemClickListener(new OnMenuItemClickListener<PowerMenuItem>() {
                    @Override
                    public void onItemClick(int position, PowerMenuItem item) {
//                        if (position == 0) {
//                            Toast.makeText(applicationClass, "Opens Profile", Toast.LENGTH_SHORT).show();
//                        }
                        if (position == 0) {
                            Intent intent = new Intent(requireContext(), SettingsActivity.class);
                            startActivity(intent, options.toBundle());
//                            Toast.makeText(applicationClass, "Opens Settings", Toast.LENGTH_SHORT).show();
                        } else if (position == 1) {
                            Intent intent = new Intent(requireContext(), AboutActivity.class);
                            startActivity(intent, options.toBundle());
                        } else if (position == 2) {
                            signOut();
                            PrefsManager.setSession(requireContext(), false);
                            Intent intent = new Intent(requireContext(), SplashScreenActivity.class);
                            startActivity(intent, options.toBundle());
                            getActivity().finishAffinity();
//                            Toast.makeText(applicationClass, "Signed Out", Toast.LENGTH_SHORT).show();
                        }

                    }
                })
                .build();


        initViews();
        setupAnimations();
        setupRecyclerViews();
//        setupClickListeners();


        return binding.getRoot();
    }


    private void initViews() {

    }

    private void setupAnimations() {
        // Animate header elements
        Animation fadeIn = AnimationUtils.loadAnimation(requireContext(), R.anim.fade_in);
        fadeIn.setStartOffset(200);
        binding.clMainContainer.startAnimation(fadeIn);

//        Animation slideUp = AnimationUtils.loadAnimation(requireContext(), R.anim.slide);
//        slideUp.setStartOffset(500);
//        binding.ivAppLogo.startAnimation(slideUp);


    }

    private void setupRecyclerViews() {
        // Setup Artists RecyclerView
        LinearLayoutManager artistLayoutManager = new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false);
        binding.rvFollowedArtists.setLayoutManager(artistLayoutManager);

        List<FollowingArtist> artists = new ArrayList<>();
        artists.add(new FollowingArtist("Taylor Swift", R.drawable.artist_placeholder));
        artists.add(new FollowingArtist("Artist name", R.drawable.artist_placeholder));
        artists.add(new FollowingArtist("Artist name", R.drawable.artist_placeholder));
        artists.add(new FollowingArtist("Artist name", R.drawable.artist_placeholder));
        artists.add(new FollowingArtist("Artist name", R.drawable.artist_placeholder));

//        artistAdapter = new FollowingArtistAdapter(requireContext(), artists);
        binding.rvFollowedArtists.setAdapter(artistAdapter);

        // Add animation to artists
        binding.rvFollowedArtists.setLayoutAnimation(AnimationUtils.loadLayoutAnimation(requireContext(), R.anim.layout_animation_slide_right));
        binding.rvFollowedArtists.scheduleLayoutAnimation();

        // Setup Playlists RecyclerView
//        GridLayoutManager playlistLayoutManager = new GridLayoutManager(requireContext(), 2);
        binding.rvPlaylists.setLayoutManager(new LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false));
        List<Playlist> playlists = new ArrayList<>();
//        playlists.add(new Playlist("After Hours", "The Weeknd", R.drawable.baseline_album_24));
//        playlists.add(new Playlist("Radical Optimism", "Dua Lipa", R.drawable.baseline_album_24));
//        playlists.add(new Playlist("The Highlights", "The Weeknd", R.drawable.baseline_album_24));
//

        playlistAdapter = new PlaylistAdapter(requireContext(), playlists);
        binding.rvPlaylists.setAdapter(playlistAdapter);

        // Add animation to playlists
        binding.rvPlaylists.setLayoutAnimation(AnimationUtils.loadLayoutAnimation(requireContext(), R.anim.layout_animation_fall_down));
        binding.rvPlaylists.scheduleLayoutAnimation();
    }


    private void animateButtonClick(View view) {
        view.animate()
                .scaleX(0.8f)
                .scaleY(0.8f)
                .setDuration(100)
                .withEndAction(() -> {
                    view.animate()
                            .scaleX(1f)
                            .scaleY(1f)
                            .setDuration(100)
                            .start();
                })
                .start();
    }

    private void animateTextClick(TextView textView) {
        ObjectAnimator colorAnim = ObjectAnimator.ofArgb(
                textView,
                "textColor",
                getResources().getColor(R.color.gray),
                getResources().getColor(R.color.white),
                getResources().getColor(R.color.gray)
        );
        colorAnim.setDuration(500);
        colorAnim.start();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        if (handler != null) {
            handler.removeCallbacks(progressRunnable);
        }
    }

//    @Override
//    protected void onDestroy() {
//        super.onDestroy();
//        if (handler != null) {
//            handler.removeCallbacks(progressRunnable);
//        }
//    }


    private void signOut() {
        int loggedInType = PrefsManager.getLogInType(requireContext());
        Toast.makeText(requireContext(), "loginType: " + loggedInType, Toast.LENGTH_SHORT).show();
        FirebaseAuth firebaseAuth = ApplicationClass.getFirebaseAuth();

        if (loggedInType == 1) { // for Email & Password
            firebaseAuth.signOut();
        } else if (loggedInType == 2) { // Google Sign-Out
            GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(requireContext(), GoogleSignInOptions.DEFAULT_SIGN_IN);
            googleSignInClient.signOut().addOnCompleteListener(task -> {
                firebaseAuth.signOut();
                navigateToSplashScreen();
            });
            return;
        } else if (loggedInType == 3) { // Facebook Sign-Out
            LoginManager.getInstance().logOut();
            firebaseAuth.signOut();
        } else if (loggedInType == 4) { // Apple Sign-Out
            // Apple sign-out logic, its to be implemented.....!!!
        } else if (loggedInType == -1) { // Guest Mode
            Toast.makeText(requireContext(), "An error occurred while logging out", Toast.LENGTH_SHORT).show();
            return;
        }

        PrefsManager.setSession(requireContext(), false);
        navigateToSplashScreen(); // Ensures navigation happens for other login types
    }

    private void navigateToSplashScreen() {
        Intent intent = new Intent(requireContext(), SplashScreenActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        requireActivity().finishAffinity();
    }


    private void listeners() {
        binding.ivProfilePic.setOnClickListener(view -> {
            powerMenu.showAsDropDown(binding.ivProfilePic);
        });

        binding.ibSearch.setOnClickListener(view -> {
            startActivity(new Intent(requireContext(), SearchMusicActivity.class));
        });


    }

    private void showSavedLibrariesData() {
//        SavedLibraries savedLibraries = SharedPreferenceManager.getInstance(requireContext()).getSavedLibrariesData();
//        binding.savedLibrariesSection.setVisibility(savedLibraries != null && !(savedLibraries.lists().isEmpty()) ? View.VISIBLE : View.GONE);
//        if (savedLibraries != null)
//            binding.savedRecyclerView.setAdapter(new SavedLibrariesAdapter(savedLibraries.lists()));
    }

//    void showPlayBarData() {
//        binding.playBarMusicTitle.setText(ApplicationClass.MUSIC_TITLE);
//        binding.playBarMusicDesc.setText(ApplicationClass.MUSIC_DESCRIPTION);
//        Picasso.get().load(Uri.parse(ApplicationClass.IMAGE_URL)).into(binding.playBarCoverImage);
//        if (ApplicationClass.player.isPlaying()) {
//            binding.playBarPlayPauseIcon.setImageResource(R.drawable.baseline_pause_24);
//        } else {
//            binding.playBarPlayPauseIcon.setImageResource(R.drawable.play_arrow_24px);
//        }
//
//        GradientDrawable gradientDrawable = new GradientDrawable();
//        gradientDrawable.setColor(ApplicationClass.IMAGE_BG_COLOR);
//        gradientDrawable.setCornerRadius(18);
//
//        binding.playBarBackground.setBackground(gradientDrawable);
//
//        binding.playBarMusicTitle.setTextColor(ApplicationClass.TEXT_ON_IMAGE_COLOR1);
//        binding.playBarMusicDesc.setTextColor(ApplicationClass.TEXT_ON_IMAGE_COLOR1);
//
//        binding.playBarPlayPauseIcon.setImageTintList(ColorStateList.valueOf(ApplicationClass.TEXT_ON_IMAGE_COLOR));
//        binding.playBarPrevIcon.setImageTintList(ColorStateList.valueOf(ApplicationClass.TEXT_ON_IMAGE_COLOR));
//        binding.playBarNextIcon.setImageTintList(ColorStateList.valueOf(ApplicationClass.TEXT_ON_IMAGE_COLOR));
//
//        OverScrollDecoratorHelper.setUpStaticOverScroll(binding.getRoot(), OverScrollDecoratorHelper.ORIENTATION_VERTICAL);
//
//        handler.postDelayed(runnable, 1000);
//    }

//    private void showShimmerData() {
//        final List<AlbumItem> data_shimmer = new ArrayList<>();
//        final List<ArtistsSearch.Data.Results> artists_shimmer = new ArrayList<>();
//        for (int i = 0; i < 11; i++) {
//            data_shimmer.add(new AlbumItem("<shimmer>", "<shimmer>", "<shimmer>", "<shimmer>"));
//            artists_shimmer.add(new ArtistsSearch.Data.Results(
//                    "<shimmer>",
//                    "<shimmer>",
//                    "<shimmer>",
//                    "<shimmer>",
//                    "<shimmer>",
//                    null
//            ));
//        }
//        binding.popularSongsRecyclerView.setAdapter(new ActivityMainAlbumItemAdapter(data_shimmer));
//        binding.popularAlbumsRecyclerView.setAdapter(new ActivityMainAlbumItemAdapter(data_shimmer));
//        binding.popularArtistsRecyclerView.setAdapter(new ActivityMainArtistsItemAdapter(artists_shimmer));
//        binding.playlistRecyclerView.setAdapter(new ActivityMainPlaylistAdapter(data_shimmer));
//
//    }

//    private void onDrawerItemsClicked() {
//        slidingRootNavBuilder.getLayout().findViewById(R.id.settings).setOnClickListener(v -> {
//            startActivity(new Intent(requireContext(), SettingsActivity.class));
//            slidingRootNavBuilder.closeMenu();
//        });
//
//        slidingRootNavBuilder.getLayout().findViewById(R.id.logo).setOnClickListener(view -> slidingRootNavBuilder.closeMenu());
//
//        slidingRootNavBuilder.getLayout().findViewById(R.id.library).setOnClickListener(view -> {
//            startActivity(new Intent(requireContext(), SavedLibrariesActivity.class));
//            slidingRootNavBuilder.closeMenu();
//        });
//
//        slidingRootNavBuilder.getLayout().findViewById(R.id.about).setOnClickListener(view -> {
//            startActivity(new Intent(requireContext(), AboutActivity.class));
//            slidingRootNavBuilder.closeMenu();
//        });
//
//    }

    private void showData() {
        songs.clear();
        artists.clear();
        albums.clear();
        playlists.clear();

        final ApiManager apiManager = new ApiManager(requireActivity());

//        apiManager.searchSongs(" ", 0, 15, new RequestNetwork.RequestListener() {
//            @Override
//            public void onResponse(String tag, String response, HashMap<String, Object> responseHeaders) {
//                SongSearch songSearch = new Gson().fromJson(response, SongSearch.class);
//                Log.i(TAG, "onResponse: " + response);
//                if (songSearch.success()) {
//                    songSearch.data().results().forEach(results -> {
//                        songs.add(new AlbumItem(results.name(), results.language() + " " + results.year(), results.image().get(results.image().size() - 1).url(), results.id()));
//                        ActivityMainPopularSongs adapter = new ActivityMainPopularSongs(songs);
//                        binding.popularSongsRecyclerView.setAdapter(adapter);
//                        adapter.notifyDataSetChanged();
//                    });
//                    ApplicationClass.sharedPreferenceManager.setHomeSongsRecommended(songSearch);
//                } else {
//                    try {
//                        showOfflineData();
//                        Toast.makeText(requireContext(), new JSONObject(response).getString("message"), Toast.LENGTH_SHORT).show();
//                    } catch (JSONException e) {
//                        Log.e(TAG, "onResponse: ", e);
//                    }
//                }
//            }
//
//
//            @Override
//            public void onErrorResponse(String tag, String message) {
//                showOfflineData();
//            }
//        });

        apiManager.searchArtists(" ", 0, 15, new RequestNetwork.RequestListener() {
            @Override
            public void onResponse(String tag, String response, HashMap<String, Object> responseHeaders) {
                ArtistsSearch artistSearch = new Gson().fromJson(response, ArtistsSearch.class);
                Log.i(TAG, "onResponse: " + response);
                if (artistSearch.success()) {
                    artistSearch.data().results().forEach(results -> {
                        artists.add(results);
                        Gson gson = new Gson();
//                        Log.d("artistsTAG", gson.toJson(results));
//                        ActivityMainArtistsItemAdapter adapter = new ActivityMainArtistsItemAdapter(artists);
                        FollowingArtistAdapter adapter = new FollowingArtistAdapter(requireContext(), artists);
                        binding.rvFollowedArtists.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    });
                    ApplicationClass.sharedPreferenceManager.setHomeArtistsRecommended(artistSearch);
                } else {
                    try {
//                        showOfflineData();
                        Toast.makeText(requireContext(), new JSONObject(response).getString("message"), Toast.LENGTH_SHORT).show();
                    } catch (JSONException e) {
                        Log.e(TAG, "onResponse: ", e);
                    }
                }
            }

            @Override
            public void onErrorResponse(String tag, String message) {
//                showOfflineData();
            }
        });

        apiManager.searchAlbums("Yo Yo Honey Singh", 0, 15, new RequestNetwork.RequestListener() {
            @Override
            public void onResponse(String tag, String response, HashMap<String, Object> responseHeaders) {
                AlbumsSearch albumsSearch = new Gson().fromJson(response, AlbumsSearch.class);
                Log.i(TAG, "onResponse: " + response);
                if (albumsSearch.success()) {
                    albumsSearch.data().results().forEach(results -> {
                        Gson gson = new Gson();
//                        Log.d("playlistTAG", gson.toJson(results));
                        playlists.add(new Playlist(results.name(), results.artist().all().get(0).name(), results.image().get(2).url()));
//                        albums.add(new AlbumItem(results.name(), results.language() + " " + results.year(), results.image().get(results.image().size() - 1).url(), results.id()));
//                        ActivityMainAlbumItemAdapter adapter = new ActivityMainAlbumItemAdapter(albums);
//                        binding.popularAlbumsRecyclerView.setAdapter(adapter);
//                        playlists.add(albums);
                        PlaylistAdapter adapter = new PlaylistAdapter(requireContext(), playlists);
                        binding.rvPlaylists.setAdapter(adapter);
                        adapter.notifyDataSetChanged();
                    });
                    ApplicationClass.sharedPreferenceManager.setHomeAlbumsRecommended(albumsSearch);
                } else {
                    try {
                        Toast.makeText(requireContext(), new JSONObject(response).getString("message"), Toast.LENGTH_SHORT).show();
//                        showOfflineData();
                    } catch (JSONException e) {
                        Log.e(TAG, "onResponse: ", e);
                    }
                }
            }

            @Override
            public void onErrorResponse(String tag, String message) {
//                showOfflineData();
            }
        });

        // String.valueOf(Calendar.getInstance().get(Calendar.YEAR))
//        apiManager.searchPlaylists("2024", 0, 15, new RequestNetwork.RequestListener() {
//            @Override
//            public void onResponse(String tag, String response, HashMap<String, Object> responseHeaders) {
//                PlaylistsSearch playlistsSearch = new Gson().fromJson(response, PlaylistsSearch.class);
//                Log.i(TAG, "onResponse: " + response);
//                if (playlistsSearch.success()) {
//                    playlistsSearch.data().results().forEach(results -> {
//                        playlists.add(new AlbumItem(results.name(), "", results.image().get(results.image().size() - 1).url(), results.id()));
//                        //binding.playlistRecyclerView.setAdapter(new ActivityMainPlaylistAdapter(playlists));
//
//                        ActivityMainPlaylistAdapter adapter = new ActivityMainPlaylistAdapter(playlists);
//                        binding.playlistRecyclerView.setAdapter(adapter);
//                        adapter.notifyDataSetChanged();
//                    });
//                    ApplicationClass.sharedPreferenceManager.setHomePlaylistRecommended(playlistsSearch);
//                } else {
//                    try {
//                        Toast.makeText(requireContext(), new JSONObject(response).getString("message"), Toast.LENGTH_SHORT).show();
//                        showOfflineData();
//                    } catch (JSONException e) {
//                        Log.e(TAG, "onResponse: ", e);
//                    }
//                }
//            }
//
//            @Override
//            public void onErrorResponse(String tag, String message) {
//                showOfflineData();
//            }
//        });
    }

//    private void showOfflineData() {
//        if (ApplicationClass.sharedPreferenceManager.getHomeSongsRecommended() != null) {
//            SongSearch songSearch = ApplicationClass.sharedPreferenceManager.getHomeSongsRecommended();
//            songSearch.data().results().forEach(results -> {
//                songs.add(new AlbumItem(results.name(), results.language() + " " + results.year(), results.image().get(results.image().size() - 1).url(), results.id()));
//                ActivityMainPopularSongs adapter = new ActivityMainPopularSongs(songs);
//                binding.popularSongsRecyclerView.setAdapter(adapter);
//                adapter.notifyDataSetChanged();
//            });
//        }
//
//
//        if (ApplicationClass.sharedPreferenceManager.getHomeArtistsRecommended() != null) {
//            ArtistsSearch artistsSearch = ApplicationClass.sharedPreferenceManager.getHomeArtistsRecommended();
//            artistsSearch.data().results().forEach(results -> {
//                artists.add(results);
//                ActivityMainArtistsItemAdapter adapter = new ActivityMainArtistsItemAdapter(artists);
//                binding.popularArtistsRecyclerView.setAdapter(adapter);
//                adapter.notifyDataSetChanged();
//            });
//        }
//
//
//        if (ApplicationClass.sharedPreferenceManager.getHomeAlbumsRecommended() != null) {
//            AlbumsSearch albumsSearch = ApplicationClass.sharedPreferenceManager.getHomeAlbumsRecommended();
//            albumsSearch.data().results().forEach(results -> {
//                albums.add(new AlbumItem(results.name(), results.language() + " " + results.year(), results.image().get(results.image().size() - 1).url(), results.id()));
//                ActivityMainAlbumItemAdapter adapter = new ActivityMainAlbumItemAdapter(albums);
//                binding.popularAlbumsRecyclerView.setAdapter(adapter);
//                adapter.notifyDataSetChanged();
//            });
//        }
//
//        if (ApplicationClass.sharedPreferenceManager.getHomePlaylistRecommended() != null) {
//            PlaylistsSearch playlistsSearch = ApplicationClass.sharedPreferenceManager.getHomePlaylistRecommended();
//            playlistsSearch.data().results().forEach(results -> {
//                playlists.add(new AlbumItem(results.name(), "", results.image().get(results.image().size() - 1).url(), results.id()));
//                //binding.playlistRecyclerView.setAdapter(new ActivityMainPlaylistAdapter(playlists));
//
//                ActivityMainPlaylistAdapter adapter = new ActivityMainPlaylistAdapter(playlists);
//                binding.playlistRecyclerView.setAdapter(adapter);
//                adapter.notifyDataSetChanged();
//            });
//        }
//
//        //showData(); //TODO: showData if new data is available
//    }

}