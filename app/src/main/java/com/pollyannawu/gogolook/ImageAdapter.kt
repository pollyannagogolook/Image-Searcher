package com.pollyannawu.gogolook

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.pollyannawu.gogolook.data.dataclass.Hit
import com.pollyannawu.gogolook.databinding.ImageViewholderBinding

class ImageAdapter : ListAdapter<Hit, ImageAdapter.ImageViewHolder>(DiffCallback) {

    class ImageViewHolder(private val binding: ImageViewholderBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val shimmerLayout = binding.shimmerLayout

        fun bind(hit: Hit) {
            binding.hit = hit
            binding.executePendingBindings()
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        return ImageViewHolder(
            ImageViewholderBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        holder.bind(getItem(position))

    }

    companion object DiffCallback : DiffUtil.ItemCallback<Hit>() {
        override fun areItemsTheSame(oldItem: Hit, newItem: Hit): Boolean {
            return oldItem === newItem
        }

        override fun areContentsTheSame(oldItem: Hit, newItem: Hit): Boolean {
            return oldItem.id == newItem.id
        }
    }

}