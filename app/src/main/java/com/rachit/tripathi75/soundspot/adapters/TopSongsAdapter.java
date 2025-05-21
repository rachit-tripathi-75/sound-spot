package com.rachit.tripathi75.soundspot.adapters;

import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.recyclerview.widget.RecyclerView;

import com.rachit.tripathi75.soundspot.R;
import com.rachit.tripathi75.soundspot.model.NewReleaseSong;
import com.rachit.tripathi75.soundspot.records.ArtistsSearch;
import com.squareup.picasso.Picasso;

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


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // Inflate your item layout
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_top_song, parent, false);
        return new ViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        NewReleaseSong topSong = songs.get(position);
        holder.tvSongId.setText(topSong.getId());
        holder.tvSongTitle.setText(topSong.getTitle());
        holder.tvArtistName.setText(topSong.getArtist());
        Log.d("topSongsAlbumArtTAG", topSong.getImageUrl());
        Picasso.get().load(Uri.parse(topSong.getImageUrl())).placeholder(R.drawable.baseline_album_24).into(holder.ivSongAlbumArt);

    }

    @Override
    public int getItemCount() {
        return songs.size();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvSongTitle, tvArtistName, tvSongId;
        ImageView ivSongAlbumArt;

        ViewHolder(View itemView) {
            super(itemView);
            ivSongAlbumArt = itemView.findViewById(R.id.ivSongAlbumArt);
            tvArtistName = itemView.findViewById(R.id.tvArtistName);
            tvSongTitle = itemView.findViewById(R.id.tvSongTitle);
            tvSongId = itemView.findViewById(R.id.tvSongId);

        }
    }
}


