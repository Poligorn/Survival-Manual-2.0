package com.survivalwiki.ui.navigation

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.GridView
import androidx.compose.ui.graphics.vector.ImageVector

sealed class BottomNavItem(val route: String, val title: String, val icon: ImageVector) {
    data object Catalog : BottomNavItem("catalog", "Каталог", Icons.Default.GridView)
    data object Home : BottomNavItem("home", "Главная", Icons.Default.Home)
    data object Settings : BottomNavItem("settings", "Настройки", Icons.Default.Settings)
}