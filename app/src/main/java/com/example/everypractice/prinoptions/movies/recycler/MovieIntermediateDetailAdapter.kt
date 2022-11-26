package com.example.everypractice.prinoptions.movies.recycler


import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import coil.load
import com.example.everypractice.databinding.MovieSearchIntermediateDetailItemBinding
import com.example.everypractice.prinoptions.movies.data.models.MovieSearchElementGson
import com.google.android.material.snackbar.Snackbar
import timber.log.Timber


class MovieIntermediateDetailAdapter(
    private val onClickButtonToDetails: (MovieSearchElementGson)->Unit
) :
    ListAdapter<MovieSearchElementGson, MovieIntermediateDetailAdapter.MovieIntermediateDetailViewHolder>(
        DiffCallBack
    ) {

    class MovieIntermediateDetailViewHolder(private val binding: MovieSearchIntermediateDetailItemBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(
            detailMovie: MovieSearchElementGson
            ,onClickButtonToDetails: (MovieSearchElementGson)->Unit
        ) {
            binding.ivPosterPath.load(detailMovie.posterUrl)
            binding.tvTitleMovie.text = detailMovie.title
            binding.tvMovieDuration.text = detailMovie.releaseDate
            //onClickButtonToDetails(detailMovie.id!!)

            binding.ivPosterPath.setOnClickListener {
                onClickButtonToDetails(detailMovie)
            }

        }

    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): MovieIntermediateDetailViewHolder {

        return MovieIntermediateDetailViewHolder(
            MovieSearchIntermediateDetailItemBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: MovieIntermediateDetailViewHolder, position: Int) {

        /*
        RECUENTO: AQUI DEBEMOS MODIFICAR LA POSITION PARA QUE APAREZCA EL ITEM SELECCIONADO Y NO
        EL PRIMERO, REVISAR ENL STACK Y LUEGO TAMBIEN YA ESTA LISTO LA RECEPCION DE LOS DETALLES
        DE LA PELI SELECCIONADA, SOLO FALTARIA RECEPCIONARLA EN EL FRAGMENT DE TODOS LOS DETALLES
        */

        //Obtenemos el size
        val listSize = currentList.size
        //obtenemos el resto para siempre tener laposicion dentro del limite
        val positionObject = position % listSize

        val current = getItem(positionObject)


        Timber.d("listSize: $listSize and position: $position y position Objetct: $positionObject")
        holder.itemView.setOnClickListener {
            val snack = Snackbar.make(it, "Item selected", Snackbar.LENGTH_SHORT)
            snack.show()
        }
        holder.bind(
            current,
            onClickButtonToDetails
        )
    }

    //este aumenta el limite del recycler para mostrar, o los espacios para mostrar, por defecto se autodevuelve
     override fun getItemCount(): Int {
         return Integer.MAX_VALUE
     }




    companion object {
        private val DiffCallBack = object : DiffUtil.ItemCallback<MovieSearchElementGson>() {
            override fun areItemsTheSame(
                oldItem: MovieSearchElementGson,
                newItem: MovieSearchElementGson
            ): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(
                oldItem: MovieSearchElementGson,
                newItem: MovieSearchElementGson
            ): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}
