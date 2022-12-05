package com.example.everypractice.ui.search

import android.os.*
import android.view.*
import android.view.inputmethod.*
import android.widget.*
import androidx.activity.*
import androidx.fragment.app.*
import androidx.lifecycle.*
import androidx.navigation.fragment.*
import com.example.everypractice.data.RequestNetStatus.*
import com.example.everypractice.databinding.*
import com.example.everypractice.helpers.extensions.*
import com.example.everypractice.ui.*
import dagger.hilt.android.*
import kotlinx.coroutines.flow.*
import timber.log.*

@AndroidEntryPoint
class SearchResultFragment : Fragment() {

    private var _binding: FragmentSearchResultBinding? = null
    private val binding get() = _binding!!

    private lateinit var movieSearch: String

    private val sharedViewModel: MovieViewModel by activityViewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments.let {
            movieSearch = it!!.getString("movie").toString()
        }
        requireActivity().onBackPressedDispatcher.addCallback(this) {
            goBackToNavigationSearch()
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentSearchResultBinding.inflate(layoutInflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        //Coloca el extra de la busqueda en el editText
        binding.tfSearch.setText(movieSearch, TextView.BufferType.SPANNABLE)
        //Boton enter al teclado hara la busqueda
        binding.tfSearch.onSearch { onClickSearchMovie() }
        //Context que se manda al adapter y asi poder hacer el efecto Glass al recycler
        val context = requireContext()

        val adapter = MovieSearchAdapter(
            context
        ) { element, position, listSize ->
            goToIntermediateDetail(element.id, position, listSize)
        }

        lifecycleScope.launchWhenStarted {
            viewLifecycleOwner.repeatOnLifecycle(Lifecycle.State.STARTED) {
                sharedViewModel.showListFromSearch().collectLatest {
                    when (it.state) {
                        LOADING -> {
                            binding.rvMovieSearch.visibility = View.GONE
                            binding.layoutShimmerNotificationsLoader.shimmerLoader.visible()
                        }
                        ERROR -> {
                            binding.rvMovieSearch.visibility = View.GONE
                            binding.layoutShimmerNotificationsLoader.shimmerLoader.gone()
                        }
                        DONE -> {
                            binding.layoutShimmerNotificationsLoader.shimmerLoader.gone()
                            binding.rvMovieSearch.visibility = View.VISIBLE
                            adapter.submitList(it.data.results)
                            binding.rvMovieSearch.adapter = adapter
                        }
                    }
                }
            }
        }
    }


    private fun onClickSearchMovie() {
        var entrySearchMovie = ""
        requireActivity().runOnUiThread {
            entrySearchMovie = binding.tfSearch.text.toString()
            Timber.d("getText from ui: $entrySearchMovie")
        }
        goToBrowserMovie(entrySearchMovie)
    }

    private fun goToBrowserMovie(entry: String) {
        sharedViewModel.sendPetitionSearchMovie(entry)
    }

    private fun EditText.onSearch(callback: () -> Unit) {
        setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                if (binding.tfSearch.text.toString().trim().isNotEmpty()) {
                    callback.invoke()
                }
                return@setOnEditorActionListener true
            } else false
        }
    }

    private fun goToIntermediateDetail(id: Int, position: Int, listSize: Int) {
        onClickMovieAndSendPetition(id)

        val action = SearchResultFragmentDirections
            .actionSearchMoviesFragmentToIntermediateDetailFragment(id = id, position = position)
        findNavController().navigate(action)
    }

    private fun onClickMovieAndSendPetition(id: Int) {
        sharedViewModel.sendPetitionToGetMovieDetails(id)
    }

    private fun goBackToNavigationSearch() {
        val action = SearchResultFragmentDirections.toNavigationSearch()
        findNavController().navigate(action)
    }


}