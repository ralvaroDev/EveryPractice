package com.example.everypractice.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.widget.EditText
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import com.example.everypractice.data.domain.GenresMovies
import com.example.everypractice.databinding.FragmentSearchBinding
import com.example.everypractice.ui.MainApplication
import com.example.everypractice.ui.movies.vm.FavouriteMoviesViewModelFactory
import com.example.everypractice.ui.movies.vm.MovieViewModel
import com.example.everypractice.ui.movies.vm.RequestMovieStatus
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber

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

    private val sharedViewModel: MovieViewModel by activityViewModels {
        FavouriteMoviesViewModelFactory(
            (requireActivity().application as MainApplication).movieRepository
        )
    }

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

        lifecycleScope.launchWhenStarted {
            sharedViewModel.requestPopularStatus.collectLatest { status ->
                Timber.d("MovieStatus POPULAR in collector: $status")
                when (status) {
                    RequestMovieStatus.LOADING -> {
                        /*binding.rvMovieSearch.visibility = View.GONE
                        binding.layoutShimmerNotificationsLoader.shimmerLoader.visible()*/
                    }
                    RequestMovieStatus.ERROR -> {
                        /*binding.rvMovieSearch.visibility = View.GONE
                        binding.layoutShimmerNotificationsLoader.shimmerLoader.gone()*/
                    }
                    RequestMovieStatus.DONE -> {
                        /*binding.layoutShimmerNotificationsLoader.shimmerLoader.gone()
                        binding.rvMovieSearch.visibility = View.VISIBLE*/
                        Timber.d("MovieStatus inside IF: $status")
                        sharedViewModel.showListOfPopularMovie().collectLatest {
                            adapter.submitList(it.results)
                        }
                    }
                }
            }
        }

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