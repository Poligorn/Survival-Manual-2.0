package com.survivalwiki.ui

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.survivalwiki.LauncherIconController
import com.survivalwiki.data.ArticleRepository
import com.survivalwiki.data.entity.Article
import com.survivalwiki.data.entity.Category
import com.survivalwiki.data.SettingsManager
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class SurvivalViewModel(
    private val repository: ArticleRepository,
    private val settingsManager: SettingsManager,
    private val application: Application
) : ViewModel() {

    val themeFlow = settingsManager.themeFlow
    val fontSizeFlow = settingsManager.fontSizeFlow
    val accentColorFlow = settingsManager.accentColorFlow
    val homeListTypeFlow = settingsManager.homeListTypeFlow

    private val _categories = MutableStateFlow<List<Category>>(emptyList())
    val categories: StateFlow<List<Category>> = _categories.asStateFlow()

    private val _recentArticles = MutableStateFlow<List<Article>>(emptyList())
    val recentArticles: StateFlow<List<Article>> = _recentArticles.asStateFlow()

    private val _bookmarkedArticles = MutableStateFlow<List<Article>>(emptyList())
    val bookmarkedArticles: StateFlow<List<Article>> = _bookmarkedArticles.asStateFlow()

    private val _allArticles = MutableStateFlow<List<Article>>(emptyList())
    val allArticles: StateFlow<List<Article>> = _allArticles.asStateFlow()

    private val _introductionArticle = MutableStateFlow<Article?>(null)
    val introductionArticle: StateFlow<Article?> = _introductionArticle.asStateFlow()

    private val _searchResults = MutableStateFlow<List<Article>>(emptyList())
    val searchResults: StateFlow<List<Article>> = _searchResults.asStateFlow()

    init {
        loadCategories()
        loadRecentArticles()
        loadBookmarkedArticles()
        loadAllArticles()
        loadIntroductionArticle()
    }

    private fun loadAllArticles() {
        viewModelScope.launch {
            repository.getAllArticles().collect { list ->
                _allArticles.value = list
            }
        }
    }

    private fun loadIntroductionArticle() {
        viewModelScope.launch {
            repository.getIntroductionArticle().collect { article ->
                _introductionArticle.value = article
            }
        }
    }

    private fun loadCategories() {
        viewModelScope.launch {
            repository.getAllCategories().collect { list ->
                _categories.value = list
            }
        }
    }

    private fun loadRecentArticles() {
        viewModelScope.launch {
            repository.getRecentlyReadArticles().collect { list ->
                _recentArticles.value = list
            }
        }
    }

    private fun loadBookmarkedArticles() {
        viewModelScope.launch {
            repository.getBookmarkedArticles().collect { list ->
                _bookmarkedArticles.value = list
            }
        }
    }

    fun search(query: String) {
        if (query.isBlank()) {
            _searchResults.value = emptyList()
            return
        }
        viewModelScope.launch {
            repository.searchArticles(query).collect { list ->
                _searchResults.value = list
            }
        }
    }

    fun getArticlesByCategory(categoryId: Int): StateFlow<List<Article>> {
        val flow = MutableStateFlow<List<Article>>(emptyList())
        viewModelScope.launch {
            repository.getArticlesByCategory(categoryId).collect { list ->
                flow.value = list
            }
        }
        return flow.asStateFlow()
    }

    fun getArticleById(articleId: Int): StateFlow<Article?> {
        val flow = MutableStateFlow<Article?>(null)
        viewModelScope.launch {
            repository.getArticleById(articleId).collect { article ->
                flow.value = article
            }
        }
        return flow.asStateFlow()
    }

    fun markArticleAsRead(articleId: Int) {
        viewModelScope.launch {
            repository.markArticleAsRead(articleId)
        }
    }

    fun clearHistory() {
        viewModelScope.launch {
            repository.clearHistory()
            loadRecentArticles()
        }
    }
    fun setTheme(theme: String) {
        viewModelScope.launch {
            settingsManager.setTheme(theme)
        }
    }

    fun setFontSize(size: Int) {
        viewModelScope.launch {
            settingsManager.setFontSize(size)
        }
    }

    fun setAccentColor(color: String) {
        viewModelScope.launch {
            settingsManager.setAccentColor(color)
            LauncherIconController.syncFromAccent(application, color)
        }
    }

    fun setHomeListType(type: String) {
        viewModelScope.launch {
            settingsManager.setHomeListType(type)
        }
    }

    fun toggleBookmark(articleId: Int) {
        viewModelScope.launch {
            repository.toggleBookmark(articleId)
            // No need to reload manually as the Flow will automatically emit the new list,
            // but we might want to ensure the specific article flow emits too. Room handles this!
        }
    }
}

class SurvivalViewModelFactory(
    private val repository: ArticleRepository,
    private val settingsManager: SettingsManager,
    private val application: Application
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SurvivalViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return SurvivalViewModel(repository, settingsManager, application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}