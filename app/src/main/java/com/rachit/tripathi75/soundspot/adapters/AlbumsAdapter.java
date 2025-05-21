package com.rachit.tripathi75.soundspot.adapters;

import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.common.graph.ValueGraph;
import com.rachit.tripathi75.soundspot.R;
import com.rachit.tripathi75.soundspot.activities.SearchMusicActivity;
import com.rachit.tripathi75.soundspot.model.Album;
import com.rachit.tripathi75.soundspot.model.NewReleaseAlbum;
import com.rachit.tripathi75.soundspot.model.NewReleaseSong;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AlbumsAdapter extends RecyclerView.Adapter<AlbumsAdapter.ViewHolder> {
    private List<NewReleaseSong> albums; // this can be NewReleaseAlbum, agar zaroorat padi toh.. based on album display in list....
    private Context context;

    public AlbumsAdapter(List<NewReleaseSong> albums) {
        this.albums = albums;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.item_album, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        NewReleaseSong album = albums.get(position);

        // Set album data
        holder.tvAlbumTitle.setText(album.getTitle());
        holder.tvArtistName.setText(album.getArtist());
        Picasso.get().load(Uri.parse(album.getImageUrl())).placeholder(R.drawable.baseline_album_24).into(holder.ivAlbumArt);

        // Apply animation
        holder.itemView.startAnimation(AnimationUtils.loadAnimation(context, R.anim.scale_in));
    }

    @Override
    public int getItemCount() {
        return albums.size();
    }

    public void updateAlbums(List<NewReleaseSong> newAlbums) {
        this.albums = newAlbums;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        TextView tvAlbumTitle, tvArtistName, tvAlbumId;
        ImageView ivAlbumArt;

        ViewHolder(View itemView) {
            super(itemView);
            tvAlbumTitle = itemView.findViewById(R.id.tvAlbumTitle);
            tvArtistName = itemView.findViewById(R.id.tvArtistName);
            tvAlbumId = itemView.findViewById(R.id.tvAlbumId);
            ivAlbumArt = itemView.findViewById(R.id.ivAlbumArt);
        }
    }
}