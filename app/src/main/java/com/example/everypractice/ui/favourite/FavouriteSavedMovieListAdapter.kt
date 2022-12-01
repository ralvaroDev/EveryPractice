package com.example.everypractice.ui.favourite

import android.view.*
import androidx.recyclerview.widget.*
import androidx.transition.*
import coil.*
import com.example.everypractice.data.models.*
import com.example.everypractice.databinding.*
import com.example.everypractice.helpers.extensions.*
import timber.log.*

class FavouriteSavedMovieListAdapter(
    private val onBtnSeenClicked: (id: Int) -> Unit,
    private val onSeeDetailsClicked: (id: Int) -> Unit
) :
    ListAdapter<PermanentFavouriteMovies, FavouriteSavedMovieListAdapter.FavouriteMovieListViewHolder>(
        DiffCallBack
    ) {


    class FavouriteMovieListViewHolder(private val binding: FavouriteMovieItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            favouriteMovie: PermanentFavouriteMovies,
            onBtnSeenClicked: (id: Int) -> Unit,
            onSeeDetailsClicked: (id: Int) -> Unit
        ) {
            binding.movieName.text = favouriteMovie.movieTitle
            binding.tvRating.text = favouriteMovie.vote_Average.toString()
            binding.ivPosterPathFavourite.load(favouriteMovie.posterPath)
            binding.permanentMovie=favouriteMovie
            binding.executePendingBindings()
            binding.materialCardView9.setOnClickListener {
                TransitionManager.beginDelayedTransition(binding.optionBarBtns, AutoTransition())
                binding.optionBarBtns.alternateGoneVisible()
            }
            binding.btnMoveToSeen.setOnClickListener {
                onBtnSeenClicked(favouriteMovie.id)
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
        holder.bind(current, onBtnSeenClicked, onSeeDetailsClicked)
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