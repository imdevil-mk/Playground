package com.imdevil.playground.jsoup

import okhttp3.ResponseBody
import org.jsoup.Jsoup
import retrofit2.Converter

class ArticleConverter : Converter<ResponseBody, Article> {
    override fun convert(value: ResponseBody): Article {
        val html = value.string()
        return handleHtml(html)
    }

    private fun handleHtml(html: String): Article {
        val doc = Jsoup.parseBodyFragment(html)

        val article = doc.getElementsByTag("article").first()!!

        val title = article.getElementsByTag("h1").first()!!.text()

        val metaDataHtml = article.getElementById("metadata_qrcode")!!.firstElementChild()!!
        val spans = metaDataHtml.getElementsByTag("span")

        val imageList = mutableListOf<String>()
        val spotlights = article.getElementsByClass("spotlight-group").first()!!
        val images = spotlights.getElementsByAttribute("onClick")
        for (image in images) {
            imageList.add(image.attr("data-src"))
        }
        return Article(title, imageList)
    }

    companion object {
        private const val TAG = "ArticleConverter"
    }
}