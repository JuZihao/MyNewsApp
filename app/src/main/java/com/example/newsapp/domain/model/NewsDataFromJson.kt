package com.example.newsapp.domain.model

data class NewsDataFromJson(
    val articles: List<NewsArticle>,
    val status: String,
    val totalResults: Int
)



