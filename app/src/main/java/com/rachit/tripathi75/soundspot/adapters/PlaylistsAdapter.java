package com.rachit.tripathi75.soundspot.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.rachit.tripathi75.soundspot.R;
import com.rachit.tripathi75.soundspot.model.PlaylistItem;

import java.util.List;

public class PlaylistsAdapter extends RecyclerView.Adapter<PlaylistsAdapter.ViewHolder> {
    private Context context;
    private List<PlaylistItem> items;

    public PlaylistsAdapter(Context context, List<PlaylistItem> items) {
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
        return new PlaylistsAdapter.ViewHolder(view);
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