package com.example.everypractice.ui.favourite

import android.os.*
import android.view.*
import androidx.fragment.app.*
import androidx.lifecycle.*
import androidx.navigation.fragment.*
import com.example.everypractice.*
import com.example.everypractice.databinding.*
import com.example.everypractice.ui.movies.vm.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*

class FavouriteSeenFragment : Fragment() {

    private var _binding: FragmentFavouriteSeenBinding? = null
    private val binding get() = _binding!!

    private val sharedViewModel: MovieViewModel by activityViewModels {
        FavouriteMoviesViewModelFactory(
            (requireActivity().application as MainApplication).movieRepository
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentFavouriteSeenBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = FavouriteSeenMovieListAdapter(
            { moveMovieToRestored(it) },
            { goToSeeDetails(it) })
        binding.rvFavouriteMoviesUNSaved.adapter = adapter


        lifecycleScope.launch {
            sharedViewModel.getFavouriteSeenMovies().collectLatest {
                adapter.submitList(it)
            }
        }
    }

    private fun goToSeeDetails(idMovie: Int) {

        onClickMovieAndSendPetition(idMovie)

        val action = FavouritesFragmentDirections.toDetailMovieFragment(
            idmovie = idMovie,
            recorderposition = -2
        )
        findNavController().navigate(action)
    }

    private fun onClickMovieAndSendPetition(id: Int) {
        //TODO PEDIMOS TODO NUEVAMENTE, DEBIDO A QUE TENEMOS LUEGO QUE AGREGAR FUNCIONALIDAD DE
        // QUE CUANDO VENGAN DE AQUI, AL OTRO LADO NO ESCUCHE LOS DETAILS, SINO SOLO LLAME A LOS
        // GUARDADOS, O TAMBIEN PUEDE SER QUE MUESTRE Y LUEGO PIDA, QUE VERIFIQUE SI LA PETICION
        // ESTA Y DEVUELVE DONE, SINO EN DICHO CASO, QUE USE DE LA BASE DE DATOS (DATO) EL 2DO MEJOR
        sharedViewModel.sendPetitionToGetMovieDetails(id)
        sharedViewModel.sendPetitionToGetStaffFromMovieWithGivenId(id)
        sharedViewModel.sendPetitionToGetImagesFromMovieWithGivenId(id)
    }

    private fun moveMovieToRestored(id: Int) {
        lifecycleScope.launch {
            sharedViewModel.updateSavedStatusFromDatabaseWithId(saved = true, id = id)
        }
    }

}