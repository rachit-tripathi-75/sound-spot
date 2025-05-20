package com.rachit.tripathi75.soundspot.adapters;

import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.rachit.tripathi75.soundspot.model.TrendingItem;

import java.util.List;

public class TrendingAdapter extends RecyclerView.Adapter<TrendingAdapter.ViewHolder> {
    private List<TrendingItem> items;

    public TrendingAdapter(List<TrendingItem> items) {
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
        // Implementation...
        return null;
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