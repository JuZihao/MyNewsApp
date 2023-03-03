package com.example.newsapp.domain.repository

import com.example.newsapp.domain.model.NewsDataFromJson
import com.example.newsapp.news_api.util.ApiResult
import com.example.newsapp.ui.NewsCategories
import kotlinx.coroutines.flow.Flow
import retrofit2.Response

interface NewsRepository {

    suspend fun getNewsList(): Flow<ApiResult<NewsDataFromJson>>

    suspend fun getNewsByCategory(category: NewsCategories): Flow<ApiResult<NewsDataFromJson>>

    suspend fun getNewsByQuery(query: String): Flow<ApiResult<NewsDataFromJson>>
}