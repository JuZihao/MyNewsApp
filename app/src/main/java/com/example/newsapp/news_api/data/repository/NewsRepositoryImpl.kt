package com.example.newsapp.news_api.data.repository

import com.example.newsapp.domain.model.NewsDataFromJson
import com.example.newsapp.domain.repository.NewsRepository
import com.example.newsapp.news_api.NewsApiService
import com.example.newsapp.news_api.util.ApiResult
import com.example.newsapp.news_api.util.handleApi
import com.example.newsapp.ui.NewsCategories
import javax.inject.Inject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow


class NewsRepositoryImpl @Inject constructor(
    val newsApi: NewsApiService
): NewsRepository {

    override suspend fun getNewsList(): Flow<ApiResult<NewsDataFromJson>> = flow {
        emit(handleApi { newsApi.getBreakingNews() })
    }


    override suspend fun getNewsByCategory(category: NewsCategories): ApiResult<NewsDataFromJson> =
        handleApi {
            when (category) {
                NewsCategories.BUSINESS -> newsApi.getCategoryNews(NewsCategories.BUSINESS.toString())
                NewsCategories.LATEST -> newsApi.getCategoryNews(NewsCategories.LATEST.toString())
                NewsCategories.GENERAL -> newsApi.getCategoryNews(NewsCategories.GENERAL.toString())
                NewsCategories.TECHNOLOGY -> newsApi.getCategoryNews(NewsCategories.TECHNOLOGY.toString())
                NewsCategories.ENTERTAINMENT -> newsApi.getCategoryNews(
                    NewsCategories.ENTERTAINMENT.toString()
                )
                NewsCategories.SCIENCE -> newsApi.getCategoryNews(NewsCategories.SCIENCE.toString())
                NewsCategories.HEALTH -> newsApi.getCategoryNews(NewsCategories.HEALTH.toString())
            }
        }

    override suspend fun getNewsByQuery(query: String): ApiResult<NewsDataFromJson> = handleApi { newsApi.searchForNews(query) }
}