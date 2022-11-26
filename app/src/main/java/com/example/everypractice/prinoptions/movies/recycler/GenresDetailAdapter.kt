package com.example.everypractice.prinoptions.movies.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.everypractice.databinding.ItemGenresFavouriteBinding
import com.example.everypractice.general.util.getTransformationIdsToGenres

class GenresDetailAdapter(private val genreList: List<Int>) :
    RecyclerView.Adapter<GenresDetailAdapter.GenresDetailViewHolder>() {

    class GenresDetailViewHolder(private val binding: ItemGenresFavouriteBinding) :
        RecyclerView.ViewHolder(binding.root) {

            fun bind(
                genre: String
            ){
                binding.cardGenreFavourite.text = genre
            }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GenresDetailViewHolder {
        return GenresDetailViewHolder(
            ItemGenresFavouriteBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: GenresDetailViewHolder, position: Int) {
        holder.bind(genreList.getTransformationIdsToGenres()[position])
    }

    override fun getItemCount(): Int {
        return genreList.size
    }
}