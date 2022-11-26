package com.example.everypractice.prinoptions.movies.ui.favourite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import com.example.everypractice.databinding.FragmentFavouriteSeenBinding
import com.example.everypractice.prinoptions.HistoryApplication
import com.example.everypractice.prinoptions.movies.recycler.FavouriteSeenMovieListAdapter
import com.example.everypractice.prinoptions.movies.vm.FavouriteMoviesViewModelFactory
import com.example.everypractice.prinoptions.movies.vm.MovieViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch

class FavouriteSeenFragment : Fragment() {

    private var _binding: FragmentFavouriteSeenBinding? = null
    private val binding get() = _binding!!

    private val sharedViewModel: MovieViewModel by activityViewModels {
        FavouriteMoviesViewModelFactory(
            (requireActivity().application as HistoryApplication).movieRepository
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

        val adapter = FavouriteSeenMovieListAdapter {
            moveMovieToRestored(it)
        }
        binding.rvFavouriteMoviesUNSaved.adapter = adapter


        lifecycleScope.launch{
            sharedViewModel.getFavouriteSeenMovies().collectLatest {
                adapter.submitList(it)
            }
        }
    }

    private fun moveMovieToRestored(id: Int) {
        lifecycleScope.launch {
            sharedViewModel.updateSavedStatusFromDatabaseWithId(saved = true, id = id)
        }
    }

}