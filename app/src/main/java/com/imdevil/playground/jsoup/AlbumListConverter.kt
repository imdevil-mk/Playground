package com.imdevil.playground.jsoup

import okhttp3.ResponseBody
import org.jsoup.Jsoup
import retrofit2.Converter

class AlbumListConverter : Converter<ResponseBody, List<Album>> {
    override fun convert(value: ResponseBody): List<Album> {
        val html = value.string()
        return handleHomeHtml(html)
    }

    private fun handleHomeHtml(html: String): List<Album> {
        val list = mutableListOf<Album>()

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

            list.add(Album(title, postId, src, href))
        }
        return list
    }

    companion object {
        private const val TAG = "AlbumListConverter"
    }
}