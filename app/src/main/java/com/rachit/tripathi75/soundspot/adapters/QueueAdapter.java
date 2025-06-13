package com.rachit.tripathi75.soundspot.adapters;

import android.net.Uri;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;


import com.rachit.tripathi75.soundspot.R;
import com.rachit.tripathi75.soundspot.databinding.ItemQueueSongBinding;
import com.rachit.tripathi75.soundspot.interfaces.ItemTouchHelperAdapter;
import com.rachit.tripathi75.soundspot.interfaces.ItemTouchHelperViewHolder;
import com.rachit.tripathi75.soundspot.model.QueueSong;
import com.squareup.picasso.Picasso;

import java.util.Collections;
import java.util.List;

public class QueueAdapter extends RecyclerView.Adapter<QueueAdapter.SongViewHolder> implements ItemTouchHelperAdapter {

    private final List<QueueSong> songs;
    private final OnItemClickListener listener;

    public interface OnItemClickListener {
        void onItemClick(int position);
        void onMoreClick(int position, View view);
        void onStartDrag(RecyclerView.ViewHolder viewHolder);
    }

    public QueueAdapter(List<QueueSong> songs, OnItemClickListener listener) {
        this.songs = songs;
        this.listener = listener;
    }

    @NonNull
    @Override
    public SongViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        ItemQueueSongBinding binding = ItemQueueSongBinding.inflate(
                LayoutInflater.from(parent.getContext()),
                parent,
                false
        );
        return new SongViewHolder(binding);
    }

    @Override
    public void onBindViewHolder(@NonNull SongViewHolder holder, int position) {
        QueueSong song = songs.get(position);
        holder.bind(song);

        // Apply animation
        int animationResId = R.anim.item_animation_from_bottom; // Make sure you have this animation resource
        if (animationResId != 0) {
            android.view.animation.Animation animation = AnimationUtils.loadAnimation(holder.itemView.getContext(), animationResId);
            holder.itemView.startAnimation(animation);
        }
    }

    @Override
    public int getItemCount() {
        return songs.size();
    }

    @Override
    public boolean onItemMove(int fromPosition, int toPosition) {
        if (fromPosition < toPosition) {
            for (int i = fromPosition; i < toPosition; i++) {
                Collections.swap(songs, i, i + 1);
            }
        } else {
            for (int i = fromPosition; i > toPosition; i--) {
                Collections.swap(songs, i, i - 1);
            }
        }
        notifyItemMoved(fromPosition, toPosition);
        return true;
    }

    @Override
    public void onItemDismiss(int position) {
        songs.remove(position);
        notifyItemRemoved(position);
    }

    public class SongViewHolder extends RecyclerView.ViewHolder implements ItemTouchHelperViewHolder {
        private final ItemQueueSongBinding binding;

        public SongViewHolder(@NonNull ItemQueueSongBinding binding) {
            super(binding.getRoot());
            this.binding = binding;

            binding.getRoot().setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    listener.onItemClick(position);
                }
            });

            binding.btnMore.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    listener.onMoreClick(position, v);
                }
            });

            // Setup touch listener for drag-and-drop
            binding.getRoot().setOnTouchListener((v, event) -> {
                if (event.getActionMasked() == MotionEvent.ACTION_DOWN) {
                    listener.onStartDrag(SongViewHolder.this);
                }
                return false;
            });
        }

        public void bind(QueueSong song) {
            binding.txtTitle.setText(song.getTitle());
            if (song.getViews() == null || song.getViews().isEmpty()) {
                binding.txtArtist.setText(song.getArtist() + " • " + song.getDuration());
            } else {
                binding.txtArtist.setText(song.getArtist() + " • " + song.getViews());
            }
            Picasso.get().load(Uri.parse(song.getAlbumArtUrl())).placeholder(R.drawable.baseline_album_24).into(binding.imgAlbum);
//            binding.imgAlbum.setImageResource(song.getAlbumArtUrl());
        }

        @Override
        public void onItemSelected() {
            itemView.setAlpha(0.8f);
            itemView.animate().scaleX(1.05f).scaleY(1.05f).setDuration(150).start();
        }

        @Override
        public void onItemClear() {
            itemView.setAlpha(1.0f);
            itemView.animate().scaleX(1.0f).scaleY(1.0f).setDuration(150).start();
        }
    }
}