package com.example.newsapp.model

import com.example.newsapp.news_api.data.Constant.API_KEY
import com.example.newsapp.news_api.data.Constant.BASE_IRL
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query



private val moshi = Moshi.Builder()
    .add(KotlinJsonAdapterFactory())
    .build()

private val retrofit = Retrofit.Builder()
    .addConverterFactory(MoshiConverterFactory.create(moshi))
    .baseUrl(BASE_IRL)
    .build()

interface NewsApiService {
    @GET("top-headlines")
    suspend fun getNews(
        @Query("country") country: String,
        @Query("category") category: String = "",
        @Query("q") query: String = "",
        @Query("apikey") api_key: String = API_KEY
    ): NewsDataFromJson
}

object NewsApi {
    val retrofitService : NewsApiService by lazy {
        retrofit.create(NewsApiService::class.java)
    }
}