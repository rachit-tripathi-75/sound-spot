package com.rachit.tripathi75.soundspot.classes;


import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.imageview.ShapeableImageView;
import com.google.gson.Gson;
import com.rachit.tripathi75.soundspot.R;
import com.rachit.tripathi75.soundspot.activities.ArtistProfileActivity;
import com.rachit.tripathi75.soundspot.model.BasicDataRecord;
import com.rachit.tripathi75.soundspot.model.FollowedArtist;
import com.rachit.tripathi75.soundspot.records.ArtistsSearch;
import com.squareup.picasso.Picasso;

import java.util.List;

public class FollowingArtistAdapter extends RecyclerView.Adapter<FollowingArtistAdapter.ArtistViewHolder> {

    private Context context;
    private List<FollowedArtist> artists;
    private int lastPosition = -1;

    public FollowingArtistAdapter(Context context, List<FollowedArtist> artists) {
        this.context = context;
        this.artists = artists;
    }

    @NonNull
    @Override
    public ArtistViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_chosen_artist, parent, false);
        return new ArtistViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ArtistViewHolder holder, int position) {
        FollowedArtist artist = artists.get(position);
        holder.tvArtistName.setText(artist.getName());
        holder.tvArtistId.setText(artist.getId());

        // Load image using Picasso
        Picasso.get().load(Uri.parse(artist.getPhotoUri())).into(holder.ivArtistProfilePic);

        // Animate
        setAnimation(holder.itemView, position);

        // Handle click directly in adapter
        holder.itemView.setOnClickListener(v -> {
            Log.d("artistIdTAG", "artist ID: " + artist.getId());
            Intent intent = new Intent();
            intent.setClass(holder.itemView.getContext(), ArtistProfileActivity.class);
            intent.putExtra("data", new Gson().toJson(new BasicDataRecord(artist.getId(), artist.getName(), "", artist.getPhotoUri())));
            context.startActivity(intent);
        });
    }

    private void setAnimation(View viewToAnimate, int position) {
        if (position > lastPosition) {
            Animation animation = AnimationUtils.loadAnimation(context, R.anim.fade_in);
            animation.setStartOffset(position * 100);
            viewToAnimate.startAnimation(animation);
            lastPosition = position;
        }
    }

    @Override
    public int getItemCount() {
        return artists.size();
    }

    public static class ArtistViewHolder extends RecyclerView.ViewHolder {
        ShapeableImageView ivArtistProfilePic;
        TextView tvArtistName, tvArtistId;

        public ArtistViewHolder(@NonNull View itemView) {
            super(itemView);
            ivArtistProfilePic = itemView.findViewById(R.id.ivArtistProfilePic);
            tvArtistName = itemView.findViewById(R.id.tvArtistName);
            tvArtistId = itemView.findViewById(R.id.tvArtistId);

            // Optional scale animation on click
            itemView.setOnClickListener(v -> {
                v.animate()
                        .scaleX(0.95f)
                        .scaleY(0.95f)
                        .setDuration(100)
                        .withEndAction(() -> {
                            v.animate()
                                    .scaleX(1f)
                                    .scaleY(1f)
                                    .setDuration(100)
                                    .start();
                        })
                        .start();
            });
        }
    }
}

