package com.example.everypractice.prinoptions.search

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.annotation.SuppressLint
import android.content.Context
import android.content.Context.INPUT_METHOD_SERVICE
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.example.everypractice.databinding.FragmentSearchBinding
import com.example.everypractice.prinoptions.search.data.LastSearch
import com.example.everypractice.prinoptions.search.recycler.HistoryListAdapter
import com.example.everypractice.prinoptions.search.viewmodel.SearchViewModel
import com.example.everypractice.prinoptions.search.viewmodel.SearchViewModelFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlin.math.min

class SearchFragment : Fragment() {

    private val TAG = "aaa"
    var MIN_SWIPE_DISTANCE: Float? = 0.0f

    override fun onAttach(context: Context) {
        super.onAttach(context)
        MIN_SWIPE_DISTANCE =
            resources.displayMetrics?.widthPixels?.toFloat()?.div(5)
    }

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SearchViewModel by viewModels {
        SearchViewModelFactory(
            (requireActivity().application as HistoryApplication).repository
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        Log.d(TAG, "Fragment onCreateView ")
        // Inflate the layout for this fragment
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        enableOrDisableButtonSearch()

        //PARA TENER DOS LAMBDAS SE USA EL PARENTESIS Y EN CADA VALOR UN CORCHETE
        val adapter = HistoryListAdapter(
            { onAutoWriteSelected(it) },
            { deleteItemInDatabase(it) },
            { card, element ->
                swipeAbility(card, element)
            }
        )
        binding.rvHistory.adapter = adapter

        //para que actualice la lista ante cambios
        viewModel.allHistory.observe(this.viewLifecycleOwner) {
            it.let {
                /*val list = StringBuilder()
                it.forEach { obj ->
                    list.append(obj.lastSearch).append(", ")
                }
                Log.d(TAG, "List of words: $list")*/
                adapter.submitList(it)
            }
        }

        binding.btnGoGoogle.setOnClickListener {
            onClickSearch()
        }
/*
        //Esto lo que hace es setear el boton enter en realizar la accion
        binding.tfSearch.setOnEditorActionListener { v, actionId, event ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH){
                goGoogle(v.text.toString())
                true
            }else{
                false
            }
        }*/

        swipeAbility(binding.exampleCard, null)
        //Esto lo que hace es setear el boton enter en realizar la accion
        binding.tfSearch.onSearch { onClickSearch() }
    }

    //EDIT TEXT LISTENER
    private fun enableOrDisableButtonSearch() {
        binding.btnGoGoogle.isEnabled = false
        binding.tfSearch.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun afterTextChanged(s: Editable?) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                //Log.d(TAG, "onTextChanged: ${s.toString().trim()}")
                //aqui la funcion del trim lo que hace es evitar que no cuente si el char especificado esta por delante
                //los elimina de los valores que revisa para poner enable, evitamos que se active el boton solo colocando espacios
                binding.btnGoGoogle.isEnabled = s.toString().trim { it <= ' ' }.isNotEmpty()
            }


        })
    }

    //HANDLE SWIPES
    @SuppressLint("ClickableViewAccessibility")
    fun swipeAbility(view: View, element: LastSearch?) {
        val exampleCard = view
        val start = 300f
        exampleCard.setOnTouchListener(
            View.OnTouchListener { v, event ->

                // variables to store current configuration of quote card.

                val displayMetrics = resources.displayMetrics
                val cardWidth = exampleCard.width
                val cardStart = (displayMetrics.widthPixels.toFloat() / 2) - (cardWidth / 2)

                var done = true
                Log.d(TAG, "event: ${event.action}")
                when (event.action) {

                    MotionEvent.ACTION_UP -> {
                        var currentX = exampleCard.x
                        Log.d(TAG, "start MOVE UP, swipeAbility: $currentX")
                        exampleCard.animate()
                            .x(cardStart)
                            .setDuration(150)
                            .setListener(object : AnimatorListenerAdapter() {
                                override fun onAnimationEnd(animation: Animator) {

                                    lifecycleScope.launch(Dispatchers.Default) {
                                        delay(100)
                                        // check if the swipe distance was more than
                                        // minimum swipe required to load a new quote
                                        if (currentX < MIN_SWIPE_DISTANCE!!) {
                                            // Load a new quote if swiped adequately
                                            Log.d(TAG, "onAnimationEnd: SWIPEEEE")
                                            //CREO QUE EXPLOTA PORQUE MANDA MUCHOS INTENTS A LA VEZ
                                            //YA QUE AL USAR UN DISPATCHER ESTE ES CONSTANTE
                                            //ESTO SERIA PARA ACTUALIZAR UN VALOR, COMO UN LIVE DATA Y OBSERVER
                                            if (done) {
                                                done = false
                                                onClickSearch()
                                            }

                                            //goGoogle()
                                            currentX = 0f
                                        }
                                    }
                                    Log.d(TAG, "onAnimationEnd: it finished")
                                }
                            })

                            .start()
                        Log.d(
                            TAG,
                            "Values MOVE UP: $cardStart --- ${exampleCard.x} ---- ${displayMetrics.widthPixels.toFloat()}  ---- ${exampleCard.x - (cardWidth / 2)}"
                        )
                        Log.d(TAG, "swipeAbility: SECOND SWIPE")
                        // AQUI SI YA NO OCURRE EL BUG, SOLO MANDA UNA VEZ
                        //TODO("PUT THE ACTION HERE")


                    }

                    MotionEvent.ACTION_MOVE -> {
                        // get the new co-ordinate of X-axis
                        val newX = event.rawX

                        // carry out swipe only if newX < cardStart, that is,
                        // the card is swiped to the left side, not to the right
                        // Detailed explanation at: https://genicsblog.com/swipe-animation-on-a-cardview-android
                        if (newX - cardWidth < cardStart) {
                            Log.d(
                                TAG,
                                "Values MOVE: $cardStart --- $newX ---- ${displayMetrics.widthPixels.toFloat()}  ---- ${newX - (cardWidth / 2)}"
                            )
                            exampleCard.animate()
                                .x(
                                    min(cardStart, newX - (cardWidth / 2))
                                )
                                .setDuration(0)
                                .start()
                        } else {
                            Log.d(TAG, "false!")
                        }
                    }

                    MotionEvent.ACTION_CANCEL -> {
                        exampleCard.animate()
                            .x(cardStart)
                            .setDuration(150)
                            .start()
                    }
                }

                // required to by-pass lint warning
                v.performClick()
                return@OnTouchListener true
            }
        )
    }

    private fun deleteItemInDatabase(lastSearch: LastSearch) {
        viewModel.deleteLastSearchDatabase(lastSearch)
    }

    private fun onAutoWriteSelected(lastSearch: LastSearch) {
        binding.apply {
            tfSearch.setText(lastSearch.lastSearch, TextView.BufferType.SPANNABLE)
        }
    }

    private fun onClickSearch() {
        var entrySearch = ""
        requireActivity().runOnUiThread {
            entrySearch = binding.tfSearch.text.toString()
            Log.d(TAG, "getText from ui: $entrySearch")
        }

        //binding.tfSearch.setText("", TextView.BufferType.SPANNABLE)
        //val snack = Snackbar.make(it, "$entrySearch", Snackbar.LENGTH_SHORT)
        //snack.show()
        viewModel.updateWordInLastSearchList(entrySearch)
        goToBrowser(entrySearch)
    }

    private fun goToBrowser(entry: String) {
        Log.d(TAG, "google Intent: $entry")
        //Creamos la Url
        val queryUrl: Uri = Uri.parse(SearchActivity.SEARCH_PREFIX.plus(entry))

        val intent = Intent(Intent.ACTION_VIEW, queryUrl)
        //requireContext().startActivity(intent)
        requireActivity().runOnUiThread {
            Toast.makeText(requireContext(), "search $entry", Toast.LENGTH_SHORT).show()
        }
    }

    //CREA UNA EXTENSION KOTLIN QUE PERMITE LINKEAR EL BOTON ENTER A CUALQUIER FUNCION QUE LA NECESITE
    private fun EditText.onSearch(callback: () -> Unit) {
        setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                if (binding.tfSearch.text.toString().trim().isNotEmpty()) {
                    callback.invoke()
                }
                return@setOnEditorActionListener true
            } else false
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        //Hide keyboard.
        val inputMethodManager = requireActivity().getSystemService(INPUT_METHOD_SERVICE) as
                InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(requireActivity().currentFocus?.windowToken, 0)
        _binding = null
    }

}