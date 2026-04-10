package com.survivalwiki.data

import com.survivalwiki.data.dao.ArticleDao
import com.survivalwiki.data.entity.Article
import com.survivalwiki.data.entity.Category
import kotlinx.coroutines.flow.Flow

class ArticleRepository(private val articleDao: ArticleDao) {

    fun getAllArticles(): Flow<List<Article>> {
        return articleDao.getAllArticles()
    }

    fun getIntroductionArticle(): Flow<Article?> {
        return articleDao.getIntroductionArticle()
    }

    fun getAllCategories(): Flow<List<Category>> {
        return articleDao.getAllCategories()
    }

    fun getArticlesByCategory(categoryId: Int): Flow<List<Article>> {
        return articleDao.getArticlesByCategory(categoryId)
    }

    fun getArticleById(id: Int): Flow<Article?> {
        return articleDao.getArticleById(id)
    }

    fun getRecentlyReadArticles(): Flow<List<Article>> {
        return articleDao.getRecentlyReadArticles()
    }

    fun searchArticles(query: String): Flow<List<Article>> {
        // Room FTS MATCH needs an asterisk for prefix search, e.g., "query*"
        val ftsQuery = "*$query*"
        return articleDao.searchArticles(ftsQuery)
    }

    fun getBookmarkedArticles(): Flow<List<Article>> {
        return articleDao.getBookmarkedArticles()
    }

    suspend fun toggleBookmark(articleId: Int) {
        articleDao.toggleBookmark(articleId)
    }

    suspend fun markArticleAsRead(articleId: Int) {
        val currentTime = System.currentTimeMillis()
        articleDao.updateLastReadTime(articleId, currentTime)
    }

    suspend fun clearHistory() {
        articleDao.clearHistory()
    }
}