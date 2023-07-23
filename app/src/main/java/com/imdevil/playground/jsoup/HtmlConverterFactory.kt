package com.imdevil.playground.jsoup

import android.util.Log
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.Type

class HtmlConverterFactory : Converter.Factory() {

    private val converterCreator: Map<String, () -> Converter<ResponseBody, *>> = mapOf(
        "java.util.List<com.imdevil.playground.jsoup.Album>" to { AlbumListConverter() },
        "class com.imdevil.playground.jsoup.Article" to { ArticleConverter() },
    )

    override fun responseBodyConverter(
        type: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit
    ): Converter<ResponseBody, *>? {
        Log.d(TAG, "responseBodyConverter: type = $type")
        Log.d(TAG, "responseBodyConverter: ${converterCreator.keys}")
        return converterCreator[type.toString()]?.let { it() }
    }

    companion object {
        private const val TAG = "HtmlConverterFactory"
    }
}