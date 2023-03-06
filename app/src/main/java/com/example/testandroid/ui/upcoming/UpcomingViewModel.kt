package com.example.testandroid.ui.upcoming

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.testandroid.data.entities.MovieEntity
import com.example.testandroid.data.repository.MovieRepository
import com.example.testandroid.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class UpcomingViewModel @Inject constructor (private val repository: MovieRepository) : ViewModel() {

    val fetchUpcomingMovies: LiveData<Resource<List<MovieEntity>>> = repository.getUpcomingMovies()

}