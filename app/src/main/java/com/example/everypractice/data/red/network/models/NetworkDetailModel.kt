package com.example.everypractice.data.red.network.models

import com.example.everypractice.consval.BASE_BACKGROUND_IMAGE_URL
import com.example.everypractice.consval.BASE_POSTER_IMAGE_URL
import com.example.everypractice.data.database.DatabaseFavouriteMovie
import com.example.everypractice.data.domain.TemporaryDetailMovie
import com.google.gson.annotations.SerializedName

data class NetworkMovieDetailedContainer(
    val adult: Boolean? = null,
    @SerializedName("backdrop_path") val backdropPath: String? = null,
    //aqui no se pone lista porque siempre enviaran uno solo, asi que ponemos que nos devuelve solo el tipo de dato
    @SerializedName("belongs_to_collection")
    val belongsToCollection: NetworkBelongsToCollectionContainer? = NetworkBelongsToCollectionContainer(),
    val budget: Int? = null,
    val genres: ArrayList<NetworkGenresDetailContainer> = arrayListOf(),
    val homepage: String? = null,
    val id: Int? = null,
    @SerializedName("imdb_id") val imdbId: String? = null,
    @SerializedName("original_language") val originalLanguage: String? = null,
    @SerializedName("original_title") val originalTitle: String? = null,
    val overview: String? = null,
    val popularity: Double? = null,
    @SerializedName("poster_path") val posterPath: String? = null,
    @SerializedName("production_companies")
    val productionCompanies: ArrayList<NetworkProductionCompaniesContainer> = arrayListOf(),
    @SerializedName("production_countries")
    val productionCountries: ArrayList<NetworkProductionCountriesContainer> = arrayListOf(),
    @SerializedName("release_date") val releaseDate: String? = null,
    val revenue: Long? = null,
    val runtime: Int? = null,
    @SerializedName("spoken_languages")
    val spokenLanguages: ArrayList<NetworkSpokenLanguagesDetailContainer> = arrayListOf(),
    val status: String? = null,
    val tagline: String? = null,
    val title: String? = null,
    val video: Boolean? = null,
    @SerializedName("vote_average") val voteAverage: Double? = null,
    @SerializedName("vote_count") val voteCount: Int? = null
){
    val posterUrl: String
        get() = BASE_POSTER_IMAGE_URL.plus(posterPath)

    val backgroundUrl: String
        get() = BASE_BACKGROUND_IMAGE_URL.plus(backdropPath)
}

data class NetworkBelongsToCollectionContainer(
    val id: Int? = null,
    val name: String? = null,
    @SerializedName("poster_path") val posterPath: String? = null,
    @SerializedName("backdrop_path") val backdropCollectionPath: String? = null
){
    val backdropCollectionUrl: String
        get() = BASE_BACKGROUND_IMAGE_URL.plus(backdropCollectionPath)
}

data class NetworkGenresDetailContainer(
    val id: Int,
    val name: String? = null
)

data class NetworkProductionCompaniesContainer(
    val id: Int? = null,
    @SerializedName("logo_path") val logoPath: String? = null,
    val name: String? = null,
    @SerializedName("origin_country") val originCountry: String? = null
)

data class NetworkProductionCountriesContainer(
    @SerializedName("iso_3166_1") val iso31661: String? = null,
    val name: String? = null
)

data class NetworkSpokenLanguagesDetailContainer(
    @SerializedName("english_name") val englishName: String? = null,
    @SerializedName("iso_639_1") val iso6391: String? = null,
    val name: String? = null
)

//CONVERSORES UNITARIOS

fun NetworkMovieDetailedContainer.asDatabaseModel(timestimeAdd: Long): DatabaseFavouriteMovie {
    return DatabaseFavouriteMovie(
        id = id!!,
        movieTitle = title!!,
        overview = overview!!,
        adult = adult?: false,
        generes = genres.map { it.id },
        //it es un string y poster un bitmap. Aqui en caso explote usar la func get bitmap
        //creo que explotara por el suspend
        posterPath = posterUrl,
        //aqui usamos la func de una funcion
        backdropPath = backgroundUrl,
        releaseDate = releaseDate!!,
        vote_Average = voteAverage!!,
        //TODO AQUI CUANDO SE CREA EL LAYOUT QUE LEE, DEBE SER EN BASE A LAS RESPUESTAS, SI ES ERROR QUE NO APAREZCA
        idCollection = belongsToCollection?.id ?: 0,
        backgroundCollectionPath = belongsToCollection?.backdropCollectionUrl ?: "",
        nameCollectionBelongsTo = belongsToCollection?.name ?: "No name collection",
        runtime = runtime!!,
        timestampAdd = timestimeAdd,
        saved = true
    )
}

fun NetworkMovieDetailedContainer.asTemporaryDetailDomainModel() : TemporaryDetailMovie {
    return TemporaryDetailMovie(
        id = id!!,
        movieTitle = title!!,
        overview = overview ?: "No overview",
        adult = adult ?: false,
        genres = genres.map { it.id },
        //TODO ES LOS MISMO SI AQUI LOS DESCARGO O SI EN CADA DATO A LA HORA DE BINDEAR?
        posterPathUrl = posterUrl,
        backdropPathUrl = backgroundUrl,
        releaseDate = releaseDate!!,
        voteAverage = voteAverage ?: 90.0,
        idCollection = belongsToCollection?.id ?: 0,
        backgroundCollectionPathUrl = belongsToCollection?.backdropCollectionUrl ?: "",
        nameCollectionBelongsTo = belongsToCollection?.name ?: "No name collection",
        runtime = runtime!!,
    )
}

//CONVERSORES EN LISTAS
/**
 * Convert Network results to database objects
 * Esto sera para cuando queremos actualizar datos directamente a la base
 * O tambien cuando queremos mantener de primera una pantalla entera
 * Porque normalmente debe ser de Network to domain y de domain to database
 */
fun List<NetworkMovieDetailedContainer>.asDatabaseModel(timestimeAdd: Long): List<DatabaseFavouriteMovie>{
    return map{
        it.asDatabaseModel(timestimeAdd)
        /*DatabaseFavouriteMovie(
            id = it.id!!,
            movieTitle = it.title!!,
            overview = it.overview!!,
            adult = it.adult?: false,
            generes = it.genres.map { gen->
                gen.id },
            //it es un string y poster un bitmap. Aqui en caso explote usar la func get bitmap
            //creo que explotara por el suspend
            posterPath = ((ImageRequest.Builder(context)
                .data(it.posterUrl) as SuccessResult).drawable as BitmapDrawable).bitmap,
            //aqui usamos la func de una funcion
            backdropPath = getBitmap(it.backgroundUrl,context),
            releaseDate = it.releaseDate!!,
            vote_Average = it.voteAverage!!,
            //TODO AQUI CUANDO SE CREA EL LAYOUT QUE LEE, DEBE SER EN BASE A LAS RESPUESTAS, SI ES ERROR QUE NO APAREZCA
            idCollection = it.belongsToCollection?.id ?: 0,
            backgroundCollectionPath = getBitmap(it.belongsToCollection?.backdropCollectionUrl ?: "",context),
            nameCollectionBelongsTo = it.belongsToCollection?.name ?: "No name collection",
            runtime = it.runtime!!,
            timestampAdd = timestimeAdd
        )*/
    }
}

//TODO REVISAR QUE NO EXPLOTE Y QUE CON ESTO DISMINUIR CODIGO EN LOS OTROS LADOS DE EXTENSION
/**
 * Convert Network results to Temporary Domain object in order to use it to print views
 */
fun List<NetworkMovieDetailedContainer>.asTemporaryDetailDomainModel(): List<TemporaryDetailMovie> {
    return map {
        it.asTemporaryDetailDomainModel()
        /*TemporaryDetailMovie(
            id = it.id!!,
            movieTitle = it.title!!,
            overview = it.overview!!,
            adult = it.adult ?: false,
            genres = it.genres.map { gen->
                gen.id },
            //TODO ES LOS MISMO SI AQUI LOS DESCARGO O SI EN CADA DATO A LA HORA DE BINDEAR?
            posterPathUrl = it.posterUrl,
            backdropPathUrl = it.backgroundUrl,
            releaseDate = it.releaseDate!!,
            voteAverage = it.voteAverage ?: 90.0,
            idCollection = it.belongsToCollection?.id,
            backgroundCollectionPathUrl = it.belongsToCollection?.backdropCollectionUrl,
            nameCollectionBelongsTo = it.belongsToCollection?.name ?: "No name collection",
            runtime = it.runtime!!,
        )*/
    }
}













