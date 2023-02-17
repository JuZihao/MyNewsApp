package com.example.newsapp.model

data class NewsDataFromJson(
    val articles: List<NewsArticle>,
    val status: String,
    val totalResults: Int
)



