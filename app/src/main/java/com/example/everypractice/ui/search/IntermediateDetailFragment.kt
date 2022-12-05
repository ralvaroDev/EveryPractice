package com.example.everypractice.ui.search

import android.os.*
import android.view.*
import androidx.activity.*
import androidx.fragment.app.*
import androidx.lifecycle.*
import androidx.navigation.fragment.*
import androidx.viewpager2.widget.*
import coil.*
import coil.request.*
import coil.transform.*
import com.example.everypractice.data.models.*
import com.example.everypractice.databinding.*
import com.example.everypractice.trash.*
import com.example.everypractice.ui.*
import dagger.hilt.android.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import timber.log.*
import kotlin.math.*

@AndroidEntryPoint
class IntermediateDetailFragment : Fragment() {

    private var _binding: FragmentIntermediateDetailBinding? = null
    private val binding get() = _binding!!

    private var movieId: Int = 0
    private var positionClicked: Int = 0

    private var currentIdView: List<TemporarySearchMovieElement> = listOf()

    /*VALORES PARA EL VP DE EJEMPLO
    private lateinit var handler: Handler
    private lateinit var imageList: ArrayList<Int>
    private lateinit var adapter: IntermediateAdapterViewPage*/
    private lateinit var adapter2: AdapterViewPageIntermediate
    private lateinit var viewPager2: ViewPager2

    private val sharedViewModel: MovieViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments.let {
            movieId = it!!.getInt("id")
            positionClicked = it.getInt("position")
        }
        val callback = requireActivity().onBackPressedDispatcher.addCallback(this){
            goBackToFragmentSearchMovies()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentIntermediateDetailBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.extendedFabBack.setOnClickListener {
            goBackToFragmentSearchMovies()
        }
        binding.topTitleApp.text = movieId.toString()
        binding.extendedFabSearch.setOnClickListener {
            //TODO IMPLEMENT THIS BUTTON TO SEARCH AGAIN
        }

        initViewPager()
        setUpTransformerPager2()

        viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                binding.btnSeeDetails.setOnClickListener {
                    goToFragmentDetailMovies(currentIdView[position].id,position)
                }
                binding.backgroungColorToGlide.load(currentIdView[position].posterPathUrl){
                    crossfade(750)
                        .transformations(
                            BlurTransformation(requireContext(), radius = 18f, sampling = 5f)
                        //el sampling le da mayor borrosidad
                        )
                            //esto es para que siempre se mantenga el efecto de crossfade
                        .memoryCachePolicy(CachePolicy.WRITE_ONLY)
                        .build()
                }
                Timber.d("page: $position")

            }
        })


        /*viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback(){
            override fun onPageSelected(position: Int) {
                super.onPageSelected(position)
                handler.removeCallbacks(runnable)
                //cada que seleccionamoos luego de dos =segundos cambiara automaticamente al siguiente objeto
                handler.postDelayed(runnable,2000)

            }
        })*/
    }

    /*override fun onPause() {
        super.onPause()
        handler.removeCallbacks(runnable)
    }*/

    /*override fun onResume() {
        super.onResume()
        handler.postDelayed(runnable,2000)
    }
*/


    /**
     * Function that make a format to the elements of viewPager
     * */
    private fun setUpTransformerPager2() {
        val transformer = CompositePageTransformer()
        transformer.addTransformer(MarginPageTransformer(40))
        transformer.addTransformer { page, position ->
            val r = 1 - abs(position)
            page.scaleY = 0.85f + r * 0.14f
        }

        viewPager2.setPageTransformer(transformer)
    }

    /**
     * That creates the viewPager sending the data to print
     * */
    private fun initViewPager() {
        viewPager2 = binding.viewPager2
        lifecycleScope.launchWhenCreated {
            sharedViewModel.showListFromSearch().collectLatest {
                adapter2 = AdapterViewPageIntermediate(it.data.results)
                //{ _, _ -> lambdaFunctionDisable() }

                currentIdView = it.data.results
            }
        }
        viewPager2.adapter = adapter2
        viewPager2.offscreenPageLimit = 3
        viewPager2.clipToPadding = false
        viewPager2.clipChildren = false
        viewPager2.getChildAt(0).overScrollMode = 2
        /*requireActivity().runOnUiThread {
            val snackbar = Snackbar.make(
                requireView(),
                "position clicked: $positionClicked",
                Snackbar.LENGTH_SHORT
            )
            snackbar.show()
        }*/

        //el post es para viewPager2
        viewPager2.post {
            viewPager2.setCurrentItem(positionClicked, true)
        }//este funcion, el de abajo nel pastel
        //viewPager2.currentItem = positionClicked


        //ESTO SOLO FUNCIONA EN EL UNO
        /*viewPager2.post {
            Timber.d("Current: ${viewPager2.currentItem}")
        }*/

    }

    private fun goBackToFragmentSearchMovies() {
        val action = IntermediateDetailFragmentDirections
            .actionIntermediateDetailFragmentToSearchMoviesFragment("")
        findNavController().navigate(action)
    }

    private fun lambdaFunctionDisable() {
        //TO IMPLEMENT IN FUTURE
    }

    //AQUI DEBEMOS PEDIRLE NUEVAMENTE EL POSITION DEL CLICADO PARA PASARLO
    private fun goToFragmentDetailMovies(
        currentId: Int,
        position: Int,
    ) {
        Timber.d("Go to more details with Id: $currentId")
        onClickMovieAndSendPetition(currentId)


        val action = IntermediateDetailFragmentDirections
                .toDetailMovieFragment(idmovie = currentId, recorderposition = position)
        findNavController().navigate(action)
    }

    private fun onClickMovieAndSendPetition(id: Int) {
        lifecycleScope.launch {
            sharedViewModel.sendPetitionToGetMovieDetails(id)
        }
        sharedViewModel.sendPetitionToGetStaffFromMovieWithGivenId(id)
        sharedViewModel.sendPetitionToGetImagesFromMovieWithGivenId(id)
    }

}