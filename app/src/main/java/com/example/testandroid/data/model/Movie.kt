package com.example.testandroid.data.model

import android.os.Parcelable
import com.example.testandroid.data.entities.MovieEntity
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize
import java.util.*
import kotlin.collections.ArrayList

@Parcelize
data class Movie(
    @SerializedName("id")
    val id: Int = 0,
    @SerializedName("adult")
    val adult: Boolean = false,
    @SerializedName("genre_ids")
    var genres: List<Long> = ArrayList(),
    @SerializedName("backdrop_path")
    var backdropPath: String? = "",
    @SerializedName("original_title")
    var originalTitle: String? = "",
    @SerializedName("original_language")
    var originalLanguage: String? = "",
    @SerializedName("overview")
    var overview: String? = "",
    @SerializedName("popularity")
    var popularity: Float? = 0f,
    @SerializedName("poster_path")
    var posterPath: String? = "",
    @SerializedName("release_date")
    var releaseDate: String,
    @SerializedName("title")
    var title: String? = "",
    @SerializedName("video")
    var video: Boolean? = false,
    @SerializedName("vote_average")
    var voteAverage: Float? = 0f,
    @SerializedName("vote_count")
    var voteCount: Int? = 0
) : Parcelable


fun Movie.toMovieEntity(movieType: Int): MovieEntity = MovieEntity(
    id = this.id,
    adult = this.adult,
    backdropPath = this.backdropPath,
    originalTitle = this.originalTitle,
    originalLanguage = this.originalLanguage,
    overview = this.overview,
    popularity = this.popularity,
    posterPath = this.posterPath,
    releaseDate = this.releaseDate,
    title = this.title,
    video = this.video,
    voteAverage = this.voteAverage,
    voteCount = this.voteCount,
    movie_type = movieType
)

fun List<Movie>.toMovieEntityList(movieType: Int): List<MovieEntity> {
    val resultList = mutableListOf<MovieEntity>()
    this.forEach { movies ->
        resultList.add(movies.toMovieEntity(movieType))
    }
    return resultList
}