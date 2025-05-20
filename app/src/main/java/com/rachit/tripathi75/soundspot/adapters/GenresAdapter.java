package com.rachit.tripathi75.soundspot.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.rachit.tripathi75.soundspot.R;
import com.rachit.tripathi75.soundspot.model.GenreItem;

import java.util.List;

public class GenresAdapter extends RecyclerView.Adapter<GenresAdapter.ViewHolder> {
    private Context context;
    private List<GenreItem> items;

    public GenresAdapter(Context context, List<GenreItem> items) {
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
        return new GenresAdapter.ViewHolder(view);
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