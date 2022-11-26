package com.example.everypractice.prinoptions.movies.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.everypractice.databinding.ItemPopularMovieBinding
import com.example.everypractice.prinoptions.movies.data.domain.TemporaryPopularMovieElement

class PopularMoviesAdapter(
    private val onPopularElementClicked: (id: Int) -> Unit
) :
    ListAdapter<TemporaryPopularMovieElement, PopularMoviesAdapter.PopularMoviesViewHolder>(
        DiffCallBack) {


    class PopularMoviesViewHolder(private val binding: ItemPopularMovieBinding) :
        RecyclerView.ViewHolder(binding.root) {

            fun bind(
                popularMovieElement: TemporaryPopularMovieElement,
                onPopularElementClicked: (id: Int) -> Unit
            ) {
                binding.portada.load(popularMovieElement.posterPathUrl)
                binding.titleMovie.text = popularMovieElement.title
                binding.tvRated.text = popularMovieElement.voteAverage.toString()
                binding.elementPopularMovie.setOnClickListener {
                    onPopularElementClicked(popularMovieElement.id)
                }
            }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PopularMoviesViewHolder {
        return PopularMoviesViewHolder(
            ItemPopularMovieBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: PopularMoviesViewHolder, position: Int) {
        val current = getItem(position)
        holder.bind(current, onPopularElementClicked)
    }

    companion object {
        private val DiffCallBack = object : DiffUtil.ItemCallback<TemporaryPopularMovieElement>() {
            override fun areItemsTheSame(
                oldItem: TemporaryPopularMovieElement,
                newItem: TemporaryPopularMovieElement,
            ): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(
                oldItem: TemporaryPopularMovieElement,
                newItem: TemporaryPopularMovieElement,
            ): Boolean {
                return oldItem.id == newItem.id
            }

        }
    }

}