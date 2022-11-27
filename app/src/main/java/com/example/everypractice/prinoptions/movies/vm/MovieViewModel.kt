package com.example.everypractice.prinoptions.movies.vm

import androidx.lifecycle.*
import com.example.everypractice.prinoptions.movies.data.domain.*
import com.example.everypractice.prinoptions.movies.data.models.*
import com.example.everypractice.prinoptions.movies.repository.FavouriteMovieRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import timber.log.Timber

enum class RequestMovieStatus { LOADING, ERROR, DONE }


class MovieViewModel(
    private val repository: FavouriteMovieRepository
) : ViewModel() {

    init {
        sendPetitionPopularMovieAndShowIt()
    }

    /*---------SPACE TO DECLARE ALL VARIABLES TO USE----------*/

    /**
     * This contains the container LIST SEARCH RESULTS from a petition as TemporarySearchDomain
     */
    private var resultsListSearchMovie = TemporarySearchMovie()

    /**
     * This contains the container POPULAR MOVIES from a petition as TemporaryPopularDomain
     */
    private var resultsPopularMovie = TemporaryPopularMovie()

    /**
     * This var contains the object DETAIL OF A MOVIE from a petition as TemporaryDetailMovie
     */
    private var resultOfDetailMovie = TemporaryDetailMovie()

    /**
     * This var contains the list of a STAFF from a Movie from a petition as TemporaryStaffModel
     */
    private var listOfStaffFromMovie = listOf<TemporaryStaffMovie>()

    /**
     * This var contains the object IMAGES from a Movie from a petition as TemporaryImageModel
     */
    private var resultOfImagesFromMovie = TemporaryImageMovie(id = 0)

    /*---------SPACE TO DECLARE ALL STATUS TO USE----------*/

    /**
     * Shows the status of the SEARCH PETITION
     * */
    private val _requestMovieSearchStatus = MutableStateFlow(RequestMovieStatus.LOADING)
    val requestMovieSearchStatus = _requestMovieSearchStatus.asStateFlow()

    /**
     * Shows the status of the POPULAR PETITION
     */
    private val _requestPopularStatus = MutableStateFlow(RequestMovieStatus.LOADING)
    val requestPopularStatus = _requestPopularStatus.asStateFlow()

    /**
     * Shows the status of the DETAIL OF MOVIE PETITION
     * */
    private val _requestMovieDetailStatus = MutableStateFlow(RequestMovieStatus.LOADING)
    val requestMovieDetailStatus = _requestMovieDetailStatus.asStateFlow()

    /**
     * Shows the status of the STAFF OF MOVIE PETITION
     */
    private val _requestStaffStatus = MutableStateFlow(RequestMovieStatus.LOADING)
    val requestStaffStatus = _requestStaffStatus.asStateFlow()

    /**
     * Shows the status of the IMAGES OF MOVIE PETITION
     */
    private val _requestImagesStatus = MutableStateFlow(RequestMovieStatus.LOADING)
    val requestImagesStatus = _requestImagesStatus.asStateFlow()


    /*---------SPACE TO DECLARE ALL FUNCTIONS NET TO USE----------*/

    fun obtainMultiSEARCHWithWord(word: String){
        viewModelScope.launch {
            try {
                var multiResponse = repository.obtainMultiSEARCHWithWord(word)
                Timber.d("RESULT MULTI SEARCH: ----> ${multiResponse.results.toList()[2]}$")
            } catch (e:Exception){
                Timber.d("Error with MULTI SEARCH: ${e.message}")
            }
        }
    }


    /**
     *  Function that send petition POPULAR, update the status of it and save the results
     */
    private fun sendPetitionPopularMovieAndShowIt() {
        Timber.d("Petition POPULAR received")
        viewModelScope.launch {
            _requestPopularStatus.value = RequestMovieStatus.LOADING
            Timber.d("Status POPULAR emitted")
            try {
                resultsPopularMovie = repository.obtainListOfPopularMovies()
                Timber.d("Object POPULAR received and saved in the state")
                _requestPopularStatus.value = RequestMovieStatus.DONE
                Timber.d("Status POPULAR done emitted")
            } catch (e: Exception) {
                _requestPopularStatus.value = RequestMovieStatus.ERROR
                Timber.d("Error sending petition POPULAR: ${e.message} \n MovieStatus In Catch: ERROR")
            }
        }
    }

    /**
     * Function that emit the ListOfPopularMovies saved
     */
    fun showListOfPopularMovie(): Flow<TemporaryPopularMovie> {
        return flow {
            emit(resultsPopularMovie)
        }
    }

    /**
     * Function that only send petition SEARCH, update the status of it and save the results
     * */
    fun sendPetitionSearchMovie(entry: String) {
        Timber.d("Petition SEARCH received")
        viewModelScope.launch {
            _requestMovieSearchStatus.value = RequestMovieStatus.LOADING
            Timber.d("Status emitted")
            try {
                resultsListSearchMovie = repository.obtainListOfMoviesFromSearchWithWord(entry)
                Timber.d("Object received and saved in the state")
                _requestMovieSearchStatus.value = RequestMovieStatus.DONE
                Timber.d("Status done emitted")
            } catch (e: Exception) {
                _requestMovieSearchStatus.value = RequestMovieStatus.ERROR
                Timber.d("Error sending petition SEARCH: ${e.message} \n MovieStatus In Catch: ERROR")
            }
        }
    }

    /**
     * Function that emit the TemporaryListSearch saved
     */
    fun showListFromSearch(): Flow<TemporarySearchMovie> {
        return flow {
            try {
                Timber.d("Emit flow with movies")
                emit(resultsListSearchMovie)
            } catch (e: Exception) {
                Timber.d("Error getting list from movies: ${e.message}")
            }
        }
    }

    /**
     * Function that send petition DETAILS, update the status and save the result
     */
    fun sendPetitionToGetMovieDetails(entryId: Int) {
        Timber.d("Petition DETAIL Id: $entryId received")
        viewModelScope.launch {
            _requestMovieDetailStatus.value = RequestMovieStatus.LOADING
            Timber.d("Status DETAIL emitted and sending Petition Id to Internet")
            try {
                resultOfDetailMovie = repository.obtainDetailFromMovieWithId(entryId)
                Timber.d("Element from petition with ID received and saved in the state")
                _requestMovieDetailStatus.value = RequestMovieStatus.DONE
                Timber.d("Status DETAIL done emitted")
            } catch (e: Exception) {
                _requestMovieDetailStatus.value = RequestMovieStatus.ERROR
                Timber.d("Error sending DETAIL petition: ${e.message} \n MovieDetailStatus In Catch: ERROR")
            }
        }
    }

    /**
     *  Function that emit the TemporaryDetails saved
     */
    fun showMovieWithDetails(): Flow<TemporaryDetailMovie> {
        return flow {
            try {
                Timber.d("Emit flow with details")
                emit(resultOfDetailMovie)
            } catch (e: Exception) {
                Timber.d("Error getting the movie with Detail: ${e.message}")
            }
        }
    }

    /**
     * Function that send petition STAFF, update the status and save the result
     */
    fun sendPetitionToGetStaffFromMovieWithGivenId(movieId: Int) {
        Timber.d("Petition STAFF Id: $movieId received")
        viewModelScope.launch {
            _requestStaffStatus.value = RequestMovieStatus.LOADING
            Timber.d("Status STAFF emitted and sending Petition Id to Internet")
            try {
                listOfStaffFromMovie = repository.obtainStaffOfAMovieWithId(movieId)
                Timber.d("Element from petition STAFF with ID received and saved in the state")
                _requestStaffStatus.value = RequestMovieStatus.DONE
                Timber.d("Status STAFF done emitted")
            } catch (e: Exception) {
                _requestStaffStatus.value = RequestMovieStatus.ERROR
                Timber.d("Error sending STAFF petition: ${e.message} \n requestStaffStatus In Catch: ERROR")
            }
        }
    }

    /**
     *  Function that emit the listOfStaffFromAMovie saved
     */
    fun showStaffFromAMovie(): Flow<List<TemporaryStaffMovie>> {
        return flow {
            try {
                Timber.d("Emit flow with STAFF")
                emit(listOfStaffFromMovie)
            } catch (e: Exception) {
                Timber.d("Error getting the STAFF: ${e.message}")
            }
        }
    }

    /**
     * Function that send petition IMAGES, update the status and save the result
     */
    fun sendPetitionToGetImagesFromMovieWithGivenId(movieId: Int, language: String = "es") {
        Timber.d("Petition IMAGES Id: $movieId received")
        viewModelScope.launch {
            _requestImagesStatus.value = RequestMovieStatus.LOADING
            Timber.d("Status IMAGES emitted and sending Petition Id to Internet")
            try {
                resultOfImagesFromMovie = repository.obtainImagesESFromMovieWithGivenId(movieId, language)
                Timber.d("Element from petition IMAGES with ID received and saved in the state")
                _requestImagesStatus.value = RequestMovieStatus.DONE
                Timber.d("Status IMAGES done emitted")
            } catch (e: Exception) {
                _requestImagesStatus.value = RequestMovieStatus.ERROR
                Timber.d("Error sending IMAGES petition: ${e.message} \n requestStaffStatus In Catch: ERROR")
            }
        }
    }

    /**
     *  Function that emit the OBJECT IMAGES saved
     */
    fun showImagesFromMovie(): Flow<TemporaryImageMovie>{
        return flow {
            try {
                Timber.d("Emit flow with IMAGES")
                emit(resultOfImagesFromMovie)
            } catch (e: Exception) {
                Timber.d("Error getting the IMAGES: ${e.message}")
            }
        }
    }

    //--------------------DATA FROM DATABASE----------------------

    /**
     * Function that add a Movie to Database
     */
    fun addMovieToFavouriteMoviesDatabase(
        temporaryDetailMovie: TemporaryDetailMovie,
        timestamp: Long,
    ) {
        viewModelScope.launch(Dispatchers.IO) {
            Timber.d("add ${temporaryDetailMovie.movieTitle} to database")
            repository.insert(temporaryDetailMovie, timestamp)
        }
    }

    /**
     * Function that delete a Movie from the database
     */
    fun deleteMovieFromDatabase(
        permanentFavouriteMovies: PermanentFavouriteMovies,
    ) {
        viewModelScope.launch {
            Timber.d("delete ${permanentFavouriteMovies.movieTitle} to database")
            repository.delete(permanentFavouriteMovies)
        }
    }

    /**
     * Function that delete a Movie from the database in the DetailFragment
     */
    fun deleteMovieFromDatabaseInDetailFragment(id: Int) {
        viewModelScope.launch {
            Timber.d("delete ${id} to database")
            repository.customDeleteWithId(id)
        }
    }

    /**
     * Function that update tha SAVED Status from a Given IDMovie
     */
    fun updateSavedStatusFromDatabaseWithId(saved: Boolean,id: Int) {
        viewModelScope.launch {
            repository.customUpdateWithIdAndNewStateSaved(saved, id)
        }
    }

    /**
     * Return the FlowList of favourite movies from Database STATE SAVED
     * */
    fun getFavouriteMovies(): Flow<List<PermanentFavouriteMovies>> {
        return repository.allListFavouriteMoviesFromDatabase
    }

    /**
     *  Return the FlowList of favourite movies from Database STATE UN-SAVED
     */
    fun getFavouriteSeenMovies(): Flow<List<PermanentFavouriteMovies>> {
        return repository.allListFavouriteUnSaveMoviesFromDatabase
    }

    /**
     * Return the list of Ids stored in Database
     */
    fun obtainFullListOfIdsStoredInDatabase(): Flow<List<Int>> {
        return repository.fullListOfIdsStoredInDatabase
    }

    /**
     * Return a FlowList with only 5 random elements from favourite movies Database
     */
    fun get4FavouriteRecommendedMovies(): Flow<List<PermanentFavouriteMovies>> {
        return repository.randomFOURFavouriteMoviesFromDatabase
    }

    //TODO ESTO ES NECESARIO?
    //convert search to string to send retrofit
    private fun convertWordToRetrofit(name: String): String {
        return name.trim { it <= ' ' }.replace(" ", "+", true)
    }

}


@Suppress("UNCHECKED_CAST")
class FavouriteMoviesViewModelFactory(
    private val movieRepository: FavouriteMovieRepository
) :
    ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MovieViewModel::class.java)) {
            return MovieViewModel(movieRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }

}