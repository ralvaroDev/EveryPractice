package com.example.everypractice.trash

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.everypractice.databinding.MovieSearchIntermediateDetailItemBinding
import com.example.everypractice.data.domain.TemporarySearchMovieElement


class AdapterViewPageIntermediate(
    private val movieList: List<TemporarySearchMovieElement>,
    //private val onClickButtonToDetails: (TemporarySearchMovieElement, position: Int) -> Unit
) : RecyclerView.Adapter<AdapterViewPageIntermediate.MovieListViewHolder>() {

    class MovieListViewHolder(private val binding: MovieSearchIntermediateDetailItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            element: TemporarySearchMovieElement,
            //onClickButtonToDetails: (TemporarySearchMovieElement, position: Int) -> Unit
        ) {
            //onClickButtonToDetails(element, adapterPosition)
            binding.ivPosterPath.load(element.posterPathUrl)
            binding.tvTitleMovie.text = element.title
            binding.tvRated.text = element.voteAverage.toString()
            val adapter = GenresIntermediateAdapter(element.genreIds)
            binding.rvGenres.adapter = adapter
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MovieListViewHolder {
        return MovieListViewHolder(
            MovieSearchIntermediateDetailItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: MovieListViewHolder, position: Int) {

        holder.bind(
            movieList[position],
            //onClickButtonToDetails
        )
        /*if (position==movieList.size-1){
            viewPager2.post(runnable)
        }*/

    }

    override fun getItemCount(): Int {
        return movieList.size
    }

    /*private val runnable = Runnable {
        movieList.addAll(movieList)
        notifyDataSetChanged()
    }

*/
}