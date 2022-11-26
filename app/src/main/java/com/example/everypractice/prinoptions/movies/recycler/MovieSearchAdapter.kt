package com.example.everypractice.prinoptions.movies.recycler

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import coil.transform.BlurTransformation
import com.example.everypractice.databinding.MovieSearchItemBinding
import com.example.everypractice.prinoptions.movies.data.domain.TemporarySearchMovieElement
import com.google.android.material.snackbar.Snackbar

class MovieSearchAdapter(
    private val context: Context,
    private val onLayoutClicked: (TemporarySearchMovieElement, position: Int, listSize: Int) -> Unit
) :
    ListAdapter<TemporarySearchMovieElement, MovieSearchAdapter.MovieSearchViewHolder>(DiffCallBack) {

    class MovieSearchViewHolder(private val binding: MovieSearchItemBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(
            searchMovie: TemporarySearchMovieElement,
            context: Context,
            onLayoutClicked: (TemporarySearchMovieElement, position: Int, listSize: Int) -> Unit,
            position: Int,
            listSize: Int
        ) {
            //binding.titleMovie.text = searchMovie.title
            binding.glass.load(searchMovie.posterPathUrl) {
                crossfade(250)
                    .transformations(
                        BlurTransformation(context, radius = 18f)
                    )
                    .build()
            }
            binding.portada.setOnClickListener {
                onLayoutClicked(searchMovie, position, listSize)
            }

            binding.movie = searchMovie
            binding.executePendingBindings()


            //Glide.with().load(searchMovie.posterUrl).into(binding.imgMoviePath)
        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MovieSearchViewHolder {

        return MovieSearchViewHolder(
            MovieSearchItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: MovieSearchViewHolder, position: Int) {
        val current = getItem(position)
        holder.itemView.setOnClickListener {
            val snack = Snackbar.make(it, "Item selected", Snackbar.LENGTH_SHORT)
            snack.show()
        }

        holder.bind(current, context, onLayoutClicked, position, currentList.size)
    }

    companion object {
        private val DiffCallBack = object : DiffUtil.ItemCallback<TemporarySearchMovieElement>() {
            override fun areItemsTheSame(
                oldItem: TemporarySearchMovieElement,
                newItem: TemporarySearchMovieElement
            ): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(
                oldItem: TemporarySearchMovieElement,
                newItem: TemporarySearchMovieElement
            ): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }

}