package com.example.newsapp.model

import java.util.Date

data class NewsArticle (
    val source: Source?,
    val author: String?,
    val title: String,
    val description: String?,
    val url: String,
    val urlToImage: String?,
    var publishedAt: String,
    var content: String?
    )