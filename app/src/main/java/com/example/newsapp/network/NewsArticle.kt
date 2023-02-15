package com.example.newsapp.network

data class NewsArticle (
    val source: Source?,
    val author: String?,
    val title: String,
    val description: String?,
    val url: String,
    val urlToImage: String?,
    var publishedAt: String,
    val content: String?
    )