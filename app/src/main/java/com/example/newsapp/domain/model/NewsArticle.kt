package com.example.newsapp.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(
    tableName = "articles"
)
data class NewsArticle (
    @PrimaryKey(autoGenerate = true)
    var id: Int? = null,
    val source: Source = Source("",""),
    val author: String? = "",
    val title: String = "",
    val description: String? = "",
    val url: String = "",
    val urlToImage: String? = "",
    var publishedAt: String = "",
    var content: String? = ""
    )