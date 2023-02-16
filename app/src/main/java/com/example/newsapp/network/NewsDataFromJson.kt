package com.example.newsapp.network

data class NewsDataFromJson(
    val articles: List<NewsArticle>,
    val status: String,
    val totalResults: Int
)



