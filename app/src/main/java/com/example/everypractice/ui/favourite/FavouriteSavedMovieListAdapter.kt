package com.example.everypractice.ui.favourite

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.everypractice.data.domain.PermanentFavouriteMovies
import com.example.everypractice.databinding.FavouriteMovieItemBinding
import com.example.everypractice.helpers.extensions.alternateGoneVisible
import timber.log.Timber

class FavouriteSavedMovieListAdapter(
    private val onBtnSeenClicked: (id: Int) -> Unit
) :
    ListAdapter<PermanentFavouriteMovies, FavouriteSavedMovieListAdapter.FavouriteMovieListViewHolder>(
        DiffCallBack
    ) {


    class FavouriteMovieListViewHolder(private val binding: FavouriteMovieItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            favouriteMovie: PermanentFavouriteMovies,
            onBtnSeenClicked: (id: Int) -> Unit
        ) {
            binding.movieName.text = favouriteMovie.movieTitle
            binding.tvRating.text = favouriteMovie.vote_Average.toString()
            binding.ivPosterPathFavourite.load(favouriteMovie.posterPath)
            binding.permanentMovie=favouriteMovie
            binding.executePendingBindings()
            binding.materialCardView9.setOnClickListener {
                binding.btnToggleOptionsa.alternateGoneVisible()
            }
            binding.btnMoveToSeen.setOnClickListener {
                onBtnSeenClicked(favouriteMovie.id)
            }
        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): FavouriteMovieListViewHolder {

        return FavouriteMovieListViewHolder(
            FavouriteMovieItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }


    override fun onBindViewHolder(holder: FavouriteMovieListViewHolder, position: Int) {
        val current = getItem(position)
        Timber.d("Size of the list in adapter $itemCount")
        holder.bind(current, onBtnSeenClicked)
    }

    companion object {
        private val DiffCallBack = object : DiffUtil.ItemCallback<PermanentFavouriteMovies>() {
            override fun areItemsTheSame(
                oldItem: PermanentFavouriteMovies,
                newItem: PermanentFavouriteMovies
            ): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(
                oldItem: PermanentFavouriteMovies,
                newItem: PermanentFavouriteMovies
            ): Boolean {
                return oldItem == newItem
            }

        }
    }


}