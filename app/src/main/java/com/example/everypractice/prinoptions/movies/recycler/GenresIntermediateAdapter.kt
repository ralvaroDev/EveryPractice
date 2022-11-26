package com.example.everypractice.prinoptions.movies.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.everypractice.databinding.ElementGenresDetailIntermediateBinding
import com.example.everypractice.general.util.getTransformationIdsToGenres

class GenresIntermediateAdapter(private val genresList: List<Int>) :
    RecyclerView.Adapter<GenresIntermediateAdapter.GenresIntermediateViewHolder>() {

    class GenresIntermediateViewHolder(private val binding: ElementGenresDetailIntermediateBinding) :
        RecyclerView.ViewHolder(binding.root) {

            fun bind(
                genre: String
            ){
                binding.tvGenre.text = genre
            }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): GenresIntermediateViewHolder {
        return GenresIntermediateViewHolder(
            ElementGenresDetailIntermediateBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: GenresIntermediateViewHolder, position: Int) {
        holder.bind(genresList.getTransformationIdsToGenres()[position])
    }

    override fun getItemCount(): Int {
        return genresList.size
    }
}