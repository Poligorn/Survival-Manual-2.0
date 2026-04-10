package com.survivalwiki.data

import android.content.Context
import android.util.Log
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.survivalwiki.data.entity.Article
import com.survivalwiki.data.entity.Category
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InputStreamReader

object DatabaseInitializer {

    suspend fun populateDatabase(context: Context, database: AppDatabase) {
        withContext(Dispatchers.IO) {
            val dao = database.articleDao()
            
            // Check if db is already populated (optional, you can check category count)
            // For simplicity in this dummy, we just overwrite if it's empty or reload
            
            val gson = Gson()
            try {
                // Load categories
                val categoriesStream = context.assets.open("data/categories.json")
                val categoriesReader = InputStreamReader(categoriesStream)
                val categoryType = object : TypeToken<List<Category>>() {}.type
                val categories: List<Category> = gson.fromJson(categoriesReader, categoryType)
                dao.insertCategories(categories)

                // Load articles
                val articlesStream = context.assets.open("data/articles.json")
                val articlesReader = InputStreamReader(articlesStream)
                val articleType = object : TypeToken<List<Article>>() {}.type
                val articles: List<Article> = gson.fromJson(articlesReader, articleType)
                dao.insertArticles(articles)

                Log.d("DB_INIT", "Database successfully populated with JSON data")
            } catch (e: Exception) {
                Log.e("DB_INIT", "Error populating database from JSON: ${e.message}")
            }
        }
    }
}