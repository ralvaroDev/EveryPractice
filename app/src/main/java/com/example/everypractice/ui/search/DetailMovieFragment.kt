package com.example.everypractice.ui.search

import android.os.*
import android.view.*
import androidx.activity.*
import androidx.fragment.app.*
import androidx.lifecycle.*
import com.example.everypractice.databinding.*
import com.example.everypractice.ui.*
import com.example.everypractice.utils.util.*
import dagger.hilt.android.*
import kotlinx.coroutines.flow.*
import timber.log.*

@AndroidEntryPoint
class DetailMovieFragment : Fragment() {

    private var _binding: FragmentDetailMovieBinding? = null
    private val binding get() = _binding!!

    private var movieId: Int = 0
    private var recorderPosition: Int = 0

    private val sharedViewModel: MovieViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments.let {
            movieId = it!!.getInt("idmovie")
            recorderPosition = it.getInt("recorderposition")
        }
        val callback = requireActivity().onBackPressedDispatcher.addCallback(this){
            //goBackToFragmentIntermediateMovies()

        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        // Inflate the layout for this fragment
        _binding = FragmentDetailMovieBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)



        lifecycleScope.launchWhenStarted {
            sharedViewModel.requestMovieDetailStatus.collectLatest { status ->
                Timber.d("MovieDetailStatus in collector: $status")
                when(status){

                    RequestMovieStatus.LOADING -> {
                        //binding.layoutShimmerNotificationsLoader.shimmerLoader.visible()
                    }
                    RequestMovieStatus.ERROR -> {
                        //binding.layoutShimmerNotificationsLoader.shimmerLoader.gone()
                    }
                    RequestMovieStatus.DONE -> {
                        //binding.layoutShimmerNotificationsLoader.shimmerLoader.gone()
                        Timber.d("MovieDetailStatus inside IF: $status")
                        sharedViewModel.showMovieWithDetails().collectLatest {movie->
                            binding.layoutDetailOfMovieLoader.apply {
                                executePendingBindings()
                                detailMovie = movie
                                tvTitleDetail.text = movie.movieTitle
                                tvRatingDetail.text = movie.voteAverage.toString()
                                tvDurationDetail.text = movie.runtime.toString()
                                tvOverview.text = movie.overview
                                tvMainGenre.text = movie.genres.getTransformationIdsToGenres()[0]
                                tvNameColectionInDetail.text = movie.nameCollectionBelongsTo
                            }


                            //TODO SIENTO QUE VA A EXPLOTAR XD
                            //TODO, QUIZA SE LE PUEDE PONER UN DELAY AL ELIMINADO, Y CON ESTO
                            //COMO UNA PAPELERA, QUE SE ELIMINE DENTRO DE 10 MINUTOS
                            //SI LE DOY SEGUIDO NORMAL, SOLO CAMBIARA EL BOTON MAS NO EMITIRA UNA PETICION
                            //O HARA EL INGRESO A LA BASE DE DATOS
                            //REVISANDO EL CACHE CONTROL DE LA PETICION, ESTE PERMITE ALMACENAR LAS IMAGENES
                            //POR ENDE SE PUEDE QUITAL LA IMAGEN DE LA BASE DE DATOS

                            sharedViewModel.obtainFullListOfIdsStoredInDatabase().collectLatest { listIds ->

                                binding.btnAddToFavouritesActivate.setOnClickListener {
                                    sharedViewModel.addMovieToFavouriteMoviesDatabase(
                                        movie, System.currentTimeMillis()
                                    )
                                }

                                binding.btnAddToFavouritesInactive.setOnClickListener {
                                    sharedViewModel.deleteMovieFromDatabaseInDetailFragment(movie.id)
                                }

                                when{
                                    listIds.contains(movie.id) -> {
                                        binding.btnAddToFavouritesActivate.visibility = View.INVISIBLE
                                        binding.btnAddToFavouritesInactive.visibility = View.VISIBLE
                                    }
                                    else -> {
                                        binding.btnAddToFavouritesActivate.visibility = View.VISIBLE
                                        binding.btnAddToFavouritesInactive.visibility = View.INVISIBLE
                                    }
                                }
                            }

                        }
                    }

                }
            }

        }


        binding.icBack.setOnClickListener {
            //goBackToFragmentIntermediateMovies()
        }

        //val alto = binding.layoutDetailOfMovieLoader.tvOverview.layoutParams.height
        //binding.layoutDetailOfMovieLoader.tvOverview.maxHeight = 200

        //TODO OBTAIN THE SIZE OF THE OVERVIEW Y CALCULAR CON ESTO EL TAMANO TOTAL QUE TENDRIAN SEPARADOS
        binding.layoutDetailOfMovieLoader.tvOverview.setOnClickListener {
            /*val params = it.layoutParams
            it.layoutParams = params*/
            it.minimumHeight = 900
        }


    }

    /*private fun goBackToFragmentIntermediateMovies() {
        val action = DetailMovieFragmentDirections.actionDetailMovieFragmentToIntermediateDetailFragment(
            id = 0,
            position = recorderPosition
        )
        Timber.d("Position in func. Back Detail: $recorderPosition")
        findNavController().navigate(action)
    }*/

    override fun onDetach() {
        super.onDetach()
        //SI EL MANEJO DE ALGUN SUSPEND AQUI DENTRO ES MUUY PESADO Y HAY POSIBILIDAD DE QUE
        //ESTE CONSUMA MUCHO SI SE HACE CONSTANTEMENTE
        //PUEDE HACERSE AQUI, Y QUE SE HAGA SOLO EL ESTADO FINAL DE LA ACCION
        //SI SE DESEA HACER UN CALCULO AQUI LUEGO DE RECIEN DESTRUIDO SE HACE AQUI
    }





}