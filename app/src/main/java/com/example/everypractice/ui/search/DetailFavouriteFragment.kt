package com.example.everypractice.ui.search

import android.os.*
import android.view.*
import androidx.activity.*
import androidx.fragment.app.*
import androidx.lifecycle.*
import androidx.navigation.fragment.*
import coil.*
import com.example.everypractice.R.drawable
import com.example.everypractice.data.RequestNetStatus.*
import com.example.everypractice.data.models.*
import com.example.everypractice.databinding.*
import com.example.everypractice.helpers.extensions.*
import com.example.everypractice.ui.*
import com.example.everypractice.utils.util.*
import dagger.hilt.android.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import timber.log.*

@AndroidEntryPoint
class DetailFavouriteFragment : Fragment() {

    private var savedId: String? = null

    private var movieId: Int = 0
    private var recorderPosition: Int = 0

    private val sharedViewModel: MovieViewModel by activityViewModels()
    private val detailViewModel: DetailFavouriteViewModel by viewModels()

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
            } else if (recorderPosition == -2) {
                goToFavouriteFragment(recorderPosition)
            } else if (recorderPosition == -3) {
                goToFavouriteFragment(recorderPosition)
            } else goBackToFragmentIntermediateMovies()
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

        lifecycleScope.launch {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                sharedViewModel.showMovieWithDetails().collectLatest {
                    when (it.state) {
                        LOADING -> {
                            Timber.d("Loading BRO")
                        }
                        ERROR -> {}
                        DONE -> {
                            //TODO CORREGIR ESTE MOVIE
                            Timber.d("DATOS IN DETAIL: ${it.data}")
                            val movie = it.data
                            binding.layoutDetailOfFavouriteMovieLoader.apply {

                                val adapter = GenresInDetailAdapter(movie.genres)
                                rvGenresList.adapter = adapter

                                imgBackdrop.load(it.data.backdropPathUrl) {
                                    error(drawable.ic_not_found)
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

                            //TODO ESTE SI ES SOLO DE AQYU ASI QUE REQUIERE SU PRIPIO FRAGMENT
                            detailViewModel.showListOfIdsStored
                                .collectLatest { listIds ->
                                    binding.btnSave.setOnClickListener {
                                        detailViewModel.saveMovie(movie, System.currentTimeMillis())
                                    }
                                    binding.btnUnsave.setOnClickListener {
                                        detailViewModel.unSaveMovie(movieId)
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
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                sharedViewModel.showStaffFromAMovie().collectLatest {
                    when (it.state) {
                        LOADING -> {
                            //binding.layoutShimmerNotificationsLoader.shimmerLoader.visible()
                            binding.layoutDetailOfFavouriteMovieLoader.rvStaff.inVisible()
                            binding.layoutDetailOfFavouriteMovieLoader.tvTitleStaff.inVisible()

                        }
                        ERROR -> {
                            //binding.layoutShimmerNotificationsLoader.shimmerLoader.gone()
                            binding.layoutDetailOfFavouriteMovieLoader.rvStaff.gone()
                            binding.layoutDetailOfFavouriteMovieLoader.tvTitleStaff.gone()
                        }
                        DONE -> {
                            val staff = it.data
                            val adapterStaff = StaffDetailsAdapter(staff.filter { element ->
                                element.profilePath.length != 35
                            }) {
                                //TODO IMPLEMENT THE PROFILE OF AN ACTOR
                            }
                            binding.layoutDetailOfFavouriteMovieLoader.rvStaff.adapter =
                                adapterStaff
                            binding.layoutDetailOfFavouriteMovieLoader.rvStaff.visible()
                            binding.layoutDetailOfFavouriteMovieLoader.tvTitleStaff.visible()
                        }
                    }
                }
            }
        }

        lifecycleScope.launchWhenStarted {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                sharedViewModel.showImagesFromMovie().collectLatest {
                    when (it.state) {
                        LOADING -> {
                            binding.layoutDetailOfFavouriteMovieLoader.rvImages.inVisible()
                            binding.layoutDetailOfFavouriteMovieLoader.tvTitleImages.inVisible()
                        }
                        ERROR -> {
                            //binding.layoutShimmerNotificationsLoader.shimmerLoader.gone()
                            binding.layoutDetailOfFavouriteMovieLoader.rvImages.gone()
                            binding.layoutDetailOfFavouriteMovieLoader.tvTitleImages.gone()
                        }
                        DONE -> {
                            val images = it.data
                            val allImages: MutableList<AllImages> = mutableListOf()
                            allImages.addAll(images.backdrops.toUnitedObject1())
                            allImages.addAll(images.posters.toUnitedObject2())
                            val adapterImages = RelatedImagesAdapter() {
                                //TODO IMPLEMENT SEE IMAGE IN FULL
                            }
                            adapterImages.submitList(allImages)
                            binding.layoutDetailOfFavouriteMovieLoader.rvImages.adapter =
                                adapterImages
                            if (allImages.size != 0) {
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
        findNavController().navigate(
            DetailFavouriteFragmentDirections.toNavigationFavourite(
                recorder
            )
        )
    }


    private fun goBackToFragmentIntermediateMovies() {
        val action = DetailFavouriteFragmentDirections.toIntermediateDetails(
            id = 0, position = recorderPosition
        )
        Timber.d("Position in func. Back Detail: $recorderPosition")
        findNavController().navigate(action)
    }

}