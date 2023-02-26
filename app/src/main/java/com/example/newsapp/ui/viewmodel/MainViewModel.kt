package com.example.newsapp.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsapp.model.NewsApi
import com.example.newsapp.model.NewsArticle
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat


enum class NewsApiStatus {LOADING, ERROR, DONE}

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
class MainViewModel : ViewModel() {

    val rawNewsList: MutableMap<NewsCategories, List<NewsArticle>> = mutableMapOf()

    private val _status = MutableLiveData<NewsApiStatus>()
    val status: LiveData<NewsApiStatus> = _status

    private val _size = MutableLiveData<String>()
    val size: LiveData<String> = _size

    private val _newsList = MutableLiveData<List<NewsArticle>>()
    val newsList: MutableLiveData<List<NewsArticle>> = _newsList

    private val _categoryNewsList = MutableLiveData<List<NewsArticle>>()
    val categoryNewsList: MutableLiveData<List<NewsArticle>> = _categoryNewsList

    private val _news = MutableLiveData<NewsArticle>()
    val news: MutableLiveData<NewsArticle> = _news

    val dateFormatter = SimpleDateFormat("EEEE, dd MMM yyyy")
    val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")

    init {
        getNewsArticlesList()
    }

    fun getNewsArticlesList() {
        viewModelScope.launch {
            _status.value = NewsApiStatus.LOADING
            try {
                val latestNewsTag = NewsCategories.LATEST
                val latestNews = NewsApi.retrofitService.getNews("us").articles
                rawNewsList[latestNewsTag] =  processData(latestNews)
                _newsList.value = rawNewsList[latestNewsTag]

                getCategoryNews(NewsCategories.BUSINESS)
                setCategoryNewsList(rawNewsList[NewsCategories.BUSINESS])

                _status.value = NewsApiStatus.DONE
                _size.value = "Received news from the api with size: ${rawNewsList[latestNewsTag]!!.size}"
            } catch (e: Exception) {
                _status.value = NewsApiStatus.ERROR
                _size.value = "API error:${e}}"
                _newsList.value = listOf()
                setCategoryNewsList(listOf())
            }
        }
    }

    fun onNewsClicked(news: NewsArticle) {
        _news.value = news
    }

    fun changeNewsCategory(category: NewsCategories = NewsCategories.BUSINESS) {
        when (category){
            NewsCategories.BUSINESS -> _categoryNewsList.value = rawNewsList[NewsCategories.BUSINESS]
            NewsCategories.ENTERTAINMENT -> getCategoryNews(NewsCategories.ENTERTAINMENT)
            NewsCategories.GENERAL -> getCategoryNews(NewsCategories.GENERAL)
            NewsCategories.TECHNOLOGY -> getCategoryNews(NewsCategories.TECHNOLOGY)
            NewsCategories.HEALTH -> getCategoryNews(NewsCategories.HEALTH)
            NewsCategories.SCIENCE -> getCategoryNews(NewsCategories.SCIENCE)
            else -> {
                _categoryNewsList.value = rawNewsList[NewsCategories.LATEST]
                _size.value = "About ${rawNewsList[NewsCategories.LATEST]?.size} results for All News"
            }
        }
    }

    private fun processData(newsList: List<NewsArticle>) : List<NewsArticle> {
        for (news in newsList) {
            val date = inputFormat.parse(news.publishedAt)
            news.publishedAt = dateFormatter.format(date)

            val text = news.content?.split("[")
            news.content = text?.get(0)
        }
        return newsList
    }

    fun getQueryNews(query: String) {
        viewModelScope.launch {
            try {
                val queryNews = NewsApi.retrofitService.getNews("us", query = query).articles
                if (queryNews.isEmpty()) {
                    _size.value = "No results for $query"
                    setCategoryNewsList(listOf())
                } else {
                    setCategoryNewsList(processData(queryNews))
                    _size.value = "About ${queryNews.size} results for $query"
                }
            } catch (e: Exception) {
                _status.value = NewsApiStatus.ERROR
                _size.value = "API error:${e}}"
                setCategoryNewsList(listOf())
            }
        }
    }

    fun getCategoryNews(category: NewsCategories) {
        if (rawNewsList[category].isNullOrEmpty()) {
            viewModelScope.launch {
                try {
                    val categoryNews = NewsApi.retrofitService.getNews("us", category.toString()).articles
                    val processedData = processData(categoryNews)
                    rawNewsList[category] = processedData
                    rawNewsList[NewsCategories.LATEST] = rawNewsList[NewsCategories.LATEST]!! + processedData
                    setCategoryNewsList(rawNewsList[category])
                } catch (e: Exception) {
                    _status.value = NewsApiStatus.ERROR
                    _size.value = "API error:${e}}"
                    _categoryNewsList.value = listOf()
                }
            }
        } else {
            setCategoryNewsList(rawNewsList[category])
        }
    }

    private fun setCategoryNewsList(news: List<NewsArticle>?) {
        if (news.isNullOrEmpty()) {
            _categoryNewsList.value = listOf()
        } else {
            _categoryNewsList.value = news!!
        }
    }

    fun applyFilter(filterType: SortByTypes) {
        when (filterType) {
            SortByTypes.DEFAULT -> setCategoryNewsList(rawNewsList[NewsCategories.LATEST])
            SortByTypes.RECOMMENDED -> setCategoryNewsList(rawNewsList[NewsCategories.LATEST])
            SortByTypes.LATEST -> {
                setCategoryNewsList(_categoryNewsList.value?.sortedByDescending {
                    dateFormatter.parse(it.publishedAt)
                }
                )
            }
            SortByTypes.VIEW -> setCategoryNewsList(rawNewsList[NewsCategories.LATEST])
        }
    }

    fun getNewsUrl(): String? {
        return news.value?.url
    }
}