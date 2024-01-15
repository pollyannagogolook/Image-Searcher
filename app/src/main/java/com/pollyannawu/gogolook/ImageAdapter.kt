package com.pollyannawu.gogolook

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.paging.PagingData
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.pollyannawu.gogolook.data.dataclass.Hit
import com.pollyannawu.gogolook.data.dataclass.ImageLayoutType
import com.pollyannawu.gogolook.databinding.ImageGridViewholderBinding
import com.pollyannawu.gogolook.databinding.ImageLinearViewholderBinding


class ImageAdapter: PagingDataAdapter<ImageLayoutType, RecyclerView.ViewHolder>(DiffCallback()) {


    companion object{
        const val VIEW_TYPE_LINEAR = 0
        const val VIEW_TYPE_GRID = 1
        const val INVALID_VIEW_TYPE = "invalid view type"
    }


    class LinearViewHolder(private val binding: ImageLinearViewholderBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(hit: Hit) {
            binding.hit = hit
            binding.executePendingBindings()
        }

    }

    class GridViewHolder(private val binding: ImageGridViewholderBinding): RecyclerView.ViewHolder(binding.root){
        fun bind(hit: Hit){
            binding.hit = hit
            binding.executePendingBindings()
        }
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder{
        return when (viewType) {
            VIEW_TYPE_LINEAR -> {
                val binding = ImageLinearViewholderBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                LinearViewHolder(binding)
            }
            VIEW_TYPE_GRID -> {
                val binding = ImageGridViewholderBinding.inflate(
                    LayoutInflater.from(parent.context),
                    parent,
                    false
                )
                GridViewHolder(binding)
            }

            else -> throw IllegalArgumentException(INVALID_VIEW_TYPE)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return when(getItem(position)){
            is ImageLayoutType.LinearImage -> VIEW_TYPE_LINEAR
            is ImageLayoutType.GridImage -> VIEW_TYPE_GRID
            else -> throw IllegalArgumentException(INVALID_VIEW_TYPE)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val imageLayoutType = getItem(position)

        when (imageLayoutType){
            is ImageLayoutType.LinearImage -> (holder as LinearViewHolder).bind(imageLayoutType.data)
            is ImageLayoutType.GridImage -> (holder as GridViewHolder).bind(imageLayoutType.data)
            else -> throw IllegalArgumentException(INVALID_VIEW_TYPE)
        }
    }





    class DiffCallback : DiffUtil.ItemCallback<ImageLayoutType>() {
        override fun areItemsTheSame(oldItem: ImageLayoutType, newItem: ImageLayoutType): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: ImageLayoutType, newItem: ImageLayoutType): Boolean {
            return when (oldItem){
                is ImageLayoutType.LinearImage -> oldItem.data.id == (newItem as ImageLayoutType.LinearImage).data.id
                is ImageLayoutType.GridImage -> oldItem.data.id == (newItem as ImageLayoutType.GridImage).data.id
            }
        }
    }
}