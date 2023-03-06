package com.imdevil.playground

import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Device(
    @Json(name = "deviceId")
    val deviceId: String,
    @Json(name = "deviceKey")
    val deviceKey: String,
)