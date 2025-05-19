package com.rachit.tripathi75.soundspot.adapters;

import android.content.Intent;
import android.net.Uri;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.gson.Gson;
import com.rachit.tripathi75.soundspot.R;
import com.rachit.tripathi75.soundspot.activities.ListActivity;
import com.rachit.tripathi75.soundspot.databinding.ActivityArtistProfileViewTopSongsItemBinding;
import com.rachit.tripathi75.soundspot.databinding.ItemAlbumsBinding;
import com.rachit.tripathi75.soundspot.model.AlbumItem;
import com.rachit.tripathi75.soundspot.records.AlbumsSearch;
import com.squareup.picasso.Picasso;

import java.util.List;

public class ActivityArtistProfileTopAlbumsAdapter extends RecyclerView.Adapter<ActivityArtistProfileTopAlbumsAdapter.ViewHolder> {

    private final List<AlbumsSearch.Data.Results> albums;

    public ActivityArtistProfileTopAlbumsAdapter(List<AlbumsSearch.Data.Results> albums) {
        this.albums = albums;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View _v = View.inflate(parent.getContext(), viewType == 1 ? R.layout.item_albums : R.layout.artist_profile_view_top_songs_shimmer, null);

        return new ViewHolder(_v);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        if (getItemViewType(position) == 0) {
            ((ShimmerFrameLayout) holder.itemView.findViewById(R.id.shimmer)).startShimmer();
            return;
        }

        final ItemAlbumsBinding binding = ItemAlbumsBinding.bind(holder.itemView);

//        itemView.position.setText(String.valueOf(position + 1));
        binding.tvAlbumTitle.setText(albums.get(position).name());
        binding.tvAlbumInformation.setText(
                String.format("%s | %s", albums.get(position).year(), albums.get(position).language())
        );
        Picasso.get().load(Uri.parse(albums.get(position).image().get(albums.get(position).image().size() - 1).url())).into(binding.ivAlbumArt);

        holder.itemView.setOnClickListener(view -> {
            AlbumItem albumItem = new AlbumItem(
                    albums.get(position).id(),
                    albums.get(position).name(),
                    albums.get(position).image().get(albums.get(position).image().size() - 1).url(),
                    albums.get(position).id()
            );
            holder.itemView.getContext().startActivity(new Intent(holder.itemView.getContext(), ListActivity.class)
                    .putExtra("data", new Gson().toJson(albumItem))
                    .putExtra("type", "album")
                    .putExtra("id", albums.get(position).id()));
        });
    }

    @Override
    public int getItemCount() {
        return albums.size();
    }

    @Override
    public int getItemViewType(int position) {
        if (albums.get(position).id().equals("<shimmer>")) return 0;
        else return 1;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
        }
    }
}