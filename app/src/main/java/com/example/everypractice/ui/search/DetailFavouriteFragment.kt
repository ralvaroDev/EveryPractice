package com.example.everypractice.ui.search

import android.content.*
import android.os.*
import android.view.*
import androidx.activity.*
import androidx.fragment.app.*
import androidx.lifecycle.*
import androidx.navigation.fragment.*
import coil.*
import com.example.everypractice.R
import com.example.everypractice.data.models.*
import com.example.everypractice.databinding.*
import com.example.everypractice.helpers.extensions.*
import com.example.everypractice.ui.*
import com.example.everypractice.utils.util.*
import dagger.hilt.android.*
import kotlinx.coroutines.flow.*
import timber.log.*

@AndroidEntryPoint
class DetailFavouriteFragment : Fragment() {

    private var savedId: String? = null

    private var movieId: Int = 0
    private var recorderPosition: Int = 0

    private val sharedViewModel: MovieViewModel by activityViewModels()

    private var _binding: FragmentDetailFavouriteBinding? = null
    private val binding get() = _binding!!


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            movieId = it.getInt("idmovie")
            recorderPosition = it.getInt("recorderposition")
        }
        val callback = requireActivity().onBackPressedDispatcher.addCallback(this) {
            if (recorderPosition == -1) {
                goBackToSearchFragment()
            } else if (recorderPosition == -2){
                goToFavouriteFragment(recorderPosition)
            } else if(recorderPosition == -3){
                goToFavouriteFragment(recorderPosition)
            }
            else goBackToFragmentIntermediateMovies()
        }
    }



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = FragmentDetailFavouriteBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.icBack.setOnClickListener {
            if (recorderPosition == -1) {
                goBackToSearchFragment()
            } else goBackToFragmentIntermediateMovies()
        }

        lifecycleScope.launchWhenStarted {
            sharedViewModel.requestMovieDetailStatus.collectLatest { status ->
                Timber.d("MovieDetailStatus in collector: $status")
                when (status) {

                    RequestMovieStatus.LOADING -> {
                        //binding.layoutShimmerNotificationsLoader.shimmerLoader.visible()
                    }
                    RequestMovieStatus.ERROR -> {
                        //binding.layoutShimmerNotificationsLoader.shimmerLoader.gone()
                    }
                    RequestMovieStatus.DONE -> {
                        //binding.layoutShimmerNotificationsLoader.shimmerLoader.gone()
                        Timber.d("MovieDetailStatus inside IF: $status")
                        sharedViewModel.showMovieWithDetails().collectLatest { movie ->
                            binding.layoutDetailOfFavouriteMovieLoader.apply {

                                val adapter = GenresInDetailAdapter(movie.genres)
                                rvGenresList.adapter = adapter

                                imgBackdrop.load(movie.backdropPathUrl) {
                                    error(R.drawable.ic_not_found)
                                }
                                tvTitleDetail.text = movie.movieTitle

                                tvRatingValue.text = String.format("%.1f", movie.voteAverage / 2)
                                rbTaringStart.rating = (movie.voteAverage / 2).toInt().toFloat()

                                tvOverview.text = movie.overview
                                tvNameColectionInDetail.text = movie.nameCollectionBelongsTo
                                imgBackgroundPathCollectionInDetails.load(movie.backgroundCollectionPathUrl)
                                if (movie.nameCollectionBelongsTo == "No name collection") {
                                    cardCollection.gone()
                                }
                                val year = movie.releaseDate.split("-")[0]
                                val genres = movie.genres.getTransformationIdsToGenres().take(2)
                                    .joinToString(", ")
                                tvPrimaryDetail.text =
                                    "$year | $genres  | ${getTimeToText(movie.runtime)}"

                            }

                            sharedViewModel.obtainFullListOfIdsStoredInDatabase()
                                .collectLatest { listIds ->
                                    binding.btnSave.setOnClickListener {
                                        sharedViewModel.addMovieToFavouriteMoviesDatabase(
                                            movie, System.currentTimeMillis()
                                        )
                                    }

                                    binding.btnUnsave.setOnClickListener {
                                        sharedViewModel.deleteMovieFromDatabaseInDetailFragment(
                                            movie.id)
                                    }

                                    when {
                                        listIds.contains(movie.id) -> {
                                            binding.btnSave.visibility = View.INVISIBLE
                                            binding.btnUnsave.visibility = View.VISIBLE
                                        }
                                        else -> {
                                            binding.btnSave.visibility = View.VISIBLE
                                            binding.btnUnsave.visibility = View.INVISIBLE
                                        }
                                    }
                                }

                        }
                    }

                }
            }

        }


        lifecycleScope.launchWhenStarted {
            sharedViewModel.requestStaffStatus.collectLatest { status ->
                when (status) {
                    RequestMovieStatus.LOADING -> {
                        //binding.layoutShimmerNotificationsLoader.shimmerLoader.visible()
                        binding.layoutDetailOfFavouriteMovieLoader.rvStaff.inVisible()
                        binding.layoutDetailOfFavouriteMovieLoader.tvTitleStaff.inVisible()

                    }
                    RequestMovieStatus.ERROR -> {
                        //binding.layoutShimmerNotificationsLoader.shimmerLoader.gone()
                        binding.layoutDetailOfFavouriteMovieLoader.rvStaff.gone()
                        binding.layoutDetailOfFavouriteMovieLoader.tvTitleStaff.gone()
                    }
                    RequestMovieStatus.DONE -> {
                        sharedViewModel.showStaffFromAMovie().collectLatest { listStaff ->

                            //TODO HACER UNMETODO PARA LIMPIAR STAFF SIN FOTO Y CORREGIR ALTURA DEL NOMBRE
                            val adapterStaff = StaffDetailsAdapter(listStaff) {
                                //TODO IMPLEMENT THE PROFILE OF AN ACTOR
                            }
                            binding.layoutDetailOfFavouriteMovieLoader.rvStaff.adapter =
                                adapterStaff
                        }
                        binding.layoutDetailOfFavouriteMovieLoader.rvStaff.visible()
                        binding.layoutDetailOfFavouriteMovieLoader.tvTitleStaff.visible()
                    }
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            sharedViewModel.requestImagesStatus.collectLatest { statusImages ->
                when (statusImages) {
                    RequestMovieStatus.LOADING -> {
                        binding.layoutDetailOfFavouriteMovieLoader.rvImages.inVisible()
                        binding.layoutDetailOfFavouriteMovieLoader.tvTitleImages.inVisible()
                    }
                    RequestMovieStatus.ERROR -> {
                        //binding.layoutShimmerNotificationsLoader.shimmerLoader.gone()
                        binding.layoutDetailOfFavouriteMovieLoader.rvImages.gone()
                        binding.layoutDetailOfFavouriteMovieLoader.tvTitleImages.gone()
                    }
                    RequestMovieStatus.DONE -> {
                        sharedViewModel.showImagesFromMovie().collectLatest {  listImages ->
                            Timber.d("Backdrops: ${listImages.backdrops.size}")
                            val allImages : MutableList<AllImages> = mutableListOf()
                            allImages.addAll(listImages.backdrops.toUnitedObject1())

                            allImages.addAll(listImages.posters.toUnitedObject2())
                            val adapterImages = RelatedImagesAdapter() {
                                //TODO IMPLEMENT SEE IMAGE IN FULL
                            }
                            adapterImages.submitList(allImages)
                            binding.layoutDetailOfFavouriteMovieLoader.rvImages.adapter = adapterImages
                            if (allImages.size != 0){
                                binding.layoutDetailOfFavouriteMovieLoader.rvImages.visible()
                                binding.layoutDetailOfFavouriteMovieLoader.tvTitleImages.visible()
                            }
                        }

                    }
                }
            }
        }


    }


    private fun goBackToSearchFragment() {
        val action = DetailFavouriteFragmentDirections.toNavigationSearch()
        findNavController().navigate(action)
    }

    private fun goToFavouriteFragment(recorder: Int) {
        findNavController().navigate(DetailFavouriteFragmentDirections.toNavigationFavourite(recorder))
    }


    private fun goBackToFragmentIntermediateMovies() {
        val action = DetailFavouriteFragmentDirections.toIntermediateDetails(
            id = 0,
            position = recorderPosition
        )
        Timber.d("Position in func. Back Detail: $recorderPosition")
        findNavController().navigate(action)
    }

}