package com.example.everypractice.ui.favourite

import androidx.lifecycle.*
import com.example.everypractice.data.domain.favourite.*
import com.example.everypractice.utils.*
import dagger.hilt.android.lifecycle.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import javax.inject.*

@HiltViewModel
class FavouriteViewModel @Inject constructor(
    loadStoredMoviesUseCase: LoadStoredMoviesUseCase,
    loadSeenMoviesUseCase: LoadSeenMoviesUseCase,
    private val updateStatusOfMovieActionUseCase: UpdateStatusOfMovieActionUseCase
) : ViewModel() {


    /**
     * Return the FlowList of favourite movies from Database STATE STORED
     */
    val showListOfMoviesStored = loadStoredMoviesUseCase(Unit).map {
        it.successOr(emptyList())
    }

    /**
     *  Return the FlowList of favourite movies from Database STATE SEEN
     */
    val showListOfSeenMovies = loadSeenMoviesUseCase(Unit).map {
        it.successOr(emptyList())
    }

    /**
     * Function that update tha SAVED Status from a Given IDMovie
     */
    fun moveToSeenOrStoredList(saved: Boolean, id: Int){
        viewModelScope.launch {
            updateStatusOfMovieActionUseCase(UpdateSource(saved, id))
        }
    }

}