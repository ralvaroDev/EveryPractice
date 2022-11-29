package com.example.everypractice.ui.favourite

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.GrayscaleTransformation
import com.example.everypractice.data.domain.PermanentFavouriteMovies
import com.example.everypractice.databinding.FavouriteMovieItemBinding
import com.example.everypractice.helpers.extensions.alternateGoneVisible

class FavouriteSeenMovieListAdapter(
    private val onBtnRestoredClicked: (id: Int) -> Unit
) :
    ListAdapter<PermanentFavouriteMovies, FavouriteSeenMovieListAdapter.FavouriteMovieListViewHolder>(
        DiffCallBack()
    ) {
    class FavouriteMovieListViewHolder(private val binding: FavouriteMovieItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            favouriteMovie: PermanentFavouriteMovies,
            onBtnRestoredClicked: (id: Int) -> Unit
        ) {
            binding.movieName.text = favouriteMovie.movieTitle
            binding.tvRating.text = favouriteMovie.vote_Average.toString()
            binding.ivPosterPathFavourite.load(favouriteMovie.posterPath){
                transformations(
                    GrayscaleTransformation(),
                )
                build()
            }
            binding.materialCardView9.setOnClickListener {
                binding.btnToggleOptionsa.alternateGoneVisible()
            }
            binding.btnMoveToSeen.setOnClickListener {
                onBtnRestoredClicked(favouriteMovie.id)
            }
        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): FavouriteMovieListViewHolder {
        return FavouriteMovieListViewHolder(
            FavouriteMovieItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: FavouriteMovieListViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current,onBtnRestoredClicked)
    }

    /*companion object {
        private val DiffCallBack = object : DiffUtil.ItemCallback<PermanentFavouriteMovies>() {
            override fun areItemsTheSame(
                oldItem: PermanentFavouriteMovies,
                newItem: PermanentFavouriteMovies
            ): Boolean {
                return true
            }

            override fun areContentsTheSame(
                oldItem: PermanentFavouriteMovies,
                newItem: PermanentFavouriteMovies
            ): Boolean {
                return oldItem.id == newItem.id
            }

        }
    }*/

}

class DiffCallBack : DiffUtil.ItemCallback<PermanentFavouriteMovies>(){
    override fun areItemsTheSame(
        oldItem: PermanentFavouriteMovies,
        newItem: PermanentFavouriteMovies,
    ): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(
        oldItem: PermanentFavouriteMovies,
        newItem: PermanentFavouriteMovies,
    ): Boolean {
        return oldItem == newItem
    }

}