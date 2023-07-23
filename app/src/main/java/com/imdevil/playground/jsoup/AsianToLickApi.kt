package com.imdevil.playground.jsoup

import okhttp3.ResponseBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface AsianToLickApi {

    @GET("ajax/buscar_posts.php")
    fun getPageOrigin(
        @Query("index") index: Int,
    ): Call<ResponseBody>

    @GET("ajax/buscar_posts.php")
    fun getPageWithConverter(
        @Query("index") index: Int,
    ): Call<List<Album>>

    @GET("ajax/buscar_posts.php")
    suspend fun getPageWithConverterAndSuspend(
        @Query("index") index: Int,
    ): List<Album>

    @GET("post-{id}")
    suspend fun getArticleWithConverterAndSuspend(
        @Path("id") id: Int,
    ): Article
}