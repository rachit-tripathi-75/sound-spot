package com.rachit.tripathi75.soundspot.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.OvershootInterpolator;
import android.widget.FrameLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.rachit.tripathi75.soundspot.R;
import com.rachit.tripathi75.soundspot.classes.ChooseArtist;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

// ArtistAdapter.java
public class ChooseArtistAdapter extends RecyclerView.Adapter<ChooseArtistAdapter.ArtistViewHolder> {
    private List<ChooseArtist> artists;
    private OnArtistClickListener listener;

    public interface OnArtistClickListener {
        void onArtistClick(ChooseArtist artist, int position);
    }

    public ChooseArtistAdapter(List<ChooseArtist> artists, OnArtistClickListener listener) {
        this.artists = artists;
        this.listener = listener;
    }


    @NonNull
    @Override
    public ArtistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_artist_selection, parent, false);
        return new ArtistViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ArtistViewHolder holder, int position) {
        ChooseArtist artist = artists.get(position);
        holder.bind(artist);
    }

    @Override
    public int getItemCount() {
        return artists.size();
    }

    public void updateData(List<ChooseArtist> newArtists) {
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new ArtistDiffCallback(artists, newArtists));
        this.artists = newArtists;
        diffResult.dispatchUpdatesTo(this);
    }

    class ArtistViewHolder extends RecyclerView.ViewHolder {
        private CircleImageView artistImageView;
        private TextView artistNameTextView;
        private FrameLayout selectedOverlay;

        public ArtistViewHolder(@NonNull View itemView) {
            super(itemView);
            artistImageView = itemView.findViewById(R.id.artistImageView);
            artistNameTextView = itemView.findViewById(R.id.artistNameTextView);
            selectedOverlay = itemView.findViewById(R.id.selectedOverlay);

            itemView.setOnClickListener(v -> {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    listener.onArtistClick(artists.get(position), position);
                }
            });
        }

        public void bind(ChooseArtist artist) {
            artistNameTextView.setText(artist.getName());

            // Load image with Glide
            Glide.with(itemView.getContext())
                    .load(artist.getImageUrl())
                    .placeholder(R.drawable.placeholder_artist)
                    .into(artistImageView);

            // Show/hide selection overlay
            selectedOverlay.setVisibility(artist.isSelected() ? View.VISIBLE : View.GONE);

            // Animate when binding
            itemView.setAlpha(0f);
            itemView.setScaleX(0.8f);
            itemView.setScaleY(0.8f);
            itemView.animate()
                    .alpha(1f)
                    .scaleX(1f)
                    .scaleY(1f)
                    .setDuration(300)
                    .setInterpolator(new OvershootInterpolator())
                    .start();
        }
    }

    static class ArtistDiffCallback extends DiffUtil.Callback {
        private final List<ChooseArtist> oldList;
        private final List<ChooseArtist> newList;

        public ArtistDiffCallback(List<ChooseArtist> oldList, List<ChooseArtist> newList) {
            this.oldList = oldList;
            this.newList = newList;
        }

        @Override
        public int getOldListSize() {
            return oldList.size();
        }

        @Override
        public int getNewListSize() {
            return newList.size();
        }

        @Override
        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
            return oldList.get(oldItemPosition).getId().equals(
                    newList.get(newItemPosition).getId());
        }

        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            ChooseArtist oldArtist = oldList.get(oldItemPosition);
            ChooseArtist newArtist = newList.get(newItemPosition);
            return oldArtist.isSelected() == newArtist.isSelected() &&
                    oldArtist.getName().equals(newArtist.getName());
        }
    }
}