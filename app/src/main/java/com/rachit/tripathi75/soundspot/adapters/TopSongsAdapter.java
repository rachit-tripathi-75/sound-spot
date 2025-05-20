package com.rachit.tripathi75.soundspot.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.rachit.tripathi75.soundspot.R;
import com.rachit.tripathi75.soundspot.model.NewReleaseSong;

import java.util.List;

public class TopSongsAdapter extends RecyclerView.Adapter<TopSongsAdapter.ViewHolder> {
    private List<NewReleaseSong> songs;

    public TopSongsAdapter(List<NewReleaseSong> songs) {
        this.songs = songs;
    }

    public void updateSongs(List<NewReleaseSong> newSongs) {
        this.songs = newSongs;
        notifyDataSetChanged();
    }

    // Rest of adapter implementation...

    static class ViewHolder extends RecyclerView.ViewHolder {
        // ViewHolder implementation...
        ViewHolder(View itemView) {
            super(itemView);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflate your item layout
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_top_song, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // Implementation...
    }

    @Override
    public int getItemCount() {
        return songs.size();
    }
}