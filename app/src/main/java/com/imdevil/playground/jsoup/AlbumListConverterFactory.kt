package com.imdevil.playground.jsoup

import android.util.Log
import okhttp3.ResponseBody
import retrofit2.Converter
import retrofit2.Retrofit
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

class AlbumListConverterFactory : Converter.Factory() {

    override fun responseBodyConverter(
        type: Type,
        annotations: Array<out Annotation>,
        retrofit: Retrofit
    ): Converter<ResponseBody, *>? {
        Log.d(TAG, "responseBodyConverter: type = $type")
        val rawResponseType = getRawType(type)
        if (rawResponseType != List::class.java) return null
        Log.d(TAG, "responseBodyConverter: rawResponseType = $rawResponseType")

        val innerResponseType: Type = getParameterUpperBound(0, type as ParameterizedType)
        val rawInnerResponseType = getRawType(innerResponseType)
        Log.d(TAG, "responseBodyConverter: rawInnerResponseType = $rawInnerResponseType")

        if (rawInnerResponseType != Album::class.java) return null

        return AlbumListConverter()
    }

    companion object {
        private const val TAG = "AlbumListConverterFacto"
    }
}