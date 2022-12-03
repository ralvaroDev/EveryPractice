package com.example.everypractice.data.red

import com.example.everypractice.data.models.*
import com.example.everypractice.data.red.network.models.*
import com.example.everypractice.di.ApiModule.API_KEY
import retrofit2.http.*

interface MovieApiService {

    //https://api.themoviedb.org/3/search/movie?api_key=21ff02d316f95915429ebe923b39a82c&query=Jack+Reacher
    @GET("search/movie")
    suspend fun getListMoviesFromSearchWithWord(
        @Query("query") movieName: String,
        @Query("page") page: Int = 1
    ): NetworkSearchMoviesContainer
    //DONE-USING

    //PATH ES PARA PASAR UN PARAMETRO A LA URL
    @GET("movie/{movie_id}")
    suspend fun getDetailsMovieWithGivenId(
        @Path(value = "movie_id") movieId: Int,
        @Query(value = "language") language: String = "es"
    ): NetworkMovieDetailedContainer
    //DONE-USING

    //TODO REVISAR QUE NO SE HAGAN PETICIONES DE MAS
    @GET("movie/{movie_id}/credits")
    suspend fun getStaffWithGivenId(
        @Path(value = "movie_id") movieId: Int
    ): NetworkStaffFromMovieContainer
    //DONE- USING

    @GET("movie/popular")
    suspend fun getListPopularMovies(
        @Query("page") page: Int = 1
    ): NetworkPopularMoviesContainer
    //DONE-USING


    @GET("movie/{movie_id}/images")
    suspend fun getImageMovieWithGivenId(
        @Path(value = "movie_id") movieId: Int,
        @Query("language") language: String
    ): NetworkImagesMovieContainer
    //DONE-PROCESS

    @GET("search/multi")
    suspend fun getMultiSEARCHWithWord(
        @Query("query") wordSearch: String,
    ): NetworkMultiSearchContainer
    //DONE-TESTING

    @GET("movie/now_playing")
    suspend fun getMoviesInTheatres(
        @Query(value = "language") language: String = "es",
        @Query(value = "page") page: Int = 1
    ): MoviesInTheatresGson
    //DONE-UN-USING

    @GET("movie/upcoming")
    suspend fun getUpcomingMoviesInTheatres(
        @Query(value = "language") language: String = "es",
        @Query(value = "page") page: Int = 1,
    ): MoviesInTheatresGson
    //DONE-UN-USING

    //HACER LAS COLECCIONES

    //EL QUERY AUTOMATICAMENTE METE EL &PAGE=4 AUTOMATICAMENTE CUANDO ES NECESARIO
    @GET("movie/popular?api_key=$API_KEY&page=5")
    suspend fun getListOfPopularMovies(@Query("page") page: Int): List<NetworkSearchMoviesContainer>
    //PROCESSING

    //ESTO SE USA EN CASO NO SEPAMOS EL TIPO DE URL
    /* @GET
     suspend fun getImagesMovieWithGivenId(@Url url:String) : DetailMovie*/
    //PROCESSING

    @POST("movie/{movie_id}/rating")
    suspend fun putRateMovie(
        @Body rateMovieRequest: RateMovieRequest,
        @Query("movie_id") movieId: String
    ): RateMovieResponse
    //PROCESSING

}