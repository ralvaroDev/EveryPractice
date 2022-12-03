package com.example.everypractice.ui.search

import android.os.*
import android.view.*
import android.view.inputmethod.*
import android.widget.*
import androidx.fragment.app.*
import androidx.lifecycle.*
import androidx.navigation.fragment.*
import com.example.everypractice.data.models.*
import com.example.everypractice.databinding.*
import com.example.everypractice.helpers.extensions.*
import com.example.everypractice.ui.*
import com.example.everypractice.utils.Result.*
import dagger.hilt.android.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import timber.log.*

@AndroidEntryPoint
class SearchFragment : Fragment() {

    companion object {
        private val generesList = listOf(
            GenresMovies(28),
            GenresMovies(12),
            GenresMovies(16),
            GenresMovies(35),
            GenresMovies(80),
            GenresMovies(99),
            GenresMovies(18),
            GenresMovies(10751),
            GenresMovies(14),
            GenresMovies(36),
            GenresMovies(27),
            GenresMovies(10402),
            GenresMovies(9648),
            GenresMovies(10749),
            GenresMovies(878),
            GenresMovies(10770),
            GenresMovies(53),
            GenresMovies(10752),
            GenresMovies(37)
        )
    }

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!

    private val searchViewModel: SearchViewModel by viewModels()

    private val sharedViewModel: MovieViewModel by activityViewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentSearchBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapterGenres = GenresInSearchAdapter(generesList.shuffled()){
            //function to send petition by categories
        }
        binding.layoutNewSearchLoader.rvGenresListSearch.adapter = adapterGenres



        binding.icShare.setOnClickListener {
            /*lifecycleScope.launch {
                sharedViewModel.obtainMultiSEARCHWithWord("Spider man")
            }*/
            goToBrowserMovie("Spider man")
            val action = SearchFragmentDirections.toSearchFragment(movie = "Spider man")
            findNavController().navigate(action)
        }

        binding.tfSearch.onSearch { onClickSearchMovie() }

        val adapter = PopularMoviesAdapter {
            onClickItemFromPopular(it)
        }

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED){
                searchViewModel.popularMovies.collectLatest {
                    when(it){
                        is Error -> {
                            binding.layoutNewSearchLoader.tvTitlePopular.gone()
                            binding.layoutNewSearchLoader.rvPopularMovies.gone()
                            Timber.d("Error: ${it.exception.message}")
                        }
                        Loading -> {
                            Timber.d("Loading bro")
                            binding.layoutNewSearchLoader.rvPopularMovies.inVisible()
                            binding.layoutNewSearchLoader.layoutShimmerPopularItems.shimmerLoader.visible()
                        }
                        is Success -> {
                            adapter.submitList(it.data.results)
                            binding.layoutNewSearchLoader.rvPopularMovies.visible()
                            binding.layoutNewSearchLoader.layoutShimmerPopularItems.shimmerLoader.gone()
                        }
                    }
                }
            }

        }

        /*lifecycleScope.launchWhenStarted {
            sharedViewModel.requestPopularStatus.collectLatest { status ->
                Timber.d("MovieStatus POPULAR in collector: $status")
                when (status) {
                    RequestMovieStatus.LOADING -> {
                        *//*binding.rvMovieSearch.visibility = View.GONE
                        binding.layoutShimmerNotificationsLoader.shimmerLoader.visible()*//*
                    }
                    RequestMovieStatus.ERROR -> {
                        *//*binding.rvMovieSearch.visibility = View.GONE
                        binding.layoutShimmerNotificationsLoader.shimmerLoader.gone()*//*
                    }
                    RequestMovieStatus.DONE -> {
                        *//*binding.layoutShimmerNotificationsLoader.shimmerLoader.gone()
                        binding.rvMovieSearch.visibility = View.VISIBLE*//*
                        Timber.d("MovieStatus inside IF: $status")
                        sharedViewModel.showListOfPopularMovie().collectLatest {
                            adapter.submitList(it.results)
                        }
                    }
                }
            }
        }*/

        binding.layoutNewSearchLoader.rvPopularMovies.adapter = adapter
    }


    private fun onClickItemFromPopular(id: Int) {
        sharedViewModel.sendPetitionToGetMovieDetails(id)
        sharedViewModel.sendPetitionToGetStaffFromMovieWithGivenId(id)
        sharedViewModel.sendPetitionToGetImagesFromMovieWithGivenId(id)
        val action = SearchFragmentDirections.toDetailMovieFragment(
            idmovie = id,
            //TODO SI ES MENOS UNO QUE REGRESE AQUI NO A INTERMEDIATE
            recorderposition = -1
        )
        findNavController().navigate(action)
    }


    private fun onClickSearchMovie() {
        var entrySearchMovie = ""
        requireActivity().runOnUiThread {
            entrySearchMovie = binding.tfSearch.text.toString()
        }
        goToBrowserMovie(entrySearchMovie)
        try {
            val action =
                SearchFragmentDirections.toSearchFragment(movie = entrySearchMovie)
            findNavController().navigate(action)
        } catch (e: Exception) {
            Timber.d(e)
        }
    }

    /**
     * Function that send petition  petition and update the status of it
     */
    private fun goToBrowserMovie(entry: String) {
        //ESTO MANDA LA SOLICITUD QUE A LA VEZ ALMACENA LO RECIBIDO Y EMITE LA LISTA GUARDADA
        lifecycleScope.launch {
            sharedViewModel.sendPetitionSearchMovie(entry)
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

}