package com.example.newsapp.ui

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.newsapp.MainActivity
import com.example.newsapp.network.NewsApi
import com.example.newsapp.network.NewsArticle
import com.example.newsapp.network.NewsDataFromJson
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

enum class NewsApiStatus {LOADING, ERROR, DONE}

class MainViewModel : ViewModel() {

    private val _status = MutableLiveData<NewsApiStatus>()
    val status: LiveData<NewsApiStatus> = _status

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
                _newsList.value = NewsApi.retrofitService.getNews("us").articles
                _status.value = NewsApiStatus.DONE
            } catch (e: Exception) {
                _status.value = NewsApiStatus.ERROR
                _newsList.value = listOf()
            }
        }
    }
}