package com.example.everypractice.prinoptions.movies.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.everypractice.databinding.FragmentFavouriteMoviesBinding
import com.example.everypractice.prinoptions.HistoryApplication
import com.example.everypractice.prinoptions.movies.ui.favourite.AdapterViews
import com.example.everypractice.prinoptions.movies.ui.favourite.ZoomOutPageTransformer
import com.example.everypractice.prinoptions.movies.vm.FavouriteMoviesViewModelFactory
import com.example.everypractice.prinoptions.movies.vm.MovieViewModel
import com.google.android.material.tabs.TabLayoutMediator


class FavouriteMoviesFragment : Fragment() {

    private var _binding: FragmentFavouriteMoviesBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapterViews: AdapterViews

    private val sharedViewModel: MovieViewModel by activityViewModels {
        FavouriteMoviesViewModelFactory(
            (requireActivity().application as HistoryApplication).movieRepository
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentFavouriteMoviesBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapterViews = AdapterViews(this)
        binding.viewPagerFavourite.adapter = adapterViews
        binding.viewPagerFavourite.setPageTransformer(ZoomOutPageTransformer())

        TabLayoutMediator(binding.tabLayout,binding.viewPagerFavourite){tab, position ->
            when (position){
                0 -> tab.text = "SAVED"
                1 -> tab.text = "SEEN"
            }
        }.attach()




    }


    private fun goToInitialFragment() {
        val action = FavouriteMoviesFragmentDirections
            .actionFavouriteMoviesFragmentToNavigationHome()
        findNavController().navigate(action)
    }



}