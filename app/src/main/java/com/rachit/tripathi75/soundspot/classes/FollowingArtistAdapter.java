package com.rachit.tripathi75.soundspot.classes;


import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.imageview.ShapeableImageView;
import com.rachit.tripathi75.soundspot.R;
import com.rachit.tripathi75.soundspot.records.ArtistsSearch;
import com.squareup.picasso.Picasso;

import java.util.List;

public class FollowingArtistAdapter extends RecyclerView.Adapter<FollowingArtistAdapter.ArtistViewHolder> {

    private Context context;
    private List<ArtistsSearch.Data.Results> artists;
    private int lastPosition = -1;

    public FollowingArtistAdapter(Context context, List<ArtistsSearch.Data.Results> artists) {
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
        ArtistsSearch.Data.Results artist = artists.get(position);
        holder.tvArtistName.setText(artist.name());
//        holder.artistImage.setImageResource(artist.getImageResourceId());
        Picasso.get().load(Uri.parse(artist.image().get(2).url())).into(holder.ivArtistProfilePic);
        holder.tvArtistId.setText(artist.id());
//        holder.tvArtistId.setVisibility(View.GONE);


        // Apply animation to individual items
        setAnimation(holder.itemView, position);
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

            // Add click animation
            itemView.setOnClickListener(v -> {
                v.animate()
                        .scaleX(0.9f)
                        .scaleY(0.9f)
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
