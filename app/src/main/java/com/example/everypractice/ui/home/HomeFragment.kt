package com.example.everypractice.ui.home

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.lifecycle.lifecycleScope
import androidx.viewpager2.widget.CompositePageTransformer
import androidx.viewpager2.widget.MarginPageTransformer
import androidx.viewpager2.widget.ViewPager2
import com.example.everypractice.databinding.FragmentHomeBinding
import com.example.everypractice.ui.MainApplication
import com.example.everypractice.ui.movies.vm.FavouriteMoviesViewModelFactory
import com.example.everypractice.ui.movies.vm.MovieViewModel
import com.example.everypractice.ui.signin.LoginActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber
import kotlin.math.abs

class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    //VIEW-PAGER-2
    private lateinit var viewPager2: ViewPager2

    private val sharedViewModel: MovieViewModel by activityViewModels {
        FavouriteMoviesViewModelFactory(
            (requireActivity().application as MainApplication).movieRepository
        )
    }

    override fun onResume() {
        super.onResume()
        Timber.d("ME ACABO DE CREAAAR")
    }


    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        confirmUserIsIn()

        setupCustom()

        binding.btnNotification.setOnClickListener {
            FirebaseAuth.getInstance().signOut()
            onOutSession()
            lifecycleScope.launch {
                (requireActivity().application as MainApplication).userPreferenceRepository.updateUserLastSession(false,"","")
            }
            activity?.finish()
        }

        viewPager2 = binding.vp2DailyRecomendation
        lifecycleScope.launchWhenCreated {
            sharedViewModel.get4FavouriteRecommendedMovies().collectLatest {
                viewPager2.adapter = AdapterViewPageInitialRecommended(it)
            }
        }
        viewPager2.offscreenPageLimit = 3
        viewPager2.clipToPadding = false
        viewPager2.clipChildren = false
        viewPager2.getChildAt(0).overScrollMode = 2
        viewPager2.post {
            viewPager2.setCurrentItem(1, true)
        }

        setUpoTransformer()

    }

    private fun confirmUserIsIn() {
        if (FirebaseAuth.getInstance().currentUser == null){
            lifecycleScope.launch {
                (requireActivity().application as MainApplication).userPreferenceRepository.updateUserLastSession(false,"","")
            }

            AlertDialog.Builder(requireContext()).setTitle("Session Expired")
                .setMessage("The session has been closed").setPositiveButton("OK", null).create()
                .show()
            //TODO PUT A NOTIFICATION THAT THE SESSION IS CLOSED
            activity?.finish()

        }

    }

    private fun setupCustom() {
        binding.tvPerfilName.text = Firebase.auth.currentUser?.displayName
    }

    private fun onOutSession() {

        val intent = Intent(requireContext(), LoginActivity::class.java)
        startActivity(intent)

    }

    /**
     * Function that make a format to the elements of viewPager
     * */
    private fun setUpoTransformer() {
        val transformer = CompositePageTransformer()
        transformer.addTransformer(MarginPageTransformer(40))
        transformer.addTransformer { page, position ->
            val r = 1 - abs(position)
            page.scaleY = 0.85f + r * 0.14f

            when{
                position < -1 ->{
                    //izq doble
                    page.y = -position*200f
                }

                position <0 ->{
                    //center to izq
                    page.rotation = position*90/5
                    page.y = -position*200f

                }

                position == 0f ->{
                    //centro
                    page.rotation = 0f
                    page.y = 0f
                }
                position <= 1 ->{
                    //derecha to center
                    page.rotation = position*90/5
                    page.y = 200f*position


                }
                else -> {
                    //doble derecha
                    page.rotation=position*90/5
                    page.y = 200f*position
                }

            }

        }

        viewPager2.setPageTransformer(transformer)
    }

}


