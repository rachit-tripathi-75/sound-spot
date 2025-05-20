package com.rachit.tripathi75.soundspot.adapters;

import android.content.Context;
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

import com.rachit.tripathi75.soundspot.R;
import com.rachit.tripathi75.soundspot.activities.SearchMusicActivity;
import com.rachit.tripathi75.soundspot.model.Album;
import com.rachit.tripathi75.soundspot.model.NewReleaseAlbum;

import java.util.List;

public class AlbumsAdapter extends RecyclerView.Adapter<AlbumsAdapter.ViewHolder> {
    private List<NewReleaseAlbum> albums;
    private Context context;

    public AlbumsAdapter(List<NewReleaseAlbum> albums) {
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
        NewReleaseAlbum album = albums.get(position);

        // Set album data
        holder.titleTextView.setText(album.getTitle());
        holder.artistTextView.setText(album.getArtist());
        holder.albumImageView.setImageResource(album.getImageResId());

        // Set background color
        holder.albumCardView.setCardBackgroundColor(ContextCompat.getColor(context, album.getColorResId()));

        // Apply animation
        holder.itemView.startAnimation(AnimationUtils.loadAnimation(context, R.anim.scale_in));
    }

    @Override
    public int getItemCount() {
        return albums.size();
    }

    public void updateAlbums(List<NewReleaseAlbum> newAlbums) {
        this.albums = newAlbums;
        notifyDataSetChanged();
    }

    static class ViewHolder extends RecyclerView.ViewHolder {
        CardView albumCardView;
        ImageView albumImageView;
        TextView titleTextView;
        TextView artistTextView;

        ViewHolder(View itemView) {
            super(itemView);
            albumCardView = itemView.findViewById(R.id.album_card_view);
            albumImageView = itemView.findViewById(R.id.album_image_view);
            titleTextView = itemView.findViewById(R.id.album_title_text_view);
            artistTextView = itemView.findViewById(R.id.album_artist_text_view);
        }
    }
}