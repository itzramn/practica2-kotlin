package com.example.testandroid.utils

import com.example.testandroid.data.model.ResourceStatus

data class Resource<out T>(val resourceStatus: ResourceStatus, val data: T?, val message: String?) {

    companion object {
        fun <T> success(data: T): Resource<T> {
            return Resource(ResourceStatus.SUCCESS, data, null)
        }

        fun <T> error(message: String, data: T? = null): Resource<T> {
            return Resource(ResourceStatus.ERROR, data, message)
        }

        fun <T> loading(data: T? = null): Resource<T> {
            return Resource(ResourceStatus.LOADING, data, null)
        }
    }
}