package com.example.everypractice.prinoptions.movies.recycler

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.everypractice.databinding.ItemGeneresBinding
import com.example.everypractice.prinoptions.movies.data.domain.GenresMovies

class CardGenresInSearchAdapter(
    private val listGenres: List<GenresMovies>,
    private val onGenreSelectedOnScreen: (id: Int) -> Unit
) : RecyclerView.Adapter<CardGenresInSearchAdapter.CardGenresInSearchViewHolder>() {

    class CardGenresInSearchViewHolder(private val binding: ItemGeneresBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            element: GenresMovies,
            //onGenreSelectedOnScreen: (id: Int) -> Unit
        ) {
            binding.emoji.text = element.emoji
            binding.text.text = element.name
        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): CardGenresInSearchViewHolder {
        return CardGenresInSearchViewHolder(
            ItemGeneresBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        )
    }

    override fun onBindViewHolder(holder: CardGenresInSearchViewHolder, position: Int) {
        holder.bind(
            listGenres[position],
            //onGenreSelectedOnScreen
        )
    }

    override fun getItemCount(): Int {
        return listGenres.size
    }
}