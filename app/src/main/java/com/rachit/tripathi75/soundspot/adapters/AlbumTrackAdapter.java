package com.rachit.tripathi75.soundspot.adapters;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.rachit.tripathi75.soundspot.ApplicationClass;
import com.rachit.tripathi75.soundspot.R;
import com.rachit.tripathi75.soundspot.activities.MusicOverviewActivity;
import com.rachit.tripathi75.soundspot.model.AlbumTrack;
import com.rachit.tripathi75.soundspot.records.SongResponse;
import com.squareup.picasso.Picasso;

import java.util.List;

public class AlbumTrackAdapter extends RecyclerView.Adapter<AlbumTrackAdapter.ViewHolder> {

    private final Context context;
    private final List<SongResponse.Song> tracks;
    private String currentTheme;


    public AlbumTrackAdapter(Context context, List<SongResponse.Song> tracks, String currentTheme) {
        this.context = context;
        this.tracks = tracks;
        this.currentTheme = currentTheme;
        Log.d("albumsTrackSizeTAG", String.valueOf(tracks.size()));
    }

    @NonNull
    @Override
    public AlbumTrackAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View _v = View.inflate(parent.getContext(), viewType == 0 ? R.layout.item_album_track : R.layout.activity_list_shimmer, null);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        _v.setLayoutParams(layoutParams);
        return new AlbumTrackAdapter.ViewHolder(_v);
    }

    @Override
    public void onBindViewHolder(@NonNull AlbumTrackAdapter.ViewHolder holder, int position) {


        SongResponse.Song track = tracks.get(position);

//        holder.tvSongId.setText(String.valueOf(track.id()));
//        holder.tvSongTitle.setText(track.name());
//        holder.tvArtistName.setText(track.);

        // Apply alternating background colors based on theme
        if (position % 2 == 0) {
            if ("dark".equals(currentTheme)) {
                holder.trackCard.setCardBackgroundColor(
                        ContextCompat.getColor(context, R.color.dark_surface));
            } else {
                holder.trackCard.setCardBackgroundColor(
                        ContextCompat.getColor(context, R.color.purple_surface));
            }
        } else {
            if ("dark".equals(currentTheme)) {
                holder.trackCard.setCardBackgroundColor(
                        ContextCompat.getColor(context, R.color.dark_background));
            } else {
                holder.trackCard.setCardBackgroundColor(
                        ContextCompat.getColor(context, R.color.purple_background));
            }
        }

        // Apply text colors based on theme
        if ("dark".equals(currentTheme)) {
            holder.tvSongTitle.setTextColor(
                    ContextCompat.getColor(context, R.color.white));
            holder.tvArtistName.setTextColor(
                    ContextCompat.getColor(context, R.color.dark_text_secondary));
            holder.tvSongId.setTextColor(
                    ContextCompat.getColor(context, R.color.dark_text_secondary));
        } else {
            holder.tvSongTitle.setTextColor(
                    ContextCompat.getColor(context, R.color.purple_text_primary));
            holder.tvArtistName.setTextColor(
                    ContextCompat.getColor(context, R.color.purple_text_secondary));
            holder.tvSongId.setTextColor(
                    ContextCompat.getColor(context, R.color.purple_text_secondary));
        }

        // Add animation to each item
        holder.itemView.startAnimation(
                AnimationUtils.loadAnimation(context, R.anim.item_animation_from_bottom));

        // Set click listener for the track item
        holder.itemView.setOnClickListener(v -> {
            // Handle track click
        });

        // Set click listener for more options button
        holder.ibMore.setOnClickListener(v -> {
            // Handle more options click
        });







//        if (getItemViewType(position) == 1) {
//            ((ShimmerFrameLayout) holder.itemView.findViewById(R.id.shimmer)).startShimmer();
//            return;
//        }

        SongResponse.Song song = tracks.get(position);

        holder.itemView.findViewById(R.id.tvSongTitle).setSelected(true);
        holder.itemView.findViewById(R.id.tvArtistName).setSelected(true);

        ((TextView) holder.itemView.findViewById(R.id.tvSongTitle)).setText(song.name());
        StringBuilder artistsNames = new StringBuilder();
        for (int i = 0; i < song.artists().all().size(); i++) {
            if (artistsNames.toString().contains(song.artists().all().get(i).name())) continue;
            artistsNames.append(song.artists().all().get(i).name());
            artistsNames.append(", ");
        }
        ((TextView) holder.itemView.findViewById(R.id.tvArtistName)).setText(artistsNames.toString());

//        Picasso.get().load(Uri.parse(song.image().get(song.image().size() - 1).url())).into(((ImageView) holder.itemView.findViewById(R.id.ivSongAlbumArt)));

        holder.itemView.setOnClickListener(view -> {
            if(ApplicationClass.trackQueue != null)
                if(ApplicationClass.trackQueue.contains(song.id()))
                    ApplicationClass.track_position = holder.getBindingAdapterPosition();
            holder.itemView.getContext().startActivity(new Intent(view.getContext(), MusicOverviewActivity.class).putExtra("id", song.id()));
        });
    }

    @Override
    public int getItemCount() {
        return tracks.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (tracks.get(position).id().equals("<shimmer>")) return 1;
        else return 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {

        CardView trackCard;
        TextView tvSongTitle;
        TextView tvSongId;
        TextView tvArtistName;
        ImageButton ibMore;


        public ViewHolder(@NonNull View itemView) {
            super(itemView);

            trackCard = itemView.findViewById(R.id.track_card);
            tvSongId = itemView.findViewById(R.id.tvSongId);
            tvSongTitle = itemView.findViewById(R.id.tvSongTitle);
            tvArtistName = itemView.findViewById(R.id.tvArtistName);
            ibMore = itemView.findViewById(R.id.ibMore);
        }
    }

    public void updateTheme(String newTheme) {
        this.currentTheme = newTheme;
        notifyDataSetChanged();
    }







}