package com.pollyannawu.gogolook

import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.constraintlayout.widget.ConstraintSet
import androidx.core.view.marginEnd
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.pollyannawu.gogolook.data.dataclass.Hit
import com.pollyannawu.gogolook.databinding.ImageViewholderBinding

class ImageAdapter(private val isLinear: Boolean) :
    ListAdapter<Hit, ImageAdapter.ImageViewHolder>(DiffCallback) {

    class ImageViewHolder(private val binding: ImageViewholderBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(hit: Hit, isLinear: Boolean) {
            binding.hit = hit
            if (!isLinear) {
                binding.downloadNumber.visibility = View.GONE
                binding.downloadIcon.visibility = View.GONE

                binding.viewNumber.visibility = View.GONE
                binding.viewIcon.visibility = View.GONE

                centerItems(binding.commentNumber, binding.likeNumber)

            }

            binding.executePendingBindings()
        }

        private fun centerItems(commentView: View, likeView: View) {

            val constraintLayout = commentView.parent as ConstraintLayout
            val constraintSet = ConstraintSet()

            constraintSet.clone(constraintLayout)

            constraintSet.connect(
                commentView.id, ConstraintSet.START,
                ConstraintSet.PARENT_ID, ConstraintSet.START
            )
            constraintSet.connect(
                likeView.id, ConstraintSet.END,
                ConstraintSet.PARENT_ID, ConstraintSet.END
            )
            constraintSet.applyTo(constraintLayout)

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
        holder.bind(getItem(position), isLinear)

    }


    companion object DiffCallback : DiffUtil.ItemCallback<Hit>() {
        override fun areItemsTheSame(oldItem: Hit, newItem: Hit): Boolean {
            return oldItem == newItem
        }

        override fun areContentsTheSame(oldItem: Hit, newItem: Hit): Boolean {
            return oldItem.id == newItem.id
        }
    }
}