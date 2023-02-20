package com.example.newsapp.views

import android.view.View
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


class MainViewModel : ViewModel() {

    val rawNewsList: MutableMap<String, List<NewsArticle>> = mutableMapOf()

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
                val latestNewsTag = NewsCategories.LATEST.toString()
                val latestNews = NewsApi.retrofitService.getNews("us").articles

                rawNewsList[latestNewsTag] =  latestNews

                for (category in NewsCategories.values()) {
                    val currentCategory = category.toString()
                    val categoryNews = NewsApi.retrofitService.getNews("us", currentCategory).articles
                    val processedDate = processData(categoryNews)
                    rawNewsList[currentCategory] = processedDate
                    rawNewsList[latestNewsTag] = rawNewsList[latestNewsTag]!! + processedDate
                }

                _newsList.value = processData(latestNews)
                _status.value = NewsApiStatus.DONE
                _size.value = "Received news from the api with size: ${rawNewsList[latestNewsTag]!!.size}"
                _categoryNewsList.value = rawNewsList[NewsCategories.BUSINESS.toString()]
            } catch (e: Exception) {
                _status.value = NewsApiStatus.ERROR
                _size.value = "API error:${e}}"
                _newsList.value = listOf()
                _categoryNewsList.value = listOf()
            }
        }
    }

    fun onNewsClicked(news: NewsArticle) {
        _news.value = news
    }

    fun changeNewsCategory(category: NewsCategories = NewsCategories.BUSINESS) {
        when (category){
            NewsCategories.BUSINESS -> _categoryNewsList.value = rawNewsList[NewsCategories.BUSINESS.toString()]
            NewsCategories.ENTERTAINMENT -> _categoryNewsList.value = rawNewsList[NewsCategories.ENTERTAINMENT.toString()]
            NewsCategories.GENERAL -> _categoryNewsList.value = rawNewsList[NewsCategories.GENERAL.toString()]
            NewsCategories.TECHNOLOGY -> _categoryNewsList.value = rawNewsList[NewsCategories.TECHNOLOGY.toString()]
            NewsCategories.HEALTH -> _categoryNewsList.value = rawNewsList[NewsCategories.HEALTH.toString()]
            NewsCategories.SCIENCE -> _categoryNewsList.value = rawNewsList[NewsCategories.SCIENCE.toString()]
            else -> _categoryNewsList.value = rawNewsList[NewsCategories.LATEST.toString()]
        }
    }

    private fun processData(newsList: List<NewsArticle>) : List<NewsArticle> {
        for (news in newsList) {
            val date = inputFormat.parse(news.publishedAt)
            news.publishedAt = dateFormatter.format(date)
            //news.date = date
        }
        return newsList
    }

    fun getQueryNews(query: String) {
        //val filteredNews = //rawNewsList[NewsCategories.LATEST.toString()]!!
        viewModelScope.launch {
            try {
                val queryNews = NewsApi.retrofitService.getNews("us", query = query).articles
                if (queryNews.isEmpty()) {
                    _size.value = "No results for ${query}"
                    _categoryNewsList.value = listOf()
                } else {
                    _categoryNewsList.value = processData(queryNews)
                    _size.value = "About ${queryNews.size} results for ${query}"
                }
            } catch (e: Exception) {
                _status.value = NewsApiStatus.ERROR
                _size.value = "API error:${e}}"
                _categoryNewsList.value = listOf()
            }
        }
    }
}