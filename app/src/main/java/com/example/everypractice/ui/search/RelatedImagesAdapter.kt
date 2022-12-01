package com.example.everypractice.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.everypractice.data.models.AllImages
import com.example.everypractice.databinding.ItemImageFromMoviesDetailBinding

class RelatedImagesAdapter (
    private val onItemImageSelected: (id:Int) -> Unit
        ) : ListAdapter<AllImages, RelatedImagesAdapter.ImageMovieViewHolder>(DiffCallBack) {

    class ImageMovieViewHolder(private val binding: ItemImageFromMoviesDetailBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            image: AllImages
        ) {
            binding.image.load(image.filePosterPathUrl)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageMovieViewHolder {
        return ImageMovieViewHolder(
            ItemImageFromMoviesDetailBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ImageMovieViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current)
    }

    companion object {
        private val DiffCallBack = object : DiffUtil.ItemCallback<AllImages>() {
            override fun areItemsTheSame(
                oldItem: AllImages,
                newItem: AllImages
            ): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(
                oldItem: AllImages,
                newItem: AllImages
            ): Boolean {
                return oldItem.filePosterPathUrl == newItem.filePosterPathUrl
            }

        }
    }

}