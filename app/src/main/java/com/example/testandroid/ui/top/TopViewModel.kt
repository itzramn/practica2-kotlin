package com.example.testandroid.ui.top

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
class TopViewModel @Inject constructor (private val repository: MovieRepository) : ViewModel() {

    private var currentPage = 1
    private var isLoading = false

    private val _topMovies = MediatorLiveData<Resource<List<MovieEntity>>>()
    val topMovies: LiveData<Resource<List<MovieEntity>>>
        get() = _topMovies

    init {
        fetchTopMovies()
    }

    fun fetchTopMovies() {
        if (isLoading) return
        isLoading = true
        val source = repository.getTopMovies(page = currentPage)
        _topMovies.addSource(source) { resource ->
            when (resource.resourceStatus) {
                ResourceStatus.LOADING -> {
                    _topMovies.value = Resource.loading()
                }
                ResourceStatus.SUCCESS -> {
                    val currentMovies = topMovies.value?.data?.toMutableList() ?: mutableListOf()
                    currentMovies.addAll(resource.data!!)
                    _topMovies.value = Resource.success(currentMovies)
                    isLoading = false
                    currentPage++
                }
                ResourceStatus.ERROR -> {
                    _topMovies.value = Resource.error(resource.message!!)
                    isLoading = false
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        _topMovies.removeSource(repository.getTopMovies(page = currentPage))
    }
}