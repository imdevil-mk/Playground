package com.imdevil.playground


import com.squareup.moshi.Json
import com.squareup.moshi.JsonClass

@JsonClass(generateAdapter = true)
data class Person(
    @Json(name = "age")
    val age: Int,
    @Json(name = "name")
    val name: String
)