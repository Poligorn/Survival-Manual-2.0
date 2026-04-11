package com.survivalwiki.data

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.survivalwiki.data.dao.ArticleDao
import com.survivalwiki.data.entity.Article
import com.survivalwiki.data.entity.ArticleFts
import com.survivalwiki.data.entity.Category

@Database(
    entities = [Category::class, Article::class, ArticleFts::class],
    version = 16,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {

    abstract fun articleDao(): ArticleDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "survival_wiki_database"
                )
                .fallbackToDestructiveMigration()
                .build()
                INSTANCE = instance
                instance
            }
        }
    }
}