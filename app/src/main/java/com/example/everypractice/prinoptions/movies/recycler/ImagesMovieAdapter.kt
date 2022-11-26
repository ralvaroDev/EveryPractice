package com.example.everypractice.prinoptions.movies.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.everypractice.databinding.ItemImageFromMoviesDetailBinding
import com.example.everypractice.prinoptions.movies.data.domain.AllImages
import com.example.everypractice.prinoptions.movies.data.domain.PermanentFavouriteMovies

/*class ImagesMovieAdapter (
    private val listImages: List<AllImages>,
    private val onItemImageSelected: (id:Int) -> Unit
) : RecyclerView.Adapter<ImagesMovieAdapter.ImagesMovieViewHolder>() {

    class ImagesMovieViewHolder(private val binding: ItemImageFromMoviesDetailBinding) :
        RecyclerView.ViewHolder(binding.root){

        fun bind(
            image: AllImages,
            onItemImageSelected: (id:Int) -> Unit
        ){
            binding.image.load(image.filePosterPathUrl)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImagesMovieViewHolder {
        return ImagesMovieViewHolder(
            ItemImageFromMoviesDetailBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: ImagesMovieViewHolder, position: Int) {
        holder.bind(listImages[position],onItemImageSelected)
    }

    override fun getItemCount(): Int {
        return listImages.size
    }
}*/

class ImageMovieAdapter (
    private val onItemImageSelected: (id:Int) -> Unit
        ) : ListAdapter<AllImages, ImageMovieAdapter.ImageMovieViewHolder>(DiffCallBack) {

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