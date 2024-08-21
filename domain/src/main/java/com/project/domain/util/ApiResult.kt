package com.project.domain.util


sealed class ApiResult<T> {
    class ResponseSuccess<T>(val data : T) : ApiResult<T>()
    class ResponseLoading<T> : ApiResult<T>()
    class ResponseError<T>(
        val errorMessage : String? = null,
        val errorCode : String? = null
    ) : ApiResult<T>()
}

