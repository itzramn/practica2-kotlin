package com.example.testandroid.data.entities

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.android.parcel.Parcelize

@Parcelize
@Entity(tableName = "movieEntity")
data class MovieEntity (
    @PrimaryKey
    val id: Int = 0,
    @ColumnInfo(name = "adult")
    val adult : Boolean = false,
    @ColumnInfo(name = "backdropPath")
    var backdropPath: String? = "",
    @ColumnInfo(name = "originalTitle")
    var originalTitle: String? = "",
    @ColumnInfo(name = "originalLanguage")
    var originalLanguage: String? = "",
    @ColumnInfo(name = "overview")
    var overview: String? = "",
    @ColumnInfo(name = "popularity")
    var popularity: Float? = 0f,
    @ColumnInfo(name = "posterPath")
    var posterPath: String? = "",
    @ColumnInfo(name = "releaseDate")
    var releaseDate: String,
    @ColumnInfo(name = "title")
    var title: String? = "",
    @ColumnInfo(name = "video")
    var video: Boolean? = false,
    @ColumnInfo(name = "voteAverage")
    var voteAverage: Float? = 0f,
    @ColumnInfo(name = "voteCount")
    var voteCount: Int? = 0,
    @ColumnInfo(name = "movie_type")
    var movie_type: Int = 0
):Parcelable