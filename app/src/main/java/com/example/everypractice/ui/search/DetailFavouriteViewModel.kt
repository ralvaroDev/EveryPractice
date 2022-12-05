package com.example.everypractice.ui.search

import androidx.lifecycle.*
import com.example.everypractice.data.domain.detailfav.*
import com.example.everypractice.data.models.*
import com.example.everypractice.utils.*
import dagger.hilt.android.lifecycle.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import javax.inject.*

@HiltViewModel
class DetailFavouriteViewModel @Inject constructor(
    loadListOfIdsStoredUseCase: LoadListOfIdsStoredUseCase,
    private val deleteMovieActionUseCase: DeleteMovieActionUseCase,
    private val saveMovieActionUseCase: SaveMovieActionUseCase
) : ViewModel() {

    /**
     * Return a FlowList with the ID's of movies stored in Room
     */
    val showListOfIdsStored = loadListOfIdsStoredUseCase(Unit).map {
        it.successOr(emptyList())
    }

    /**
     * Function that delete a Movie from the database in the DetailFragment
     */
    fun unSaveMovie(movieId: Int){
        viewModelScope.launch {
            deleteMovieActionUseCase(movieId)
        }
    }

    /**
     * Function that add a Movie to Database
     */
    fun saveMovie(movie: TemporaryDetailMovie, timestamp: Long){
        viewModelScope.launch {
            saveMovieActionUseCase(SaveSource(movie, timestamp))
        }
    }

}