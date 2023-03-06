package com.imdevil.playground

sealed class ApiResponse<T> {

    data class Success<T>(
        val data: T
    ) : ApiResponse<T>()

    data class BizError<T>(
        val code: Int,
        val msg: String,
    ) : ApiResponse<T>()

    data class OtherError<T>(
        val throwable: Throwable
    ) : ApiResponse<T>()
}