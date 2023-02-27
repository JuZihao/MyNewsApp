package com.example.newsapp.model

import com.example.newsapp.domain.model.NewsDataFromJson
import com.example.newsapp.news_api.util.Constant.API_KEY
import com.example.newsapp.news_api.util.Constant.BASE_IRL
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Response
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
    suspend fun getBreakingNews(
        @Query("q") query: String = "bitcoin",
        //@Query("country") country: String = "us",
        @Query("page") pageNumber: Int = 1,
        @Query("pageSize") pageSize: Int = 20,
        @Query("apikey") api_key: String = API_KEY
    ): Response<NewsDataFromJson>

    @GET("everything")
    suspend fun searchForNews(
        @Query("q") query: String,
        @Query("page") pageNumber: Int = 1,
        @Query("pageSize") pageSize: Int = 20,
        @Query("apikey") api_key: String = API_KEY
    ): Response<NewsDataFromJson>

    @GET("top-headlines")
    suspend fun getCategoryNews(
        @Query("category") category: String,
        @Query("page") pageNumber: Int = 1,
        @Query("pageSize") pageSize: Int = 20,
        @Query("apikey") api_key: String = API_KEY
    ): Response<NewsDataFromJson>

}

object NewsApi {
    val retrofitService : NewsApiService by lazy {
        retrofit.create(NewsApiService::class.java)
    }
}