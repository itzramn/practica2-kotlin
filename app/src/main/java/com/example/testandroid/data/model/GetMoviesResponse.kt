package com.example.testandroid.data.model

import com.example.testandroid.data.entities.MovieEntity
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class GetMoviesResponse(@SerializedName("page")
                        @Expose
                        var page: Int,
                        @Expose
                        @SerializedName("total_results")
                        var totalResults: Int,
                        @SerializedName("total_pages")
                        @Expose
                        var totalPage: Int,
                        @SerializedName("results")
                        @Expose
                        var results: List<Movie>)