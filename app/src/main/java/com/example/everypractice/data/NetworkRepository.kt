package com.example.everypractice.data

import com.example.everypractice.data.models.*
import com.example.everypractice.data.red.*
import com.example.everypractice.data.red.network.models.*
import com.example.everypractice.utils.Result.Success
import kotlinx.coroutines.flow.*
import javax.inject.*

class NetworkRepository @Inject constructor(
    private val movieApiService: MovieApiService
) {

    /*----NETWORK----*/

    /**
     * Function that gets detail's object from petition and return it as TemporaryDetailDomainModel
     * */
    suspend fun obtainDetailFromMovieWithId(id: Int): TemporaryDetailMovie {
        return movieApiService.getDetailsMovieWithGivenId(id)
            .asTemporaryDetailDomainModel()
    }

    /**
     * Function that gets search object from petition and return it as TemporaryDomainModel
     * */
    suspend fun obtainListOfMoviesFromSearchWithWord(movieName: String): TemporarySearchMovie {
        return movieApiService.getListMoviesFromSearchWithWord(movieName)
            .asTemporaryDomainModel()
    }

    /**
     * Function that gets popular object from petition and return it as TemporaryDomainModel
     */
    suspend fun obtainListOfPopularMovies(): Flow<com.example.everypractice.utils.Result<TemporaryPopularMovie>> {
        return flow { emit(Success(movieApiService.getListPopularMovies().asTemporaryDomainModel())) }
    }

    /**
     * Function that get Staff from a petition and return it as  TemporaryStaffModel
     */
    suspend fun obtainStaffOfAMovieWithId(idMovie: Int): List<TemporaryStaffMovie> {
        return movieApiService.getStaffWithGivenId(idMovie).cast.asTemporaryDomainModel()
    }

    //TODO CAMBIAR LOS PARAMETROS POR DEFECTO A QUE SE POUEDA PEDIR DESDE CENTRAL Y NO DESDE API
    /**
     *  Function that get Images Object from a petition and return it as TemporaryImagesMovie
     */
    suspend fun obtainImagesESFromMovieWithGivenId(
        idMovie: Int,
        language: String
    ): TemporaryImageMovie {
        return movieApiService.getImageMovieWithGivenId(idMovie, language)
            .asTemporaryDomainModel()
    }

    suspend fun obtainMultiSEARCHWithWord(searchWord: String): NetworkMultiSearchContainer {
        return movieApiService.getMultiSEARCHWithWord(searchWord)
    }


}