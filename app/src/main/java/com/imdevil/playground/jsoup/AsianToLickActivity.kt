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
            .url("https://asiantolick.com/ajax/buscar_posts.php?post=&cat=&tag=&search=&page=&index=1")
            .build()

        netClient.newCall(request).enqueue(object : Callback {
            override fun onFailure(call: Call, e: IOException) {
                Log.d(TAG, "onFailure: $e")
            }

            override fun onResponse(call: Call, response: Response) {
                val doc = Jsoup.parseBodyFragment(response.body!!.string())
                val albums = doc.getElementsByTag("a")
                for (album in albums) {
                    val links = album.attr("href")
                    val imgWrap = album.getElementsByClass("background_miniatura")
                    val img = (imgWrap.firstOrNull())?.getElementsByTag("img")?.firstOrNull()
                    img?.let {
                        val src = img.attr("src")
                        val name = img.attr("alt")
                        val postId = img.attr("post-id")
                        Log.d(TAG, "onResponse: name = $name")
                        Log.d(TAG, "onResponse: thumb = $src")
                        Log.d(TAG, "onResponse: links = $links")
                        Log.d(TAG, "onResponse: postId = $postId")
                    }
                }
            }

        })
    }
}