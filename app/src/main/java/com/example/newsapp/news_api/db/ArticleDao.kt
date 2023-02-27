package com.example.newsapp.news_api.db

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.newsapp.domain.model.NewsArticle
import kotlinx.coroutines.flow.Flow

@Dao
interface ArticleDao{

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(article: NewsArticle)

    @Query("SELECT * FROM articles")
    fun getAllArticles(): Flow<List<NewsArticle>>

    @Delete
    fun deleteArticle(article: NewsArticle)
}