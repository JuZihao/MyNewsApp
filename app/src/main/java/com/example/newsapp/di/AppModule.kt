package com.example.newsapp.di

import com.example.newsapp.domain.repository.NewsRepository
import com.example.newsapp.news_api.NewsApiService
import com.example.newsapp.news_api.data.remote.NewsRemoteDataSource
import com.example.newsapp.news_api.data.repository.NewsRepositoryImpl
import com.example.newsapp.news_api.util.Constant
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
class AppModule {

    @Provides
    @Singleton
    fun provideRemoteDataSource(): NewsRemoteDataSource {
        return NewsRemoteDataSource()
    }

    @Provides
    @Singleton
    fun provideMoshi(): Moshi {
        return Moshi.Builder()
            .add(KotlinJsonAdapterFactory())
            .build()
    }

    @Provides
    @Singleton
    fun provideBaseIrl() = Constant.BASE_IRL

    @Provides
    @Singleton
    fun provideApiServices(converter: Moshi, base_irl: String): NewsApiService {
        return Retrofit.Builder()
            .addConverterFactory(MoshiConverterFactory.create(converter))
            .baseUrl(base_irl)
            .build()
            .create(NewsApiService::class.java)
    }

    @Provides
    @Singleton
    fun provideNewRepository(newsApiService: NewsApiService): NewsRepository {
        return NewsRepositoryImpl(newsApiService)
    }
}