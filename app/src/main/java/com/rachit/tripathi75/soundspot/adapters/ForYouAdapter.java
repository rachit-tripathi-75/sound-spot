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
import com.squareup.picasso.Picasso;

import java.util.List;

public class ForYouAdapter extends RecyclerView.Adapter<ForYouAdapter.ViewHolder> {
    private List<NewReleaseSong> songs;

    public ForYouAdapter(List<NewReleaseSong> songs) {
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
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_for_you, parent, false);
        return new ViewHolder(itemView);  // Return a valid ViewHolder
    }

    @Override
    public void onBindViewHolder(ForYouAdapter.ViewHolder holder, int position) {
        NewReleaseSong topSong = songs.get(position);
        holder.tvSongId.setText(topSong.getId());
        holder.tvSongTitle.setText(topSong.getTitle());
        holder.tvArtistName.setText(topSong.getArtist());
        Log.d("forYouAlbumArtTAG", topSong.getTitle());
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
            ivSongAlbumArt = itemView.findViewById(R.id.ivSongAlbumArtF);
            tvArtistName = itemView.findViewById(R.id.tvArtistName);
            tvSongTitle = itemView.findViewById(R.id.tvSongTitle);
            tvSongId = itemView.findViewById(R.id.tvSongId);

        }
    }


}