package com.example.everypractice.ui.search

import android.os.*
import android.view.*
import android.view.inputmethod.*
import android.widget.*
import androidx.activity.*
import androidx.fragment.app.*
import androidx.lifecycle.*
import androidx.navigation.fragment.*
import com.example.everypractice.databinding.*
import com.example.everypractice.helpers.extensions.*
import com.example.everypractice.ui.*
import dagger.hilt.android.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import timber.log.*

@AndroidEntryPoint
class SearchResultFragment : Fragment() {

    private var _binding: FragmentSearchResultBinding? = null
    private val binding get() = _binding!!

    private lateinit var movieSearch: String

    private val sharedViewModel: MovieViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments.let {
            movieSearch = it!!.getString("movie").toString()
        }

        val callback = requireActivity().onBackPressedDispatcher.addCallback(this){
            goBackToNavigationSearch()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentSearchResultBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Coloca el extra de la busqueda en el editText
        binding.tfSearch.setText(movieSearch, TextView.BufferType.SPANNABLE)
        //Boton enter al teclado hara la busqueda
        binding.tfSearch.onSearch { onClickSearchMovie() }
        //Context que se manda al adapter y asi poder hacer el efecto Glass al recycler
        val context = requireContext()

        val adapter = MovieSearchAdapter(
            context
        ) { element, position,listSize ->
            goToIntermediateDetail(element.id, position,listSize)
        }

        lifecycleScope.launchWhenStarted {
            sharedViewModel.requestMovieSearchStatus.collectLatest { status ->
                Timber.d("MovieStatus in collector: $status")
                when (status) {

                    RequestMovieStatus.LOADING -> {
                        Timber.d("Loading bro")
                        binding.rvMovieSearch.visibility = View.GONE
                        binding.layoutShimmerNotificationsLoader.shimmerLoader.visible()
                    }
                    RequestMovieStatus.ERROR -> {
                        binding.rvMovieSearch.visibility = View.GONE
                        binding.layoutShimmerNotificationsLoader.shimmerLoader.gone()
                    }
                    RequestMovieStatus.DONE -> {
                        binding.layoutShimmerNotificationsLoader.shimmerLoader.gone()
                        binding.rvMovieSearch.visibility = View.VISIBLE
                        Timber.d("MovieStatus inside IF: $status")
                        sharedViewModel.showListFromSearch().collectLatest {
                            adapter.submitList(it.results)
                        }
                    }
                }
            }
        }

        binding.rvMovieSearch.adapter = adapter


    }


    private fun onClickSearchMovie() {
        var entrySearchMovie = ""
        requireActivity().runOnUiThread {
            entrySearchMovie = binding.tfSearch.text.toString()
            Timber.d("getText from ui: $entrySearchMovie")
        }
        //binding.tfSearch.setText("", TextView.BufferType.SPANNABLE)
        //val snack = Snackbar.make(it, "$entrySearch", Snackbar.LENGTH_SHORT)
        //snack.show()
        goToBrowserMovie(entrySearchMovie)
    }

    private fun goToBrowserMovie(entry: String) {
        Timber.d("movie Intent: $entry")
        /*viewModel.getListMoviesWithWordFromInitialFragm(entry)
        lifecycleScope.launchWhenStarted {
            viewModel.movieListSearch.collectLatest { listMovies->
                listMovies.forEach {
                    Timber.d("${ it.title } \n" )
                }
            }
        }*/

        lifecycleScope.launch {
            sharedViewModel.sendPetitionSearchMovie(entry)
        }

        requireActivity().runOnUiThread {
            Toast.makeText(requireContext(), "search $entry", Toast.LENGTH_SHORT).show()
        }
    }

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

    private fun goToIntermediateDetail(id: Int, position: Int, listSize:Int){

        //AQUI DEBE DE IR EL SCOPE QUE HACE LA PETICION Y SOLO LA PETICION DE LA PELI CON SU ID
        //EN EL OTRO INTENT DEBERIA DE IR YA EL QUE MUESTRA O COLECTA LOS DATOS

        onClickMovieAndSendPetition(id)

        val action = SearchResultFragmentDirections
            .actionSearchMoviesFragmentToIntermediateDetailFragment(id = id, position = position)
        findNavController().navigate(action)
    }

    private fun onClickMovieAndSendPetition(id:Int){
        requireActivity().runOnUiThread {
            Timber.d("getText from ui: $id")
        }

        lifecycleScope.launch{
            sharedViewModel.sendPetitionToGetMovieDetails(id)
        }


    }

    private fun goBackToNavigationSearch() {
        val action = SearchResultFragmentDirections.toNavigationSearch()
        findNavController().navigate(action)
    }



}