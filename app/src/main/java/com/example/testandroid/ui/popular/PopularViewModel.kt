package com.example.testandroid.ui.popular

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.testandroid.data.entities.MovieEntity
import com.example.testandroid.data.model.ResourceStatus
import com.example.testandroid.data.repository.MovieRepository
import com.example.testandroid.utils.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject


@HiltViewModel
class PopularViewModel @Inject constructor(private val repository: MovieRepository) : ViewModel() {

    private var currentPage = 1
    private var isLoading = false

    private val _popularMovies = MediatorLiveData<Resource<List<MovieEntity>>>()
    val popularMovies: LiveData<Resource<List<MovieEntity>>>
        get() = _popularMovies

    init {
        fetchPopularMovies()
    }

    fun fetchPopularMovies() {
        if (isLoading) return
        isLoading = true
        val source = repository.getPopularMovies(page = currentPage)
        _popularMovies.addSource(source) { resource ->
            when (resource.resourceStatus) {
                ResourceStatus.LOADING -> {
                    _popularMovies.value = Resource.loading()
                }
                ResourceStatus.SUCCESS -> {
                    val currentMovies = popularMovies.value?.data?.toMutableList() ?: mutableListOf()
                    currentMovies.addAll(resource.data!!)
                    _popularMovies.value = Resource.success(currentMovies)
                    isLoading = false
                    currentPage++
                }
                ResourceStatus.ERROR -> {
                    _popularMovies.value = Resource.error(resource.message!!)
                    isLoading = false
                }
            }
        }
    }

    override fun onCleared() {
        super.onCleared()
        _popularMovies.removeSource(repository.getPopularMovies(page = currentPage))
    }

    fun loadMorePopularMovies() {
        if (isLoading) return // Verificar si ya se está cargando más elementos
        currentPage++
        isLoading = true

        repository.getPopularMovies(page = currentPage).observeForever { resource ->
            when (resource.resourceStatus) {
                ResourceStatus.LOADING -> {
                    // mostrar un indicador de carga
                }
                ResourceStatus.SUCCESS -> {
                    val currentMovies = popularMovies.value?.data?.toMutableList() ?: mutableListOf()
                    currentMovies.addAll(resource.data!!)
                    _popularMovies.value = Resource.success(currentMovies)
                    isLoading = false
                    Log.e("cargador2", "Cargué mas items")
                }
                ResourceStatus.ERROR -> {
                    isLoading = false
                }
            }
        }
    }
}


