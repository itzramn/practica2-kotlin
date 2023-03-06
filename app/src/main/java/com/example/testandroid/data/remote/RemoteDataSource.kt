package com.example.testandroid.data.remote

import com.example.testandroid.utils.Const
import com.example.testandroid.utils.BaseDataSource
import javax.inject.Inject

class RemoteDataSource @Inject constructor(private val apiServices: ApiService) : BaseDataSource() {
    suspend fun getPopularMovies() = getResult { apiServices.getPopularMovies(Const.API_KEY) }
    suspend fun getUpcomingMovies() = getResult { apiServices.getUpcomingMovies(Const.API_KEY) }

    suspend fun getTopMovies() = getResult { apiServices.getTopMovies(Const.API_KEY) }


}