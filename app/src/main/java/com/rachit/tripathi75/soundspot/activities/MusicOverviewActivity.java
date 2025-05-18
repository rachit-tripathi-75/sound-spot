package com.rachit.tripathi75.soundspot.activities;

import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.res.ColorStateList;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.PopupMenu;
import androidx.media3.common.Player;

import com.google.android.material.bottomsheet.BottomSheetBehavior;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.android.material.snackbar.Snackbar;
import com.google.gson.Gson;
import com.rachit.tripathi75.soundspot.ApplicationClass;
import com.rachit.tripathi75.soundspot.R;
import com.rachit.tripathi75.soundspot.classes.ApiClient;
import com.rachit.tripathi75.soundspot.classes.ApiServices;
import com.rachit.tripathi75.soundspot.classes.GetLyricsResponse;
import com.rachit.tripathi75.soundspot.databinding.ActivityMusicOverviewBinding;
import com.rachit.tripathi75.soundspot.databinding.LyricsBottomSheetBinding;
import com.rachit.tripathi75.soundspot.databinding.MusicOverviewMoreInfoBottomSheetBinding;
import com.rachit.tripathi75.soundspot.model.AlbumItem;
import com.rachit.tripathi75.soundspot.model.BasicDataRecord;
import com.rachit.tripathi75.soundspot.network.ApiManager;
import com.rachit.tripathi75.soundspot.network.utility.RequestNetwork;
import com.rachit.tripathi75.soundspot.records.SongResponse;
import com.rachit.tripathi75.soundspot.records.sharedpref.SavedLibraries;
import com.rachit.tripathi75.soundspot.services.ActionPlaying;
import com.rachit.tripathi75.soundspot.services.MusicService;
import com.rachit.tripathi75.soundspot.utils.SharedPreferenceManager;
import com.rachit.tripathi75.soundspot.utils.customview.BottomSheetItemView;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MusicOverviewActivity extends AppCompatActivity implements ActionPlaying, ServiceConnection {

    private final String TAG = "MusicOverviewActivity";
    //private final MediaPlayer mediaPlayer = new MediaPlayer();
    private final Handler handler = new Handler();
    ActivityMusicOverviewBinding binding;
    private String SONG_URL = "";
    private String ID_FROM_EXTRA = "";
    private String IMAGE_URL = "";
    MusicService musicService;
    private List<SongResponse.Artist> artsitsList = new ArrayList<>();

    //    @SuppressLint("ClickableViewAccessibility")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMusicOverviewBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        binding.title.setSelected(true);
        binding.description.setSelected(true);

        if (!(((ApplicationClass) getApplicationContext()).getTrackQueue().size() > 1))
            binding.shuffleIcon.setVisibility(View.INVISIBLE);

        binding.playPauseImage.setOnClickListener(view -> {
            if (ApplicationClass.player.isPlaying()) {
                handler.removeCallbacks(runnable);
                ApplicationClass.player.pause();
                binding.playPauseImage.setImageResource(com.rachit.tripathi75.soundspot.R.drawable.play_arrow_24px);
            } else {
                ApplicationClass.player.play();
                binding.playPauseImage.setImageResource(R.drawable.baseline_pause_24);
                updateSeekbar();
            }
            showNotification(ApplicationClass.player.isPlaying() ? R.drawable.baseline_pause_24 : R.drawable.play_arrow_24px);
        });

        binding.seekbar.setMax(100);

        binding.seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {

            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {

            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                int playPosition = (int) ((ApplicationClass.player.getDuration() / 100) * binding.seekbar.getProgress());
                ApplicationClass.player.seekTo(playPosition);
                binding.elapsedDuration.setText(convertDuration(ApplicationClass.player.getCurrentPosition()));
            }
        });

//        ApplicationClass.player.setOnCompletionListener(mediaPlayer -> {
//            binding.seekbar.setProgress(0);
//            binding.elapsedDuration.setText("00:00");
//            binding.playPauseImage.setImageResource(R.drawable.play_arrow_24px);
//            handler.removeCallbacks(runnable);
//            mediaPlayer.seekTo(0);
//            mediaPlayer.reset();
//            ((ApplicationClass)getApplication()).nextTrack();
//        });
        //((ApplicationClass)getApplication()).setMusicDetails("","","","");

        final ApplicationClass applicationClass = (ApplicationClass) getApplicationContext();

        binding.nextIcon.setOnClickListener(view -> applicationClass.nextTrack());
        binding.prevIcon.setOnClickListener(view -> applicationClass.prevTrack());

        binding.repeatIcon.setOnClickListener(view -> {
            if (ApplicationClass.player.getRepeatMode() == Player.REPEAT_MODE_OFF)
                ApplicationClass.player.setRepeatMode(Player.REPEAT_MODE_ONE);
            else
                ApplicationClass.player.setRepeatMode(Player.REPEAT_MODE_OFF);

            if (ApplicationClass.player.getRepeatMode() == Player.REPEAT_MODE_OFF)
                binding.repeatIcon.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.textSec)));
            else
                binding.repeatIcon.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.spotify_green)));

            Toast.makeText(MusicOverviewActivity.this, "Repeat Mode Changed.", Toast.LENGTH_SHORT).show();
        });

        binding.shuffleIcon.setOnClickListener(view -> {
            ApplicationClass.player.setShuffleModeEnabled(!ApplicationClass.player.getShuffleModeEnabled());

            if (ApplicationClass.player.getShuffleModeEnabled())
                binding.shuffleIcon.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.spotify_green)));
            else
                binding.shuffleIcon.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.textSec)));

            Toast.makeText(MusicOverviewActivity.this, "Shuffle Mode Changed.", Toast.LENGTH_SHORT).show();
        });

        binding.shareIcon.setOnClickListener(view -> {
            if (SHARE_URL.isBlank()) return;
            Intent sendIntent = new Intent();
            sendIntent.setAction(Intent.ACTION_SEND);
            sendIntent.putExtra(Intent.EXTRA_TEXT, SHARE_URL);
            sendIntent.setType("text/plain");
            startActivity(sendIntent);
        });

        binding.moreIcon.setOnClickListener(view -> {
            final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(MusicOverviewActivity.this, R.style.MyBottomSheetDialogTheme);
            final MusicOverviewMoreInfoBottomSheetBinding _binding = MusicOverviewMoreInfoBottomSheetBinding.inflate(getLayoutInflater());
            _binding.albumTitle.setText(binding.title.getText().toString());
            _binding.albumSubTitle.setText(binding.description.getText().toString());
            Picasso.get().load(Uri.parse(IMAGE_URL)).into(_binding.coverImage);
            final LinearLayout linearLayout = _binding.main;

            _binding.goToAlbum.setOnClickListener(go_to_album -> {
                if (mSongResponse == null) return;
                if (mSongResponse.data().get(0).album() == null) return;
                final SongResponse.Album album = mSongResponse.data().get(0).album();
                startActivity(new Intent(MusicOverviewActivity.this, ListActivity.class)
                        .putExtra("type", "album")
                        .putExtra("id", album.id())
                        .putExtra("data", new Gson().toJson(new AlbumItem(album.name(), "", "", album.id())))
                );
            });

            _binding.addToLibrary.setOnClickListener(v -> {
                int index = -1;
                final SharedPreferenceManager sharedPreferenceManager = SharedPreferenceManager.getInstance(MusicOverviewActivity.this);
                SavedLibraries savedLibraries = sharedPreferenceManager.getSavedLibrariesData();
                if (savedLibraries == null) savedLibraries = new SavedLibraries(new ArrayList<>());
                if (savedLibraries.lists().isEmpty()) {
                    Snackbar.make(_binding.getRoot(), "No Libraries Found", Snackbar.LENGTH_SHORT).show();
                    return;
                }
                final List<String> userCreatedLibraries = new ArrayList<>();
                for (SavedLibraries.Library library : savedLibraries.lists()) {
                    if (library.isCreatedByUser())
                        userCreatedLibraries.add(library.name());
                }

                MaterialAlertDialogBuilder materialAlertDialogBuilder = getMaterialAlertDialogBuilder(userCreatedLibraries, savedLibraries, sharedPreferenceManager);
                materialAlertDialogBuilder.show();

            });

            _binding.download.setOnClickListener(v -> {
                // TODO: develop this
                Toast.makeText(MusicOverviewActivity.this, "Under Development", Toast.LENGTH_SHORT).show();
            });

            for (SongResponse.Artist artist : artsitsList) {
                try {
                    final String imgUrl = artist.image().isEmpty() ? "" : artist.image().get(artist.image().size() - 1).url();
                    BottomSheetItemView bottomSheetItemView = new BottomSheetItemView(MusicOverviewActivity.this, artist.name(), imgUrl, artist.id());
                    bottomSheetItemView.setFocusable(true);
                    bottomSheetItemView.setClickable(true);
                    bottomSheetItemView.setOnClickListener(view1 -> {
                        Log.i(TAG, "BottomSheetItemView: onCLicked!");
                        startActivity(new Intent(MusicOverviewActivity.this, ArtistProfileActivity.class)
                                .putExtra("data", new Gson().toJson(
                                        new BasicDataRecord(artist.id(), artist.name(), "", imgUrl)))
                        );
                    });
                    linearLayout.addView(bottomSheetItemView);
                } catch (Exception e) {
                    Log.e(TAG, "BottomSheetDialog: ", e);
                }
            }
            bottomSheetDialog.setContentView(_binding.getRoot());
            bottomSheetDialog.create();
            bottomSheetDialog.show();
        });

        binding.trackQuality.setOnClickListener(view -> {
            PopupMenu popupMenu = new PopupMenu(MusicOverviewActivity.this, view);
            popupMenu.getMenuInflater().inflate(R.menu.track_quality_menu, popupMenu.getMenu());
            popupMenu.setOnMenuItemClickListener(menuItem -> {
                Toast.makeText(MusicOverviewActivity.this, menuItem.getTitle(), Toast.LENGTH_SHORT).show();
                //Objects.requireNonNull(menuItem.getTitle());
                ApplicationClass.setTrackQuality(menuItem.getTitle().toString());
                onSongFetched(mSongResponse, true);
                prepareMediaPLayer();
                binding.trackQuality.setText(ApplicationClass.TRACK_QUALITY);
                return true;
            });
            popupMenu.show();
        });

        binding.trackQuality.setText(ApplicationClass.TRACK_QUALITY);

        showData();

        updateTrackInfo();

        binding.ivShowLyrics.setOnClickListener(view -> {
            showLyricsBottomSheetDialog();
        });
    }

    private void showLyricsBottomSheetDialog() {

        final BottomSheetDialog bottomSheetDialog = new BottomSheetDialog(MusicOverviewActivity.this);
        final LyricsBottomSheetBinding lyricsBottomSheetBinding = LyricsBottomSheetBinding.inflate(getLayoutInflater());
        lyricsBottomSheetBinding.albumTitle.setText(binding.title.getText().toString());
        lyricsBottomSheetBinding.albumSubTitle.setText(binding.description.getText().toString());
        Picasso.get().load(Uri.parse(IMAGE_URL)).into(lyricsBottomSheetBinding.coverImage);
        getLyrics(lyricsBottomSheetBinding, binding.title.getText().toString(), binding.description.getText().toString());
        bottomSheetDialog.setContentView(lyricsBottomSheetBinding.getRoot());
        bottomSheetDialog.create();
        lyricsBottomSheetBinding.getRoot().post(() -> {
            View bottomSheet = bottomSheetDialog.findViewById(com.google.android.material.R.id.design_bottom_sheet);
            if (bottomSheet != null) {
                BottomSheetBehavior<View> behavior = BottomSheetBehavior.from(bottomSheet);
//                behavior.setPeekHeight(Resources.getSystem().getDisplayMetrics().heightPixels); // full height
//                behavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                behavior.setDraggable(false);
            }
        });
        bottomSheetDialog.show();
    }

    private void getLyrics(LyricsBottomSheetBinding lyricsBottomSheetDialog, String songTitle, String artists) {

        lyricsBottomSheetDialog.progressBar.setVisibility(View.VISIBLE);

        ApiServices.GetLyricsApiService getLyricsApiService = ApiClient.getClient().create(ApiServices.GetLyricsApiService.class);
        Call<GetLyricsResponse> call = getLyricsApiService.getLyrics(
                "application/json",
                songTitle,
                artists
        );

        call.enqueue(new Callback<GetLyricsResponse>() {
            @Override
            public void onResponse(Call<GetLyricsResponse> call, Response<GetLyricsResponse> response) {
                if (response.isSuccessful() && response.body() != null) {
                    GetLyricsResponse s1 = response.body();
                    Log.d("lyricsTAG", s1.getLyrics());
                    lyricsBottomSheetDialog.progressBar.setVisibility(View.GONE);
                    lyricsBottomSheetDialog.scrollView.setVisibility(View.VISIBLE);
                    lyricsBottomSheetDialog.tvLyrics.setMovementMethod(new ScrollingMovementMethod());
                    lyricsBottomSheetDialog.tvLyrics.setText(formatLyrics(s1.getLyrics()));
//                    tvLyrics.setText(formatLyrics(s1.getLyrics()));
                } else {
                    Log.d("lyricsTAG", "response code: " + response.code());
                    lyricsBottomSheetDialog.progressBar.setVisibility(View.GONE);
                    lyricsBottomSheetDialog.scrollView.setVisibility(View.VISIBLE);
                    lyricsBottomSheetDialog.tvLyrics.setText("Lyrics are not available for this song");
                }
            }

            @Override
            public void onFailure(Call<GetLyricsResponse> call, Throwable t) {
                Log.d("lyricsTAG", t.getMessage());
                lyricsBottomSheetDialog.progressBar.setVisibility(View.GONE);
                lyricsBottomSheetDialog.scrollView.setVisibility(View.VISIBLE);
                lyricsBottomSheetDialog.tvLyrics.setText("Oops!!! Something went wrong");
            }
        });
    }

    private String formatLyrics(String rawLyrics) {
        String formattedLyrics = rawLyrics.replaceAll("\\\\r\\\\n|\\\\n|\\\\r", "\n");
        return formattedLyrics;
    }

    @NonNull
    private MaterialAlertDialogBuilder getMaterialAlertDialogBuilder(List<String> userCreatedLibraries, SavedLibraries savedLibraries, SharedPreferenceManager sharedPreferenceManager) {
        MaterialAlertDialogBuilder materialAlertDialogBuilder = new MaterialAlertDialogBuilder(MusicOverviewActivity.this);
        ListAdapter listAdapter = new ArrayAdapter<>(MusicOverviewActivity.this, android.R.layout.simple_list_item_1, userCreatedLibraries);
        final SavedLibraries finalSavedLibraries = savedLibraries;
        materialAlertDialogBuilder.setAdapter(listAdapter, (dialogInterface, i) -> {
            //index = i;
            Log.i(TAG, "pickedLibrary: " + i);

            final SongResponse.Song song = mSongResponse.data().get(0);

            SavedLibraries.Library.Songs songs = new SavedLibraries.Library.Songs(
                    song.id(),
                    song.name(),
                    binding.description.getText().toString(),
                    IMAGE_URL
            );

            finalSavedLibraries.lists().get(i).songs().add(songs);
            sharedPreferenceManager.setSavedLibrariesData(finalSavedLibraries);
            Toast.makeText(MusicOverviewActivity.this, "Added to " + finalSavedLibraries.lists().get(i).name(), Toast.LENGTH_SHORT).show();
        });


        materialAlertDialogBuilder.setTitle("Select Library");
        return materialAlertDialogBuilder;
    }


    @Override
    protected void onResume() {
        super.onResume();
        Intent intent = new Intent(this, MusicService.class);
        bindService(intent, this, BIND_AUTO_CREATE);
    }

    @Override
    protected void onPause() {
        super.onPause();
        unbindService(this);
    }

    @Override
    public void onServiceConnected(ComponentName name, IBinder service) {
        MusicService.MyBinder binder = (MusicService.MyBinder) service;
        musicService = binder.getService();
        musicService.setCallback(MusicOverviewActivity.this);
        Log.e(TAG, "onServiceConnected: ");
    }

    @Override
    public void onServiceDisconnected(ComponentName name) {
        Log.e(TAG, "onServiceDisconnected: ");
        musicService = null;
    }

    private String SHARE_URL = "";

    void showData() {
        if (getIntent().getExtras() == null) return;
        final ApiManager apiManager = new ApiManager(this);
        final String ID = getIntent().getExtras().getString("id", "");
        ID_FROM_EXTRA = ID;
        //((ApplicationClass)getApplicationContext()).setMusicDetails(null,null,null,ID);
        if (ApplicationClass.MUSIC_ID.equals(ID)) {
            updateSeekbar();
            if (ApplicationClass.player.isPlaying())
                binding.playPauseImage.setImageResource(R.drawable.baseline_pause_24);
            else
                binding.playPauseImage.setImageResource(R.drawable.play_arrow_24px);
        }

        if (getIntent().getExtras().getString("type", "").equals("clear")) {
            ApplicationClass applicationClass = (ApplicationClass) getApplicationContext();
            applicationClass.setTrackQueue(new ArrayList<>(Collections.singletonList(ID)));
        }
        if (SharedPreferenceManager.getInstance(MusicOverviewActivity.this).isSongResponseById(ID))
            onSongFetched(SharedPreferenceManager.getInstance(MusicOverviewActivity.this).getSongResponseById(ID));
        else
            apiManager.retrieveSongById(ID, null, new RequestNetwork.RequestListener() {
                @Override
                public void onResponse(String tag, String response, HashMap<String, Object> responseHeaders) {
                    SongResponse songResponse = new Gson().fromJson(response, SongResponse.class);
                    if (songResponse.success()) {
                        onSongFetched(songResponse);
                        SharedPreferenceManager.getInstance(MusicOverviewActivity.this).setSongResponseById(ID, songResponse);
                    } else if (SharedPreferenceManager.getInstance(MusicOverviewActivity.this).isSongResponseById(ID))
                        onSongFetched(SharedPreferenceManager.getInstance(MusicOverviewActivity.this).getSongResponseById(ID));
                    else
                        finish();
                }

                @Override
                public void onErrorResponse(String tag, String message) {
                    if (SharedPreferenceManager.getInstance(MusicOverviewActivity.this).isSongResponseById(ID))
                        onSongFetched(SharedPreferenceManager.getInstance(MusicOverviewActivity.this).getSongResponseById(ID));
                    else
                        Toast.makeText(MusicOverviewActivity.this, message, Toast.LENGTH_SHORT).show();
                }
            });
    }

    private SongResponse mSongResponse;

    private void onSongFetched(SongResponse songResponse) {
        onSongFetched(songResponse, false);
    }

    private void onSongFetched(SongResponse songResponse, boolean forced) {
        mSongResponse = songResponse;
        binding.title.setText(songResponse.data().get(0).name());
//        binding.description.setText(String.format("%s plays | %s | %s", convertPlayCount(songResponse.data().get(0).playCount()), songResponse.data().get(0).year(), songResponse.data().get(0).copyright()));
        binding.description.setText(regexArtistNameExtractor(songResponse.data().get(0).artists().toString()));
        Log.d("artistsTAG", regexArtistNameExtractor(songResponse.data().get(0).artists().toString()));
        List<SongResponse.Image> image = songResponse.data().get(0).image();
        IMAGE_URL = image.get(image.size() - 1).url();
        SHARE_URL = songResponse.data().get(0).url();
        Picasso.get().load(Uri.parse(image.get(image.size() - 1).url())).into(binding.coverImage);
        List<SongResponse.DownloadUrl> downloadUrls = songResponse.data().get(0).downloadUrl();

        artsitsList = songResponse.data().get(0).artists().primary();

        //Log.i(TAG, "onResponse: " + downloadUrls.get(downloadUrls.size() - 1).url());
        SONG_URL = ApplicationClass.getDownloadUrl(downloadUrls);
//                    if (ApplicationClass.MUSIC_ID.equals(ID)) {
//                        updateSeekbar();
//                        if (ApplicationClass.player.isPlaying())
//                            binding.playPauseImage.setImageResource(R.drawable.baseline_pause_24);
//                        else
//                            binding.playPauseImage.setImageResource(R.drawable.play_arrow_24px);
//                    } else
//                        prepareMediaPLayer();

        if ((!ApplicationClass.MUSIC_ID.equals(ID_FROM_EXTRA) || forced)) {
            ApplicationClass applicationClass = (ApplicationClass) getApplicationContext();
            applicationClass.setMusicDetails(IMAGE_URL, binding.title.getText().toString(), binding.description.getText().toString(), ID_FROM_EXTRA);
            applicationClass.setSongUrl(SONG_URL);
            prepareMediaPLayer();
        }

        //prepareMediaPLayer();

//        if(!ApplicationClass.player.isPlaying()){
//            playClicked();
//            binding.playPauseImage.performClick();
//        }

        //binding.main.setBackgroundColor(ApplicationClass.IMAGE_BG_COLOR);
    }

    private String regexArtistNameExtractor(String value) {
        String data = value;

        Pattern pattern = Pattern.compile("name=([^,]+)");
        Matcher matcher = pattern.matcher(data);

        Set<String> artistNames = new LinkedHashSet<>();
        while (matcher.find()) {
            artistNames.add(matcher.group(1).trim());
        }

        String result = String.join(", ", artistNames);
        System.out.println(result);
        return result;// Output: Sachet-Parampara, Parampara Tandon, Kausar Munir
    }

    public void backPress(View view) {
        finish();
    }

    public static String convertPlayCount(int playCount) {
        if (playCount < 1000) return playCount + "";
        if (playCount < 1000000) return playCount / 1000 + "K";
        return playCount / 1000000 + "M";
    }

    public static String convertDuration(long duration) {
        String timeString = "";
        String secondString;

        int hours = (int) (duration / (1000 * 60 * 60));
        int minutes = (int) (duration % (1000 * 60 * 60)) / (1000 * 60);
        int seconds = (int) ((duration % (1000 * 60 * 60)) % (1000 * 60) / 1000);
        if (hours > 0) {
            timeString = hours + ":";
        }
        if (seconds < 10) {
            secondString = "0" + seconds;
        } else {
            secondString = "" + seconds;
        }
        timeString = timeString + minutes + ":" + secondString;
        return timeString;
    }

    void prepareMediaPLayer() {
        ((ApplicationClass) getApplicationContext()).prepareMediaPlayer();
        binding.totalDuration.setText(convertDuration(ApplicationClass.player.getDuration()));
        playClicked();
        binding.playPauseImage.performClick();
    }

    private final Runnable runnable = this::updateSeekbar;

    void updateSeekbar() {
        if (ApplicationClass.player.isPlaying()) {
            binding.seekbar.setProgress((int) (((float) ApplicationClass.player.getCurrentPosition() / ApplicationClass.player.getDuration()) * 100));
            long currentDuration = ApplicationClass.player.getCurrentPosition();
            binding.elapsedDuration.setText(convertDuration(currentDuration));
            handler.postDelayed(runnable, 1000);
        }
    }

    private final Handler mHandler = new Handler();
    private final Runnable mUpdateTimeTask = this::updateTrackInfo;

    private void updateTrackInfo() {
        if (!binding.title.getText().toString().equals(ApplicationClass.MUSIC_TITLE))
            binding.title.setText(ApplicationClass.MUSIC_TITLE);
        if (!binding.title.getText().toString().equals(ApplicationClass.MUSIC_TITLE))
            binding.description.setText(ApplicationClass.MUSIC_DESCRIPTION);
        Picasso.get().load(Uri.parse(ApplicationClass.IMAGE_URL)).into(binding.coverImage);
        binding.seekbar.setProgress((int) (((float) ApplicationClass.player.getCurrentPosition() / ApplicationClass.player.getDuration()) * 100));
        long currentDuration = ApplicationClass.player.getCurrentPosition();
        binding.elapsedDuration.setText(convertDuration(currentDuration));

        if (!binding.totalDuration.getText().toString().equals(convertDuration(ApplicationClass.player.getDuration())))
            binding.totalDuration.setText(convertDuration(ApplicationClass.player.getDuration()));

        if (ApplicationClass.player.isPlaying())
            binding.playPauseImage.setImageResource(R.drawable.baseline_pause_24);
        else
            binding.playPauseImage.setImageResource(R.drawable.play_arrow_24px);

        //((ApplicationClass)getApplicationContext()).showNotification();

        if (ApplicationClass.player.getRepeatMode() == Player.REPEAT_MODE_OFF)
            binding.repeatIcon.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.textSec)));
        else
            binding.repeatIcon.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.spotify_green)));

        if (ApplicationClass.player.getShuffleModeEnabled())
            binding.shuffleIcon.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.spotify_green)));
        else
            binding.shuffleIcon.setImageTintList(ColorStateList.valueOf(getResources().getColor(R.color.textSec)));

        mHandler.postDelayed(mUpdateTimeTask, 1000);
    }


    @Override
    public void nextClicked() {
    }

    @Override
    public void prevClicked() {
    }

    @Override
    public void playClicked() {
        //binding.playPauseImage.performClick();
        if (!ApplicationClass.player.isPlaying()) {
            binding.playPauseImage.setImageResource(R.drawable.play_arrow_24px);
        } else {
            binding.playPauseImage.setImageResource(R.drawable.baseline_pause_24);
        }
    }

    @Override
    public void onProgressChanged(int progress) {

    }

    public void showNotification(int playPauseButton) {
        ApplicationClass applicationClass = (ApplicationClass) getApplicationContext();
        applicationClass.setMusicDetails(IMAGE_URL, binding.title.getText().toString(), binding.description.getText().toString(), getIntent().getExtras().getString("id", ""));
        applicationClass.showNotification();
    }


}