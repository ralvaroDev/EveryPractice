package com.example.everypractice.ui.favourite

import android.view.*
import androidx.recyclerview.widget.*
import androidx.transition.*
import coil.*
import coil.transform.*
import com.example.everypractice.data.models.*
import com.example.everypractice.databinding.*
import com.example.everypractice.helpers.extensions.*

class FavouriteSeenMovieListAdapter(
    private val onBtnRestoredClicked: (id: Int) -> Unit,
    private val onSeeDetailsClicked: (id: Int) -> Unit
) :
    ListAdapter<PermanentFavouriteMovies, FavouriteSeenMovieListAdapter.FavouriteMovieListViewHolder>(
        DiffCallBack()
    ) {
    class FavouriteMovieListViewHolder(private val binding: FavouriteMovieItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            favouriteMovie: PermanentFavouriteMovies,
            onBtnRestoredClicked: (id: Int) -> Unit,
            onSeeDetailsClicked: (id: Int) -> Unit
        ) {
            binding.movieName.text = favouriteMovie.movieTitle
            binding.btnMoveToSeen.text = "RESTORE"
            binding.tvRating.text = favouriteMovie.vote_Average.toString()
            binding.ivPosterPathFavourite.load(favouriteMovie.posterPath) {
                transformations(
                    GrayscaleTransformation(),
                )
                build()
            }
            binding.materialCardView9.setOnClickListener {
                TransitionManager.beginDelayedTransition(binding.optionBarBtns, AutoTransition())
                binding.optionBarBtns.alternateGoneVisible()
            }
            binding.btnMoveToSeen.setOnClickListener {
                onBtnRestoredClicked(favouriteMovie.id)
                //binding.optionBarBtns.alternateGoneVisible()
            }
            binding.btnSeeDetails.setOnClickListener {
                onSeeDetailsClicked(favouriteMovie.id)
                //binding.optionBarBtns.alternateGoneVisible()
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
        holder.bind(current, onBtnRestoredClicked, onSeeDetailsClicked)
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

class DiffCallBack : DiffUtil.ItemCallback<PermanentFavouriteMovies>() {
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