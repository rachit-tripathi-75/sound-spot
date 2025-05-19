package com.rachit.tripathi75.soundspot.adapters;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.rachit.tripathi75.soundspot.R;
import com.rachit.tripathi75.soundspot.activities.MusicOverviewActivity;
import com.rachit.tripathi75.soundspot.databinding.ItemSongBinding;
import com.rachit.tripathi75.soundspot.records.AlbumsSearch;
import com.rachit.tripathi75.soundspot.records.SongResponse;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ActivityArtistProfileTopSinglesAdapter extends RecyclerView.Adapter<ActivityArtistProfileTopSinglesAdapter.ViewHolder> {

    private final List<AlbumsSearch.Data.Results> singles;

    public ActivityArtistProfileTopSinglesAdapter(List<AlbumsSearch.Data.Results> singles) {
        this.singles = singles;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View _v = View.inflate(parent.getContext(), viewType == 1 ? R.layout.item_song : R.layout.artist_profile_view_top_songs_shimmer, null);
        _v.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        return new ViewHolder(_v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (getItemViewType(position) == 0) {
            ((ShimmerFrameLayout) holder.itemView.findViewById(R.id.shimmer)).startShimmer();
            return;
        }

        final ItemSongBinding binding = ItemSongBinding.bind(holder.itemView);

        binding.songNumber.setText(String.valueOf(position + 1));
        binding.songTitle.setText(singles.get(position).name());
        binding.songInfo.setText(
                String.format("%s", singles.get(position).year())
        );
        Picasso.get().load(Uri.parse(singles.get(position).image().get(singles.get(position).image().size() - 1).url())).into(binding.songCover);

        holder.itemView.setOnClickListener(view -> {
            view.getContext().startActivity(new Intent(view.getContext(), MusicOverviewActivity.class).putExtra("id", singles.get(position).id()));
        });
    }

    @Override
    public int getItemCount() {
        return singles.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (singles.get(position).id().equals("<shimmer>")) return 0;
        else return 1;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}
