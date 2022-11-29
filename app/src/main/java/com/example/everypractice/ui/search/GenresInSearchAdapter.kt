package com.example.everypractice.ui.search

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.everypractice.data.domain.GenresMovies
import com.example.everypractice.databinding.ItemGeneresBinding

class GenresInSearchAdapter(
    private val listGenres: List<GenresMovies>,
    private val onGenreSelectedOnScreen: (id: Int) -> Unit
) : RecyclerView.Adapter<GenresInSearchAdapter.CardGenresInSearchViewHolder>() {

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