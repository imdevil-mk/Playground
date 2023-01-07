package com.imdevil.playground.jsoup

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import com.imdevil.playground.R
import okhttp3.*
import org.jsoup.Jsoup
import java.io.IOException

private const val TAG = "AsianToLickActivity"

class AsianToLickActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_asian_to_lick)

        val netClient = OkHttpClient()
        val request = Request.Builder()
            .url("https://asiantolick.com/ajax/buscar_posts.php?post=&cat=&tag=&search=&page=&index=0")
            .build()

        netClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d(TAG, "onFailure: $e")
            }

            override fun onResponse(call: Call, response: Response) {
                val doc = Jsoup.parseBodyFragment(response.body!!.string())
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
                        .data()

                }
            }

        })
    }
}