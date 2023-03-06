package com.imdevil.playground

import com.squareup.moshi.*
import java.lang.reflect.ParameterizedType
import java.lang.reflect.Type

class ApiResponseJsonAdapterFactory : JsonAdapter.Factory {
    override fun create(
        type: Type,
        annotations: MutableSet<out Annotation>,
        moshi: Moshi
    ): JsonAdapter<*>? {
        val rawType = type.rawType
        if (rawType != ApiResponse::class.java) return null

        val dataType: Type =
            (type as? ParameterizedType)?.actualTypeArguments?.firstOrNull() ?: return null
        println("dataType = $dataType")
        println("dataType.rawType = ${dataType.rawType}")

        return ApiResponseAdapter<ApiResponse<Any>>(type, dataType, annotations, moshi)
    }
}

class ApiResponseAdapter<T>(
    private val type: Type,
    private val dataType: Type,
    private val annotations: MutableSet<out Annotation>,
    private val moshi: Moshi
) : JsonAdapter<ApiResponse<T>>() {

    override fun fromJson(reader: JsonReader): ApiResponse<T>? {
        val dataAdapter = moshi.adapter<T>(dataType, annotations)

        var msg: String? = null
        var ret: Int? = null
        var subRet: Int? = null
        var data_: T? = null

        val peek = reader.peekJson()
        peek.beginObject()
        while (peek.hasNext()) {
            when (peek.nextName()) {
                "msg" -> {
                    msg = peek.nextString()
                }
                "ret" -> {
                    ret = peek.nextInt()
                }
                "sub_ret" -> {
                    subRet = peek.nextInt()
                }
                else -> {

                    if (dataType.rawType == List::class.java) {
                        data_ = dataAdapter.fromJson(peek)
                    } else {
                        peek.skipValue()
                    }
                }
            }
        }
        peek.endObject()

        if (ret == 0 && subRet == 0 && data_ == null) {
            data_ = dataAdapter.fromJson(reader)
        } else {
            reader.skipValue()
        }

        return ApiResponse.Success(data_!!)
    }

    override fun toJson(writer: JsonWriter, value: ApiResponse<T>?) {
        writer.beginObject()
        writer.endObject()
    }
}