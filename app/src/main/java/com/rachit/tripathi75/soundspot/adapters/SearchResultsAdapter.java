package com.rachit.tripathi75.soundspot.adapters;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rachit.tripathi75.soundspot.R;
import com.rachit.tripathi75.soundspot.activities.SearchMusicActivity;

import java.util.List;

public class SearchResultsAdapter extends RecyclerView.Adapter<SearchResultsAdapter.ViewHolder> {
    private List<SearchMusicActivity.SearchResult> searchResults;
    private Context context;

    public SearchResultsAdapter(List<SearchMusicActivity.SearchResult> searchResults) {
        this.searchResults = searchResults;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.item_search_result, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        SearchMusicActivity.SearchResult result = searchResults.get(position);

        // Set image
        holder.imageView.setImageResource(result.imageResId);

        // Set title
        holder.titleTextView.setText(result.title);

        // Set type and subtitle
        String typeText = getTypeText(result.type);
        if (result.type == SearchMusicActivity.SearchResult.TYPE_ARTIST) {
            holder.subtitleTextView.setText(typeText);
        } else {
            holder.subtitleTextView.setText(typeText + " • " + result.subtitle);
        }

        // Set image shape based on type
        if (result.type == SearchMusicActivity.SearchResult.TYPE_ARTIST) {
            // Make image circular for artists
            holder.imageView.setBackgroundResource(R.drawable.circle_background);
            holder.imageView.setClipToOutline(true);
        } else {
            // Make image square with rounded corners for other types
            holder.imageView.setBackgroundResource(R.drawable.rounded_rectangle_background);
            holder.imageView.setClipToOutline(true);
        }

        // Apply animation
        holder.itemView.startAnimation(AnimationUtils.loadAnimation(context, R.anim.fade_in_slide_up));
    }

    private String getTypeText(int type) {
        switch (type) {
            case SearchMusicActivity.SearchResult.TYPE_SONG:
                return "Song";
            case SearchMusicActivity.SearchResult.TYPE_ALBUM:
                return "Album";
            case SearchMusicActivity.SearchResult.TYPE_ARTIST:
                return "Artist";
            case SearchMusicActivity.SearchResult.TYPE_PLAYLIST:
                return "Playlist";
            default:
                return "";
        }
    }

    @Override
    public int getItemCount() {
        return searchResults.size();
    }

    public void updateSearchResults(List<SearchMusicActivity.SearchResult> newResults) {
        this.searchResults = newResults;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView titleTextView;
        TextView subtitleTextView;

        ViewHolder(View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.search_result_image_view);
            titleTextView = itemView.findViewById(R.id.search_result_title_text_view);
            subtitleTextView = itemView.findViewById(R.id.search_result_subtitle_text_view);
        }
    }
}