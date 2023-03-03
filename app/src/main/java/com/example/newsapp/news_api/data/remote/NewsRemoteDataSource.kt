package com.example.newsapp.news_api.data.remote

import com.example.newsapp.domain.model.NewsDataFromJson
import com.example.newsapp.news_api.NewsApi
import com.example.newsapp.news_api.util.ApiResult
import com.example.newsapp.news_api.util.handleApi
import com.example.newsapp.ui.NewsCategories

class NewsRemoteDataSource(
) {
    suspend operator fun invoke(): ApiResult<NewsDataFromJson> =
        handleApi { NewsApi.retrofitService.getBreakingNews() }

    suspend fun getNewsByCategory(category: NewsCategories): ApiResult<NewsDataFromJson> =
        handleApi {
            when (category) {
                NewsCategories.BUSINESS -> NewsApi.retrofitService.getCategoryNews(NewsCategories.BUSINESS.toString())
                NewsCategories.LATEST -> NewsApi.retrofitService.getCategoryNews(NewsCategories.LATEST.toString())
                NewsCategories.GENERAL -> NewsApi.retrofitService.getCategoryNews(NewsCategories.GENERAL.toString())
                NewsCategories.TECHNOLOGY -> NewsApi.retrofitService.getCategoryNews(NewsCategories.TECHNOLOGY.toString())
                NewsCategories.ENTERTAINMENT -> NewsApi.retrofitService.getCategoryNews(
                    NewsCategories.ENTERTAINMENT.toString())
                NewsCategories.SCIENCE -> NewsApi.retrofitService.getCategoryNews(NewsCategories.SCIENCE.toString())
                NewsCategories.HEALTH -> NewsApi.retrofitService.getCategoryNews(NewsCategories.HEALTH.toString())
            }
        }

    suspend fun getNewsByQuery(query: String): ApiResult<NewsDataFromJson> =
        handleApi { NewsApi.retrofitService.searchForNews(query) }
}

