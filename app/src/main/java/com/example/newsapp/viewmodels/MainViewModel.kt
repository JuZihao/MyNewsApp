package com.example.newsapp.views

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsapp.model.NewsApi
import com.example.newsapp.model.NewsArticle
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat


enum class NewsApiStatus {LOADING, ERROR, DONE}


class MainViewModel : ViewModel() {

    lateinit var rawNewsList: List<NewsArticle>

    private val _status = MutableLiveData<NewsApiStatus>()
    val status: LiveData<NewsApiStatus> = _status

    private val _size = MutableLiveData<String>()
    val size: LiveData<String> = _size

    private val _newsList = MutableLiveData<List<NewsArticle>>()
    val newsList: MutableLiveData<List<NewsArticle>> = _newsList

    private val _news = MutableLiveData<NewsArticle>()
    val news: MutableLiveData<NewsArticle> = _news

    init {
        getNewsArticlesList()
    }

    fun getNewsArticlesList() {
        viewModelScope.launch {
            _status.value = NewsApiStatus.LOADING
            try {
                rawNewsList = NewsApi.retrofitService.getNews("us").articles
                val dateFormatter = SimpleDateFormat("EEEE, dd MMM yyyy")
                val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'")
                for (news in rawNewsList) {
                    val date = inputFormat.parse(news.publishedAt)
                    news.publishedAt = dateFormatter.format(date)
                }
                _newsList.value = rawNewsList
                _status.value = NewsApiStatus.DONE
                _size.value = "Received news from the api with size: ${rawNewsList.size}"
            } catch (e: Exception) {
                _status.value = NewsApiStatus.ERROR
                _size.value = "API error:${e}}"
                _newsList.value = listOf()
            }
        }
    }

    fun onNewsClicked(news: NewsArticle) {
        _news.value = news
    }
}