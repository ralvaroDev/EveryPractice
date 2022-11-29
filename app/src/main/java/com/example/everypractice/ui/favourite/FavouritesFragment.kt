package com.example.everypractice.ui.favourite

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.navigation.fragment.findNavController
import com.example.everypractice.databinding.FragmentFavouritesBinding
import com.example.everypractice.ui.MainApplication
import com.example.everypractice.ui.movies.vm.FavouriteMoviesViewModelFactory
import com.example.everypractice.ui.movies.vm.MovieViewModel
import com.google.android.material.tabs.TabLayoutMediator


class FavouritesFragment : Fragment() {

    private var _binding: FragmentFavouritesBinding? = null
    private val binding get() = _binding!!

    private lateinit var adapterViews: AdapterViews

    private val sharedViewModel: MovieViewModel by activityViewModels {
        FavouriteMoviesViewModelFactory(
            (requireActivity().application as MainApplication).movieRepository
        )
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentFavouritesBinding.inflate(layoutInflater)
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
        val action = FavouritesFragmentDirections
            .actionFavouriteMoviesFragmentToNavigationHome()
        findNavController().navigate(action)
    }



}