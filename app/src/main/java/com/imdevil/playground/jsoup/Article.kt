package com.imdevil.playground.jsoup

data class Article(
    val title: String,
    val images: List<String>
) {
    fun print(): String {
        val sb = StringBuilder()
        sb.append("  title = $title")

        images.forEach {
            sb.append("\n")
            sb.append("    $it")
        }

        return sb.toString()
    }
}
