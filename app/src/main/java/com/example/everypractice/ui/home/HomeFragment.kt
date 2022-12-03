package com.example.everypractice.ui.home

import android.content.*
import android.os.*
import android.view.*
import androidx.appcompat.app.*
import androidx.fragment.app.*
import androidx.lifecycle.*
import androidx.viewpager2.widget.*
import com.example.everypractice.databinding.*
import com.example.everypractice.ui.signin.*
import com.example.everypractice.utils.Result.*
import com.google.firebase.auth.*
import dagger.hilt.android.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import timber.log.*
import javax.inject.*
import kotlin.math.*

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    private val binding get() = _binding!!

    private val homeViewModel: HomeViewModel by viewModels()

    @Inject
    lateinit var firebaseAuth: FirebaseAuth

    //VIEW-PAGER-2
    private lateinit var viewPager2: ViewPager2

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHomeBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        confirmUserIsIn()
        setupCustom()

        binding.btnNotification.setOnClickListener {
            firebaseAuth.signOut()
            onOutSession()
            homeViewModel.setPreferenceUnLogged()
            activity?.finish()
        }

        viewPager2 = binding.vp2DailyRecomendation
        lifecycleScope.launch {
            homeViewModel.showRecommendedMoviesHome.collectLatest {
                when(it){
                    is Error -> {Timber.d("Error getting movies from Database")}
                    Loading -> {}
                    is Success -> {
                        viewPager2.adapter = AdapterViewPageInitialRecommended(it.data)
                    }
                }
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
        if (firebaseAuth.currentUser == null) {
            homeViewModel.setPreferenceUnLogged()

            AlertDialog.Builder(requireContext()).setTitle("Session Expired")
                .setMessage("The session has been closed").setPositiveButton("OK", null).create()
                .show()
            //TODO PUT A NOTIFICATION THAT THE SESSION IS CLOSED
            activity?.finish()

        }

    }

    private fun setupCustom() {
        binding.tvPerfilName.text = firebaseAuth.currentUser?.displayName
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

            when {
                position < -1 -> {
                    //izq doble
                    page.y = -position * 200f
                }

                position < 0 -> {
                    //center to izq
                    page.rotation = position * 90 / 5
                    page.y = -position * 200f

                }

                position == 0f -> {
                    //centro
                    page.rotation = 0f
                    page.y = 0f
                }
                position <= 1 -> {
                    //derecha to center
                    page.rotation = position * 90 / 5
                    page.y = 200f * position


                }
                else -> {
                    //doble derecha
                    page.rotation = position * 90 / 5
                    page.y = 200f * position
                }

            }

        }

        viewPager2.setPageTransformer(transformer)
    }

}


