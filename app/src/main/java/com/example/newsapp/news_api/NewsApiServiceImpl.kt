package com.example.newsapp.news_api

import com.example.newsapp.model.NewsApiService
import javax.inject.Inject

class NewsApiServiceImpl @Inject constructor(private val apiService: NewsApiService) {

    suspend fun getBreakingNews() = apiService.getBreakingNews()

    suspend fun searchForNews(query: String) = apiService.searchForNews(query)

    suspend fun getCategoryNews(category: String) = apiService.getCategoryNews(category)
}