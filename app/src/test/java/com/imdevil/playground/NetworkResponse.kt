package com.imdevil.playground

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class NetworkResponse<T>(
    @Json(name = "msg")
    val msg: String,
    @Json(name = "ret")
    val ret: Int,
    @Json(name = "sub_ret")
    val subRet: Int,

    @Json(name = "data")
    val data: T,
)