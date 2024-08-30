package com.project.domain.util

sealed class LibraryResult<T> {
    class ResponseLoading<T> : LibraryResult<T>()
    class ResponseSuccess<T>(val data : T) : LibraryResult<T>()
    class ResponseError<T>(val error : String) : LibraryResult<T>()
}

