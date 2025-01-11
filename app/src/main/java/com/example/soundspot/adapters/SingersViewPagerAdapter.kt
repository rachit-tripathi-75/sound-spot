package com.example.soundspot.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.example.soundspot.R

class SingersViewPagerAdapter(private val images: List<Int>) : RecyclerView.Adapter<SingersViewPagerAdapter.ImageViewHolder>(){
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SingersViewPagerAdapter.ImageViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.singer_view_pager_item, parent, false)
        return ImageViewHolder(view)
    }

    override fun onBindViewHolder(holder: SingersViewPagerAdapter.ImageViewHolder, position: Int) {
        holder.imageView.setImageResource(images[position])
    }

    override fun getItemCount(): Int = images.size

    inner class ImageViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imageView: ImageView = itemView.findViewById(R.id.imageView)
    }

}