package com.imdevil.playground

import com.squareup.moshi.JsonAdapter
import com.squareup.moshi.Moshi
import com.squareup.moshi.adapter
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import org.junit.Test
import java.io.File

class MoshiTest {


    @OptIn(ExperimentalStdlibApi::class)
    @Test
    fun simpleTest() {
        val moshi = Moshi.Builder()
            .build()

        val json = getJson("login.json")

        val adapter: JsonAdapter<LoginResponse> = moshi.adapter()

        val login = adapter.fromJson(json)

        println(login)
    }


    @OptIn(ExperimentalStdlibApi::class)
    @Test
    fun apiResponseTestValue() {
        val moshi = Moshi.Builder()
            .add(ApiResponseJsonAdapterFactory())
            .addLast(KotlinJsonAdapterFactory())
            .build()

        val json = getJson("login.json")
        val adapter: JsonAdapter<ApiResponse<Device>> = moshi.adapter()

        val apiResponse: ApiResponse<Device>? = adapter.fromJson(json)

        println(apiResponse)
    }

    @OptIn(ExperimentalStdlibApi::class)
    @Test
    fun apiResponseTestList() {
        val moshi = Moshi.Builder()
            .add(ApiResponseJsonAdapterFactory())
            .addLast(KotlinJsonAdapterFactory())
            .build()

        val json = getJson("list.json")
        val adapter: JsonAdapter<ApiResponse<List<Person>>> = moshi.adapter()

        val apiResponse: ApiResponse<List<Person>>? = adapter.fromJson(json)

        println(apiResponse)
    }

    @OptIn(ExperimentalStdlibApi::class)
    @Test
    fun apiResponseTestObject() {
        val moshi = Moshi.Builder()
            .add(ApiResponseJsonAdapterFactory())
            .addLast(KotlinJsonAdapterFactory())
            .build()

        val json = getJson("user.json")
        val adapter: JsonAdapter<ApiResponse<Person>> = moshi.adapter()

        val apiResponse: ApiResponse<Person>? = adapter.fromJson(json)

        println(apiResponse)
    }

}


fun getRes(name: String): File {
    val loader = ClassLoader.getSystemClassLoader()
    return File(loader.getResource(name).toURI())
}

fun getJson(name: String) = getRes(name).readText()