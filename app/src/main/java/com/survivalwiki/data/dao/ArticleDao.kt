package com.survivalwiki.data.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.survivalwiki.data.entity.Article
import com.survivalwiki.data.entity.Category
import kotlinx.coroutines.flow.Flow

@Dao
interface ArticleDao {
    @Query("SELECT * FROM articles")
    fun getAllArticles(): Flow<List<Article>>

    @Query("SELECT * FROM articles WHERE title LIKE 'Введение%' LIMIT 1")
    fun getIntroductionArticle(): Flow<Article?>

    @Query("SELECT * FROM categories")
    fun getAllCategories(): Flow<List<Category>>

    @Query("SELECT * FROM articles WHERE categoryId = :categoryId")
    fun getArticlesByCategory(categoryId: Int): Flow<List<Article>>

    @Query("SELECT * FROM articles WHERE id = :id LIMIT 1")
    fun getArticleById(id: Int): Flow<Article?>

    @Query("SELECT * FROM articles WHERE lastReadTimestamp > 0 ORDER BY lastReadTimestamp DESC LIMIT 10")
    fun getRecentlyReadArticles(): Flow<List<Article>>

    @Query("SELECT * FROM articles WHERE isBookmarked = 1 ORDER BY title ASC")
    fun getBookmarkedArticles(): Flow<List<Article>>

    @Query("UPDATE articles SET isBookmarked = CASE WHEN isBookmarked = 1 THEN 0 ELSE 1 END WHERE id = :articleId")
    suspend fun toggleBookmark(articleId: Int)

    @Query("UPDATE articles SET lastReadTimestamp = :timestamp WHERE id = :articleId")
    suspend fun updateLastReadTime(articleId: Int, timestamp: Long)

    @Query("UPDATE articles SET lastReadTimestamp = 0")
    suspend fun clearHistory()

    @Transaction
    @Query("""
        SELECT articles.* FROM articles 
        JOIN articles_fts ON articles.id = articles_fts.docid 
        WHERE articles_fts MATCH :query
    """)
    fun searchArticles(query: String): Flow<List<Article>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCategories(categories: List<Category>)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertArticles(articles: List<Article>)
}