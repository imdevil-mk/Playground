package com.imdevil.playground


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class LoginResponse(
    @Json(name = "deviceId")
    val deviceId: String,
    @Json(name = "deviceKey")
    val deviceKey: String,
    @Json(name = "msg")
    val msg: String,
    @Json(name = "ret")
    val ret: Int,
    @Json(name = "sub_ret")
    val subRet: Int
)