package com.example.newsapp.ui

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsapp.R
import com.example.newsapp.domain.model.NewsArticle
import com.example.newsapp.domain.repository.NewsRepository
import com.example.newsapp.news_api.util.ApiResult
import com.example.newsapp.news_api.util.Constant.DEFAULT_NEWS_CONTENT
import com.example.newsapp.news_api.util.NewsApiStatus
import dagger.hilt.android.internal.Contexts.getApplication
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import javax.inject.Inject
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow

enum class NewsCategories(val message: String) {
    LATEST("lastestNews"),
    BUSINESS("business"),
    GENERAL("general"),
    TECHNOLOGY("technology"),
    ENTERTAINMENT("entertainment"),
    SCIENCE("science"),
    HEALTH("health");
    override fun toString() = message
}

enum class SortByTypes {DEFAULT, RECOMMENDED, LATEST, VIEW}

@HiltViewModel
class MainViewModel @Inject constructor(
    private val newsRepository: NewsRepository
) : ViewModel() {

    var isSelected: Int = R.id.business

    val rawNewsList: MutableMap<NewsCategories, List<NewsArticle>> = mutableMapOf()

    private val _status = MutableStateFlow(NewsApiStatus.LOADING)
    val status = _status.asStateFlow()

    private val _breakingNews: MutableStateFlow<List<NewsArticle>> = MutableStateFlow(listOf())
    val breakingNews = _breakingNews.asStateFlow()


    private val _categoryNewsList = MutableStateFlow<List<NewsArticle>>(listOf())
    val categoryNewsList = _categoryNewsList.asStateFlow()

    private val _size = MutableStateFlow("0")
    val size = _size.asStateFlow()


    private val _news = MutableStateFlow(NewsArticle())
    val news = _news.asStateFlow()

    val dateFormatter = SimpleDateFormat("EEEE, dd MMM yyyy")
    val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")

    init {
        getLatestNews()
        changeNewsCategory()
    }
    fun getLatestNews() = viewModelScope.launch {
        _status.value = NewsApiStatus.LOADING
        newsRepository.getNewsList().collect{
            when (it) {
                is ApiResult.Success -> {
                    _status.value = NewsApiStatus.SUCCESS
                    it.data?.let { ApiResponse ->
                        val data = processData(ApiResponse.articles)
                        _breakingNews.value = data
                        rawNewsList[NewsCategories.LATEST] = data
                    }
                }
                is ApiResult.Error -> {
                    _status.value = NewsApiStatus.ERROR
                    _breakingNews.value = listOf()
                }
                else -> {
                    _status.value = NewsApiStatus.LOADING
                }
            }
        }
    }

    fun changeNewsCategory(category: NewsCategories = NewsCategories.BUSINESS) {
        when (category){
            NewsCategories.BUSINESS -> getCategoryNews(NewsCategories.BUSINESS)
            NewsCategories.ENTERTAINMENT -> getCategoryNews(NewsCategories.ENTERTAINMENT)
            NewsCategories.GENERAL -> getCategoryNews(NewsCategories.GENERAL)
            NewsCategories.TECHNOLOGY -> getCategoryNews(NewsCategories.TECHNOLOGY)
            NewsCategories.HEALTH -> getCategoryNews(NewsCategories.HEALTH)
            NewsCategories.SCIENCE -> getCategoryNews(NewsCategories.SCIENCE)
            else -> {
                _categoryNewsList.value = rawNewsList[NewsCategories.LATEST]!!
                _size.value = "About ${rawNewsList[NewsCategories.LATEST]?.size} results for All News"
            }
        }
    }


    fun onNewsClicked(news: NewsArticle) {
        _news.value = news
    }



    private fun processData(newsList: List<NewsArticle>) : List<NewsArticle> {
        try {
            for (news in newsList) {
                val date = inputFormat.parse(news.publishedAt)
                news.publishedAt = dateFormatter.format(date!!)

                if (news.content != null) {
                    val text = news.content?.split("[")
                    news.content = text?.get(0)
                } else {
                    news.content = DEFAULT_NEWS_CONTENT
                }
            }
            return newsList
        } catch (e: Exception) {
            println(e.message)
            return emptyList()
        }
    }

    fun getQueryNews(query: String) {
        _status.value = NewsApiStatus.LOADING
        viewModelScope.launch {
            newsRepository.getNewsByQuery(query).collect {
                when (it) {
                    is ApiResult.Success -> {
                        _status.value = NewsApiStatus.SUCCESS
                        it.data?.let { ApiResponse ->
                            val data = processData(ApiResponse.articles)
                            _categoryNewsList.value = data
                        }
                    }
                    is ApiResult.Error -> {
                        _status.value = NewsApiStatus.ERROR
                        _categoryNewsList.value = listOf()
                        _size.value = it.message!!
                    }
                    else -> {
                        _status.value = NewsApiStatus.LOADING
                    }
                }
            }
        }
    }

    fun getCategoryNews(category: NewsCategories) {
        if (rawNewsList[category].isNullOrEmpty()) {
            _status.value = NewsApiStatus.LOADING
            viewModelScope.launch {
                newsRepository.getNewsByCategory(category).collect {
                    when (it) {
                        is ApiResult.Success -> {
                            _status.value = NewsApiStatus.SUCCESS
                            it.data?.let { ApiResponse ->
                                val data = processData(ApiResponse.articles)
                                _categoryNewsList.value = data
                                rawNewsList[category] = data
                            }
                        }
                        is ApiResult.Error -> {
                            _status.value = NewsApiStatus.ERROR
                            _categoryNewsList.value = listOf()
                            _size.value = it.message!!
                        }
                        else -> {
                            _status.value = NewsApiStatus.LOADING
                        }
                    }
                }
            }
        } else {
            _categoryNewsList.value = rawNewsList[category]!!
        }
    }

    fun applyFilter(filterType: SortByTypes) {
        when (filterType) {
            SortByTypes.DEFAULT -> _categoryNewsList.value = _categoryNewsList.value
            SortByTypes.RECOMMENDED -> _categoryNewsList.value = _categoryNewsList.value
            SortByTypes.LATEST -> {
                _categoryNewsList.value = _categoryNewsList.value.sortedByDescending {
                    dateFormatter.parse(it.publishedAt)
                }
            }
            SortByTypes.VIEW -> _breakingNews.value = _categoryNewsList.value
        }
    }

    fun getNewsUrl(): String {
        return _news.value.url
    }
}