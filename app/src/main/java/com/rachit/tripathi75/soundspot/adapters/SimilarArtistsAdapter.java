package com.rachit.tripathi75.soundspot.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.rachit.tripathi75.soundspot.R;
import com.rachit.tripathi75.soundspot.fragments.DiscoverFragment;
import com.rachit.tripathi75.soundspot.model.SimilarArtistItem;

import java.util.List;

public class SimilarArtistsAdapter extends RecyclerView.Adapter<SimilarArtistsAdapter.ViewHolder> {
    private Context context;
    private List<SimilarArtistItem> items;

    public SimilarArtistsAdapter(Context context, List<SimilarArtistItem> items) {
        this.context = context;
        this.items = items;
    }

    // Rest of adapter implementation...

    static class ViewHolder extends RecyclerView.ViewHolder {
        // ViewHolder implementation...
        ViewHolder(View itemView) {
            super(itemView);
        }
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.item_album, parent, false);
        return new SimilarArtistsAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {
        // Implementation...
    }

    @Override
    public int getItemCount() {
        return items.size();
    }
}