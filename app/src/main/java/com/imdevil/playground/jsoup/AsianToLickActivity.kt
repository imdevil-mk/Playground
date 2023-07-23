package com.imdevil.playground.jsoup

import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.imdevil.playground.R
import kotlinx.coroutines.launch
import okhttp3.ResponseBody
import org.jsoup.Jsoup
import retrofit2.Retrofit

private const val TAG = "AsianToLickActivity"

class AsianToLickActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_asian_to_lick)
        val image = findViewById<ImageView>(R.id.image)

        Glide.with(this)
            .load("https://wsrv.nl/?url=https://telegra.ph/file/5e8f3a90eba7a2a8ae2c1.jpg")
            .into(image)

        makeArticleRequest()
    }

    /**
     * Retrofit 最原始最简单用法
     */
    private fun makeOriginRetrofitRequest() {
        val retrofit = Retrofit.Builder().baseUrl("https://asiantolick.com").build()
        val api = retrofit.create(AsianToLickApi::class.java)

        api.getPageOrigin(0).enqueue(object : retrofit2.Callback<ResponseBody> {
            override fun onResponse(
                call: retrofit2.Call<ResponseBody>,
                response: retrofit2.Response<ResponseBody>
            ) {
                val html = response.body()!!.string()
                handleHomeHtml(html)
            }

            override fun onFailure(call: retrofit2.Call<ResponseBody>, t: Throwable) {

            }
        })
    }

    /**
     * Retrofit 添加Converter
     */
    private fun makeRetrofitRequestWithConverter() {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://asiantolick.com")
            .addConverterFactory(AlbumListConverterFactory())
            .build()

        val api = retrofit.create(AsianToLickApi::class.java)

        api.getPageWithConverter(0).enqueue(object : retrofit2.Callback<List<Album>> {
            override fun onResponse(
                call: retrofit2.Call<List<Album>>,
                response: retrofit2.Response<List<Album>>
            ) {
                response.body()?.forEach {
                    Log.d(TAG, "onResponse:\n $it")
                }
            }

            override fun onFailure(call: retrofit2.Call<List<Album>>, t: Throwable) {

            }
        })
    }

    /**
     * Retrofit对于Kotlin协程的支持
     */
    private fun makeRetrofitRequestWithConverterAndSuspend() {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://asiantolick.com")
            .addConverterFactory(HtmlConverterFactory())
            .build()

        val api = retrofit.create(AsianToLickApi::class.java)

        lifecycleScope.launch {
            val list = api.getPageWithConverterAndSuspend(0)
            list.forEach {
                Log.d(TAG, "makeRetrofitRequestWithConverterAndSuspend: \n{${it.print()}}")
            }
        }
    }

    private fun makeArticleRequest() {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://asiantolick.com")
            .addConverterFactory(HtmlConverterFactory())
            .build()

        val api = retrofit.create(AsianToLickApi::class.java)

        lifecycleScope.launch {
            val article = api.getArticleWithConverterAndSuspend(1430)
            Log.d(TAG, "makeArticleRequest: ${article.print()}")
        }
    }

    private fun handleHomeHtml(html: String) {
        val doc = Jsoup.parseBodyFragment(html)
        val albums = doc.getElementsByTag("a")
        for (album in albums) {
            val href = album.attr("href")
            val id = album.attr("id")

            val cover = album.getElementsByClass("background_miniatura")
                .first()!!
                .getElementsByTag("img")
                .first()!!
            val src = cover.attr("src")
            val dataSrc = cover.attr("data-src")
            val originalSrc = cover.attr("src-original")
            val prevSrc = cover.attr("src-prev")
            val alt = cover.attr("attr")
            val postId = cover.attr("post-id")


            val title = album.getElementsByClass("base_tt")
                .first()!!
                .getElementsByTag("span")
                .first()!!
                .text()

            Log.d(
                TAG, "handleHomeHtml:\n"
                        + " title = $title \n"
                        + " post-id = $postId\n"
                        + " cover = $src\n"
                        + " href = $href\n"
            )
        }
    }
}