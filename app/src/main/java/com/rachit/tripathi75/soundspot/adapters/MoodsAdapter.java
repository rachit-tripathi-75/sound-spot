package com.rachit.tripathi75.soundspot.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.rachit.tripathi75.soundspot.R;
import com.rachit.tripathi75.soundspot.model.MoodItem;

import java.util.List;

public class MoodsAdapter extends RecyclerView.Adapter<MoodsAdapter.ViewHolder> {

    private Context context;
    private List<MoodItem> items;

    public MoodsAdapter(Context context, List<MoodItem> items) {
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
        return new MoodsAdapter.ViewHolder(view);
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
