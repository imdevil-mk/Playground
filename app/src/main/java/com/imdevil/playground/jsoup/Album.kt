package com.imdevil.playground.jsoup

data class Album(
    val title: String,
    val id: String,
    val cover: String,
    val link: String
) {
    fun print(): String {
        return "title = $title\nid = $id\ncover = $cover\nlink = $link\n"
    }
}
