package com.example.everypractice.prinoptions.search.recycler

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.util.DisplayMetrics
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.everypractice.databinding.LastsearchListHistoryBinding
import com.example.everypractice.prinoptions.search.data.LastSearch
import com.example.everypractice.prinoptions.search.viewmodel.SearchViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import kotlin.math.min

interface MovementHelper {
    fun onMove(view: View)
}

data class TimeHelper(
    val number: Int,
    val numberIndicator: String
)

//este parametro manda lo que esta en el parentesis
class HistoryListAdapter(
    private val onHistoryClicked: (LastSearch)->Unit,
    private val onClickListener: (LastSearch)-> Unit,
    private val onSwipeListener: (View, LastSearch) -> Unit,
    private val FlowNumber: (TextView, LastSearch, Boolean)->Unit,
    private val searchViewModel: SearchViewModel
    ) :
    ListAdapter<LastSearch,HistoryListAdapter.HistoryViewHolder>(DiffCallBack){

    private val MIN_SWIPE_DISTANCE =250
    private val TAG = "HistoryAdapter"

    class HistoryViewHolder(private val binding: LastsearchListHistoryBinding):RecyclerView.ViewHolder(binding.root) {



        fun bind(
            lastSearch: LastSearch,
            onClickListener: (LastSearch)-> Unit,
            onHistoryClicked: (LastSearch)->Unit,
            onSwipeListener: (View, LastSearch) -> Unit,
            FlowNumber: (TextView, LastSearch, Boolean)-> Unit,
            searchViewModel: SearchViewModel
        ) {

            val timeHelper = getTimeFromTimestamp(lastSearch.timestamp)
            binding.apply {
                historyElement.text = lastSearch.lastSearch
                //numberTime.text = timeHelper.number.toString()
                //textTime.text = timeHelper.numberIndicator
            }
            //ESTO NOS DEVUELVE LA FUNCION DE CLICKEO CADA QUE SE CLIQUEA
            binding.deleteButton.setOnClickListener {
                onClickListener(lastSearch)
                FlowNumber(binding.example, lastSearch, false)
            }
            binding.textContainer.setOnClickListener {
                //onClickListener(lastSearch)
                onHistoryClicked(lastSearch)
            }
            FlowNumber(binding.example,lastSearch, true)

            onSwipeListener(binding.historyCard,lastSearch)

            binding.lastSearch = lastSearch
            binding.viewModel = searchViewModel
            binding.executePendingBindings()
        }


        private fun getTimeFromTimestamp(unformattedTimestamp: Long) : TimeHelper {
            val seconds = unformattedTimestamp / 1000
            val minutes = seconds.toInt() / 60
            val hours = minutes / 60
            val days = hours / 24
            val weeks = days / 7
            val months = weeks / 4
            val years = months / 12
            val dumpList = listOf(
                TimeHelper(years, "Years"),
                TimeHelper(months, "Months"),
                TimeHelper(weeks, "Weeks"),
                TimeHelper(days, "Days"),
                TimeHelper(hours, "Hours"),
                TimeHelper(minutes, "Min"),
                TimeHelper(seconds.toInt(), "Sec")
            )
            Log.d("aaa", "list of times: $dumpList")
            try {
                return run loop@{
                    dumpList.forEach { element ->
                        if (element.number != 0) {
                            return@loop element
                        }
                    }
                } as TimeHelper
            } catch (e: Exception) {
                Log.d("aaa", "error cast to TimeHelper: ${e.message}")
                return TimeHelper(1, "Sec")
            }
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
            onSwipeListener,
            FlowNumber,
            searchViewModel
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

