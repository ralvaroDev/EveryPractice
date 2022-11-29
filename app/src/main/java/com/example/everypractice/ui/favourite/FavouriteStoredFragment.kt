package com.example.everypractice.ui.favourite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.example.everypractice.databinding.FragmentFavouriteStoredBinding
import com.example.everypractice.ui.MainApplication
import com.example.everypractice.ui.movies.vm.FavouriteMoviesViewModelFactory
import com.example.everypractice.ui.movies.vm.MovieViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch


class FavouriteStoredFragment : Fragment() {

    private var _binding: FragmentFavouriteStoredBinding? = null
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
        _binding = FragmentFavouriteStoredBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val adapter = FavouriteSavedMovieListAdapter {
            moveMovieToSeen(it)
        }
        binding.rvFavouriteMoviesSaved.adapter = adapter


        lifecycleScope.launch{
            sharedViewModel.getFavouriteMovies().collectLatest {
                adapter.submitList(it)
            }
        }

    }

    private fun moveMovieToSeen(id: Int) {
        lifecycleScope.launch {
            sharedViewModel.updateSavedStatusFromDatabaseWithId(saved = false, id = id)
        }
    }


}