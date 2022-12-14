package com.example.everypractice.ui.home

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.everypractice.data.models.PermanentFavouriteMovies
import com.example.everypractice.databinding.FavouriteMovieRecommendedInitialItemBinding

class AdapterViewPageInitialRecommended(
    private val movieList: List<PermanentFavouriteMovies>,
    //private val viewPager2: ViewPager2
) :
    RecyclerView.Adapter<AdapterViewPageInitialRecommended.InitialRecommendedViewHolder>() {


    class InitialRecommendedViewHolder(private val binding: FavouriteMovieRecommendedInitialItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            element: PermanentFavouriteMovies
        ) {
            binding.ivPosterPath.load(element.posterPath)
        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): InitialRecommendedViewHolder {
        return InitialRecommendedViewHolder(
            FavouriteMovieRecommendedInitialItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: InitialRecommendedViewHolder, position: Int) {
        holder.bind(movieList[position])
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
    }*/
}