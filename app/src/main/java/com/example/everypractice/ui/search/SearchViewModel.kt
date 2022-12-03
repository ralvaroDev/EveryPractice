package com.example.everypractice.ui.search

import androidx.lifecycle.*
import com.example.everypractice.data.domain.search.*
import com.example.everypractice.data.models.*
import com.example.everypractice.utils.Result.*
import dagger.hilt.android.lifecycle.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import javax.inject.*

@HiltViewModel
class SearchViewModel @Inject constructor(
    private val popularMoviesActionUseCase: PopularMoviesActionUseCase
) : ViewModel(){

    init {
        setPopular()
    }

    private val _popularMovies = MutableStateFlow<com.example.everypractice.utils.Result<TemporaryPopularMovie>>(Loading)
    val popularMovies = _popularMovies.asStateFlow()

    private fun setPopular(){

        viewModelScope.launch {
            popularMoviesActionUseCase(Unit).collectLatest {
                when(it){
                    is Error -> {Error(it.exception)}
                    Loading -> {throw IllegalStateException()}
                    is Success -> _popularMovies.value = it
                }
            }

        }


    }

}