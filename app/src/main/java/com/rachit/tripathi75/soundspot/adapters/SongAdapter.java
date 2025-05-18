package com.rachit.tripathi75.soundspot.adapters;

// SongAdapter.java

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.rachit.tripathi75.soundspot.R;
import com.rachit.tripathi75.soundspot.model.Track;

import java.util.List;

public class SongAdapter extends RecyclerView.Adapter<SongAdapter.SongViewHolder> {

    private List<Track> tracks;
    private OnSongClickListener listener;
    private int currentlyPlayingPosition = -1;

    public interface OnSongClickListener {
        void onSongClick(Track track);
    }

    public SongAdapter(List<Track> tracks, OnSongClickListener listener) {
        this.tracks = tracks;
        this.listener = listener;
    }

    @NonNull
    @Override
    public SongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(com.skydoves.powermenu.R.layout.item_power_menu_library_skydoves, parent, false);
        return new SongViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull SongViewHolder holder, int position) {
        Track track = tracks.get(position);
        holder.bind(track, position == currentlyPlayingPosition);
    }

    @Override
    public int getItemCount() {
        return tracks.size();
    }

    public void updateData(List<Track> newTracks) {
        this.tracks = newTracks;
        notifyDataSetChanged();
    }

    public void setCurrentlyPlayingPosition(int position) {
        int oldPosition = currentlyPlayingPosition;
        currentlyPlayingPosition = position;

        if (oldPosition != -1) {
            notifyItemChanged(oldPosition);
        }

        if (position != -1) {
            notifyItemChanged(position);
        }
    }

    class SongViewHolder extends RecyclerView.ViewHolder {
        private ImageView albumCover;
        private TextView songTitle;
        private TextView songPlays;
        private TextView songDuration;
        private ImageButton likeButton;
        private ImageButton moreButton;
        private View playingIndicator;

        public SongViewHolder(@NonNull View itemView) {
            super(itemView);
//            albumCover = itemView.findViewById(R.id.albumCover);
//            songTitle = itemView.findViewById(R.id.songTitle);
//            songPlays = itemView.findViewById(R.id.songPlays);
//            songDuration = itemView.findViewById(R.id.songDuration);
//            likeButton = itemView.findViewById(R.id.likeButton);
//            moreButton = itemView.findViewById(R.id.moreButton);
//            playingIndicator = itemView.findViewById(R.id.playingIndicator);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    listener.onSongClick(tracks.get(position));
                    setCurrentlyPlayingPosition(position);
                }
            });

            likeButton.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    Track track = tracks.get(position);
                    track.setLiked(!track.isLiked());
                    notifyItemChanged(position);
                }
            });
        }

        public void bind(Track track, boolean isPlaying) {
            songTitle.setText(track.getTitle());
            songPlays.setText(track.getFormattedPlayCount() + " plays");
            songDuration.setText(track.getFormattedDuration());

            Glide.with(itemView.getContext())
                    .load(track.getCoverUrl())
                    .placeholder(R.drawable.baseline_album_24)
                    .into(albumCover);

            likeButton.setImageResource(track.isLiked() ?
                    R.drawable.ic_heart : R.drawable.ic_heart);

            playingIndicator.setVisibility(isPlaying ? View.VISIBLE : View.GONE);

            if (isPlaying) {
                itemView.setBackgroundResource(R.drawable.favorite_24px);
            } else {
                itemView.setBackgroundResource(R.drawable.favorite_24px);
            }
        }
    }
}