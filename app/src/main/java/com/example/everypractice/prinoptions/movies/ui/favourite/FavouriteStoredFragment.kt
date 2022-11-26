package com.example.everypractice.prinoptions.movies.ui.favourite

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.example.everypractice.R
import com.example.everypractice.databinding.FragmentFavouriteSeenBinding
import com.example.everypractice.databinding.FragmentFavouriteStoredBinding
import com.example.everypractice.prinoptions.HistoryApplication
import com.example.everypractice.prinoptions.movies.recycler.FavouriteMovieListAdapter
import com.example.everypractice.prinoptions.movies.vm.FavouriteMoviesViewModelFactory
import com.example.everypractice.prinoptions.movies.vm.MovieViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber


class FavouriteStoredFragment : Fragment() {

    private var _binding: FragmentFavouriteStoredBinding? = null
    private val binding get() = _binding!!

    private val sharedViewModel: MovieViewModel by activityViewModels {
        FavouriteMoviesViewModelFactory(
            (requireActivity().application as HistoryApplication).movieRepository
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

        val adapter = FavouriteMovieListAdapter {
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