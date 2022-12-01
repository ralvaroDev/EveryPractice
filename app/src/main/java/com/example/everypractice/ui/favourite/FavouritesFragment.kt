package com.example.everypractice.ui.favourite

import android.os.*
import android.view.*
import androidx.fragment.app.*
import androidx.navigation.fragment.*
import com.example.everypractice.*
import com.example.everypractice.databinding.*
import com.example.everypractice.ui.movies.vm.*
import com.google.android.material.tabs.*


class FavouritesFragment : Fragment() {

    private var _binding: FragmentFavouritesBinding? = null
    private val binding get() = _binding!!

    private var tabSelected: Int = 0

    private lateinit var adapterViews: AdapterViews

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments.let {
            tabSelected = it!!.getInt("fromtab")
        }
    }

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
        binding.tabLayout.selectTab(binding.tabLayout.getTabAt(tabSelected))



    }


    private fun goToInitialFragment() {
        val action = FavouritesFragmentDirections
            .actionFavouriteMoviesFragmentToNavigationHome()
        findNavController().navigate(action)
    }



}