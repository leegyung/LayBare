package com.project.domain.util

sealed class ApiResult<T>(
    val data : T? = null,
    val errorMessage : String? = null,
    val errorCode : String? = null
) {
    class ResponseSuccess<T>(data : T) : ApiResult<T>(data)
    class ResponseLoading<T> : ApiResult<T>()
    class ResponseError<T>(errorMessage : String? = null, errorCode : String? = null, data: T? = null) : ApiResult<T>(data, errorMessage, errorCode)
}