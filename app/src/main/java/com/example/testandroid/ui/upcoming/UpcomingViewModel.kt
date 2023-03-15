package com.example.testandroid.ui.upcoming

import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.example.testandroid.data.entities.MovieEntity
import com.example.testandroid.data.model.ResourceStatus
import com.example.testandroid.data.repository.MovieRepository
import com.example.testandroid.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class UpcomingViewModel @Inject constructor (private val repository: MovieRepository) : ViewModel() {

    private var currentPage = 1
    private var isLoading = false

    private val _upcomingMovies = MediatorLiveData<Resource<List<MovieEntity>>>()
    val upcomingMovies: LiveData<Resource<List<MovieEntity>>>
        get() = _upcomingMovies

    init {
        fetchUpcomingMovies()
    }

    fun fetchUpcomingMovies() {
        if (isLoading) return
        isLoading = true
        val source = repository.getUpcomingMovies(page = currentPage)
        _upcomingMovies.addSource(source) { resource ->
            when (resource.resourceStatus) {
                ResourceStatus.LOADING -> {
                    _upcomingMovies.value = Resource.loading()
                }
                ResourceStatus.SUCCESS -> {
                    val currentMovies = upcomingMovies.value?.data?.toMutableList() ?: mutableListOf()
                    currentMovies.addAll(resource.data!!)
                    _upcomingMovies.value = Resource.success(currentMovies)
                    isLoading = false
                    currentPage++
                }
                ResourceStatus.ERROR -> {
                    _upcomingMovies.value = Resource.error(resource.message!!)
                    isLoading = false
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        _upcomingMovies.removeSource(repository.getUpcomingMovies(page = currentPage))
    }

}