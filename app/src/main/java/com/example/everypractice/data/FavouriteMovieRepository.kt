package com.example.everypractice.data

import androidx.annotation.WorkerThread
import com.example.everypractice.data.database.DaoDatabaseFavouriteMovie
import com.example.everypractice.data.database.DatabaseFavouriteMovie
import com.example.everypractice.data.database.asDomainModel
import com.example.everypractice.data.models.*
import com.example.everypractice.data.red.MovieApi
import com.example.everypractice.data.red.network.models.NetworkMultiSearchContainer
import com.example.everypractice.data.red.network.models.asTemporaryDetailDomainModel
import com.example.everypractice.data.red.network.models.asTemporaryDomainModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class FavouriteMovieRepository(private val daoDatabaseFavouriteMovie: DaoDatabaseFavouriteMovie) {

    /*----DATA BASE----*/

    val allListFavouriteMoviesFromDatabase: Flow<List<PermanentFavouriteMovies>> =
        daoDatabaseFavouriteMovie.getAllListDatabaseFavouriteMovies(true).map {
            it.asDomainModel()
        }

    val allListFavouriteUnSaveMoviesFromDatabase: Flow<List<PermanentFavouriteMovies>> =
        daoDatabaseFavouriteMovie.getAllListDatabaseFavouriteMovies(false).map {
            it.asDomainModel()
        }

    val randomFOURFavouriteMoviesFromDatabase: Flow<List<PermanentFavouriteMovies>> =
        daoDatabaseFavouriteMovie.getAllListDatabaseFavouriteMovies(true).map {
            it.asDomainModel().shuffled().take(4)
        }

    val fullListOfIdsStoredInDatabase: Flow<List<Int>> =
        daoDatabaseFavouriteMovie.getListOfIdStoredInDatabase()

    fun obtainFavouriteMovieByIdFromDatabase(id: Int): Flow<DatabaseFavouriteMovie> {
        return daoDatabaseFavouriteMovie.getDatabaseFavouriteMovieById(id)
    }

    @WorkerThread
    suspend fun insert(
        temporaryDetailMovie: TemporaryDetailMovie,
        timestamp: Long,
    ) {
        daoDatabaseFavouriteMovie.insert(temporaryDetailMovie.asDatabaseModel(timestamp))
    }

    @WorkerThread
    suspend fun delete(
        permanentFavouriteMovies: PermanentFavouriteMovies,
    ) {
        daoDatabaseFavouriteMovie.delete(permanentFavouriteMovies.asDatabaseModel())
    }

    @WorkerThread
    suspend fun customDeleteWithId(
        id: Int,
    ) {
        daoDatabaseFavouriteMovie.customDeleteWithIdFromDetails(id)
    }

    @WorkerThread
    suspend fun customUpdateWithIdAndNewStateSaved(
        saved: Boolean, id: Int
    ){
        daoDatabaseFavouriteMovie.updateSaveStateInDatabase(saved, id)
    }

    /*----NETWORK----*/

    /**
     * Function that gets detail's object from petition and return it as TemporaryDetailDomainModel
     * */
    suspend fun obtainDetailFromMovieWithId(id: Int): TemporaryDetailMovie {
        return MovieApi.retrofitService.getDetailsMovieWithGivenId(id)
            .asTemporaryDetailDomainModel()
    }

    /**
     * Function that gets search object from petition and return it as TemporaryDomainModel
     * */
    suspend fun obtainListOfMoviesFromSearchWithWord(movieName: String): TemporarySearchMovie {
        return MovieApi.retrofitService.getListMoviesFromSearchWithWord(movieName)
            .asTemporaryDomainModel()
    }

    /**
     * Function that gets popular object from petition and return it as TemporaryDomainModel
     */
    suspend fun obtainListOfPopularMovies(): TemporaryPopularMovie {
        return MovieApi.retrofitService.getListPopularMovies().asTemporaryDomainModel()
    }

    /**
     * Function that get Staff from a petition and return it as  TemporaryStaffModel
     */
    suspend fun obtainStaffOfAMovieWithId(idMovie: Int): List<TemporaryStaffMovie> {
        return MovieApi.retrofitService.getStaffWithGivenId(idMovie).cast.asTemporaryDomainModel()
    }

    //TODO CAMBIAR LOS PARAMETROS POR DEFECTO A QUE SE POUEDA PEDIR DESDE CENTRAL Y NO DESDE API
    /**
     *  Function that get Images Object from a petition and return it as TemporaryImagesMovie
     */
    suspend fun obtainImagesESFromMovieWithGivenId(idMovie: Int, language: String): TemporaryImageMovie {
        return MovieApi.retrofitService.getImageMovieWithGivenId(idMovie, language).asTemporaryDomainModel()
    }

    suspend fun obtainMultiSEARCHWithWord(searchWord: String): NetworkMultiSearchContainer {
        return MovieApi.retrofitService.getMultiSEARCHWithWord(searchWord)
    }

}