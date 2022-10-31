package com.example.everypractice.prinoptions.search.recycler

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.everypractice.databinding.LastsearchListHistoryBinding
import com.example.everypractice.prinoptions.search.data.LastSearch
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.min

interface MovementHelper {
    fun onMove(view: View)
}


//este parametro manda lo que esta en el parentesis
class HistoryListAdapter(
    private val onHistoryClicked: (LastSearch)->Unit,
    private val onClickListener: (LastSearch)-> Unit,
    private val onSwipeListener: (View, LastSearch) -> Unit
    ) :
    ListAdapter<LastSearch,HistoryListAdapter.HistoryViewHolder>(DiffCallBack){

    private val MIN_SWIPE_DISTANCE =250
    private val TAG = "HistoryAdapter"

    class HistoryViewHolder(private val binding: LastsearchListHistoryBinding):RecyclerView.ViewHolder(binding.root) {

        fun bind(
            lastSearch:LastSearch,
            onClickListener: (LastSearch)-> Unit,
            onHistoryClicked: (LastSearch)->Unit,
            onSwipeListener: (View, LastSearch) -> Unit
        ) {

            binding.apply {
                historyElement.text = lastSearch.lastSearch
            }
            //ESTO NOS DEVUELVE LA FUNCION DE CLICKEO CADA QUE SE CLIQUEA
            binding.deleteButton.setOnClickListener { onClickListener(lastSearch) }
            binding.historyElement.setOnClickListener {
                //onClickListener(lastSearch)
                onHistoryClicked(lastSearch)
            }
            onSwipeListener(binding.historyCard,lastSearch)
        }

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        return HistoryViewHolder(
            LastsearchListHistoryBinding.inflate(
                LayoutInflater.from(parent.context), parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val current = getItem(position)
        /*
        holder.itemView.setOnClickListener {
            //ESTO DEVUELVE EL ELEMENTO EN POSICION MEDIANTE EL PADRE
            //TODOS DENTRO DE ESE ELEMENTO DE RECYCLER TIENEN UN UNICO IDENTIFICADOR
            //YA LUEGO RELACIONAMOS QUE SOLO FUNCIONE EL CLIC DE ELIMINAR CUANDO LE DAMOS A LA X
            //  onHistoryClicked(current)
        }       */

        /*holder.itemView.setOnClickListener {
            onSwipeListener(it)
        }*/

        /*holder.itemView.setOnTouchListener(
            View.OnTouchListener { v, event ->

                // variables to store current configuration of quote card.
                var displayMetrics = displayMetrics
                val cardWidth = holder.itemView.width
                val cardStart = (displayMetrics.widthPixels.toFloat() / 2) - (cardWidth / 2)

                when (event.action) {
                    MotionEvent.ACTION_UP -> {
                        var currentX = holder.itemView.x
                        holder.itemView.animate()
                            .x(cardStart)
                            .setDuration(150)
                            .setListener(object : AnimatorListenerAdapter() {
                                override fun onAnimationEnd(animation: Animator) {
                                    viewLifecycleOwner.lifecycleScope.launch(Dispatchers.Default) {
                                        delay(100)
                                        // check if the swipe distance was more than
                                        // minimum swipe required to load a new quote
                                        if (currentX < MIN_SWIPE_DISTANCE) {
                                            // Load a new quote if swiped adequately
                                            Log.d(TAG, "onAnimationEnd: SWIPEEEE")
                                            //CREO QUE EXPLOTA PORQUE MANDA MUCHOS INTENTS A LA VEZ
                                            //YA QUE AL USAR UN DISPATCHER ESTE ES CONSTANTE
                                            //ESTO SERIA PARA ACTUALIZAR UN VALOR, COMO UN LIVE DATA Y OBSERVER

                                            //goGoogle()
                                            currentX = 0f
                                        }
                                    }
                                }
                            })
                            .start()
                        Log.d(TAG, "swipeAbility: SECOND SWIPE")
                        // AQUI SI YA NO OCURRE EL BUG, SOLO MANDA UNA VEZ
                        TODO("PUT THE ACTION HERE")

                    }

                    MotionEvent.ACTION_MOVE -> {
                        // get the new co-ordinate of X-axis
                        val newX = event.rawX

                        // carry out swipe only if newX < cardStart, that is,
                        // the card is swiped to the left side, not to the right
                        // Detailed explanation at: https://genicsblog.com/swipe-animation-on-a-cardview-android
                        if (newX - cardWidth < cardStart) {
                            Log.d("Values", "$cardStart --- $newX ---- ${displayMetrics.widthPixels.toFloat()}  ---- ${newX - (cardWidth / 2)}")
                            holder.itemView.animate()
                                .x(
                                    min(cardStart, newX - (cardWidth / 2))
                                )
                                .setDuration(0)
                                .start()
                        }
                    }
                }

                // required to by-pass lint warning
                v.performClick()
                return@OnTouchListener true
            }
        )*/

        holder.bind(current,
            onClickListener,
            onHistoryClicked,
            onSwipeListener
        )
    }

    companion object{
        private val DiffCallBack = object : DiffUtil.ItemCallback<LastSearch>(){
            override fun areItemsTheSame(oldItem: LastSearch, newItem: LastSearch): Boolean {
                return oldItem === newItem
            }

            override fun areContentsTheSame(oldItem: LastSearch, newItem: LastSearch): Boolean {
                return oldItem.lastSearch == newItem.lastSearch
            }
        }
    }


}

