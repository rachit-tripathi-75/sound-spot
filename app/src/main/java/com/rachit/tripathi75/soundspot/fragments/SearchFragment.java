package com.rachit.tripathi75.soundspot.fragments;

import android.content.Context;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.gson.Gson;
import com.rachit.tripathi75.soundspot.R;
import com.rachit.tripathi75.soundspot.adapters.ActivitySearchListItemAdapter;
import com.rachit.tripathi75.soundspot.databinding.FragmentSearchBinding;
import com.rachit.tripathi75.soundspot.model.SearchListItem;
import com.rachit.tripathi75.soundspot.network.ApiManager;
import com.rachit.tripathi75.soundspot.network.utility.RequestNetwork;
import com.rachit.tripathi75.soundspot.records.GlobalSearch;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import me.everything.android.ui.overscroll.OverScrollDecoratorHelper;


public class SearchFragment extends Fragment {

    private FragmentSearchBinding binding;

    private final String TAG = "SearchActivity";
    GlobalSearch globalSearch;

    private Context context;

    public SearchFragment(Context context) {
        // Required empty public constructor
        this.context = context;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        binding = FragmentSearchBinding.inflate(inflater, container, false);


        OverScrollDecoratorHelper.setUpOverScroll(binding.hscrollview);
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(context));

        binding.edittext.requestFocus();

        binding.chipGroup.setOnCheckedStateChangeListener((group, checkedIds) -> {
            Log.i("SearchActivity", "checkedIds: " + checkedIds);
            if (globalSearch != null) {
                if (globalSearch.success()) {
                    refreshData();
                }
            }
        });

        binding.edittext.setOnEditorActionListener((textView, i, keyEvent) -> {
            showData(textView.getText().toString());
            Log.i(TAG, "onCreate: " + textView.getText().toString());
            binding.edittext.clearFocus();
            hideKeyboard(binding.edittext);
            return true;
        });

        // Show/hide clear icon based on text input
        binding.edittext.addTextChangedListener(new TextWatcher() {
            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().isEmpty()) {
                    binding.clearIcon.setVisibility(View.GONE);
                } else {
                    binding.clearIcon.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

        });

        // Clear input when clear icon is clicked
        binding.clearIcon.setOnClickListener(v -> {
            binding.edittext.setText("");
        });

        //showData("");


        return binding.getRoot();
    }

    private void hideKeyboard(View view) {
        InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
        if (inputMethodManager != null) {
            inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    private void showData(String query) {
        showShimmerData();

        final ApiManager apiManager = new ApiManager(requireActivity());
        apiManager.globalSearch(query, new RequestNetwork.RequestListener() {
            @Override
            public void onResponse(String tag, String response, HashMap<String, Object> responseHeaders) {
                globalSearch = new Gson().fromJson(response, GlobalSearch.class);
                if (globalSearch.success()) {
                    refreshData();
                }
                Log.i(TAG, "onResponse: " + response);
            }

            @Override
            public void onErrorResponse(String tag, String message) {
                Log.e(TAG, "onErrorResponse: " + message);
            }
        });
    }

    private void refreshData() {
        final List<SearchListItem> data = new ArrayList<>();
        int checkedChipId = binding.chipGroup.getCheckedChipId();
        if (checkedChipId == R.id.chip_all) {
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

        } else if (checkedChipId == R.id.chip_song) {
            addSongsData(data);
        } else if (checkedChipId == R.id.chip_albums) {
            addAlbumsData(data);
        } else if (checkedChipId == R.id.chip_playlists) {
            addPlaylistsData(data);
        } else if (checkedChipId == R.id.chip_artists) {
            addArtistsData(data);
        } else {
            throw new IllegalStateException("Unexpected value: " + binding.chipGroup.getCheckedChipId());
        }
        if (!data.isEmpty())
            binding.recyclerView.setAdapter(new ActivitySearchListItemAdapter(data));
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

    private void showShimmerData() {
        List<SearchListItem> data = new ArrayList<>();
        for (int i = 0; i < 11; i++) {
            data.add(new SearchListItem(
                    "<shimmer>",
                    "",
                    "",
                    "",
                    SearchListItem.Type.SONG
            ));
        }
        binding.recyclerView.setAdapter(new ActivitySearchListItemAdapter(data));
    }
}