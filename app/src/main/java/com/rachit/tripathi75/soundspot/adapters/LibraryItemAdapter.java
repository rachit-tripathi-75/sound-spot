package com.rachit.tripathi75.soundspot.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.rachit.tripathi75.soundspot.R;
import com.rachit.tripathi75.soundspot.model.MusicItem;

import java.util.List;

public class LibraryItemAdapter extends RecyclerView.Adapter<LibraryItemAdapter.ViewHolder> {

    private final Context context;
    private List<MusicItem> items;

    public LibraryItemAdapter(Context context, List<MusicItem> items) {
        this.context = context;
        this.items = items;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_library, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        MusicItem item = items.get(position);

        // Set data to views
        holder.titleText.setText(item.getTitle());
        holder.subtitleText.setText(item.getSubtitle());

        // Handle artist images with circular shape
        if (item.getType().equals("Artist")) {
            holder.imageView.setBackgroundResource(R.drawable.circle_background);
            holder.imageView.setClipToOutline(true);
        } else {
            holder.imageView.setBackgroundResource(0);
            holder.imageView.setClipToOutline(false);
        }

        holder.imageView.setImageResource(item.getImageResourceId());

        // Set click listener
        holder.itemView.setOnClickListener(v -> {
            // Handle item click
        });

        // Add pulse animation to first item
        if (position == 0) {
            holder.itemView.startAnimation(AnimationUtils.loadAnimation(context, R.anim.pulse_animation));
        }
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void updateItems(List<MusicItem> newItems) {
        this.items = newItems;
        notifyDataSetChanged();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView titleText;
        TextView subtitleText;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            imageView = itemView.findViewById(R.id.item_image);
            titleText = itemView.findViewById(R.id.item_title);
            subtitleText = itemView.findViewById(R.id.item_subtitle);
        }
    }
}