package com.example.testandroid.data.remote

import com.example.testandroid.utils.Const
import com.example.testandroid.utils.BaseDataSource
import javax.inject.Inject

class RemoteDataSource @Inject constructor(private val apiServices: ApiService) : BaseDataSource() {
    suspend fun getPopularMovies(page: Int) = getResult { apiServices.getPopularMovies(Const.API_KEY, page) }
    suspend fun getUpcomingMovies(page: Int) = getResult { apiServices.getUpcomingMovies(Const.API_KEY) }
    suspend fun getTopMovies(page: Int) = getResult { apiServices.getTopMovies(Const.API_KEY) }

}