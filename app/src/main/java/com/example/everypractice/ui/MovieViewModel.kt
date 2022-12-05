package com.example.everypractice.ui

import androidx.lifecycle.*
import com.example.everypractice.data.domain.search.*
import dagger.hilt.android.lifecycle.*
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.*
import timber.log.*
import javax.inject.*

@HiltViewModel
class MovieViewModel @Inject constructor(
    private  val loadMultiSearchResultUseCase: LoadMultiSearchResultUseCase,
    private val loadPopularMoviesUseCase: LoadPopularMoviesUseCase,
    private val loadMovieDetailsUseCase: LoadMovieDetailsUseCase,
    private val loadMovieStaffUseCase: LoadMovieStaffUseCase,
    private val loadMovieImagesUseCase: LoadMovieImagesUseCase,
    private val loadSearchResultUseCase: LoadSearchResultUseCase
) : ViewModel() {


    /*---------SPACE TO DECLARE ALL VARIABLES TO USE----------*/

    /**
     * This contains the container POPULAR MOVIES from a petition as TemporaryPopularDomain
     */
    private var _resultOfPopularMovies = MutableStateFlow(PopularSource())

    /**
     * This contains the container LIST SEARCH RESULTS from a petition as TemporarySearchDomain
     */
    private val _resultOfSearchResultMovies = MutableStateFlow(SearchResultSource())

    /**
     * This var contains the object DETAIL OF A MOVIE from a petition as TemporaryDetailMovie
     * */
    private val _resultOfDetailMovie = MutableStateFlow(DetailSource())

    /**
     * This var contains the list of a STAFF from a Movie from a petition as TemporaryStaffModel
     */
    private val _resultOfStaffMovie = MutableStateFlow(StaffSource())

    /**
     * This var contains the object IMAGES from a Movie from a petition as TemporaryImageModel
     */
    private val _resultOfImagesMovie = MutableStateFlow(ImageSource())

    /*---------SPACE TO DECLARE ALL FUNCTIONS NET TO USE----------*/

    init {
        //DATO, el init debe de estar en esta posicion para evitar que result no exista
        sendPetitionToGetPopularMovies()
    }

    //TODO: UNDER TESTING
    fun obtainMultiSEARCHWithWord(word: String) {
        viewModelScope.launch {
            loadMultiSearchResultUseCase.downloadResultsOfMultiSearchFromServer(word).collectLatest {
                var multiResponse = it
                Timber.d("RESULT MULTI SEARCH: ----> ${multiResponse.data.results.toList()[2]}$")
            }

        }
    }

    //TODO ES MEJOR ESPECIFICAR EL DISPATCHER O SCOPE
    //TODO AQUI DEBE CUANDO SEA ERROR MANDAR NOTI DE MOSTRAR BOTON DE ACTUALIZAR ARRIBA Y ASI
    // PODER MANDAR NUEVAS PETICIONES Y TENER LOS DATOS

    /**
     * Function that only send petition POPULAR, update the status of it and save the results
     * */
    private fun sendPetitionToGetPopularMovies() {
        viewModelScope.launch {
            loadPopularMoviesUseCase.something().collectLatest {
                _resultOfPopularMovies.value = it
            }
        }
    }

    /**
     * Function that emit the ListOfPopularMovies saved
     */
    fun showListOfPopularMovie(): StateFlow<PopularSource> = _resultOfPopularMovies.asStateFlow()

    /**
     * Function that only send petition SEARCH, update the status of it and save the results
     * */
    fun sendPetitionSearchMovie(entry: String) {
        viewModelScope.launch {
            loadSearchResultUseCase.downloadResultsOfSearchFromServer(entry).collectLatest {
                _resultOfSearchResultMovies.value = it
            }
        }
    }

    /**
     * Function that emit the TemporaryListSearch saved
     */
    fun showListFromSearch(): StateFlow<SearchResultSource> =
        _resultOfSearchResultMovies.asStateFlow()

    /**
     * Function that send petition DETAILS, update the status and save the result
     */
    fun sendPetitionToGetMovieDetails(movieId: Int) {
        viewModelScope.launch {
            loadMovieDetailsUseCase.downLoadDetailOfMovieFromServer(movieId).collectLatest {
                _resultOfDetailMovie.value = it
            }
        }
    }

    /**
     *  Function that emit the TemporaryDetails saved
     */
    fun showMovieWithDetails(): StateFlow<DetailSource> = _resultOfDetailMovie.asStateFlow()

    /**
     * Function that send petition STAFF, update the status and save the result
     */
    fun sendPetitionToGetStaffFromMovieWithGivenId(movieId: Int) {
        viewModelScope.launch {
            loadMovieStaffUseCase.downloadStaffOfMovieFromServer(movieId).collectLatest {
                _resultOfStaffMovie.value = it
            }
        }
    }

    /**
     *  Function that emit the listOfStaffFromAMovie saved
     */
    fun showStaffFromAMovie(): StateFlow<StaffSource> = _resultOfStaffMovie.asStateFlow()

    /**
     * Function that send petition IMAGES, update the status and save the result
     */
    fun sendPetitionToGetImagesFromMovieWithGivenId(movieId: Int, language: String = "es") {
        viewModelScope.launch {
            loadMovieImagesUseCase.downloadImagesOfMovieFromServer(movieId, language)
                .collectLatest {
                    _resultOfImagesMovie.value = it
                }
        }
    }

    /**
     *  Function that emit the OBJECT IMAGES saved
     */
    fun showImagesFromMovie(): StateFlow<ImageSource> = _resultOfImagesMovie.asStateFlow()

    //TODO ESTO ES NECESARIO?
//convert search to string to send retrofit
    private fun convertWordToRetrofit(name: String): String {
        return name.trim { it <= ' ' }.replace(" ", "+", true)
    }

}