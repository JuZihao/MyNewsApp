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

enum class SortByTypes {DEFAULT, RECOMMENDED, LATEST, VIEW}
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
                getCategoryNews(NewsCategories.BUSINESS.toString())
                setCategoryNewsList(rawNewsList[NewsCategories.BUSINESS.toString()])

                val latestNewsTag = NewsCategories.LATEST.toString()
                val latestNews = NewsApi.retrofitService.getNews("us").articles
                rawNewsList[latestNewsTag] =  latestNews
                _newsList.value = processData(latestNews)
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
            NewsCategories.BUSINESS -> _categoryNewsList.value = rawNewsList[NewsCategories.BUSINESS.toString()]
            NewsCategories.ENTERTAINMENT -> getCategoryNews(NewsCategories.ENTERTAINMENT.toString())
            NewsCategories.GENERAL -> getCategoryNews(NewsCategories.GENERAL.toString())
            NewsCategories.TECHNOLOGY -> getCategoryNews(NewsCategories.TECHNOLOGY.toString())
            NewsCategories.HEALTH -> getCategoryNews(NewsCategories.HEALTH.toString())
            NewsCategories.SCIENCE -> getCategoryNews(NewsCategories.SCIENCE.toString())
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
        viewModelScope.launch {
            try {
                val queryNews = NewsApi.retrofitService.getNews("us", query = query).articles
                if (queryNews.isEmpty()) {
                    _size.value = "No results for ${query}"
                    setCategoryNewsList(listOf())
                } else {
                    setCategoryNewsList(processData(queryNews))
                    _size.value = "About ${queryNews.size} results for ${query}"
                }
            } catch (e: Exception) {
                _status.value = NewsApiStatus.ERROR
                _size.value = "API error:${e}}"
                setCategoryNewsList(listOf())
            }
        }
    }

    fun getCategoryNews(category: String) {
        if (rawNewsList[category].isNullOrEmpty()) {
            viewModelScope.launch {
                try {
                    val categoryNews = NewsApi.retrofitService.getNews("us", category).articles
                    val processedDate = processData(categoryNews)
                    rawNewsList[category] = processedDate
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
        _categoryNewsList.value = news
    }
}