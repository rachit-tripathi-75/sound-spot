package com.rachit.tripathi75.soundspot.adapters;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rachit.tripathi75.soundspot.R;
import com.rachit.tripathi75.soundspot.classes.ImportedPlaylist;

import java.util.List;

public class ImportedPlaylistAdapter extends RecyclerView.Adapter<ImportedPlaylistAdapter.PlaylistViewHolder> {

    private final List<ImportedPlaylist> playlists;
    private final OnPlaylistSelectionListener listener;

    public ImportedPlaylistAdapter(List<ImportedPlaylist> playlists, OnPlaylistSelectionListener listener) {
        this.playlists = playlists;
        this.listener = listener;
    }

    @NonNull
    @Override
    public PlaylistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_imported_playlist, parent, false);
        return new PlaylistViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PlaylistViewHolder holder, int position) {
        ImportedPlaylist playlist = playlists.get(position);
        holder.bind(playlist);

        // Apply item animation
        holder.itemView.startAnimation(AnimationUtils.loadAnimation(
                holder.itemView.getContext(), R.anim.fade_in));
    }

    @Override
    public int getItemCount() {
        return playlists.size();
    }

    public interface OnPlaylistSelectionListener {
        void onPlaylistSelected(int playlistId, boolean isSelected);
        boolean isPlaylistSelected(int playlistId);
    }

    class PlaylistViewHolder extends RecyclerView.ViewHolder {
        private final TextView tvPlaylistName;
        private final TextView tvSongCount;
        private final CheckBox checkBox;

        public PlaylistViewHolder(@NonNull View itemView) {
            super(itemView);
            tvPlaylistName = itemView.findViewById(R.id.tvPlaylistName);
            tvSongCount = itemView.findViewById(R.id.tvSongCount);
            checkBox = itemView.findViewById(R.id.checkBox);
        }

        public void bind(ImportedPlaylist playlist) {
            tvPlaylistName.setText(playlist.getName());
            tvSongCount.setText(playlist.getSongCount() + " Songs");

            // Set checkbox state without triggering listener
            checkBox.setOnCheckedChangeListener(null);
            checkBox.setChecked(listener.isPlaylistSelected(playlist.getId()));

            // Set up click listeners
            checkBox.setOnCheckedChangeListener((buttonView, isChecked) ->
                    listener.onPlaylistSelected(playlist.getId(), isChecked));

            itemView.setOnClickListener(v -> {
                boolean newState = !checkBox.isChecked();
                checkBox.setChecked(newState);
                listener.onPlaylistSelected(playlist.getId(), newState);
            });
        }
    }
}