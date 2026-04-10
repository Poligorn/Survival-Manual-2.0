package com.survivalwiki.data.entity

import androidx.room.Entity
import androidx.room.Fts4
import androidx.room.PrimaryKey

@Entity(tableName = "categories")
data class Category(
    @PrimaryKey val id: Int,
    val title: String,
    val description: String?,
    val iconResName: String // string identifier to map to compose icons or local images
)

@Entity(tableName = "articles")
data class Article(
    @PrimaryKey val id: Int,
    val categoryId: Int,
    val title: String,
    val content: String,
    val tags: String, // comma separated tags e.g. "#вода,#очистка"
    val iconResName: String,
    val lastReadTimestamp: Long = 0L,
    val level: String? = null, // e.g. "Уровень: Базовый"
    val readTimeMin: Int = 0,
    val lastUpdated: String? = null,
    val isBookmarked: Boolean = false
)

@Entity(tableName = "articles_fts")
@Fts4(contentEntity = Article::class)
data class ArticleFts(
    val title: String,
    val content: String,
    val tags: String
)