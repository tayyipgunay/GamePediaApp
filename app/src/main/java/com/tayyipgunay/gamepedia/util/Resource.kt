package com.tayyipgunay.gamepedia.util

sealed class Resource<T>(val data: T? = null, val message: String? = null) {
    class Success<T>(data: T) : Resource<T>(data)
    class Error<T>(message: String = "An unexpected error occurred.", data: T? = null) : Resource<T>(data, message)
    class Loading<T>(data: T? = null, val isIndeterminate: Boolean = true) : Resource<T>(data)
}
