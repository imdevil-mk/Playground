package com.imdevil.playground.ksp

import com.imdevil.ksp.annotation.IntSummable
import com.squareup.moshi.JsonClass

@IntSummable
@JsonClass(generateAdapter = true)
data class FooInt(
    val foo: Int,
    val bar: Int,
)