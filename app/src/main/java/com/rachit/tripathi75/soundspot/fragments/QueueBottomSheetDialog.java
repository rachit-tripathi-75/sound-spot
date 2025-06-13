package com.rachit.tripathi75.soundspot.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.gson.Gson;
import com.rachit.tripathi75.soundspot.ApplicationClass;
import com.rachit.tripathi75.soundspot.R;
import com.rachit.tripathi75.soundspot.activities.MainActivity;
import com.rachit.tripathi75.soundspot.adapters.ActivityMainPopularSongs;
import com.rachit.tripathi75.soundspot.adapters.QueueAdapter;
import com.rachit.tripathi75.soundspot.classes.QueueItemAnimator;
import com.rachit.tripathi75.soundspot.databinding.QueueBottomSheetLayoutBinding;
import com.rachit.tripathi75.soundspot.interfaces.QueueItemTouchHelperCallback;
import com.rachit.tripathi75.soundspot.model.AlbumItem;
import com.rachit.tripathi75.soundspot.model.QueueSong;
import com.rachit.tripathi75.soundspot.network.ApiManager;
import com.rachit.tripathi75.soundspot.network.utility.RequestNetwork;
import com.rachit.tripathi75.soundspot.records.SongResponse;
import com.rachit.tripathi75.soundspot.records.SongSearch;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class QueueBottomSheetDialog extends BottomSheetDialogFragment {

    private QueueBottomSheetLayoutBinding binding;
    private QueueAdapter queueAdapter;
    private ArrayList<QueueSong> songsList;
    private ItemTouchHelper itemTouchHelper;
    private List<String> queue;


    public static QueueBottomSheetDialog newInstance() {
        return new QueueBottomSheetDialog();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        binding = QueueBottomSheetLayoutBinding.inflate(inflater, container, false);

        initialisers();
        setupRecyclerView();
        loadSampleData();
        setupListeners();


        return binding.getRoot();
    }

    private void initialisers() {
        binding.progressBar.setVisibility(View.VISIBLE);
        songsList = new ArrayList<>();
        queue = ApplicationClass.trackQueue;
        for (int i = 0; i < queue.size(); i++) {
            Log.d("queueTAG", queue.get(i));
            retrieveSongsById(queue.get(i));
        }
        binding.progressBar.setVisibility(View.GONE);
        binding.recyclerQueue.setVisibility(View.VISIBLE);

    }

    private void retrieveSongsById(String songId) {
        final ApiManager apiManager = new ApiManager(requireActivity());
        songsList.clear();

        apiManager.retrieveSongsByIds(songId, new RequestNetwork.RequestListener() {
            @Override
            public void onResponse(String tag, String response, HashMap<String, Object> responseHeaders) {
                SongResponse songSearch = new Gson().fromJson(response, SongResponse.class);
                Log.i("retrieveQueueSongsTAG", "onResponse: " + response);
                if (songSearch.success()) {
                    songSearch.data().forEach(results -> {
                        songsList.add(new QueueSong(results.name(), results.artists().primary().get(0).name(), results.duration(), results.image().get(2).url()));
                    });
                    queueAdapter.notifyDataSetChanged();
//                    ApplicationClass.sharedPreferenceManager.setHomeSongsRecommended(songSearch);
                } else {

                    Log.d("retrieveSongsByIdTAG", "fell here");
//                    try {
////                        showOfflineData();
////                        Toast.makeText(MainActivity.this, new JSONObject(response).getString("message"), Toast.LENGTH_SHORT).show();
//                    } catch (JSONException e) {
////                        Log.e(TAG, "onResponse: ", e);
//                    }
                }
            }

            @Override
            public void onErrorResponse(String tag, String message) {
//                showOfflineData();
            }
        });
    }


    private void setupRecyclerView() {

        queueAdapter = new QueueAdapter(songsList, new QueueAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                // Handle item click logic here
                // Example: Play the song at 'position'
                System.out.println("Item clicked at position: " + position);
            }

            @Override
            public void onMoreClick(int position, View view) {
                // Show options menu for the item at 'position'
                // 'view' can be used as an anchor for a PopupMenu
                System.out.println("More options clicked for item at position: " + position);
            }

            @Override
            public void onStartDrag(RecyclerView.ViewHolder viewHolder) {
                // Initiate the drag operation for the given ViewHolder
                if (itemTouchHelper != null) {
                    itemTouchHelper.startDrag(viewHolder);
                }
            }
        });

// Configure the RecyclerView (binding.recyclerQueue)
// This block is equivalent to Kotlin's 'apply' scope function.
        binding.recyclerQueue.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recyclerQueue.setAdapter(queueAdapter);
        binding.recyclerQueue.setItemAnimator(new QueueItemAnimator()); // Assuming QueueItemAnimator exists

// Setup ItemTouchHelper for drag and drop functionality
// Create an instance of your custom ItemTouchHelper.Callback
        QueueItemTouchHelperCallback callback = new QueueItemTouchHelperCallback(queueAdapter);

// Create an ItemTouchHelper instance with the custom callback
        itemTouchHelper = new ItemTouchHelper(callback);

// Attach the ItemTouchHelper to the RecyclerView to enable drag and drop
        itemTouchHelper.attachToRecyclerView(binding.recyclerQueue);
    }

    private void loadSampleData() {
//        songsList.add(new QueueSong("Gata Only", "FloyyMenor & Cris Mj", "3:42", R.drawable.baseline_album_24));
//        songsList.add(new QueueSong("J Balvin, Willy William - Mi Gente", "jbalvinVEVO", "3:04", R.drawable.baseline_album_24, "3.4B views"));
//        songsList.add(new QueueSong("AMISTA", "Blessd & Ovy On The Drums", "2:58", R.drawable.baseline_album_24));
//        songsList.add(new QueueSong("PELIGROSA", "FloyyMenor", "2:15", R.drawable.baseline_album_24));
//        songsList.add(new QueueSong("Mi Facha", "Cris Mj", "3:04", R.drawable.baseline_album_24));
//        songsList.add(new QueueSong("Bad Boy Vs Gata Only (Mashup)", "Joa Sosa", "3:30", R.drawable.baseline_album_24, "2.4M views"));
//        songsList.add(new QueueSong("Con Calma (feat. Snow)", "Daddy Yankee", "3:14", R.drawable.baseline_album_24));
//        songsList.add(new QueueSong("EMPELOTICA", "Lenny Tavárez & Feid", "3:11", R.drawable.baseline_album_24));
//
//        queueAdapter.notifyDataSetChanged();

    }

    private void setupListeners() {


    }


}
