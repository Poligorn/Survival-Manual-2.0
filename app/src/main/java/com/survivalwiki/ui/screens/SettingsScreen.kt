package com.survivalwiki.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.List
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.TextFormat
import androidx.compose.material.icons.filled.Image
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.survivalwiki.ui.SurvivalViewModel
import com.survivalwiki.ui.theme.PrimaryOrange
import com.survivalwiki.ui.theme.SurfaceDark

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingsScreen(viewModel: SurvivalViewModel) {
    var showClearDialog by remember { mutableStateOf(false) }
    var showThemeDialog by remember { mutableStateOf(false) }
    var showFontDialog by remember { mutableStateOf(false) }
    var showAccentDialog by remember { mutableStateOf(false) }
    var showHomeListDialog by remember { mutableStateOf(false) }

    val currentTheme by viewModel.themeFlow.collectAsState(initial = "dark")
    val currentFontSize by viewModel.fontSizeFlow.collectAsState(initial = 1)
    val currentAccent by viewModel.accentColorFlow.collectAsState(initial = "orange")
    val currentHomeList by viewModel.homeListTypeFlow.collectAsState(initial = "recent")

    val themeLabel = when (currentTheme) {
        "light" -> "Светлая тема"
        "system" -> "Системная тема"
        else -> "Темная тема (режим энергосбережения)"
    }

    val fontLabel = when (currentFontSize) {
        0 -> "Мелкий"
        2 -> "Крупный"
        else -> "Средний"
    }

    val accentLabel = when (currentAccent) {
        "olive" -> "Оливковый"
        "blue" -> "Голубой"
        else -> "Оранжевый"
    }

    val homeListLabel = when (currentHomeList) {
        "bookmarks" -> "Избранное"
        else -> "Недавно читали"
    }

    if (showClearDialog) {
        AlertDialog(
            onDismissRequest = { showClearDialog = false },
            title = { Text("Очистить историю", color = Color.White) },
            text = { Text("Вы действительно хотите удалить всю историю просмотренных статей?", color = Color.LightGray) },
            confirmButton = {
                TextButton(onClick = {
                    viewModel.clearHistory()
                    showClearDialog = false
                }) {
                    Text("Очистить", color = MaterialTheme.colorScheme.primary)
                }
            },
            dismissButton = {
                TextButton(onClick = { showClearDialog = false }) {
                    Text("Отмена", color = Color.Gray)
                }
            },
            containerColor = SurfaceDark,
            textContentColor = Color.White,
            titleContentColor = Color.White
        )
    }

    if (showThemeDialog) {
        AlertDialog(
            onDismissRequest = { showThemeDialog = false },
            title = { Text("Тема оформления", color = Color.White) },
            text = {
                Column {
                    listOf("dark" to "Темная тема", "light" to "Светлая тема", "system" to "Системная тема").forEach { (value, label) ->
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .clickable {
                                    viewModel.setTheme(value)
                                    showThemeDialog = false
                                }
                                .padding(vertical = 12.dp)
                        ) {
                            RadioButton(
                                selected = currentTheme == value,
                                onClick = {
                                    viewModel.setTheme(value)
                                    showThemeDialog = false
                                },
                                colors = RadioButtonDefaults.colors(selectedColor = MaterialTheme.colorScheme.primary)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(label, color = Color.White, modifier = Modifier.align(Alignment.CenterVertically))
                        }
                    }
                }
            },
            confirmButton = {},
            containerColor = SurfaceDark,
            titleContentColor = Color.White
        )
    }

    if (showFontDialog) {
        AlertDialog(
            onDismissRequest = { showFontDialog = false },
            title = { Text("Размер шрифта", color = Color.White) },
            text = {
                Column {
                    listOf(0 to "Мелкий", 1 to "Средний", 2 to "Крупный").forEach { (value, label) ->
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .clickable {
                                    viewModel.setFontSize(value)
                                    showFontDialog = false
                                }
                                .padding(vertical = 12.dp)
                        ) {
                            RadioButton(
                                selected = currentFontSize == value,
                                onClick = {
                                    viewModel.setFontSize(value)
                                    showFontDialog = false
                                },
                                colors = RadioButtonDefaults.colors(selectedColor = MaterialTheme.colorScheme.primary)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(label, color = MaterialTheme.colorScheme.onBackground, modifier = Modifier.align(Alignment.CenterVertically))
                        }
                    }
                }
            },
            confirmButton = {},
            containerColor = MaterialTheme.colorScheme.surface,
            titleContentColor = MaterialTheme.colorScheme.onBackground
        )
    }

    if (showAccentDialog) {
        AlertDialog(
            onDismissRequest = { showAccentDialog = false },
            title = { Text("Акцентный цвет", color = MaterialTheme.colorScheme.onBackground) },
            text = {
                Column {
                    listOf("orange" to "Оранжевый", "olive" to "Оливковый", "blue" to "Голубой").forEach { (value, label) ->
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .clickable {
                                    viewModel.setAccentColor(value)
                                    showAccentDialog = false
                                }
                                .padding(vertical = 12.dp)
                        ) {
                            RadioButton(
                                selected = currentAccent == value,
                                onClick = {
                                    viewModel.setAccentColor(value)
                                    showAccentDialog = false
                                },
                                colors = RadioButtonDefaults.colors(selectedColor = MaterialTheme.colorScheme.primary)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(label, color = MaterialTheme.colorScheme.onBackground, modifier = Modifier.align(Alignment.CenterVertically))
                        }
                    }
                }
            },
            confirmButton = {},
            containerColor = MaterialTheme.colorScheme.surface,
            titleContentColor = MaterialTheme.colorScheme.onBackground
        )
    }

    if (showHomeListDialog) {
        AlertDialog(
            onDismissRequest = { showHomeListDialog = false },
            title = { Text("Список на главном экране", color = MaterialTheme.colorScheme.onBackground) },
            text = {
                Column {
                    listOf("recent" to "Недавно читали", "bookmarks" to "Избранное").forEach { (value, label) ->
                        Row(
                            Modifier
                                .fillMaxWidth()
                                .clickable {
                                    viewModel.setHomeListType(value)
                                    showHomeListDialog = false
                                }
                                .padding(vertical = 12.dp)
                        ) {
                            RadioButton(
                                selected = currentHomeList == value,
                                onClick = {
                                    viewModel.setHomeListType(value)
                                    showHomeListDialog = false
                                },
                                colors = RadioButtonDefaults.colors(selectedColor = MaterialTheme.colorScheme.primary)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(label, color = MaterialTheme.colorScheme.onBackground, modifier = Modifier.align(Alignment.CenterVertically))
                        }
                    }
                }
            },
            confirmButton = {},
            containerColor = MaterialTheme.colorScheme.surface,
            titleContentColor = MaterialTheme.colorScheme.onBackground
        )
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color(0xFF0D0D0D))
    ) {
        TopAppBar(
            title = {
                Text(
                    text = "Настройки",
                    color = Color.White,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = Color.Transparent
            )
        )

        Column(
            modifier = Modifier.padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Clear History
            SettingsItem(
                icon = Icons.Default.Delete,
                title = "Очистить недавние",
                subtitle = "Удалить историю просмотренных статей",
                onClick = { showClearDialog = true }
            )

            // Theme
            SettingsItem(
                icon = Icons.Default.Palette,
                title = "Тема оформления",
                subtitle = themeLabel,
                onClick = { showThemeDialog = true }
            )

            // Accent Color
            SettingsItem(
                icon = Icons.Default.Palette,
                title = "Акцентный цвет",
                subtitle = accentLabel,
                onClick = { showAccentDialog = true }
            )

            // Text Size
            SettingsItem(
                icon = Icons.Default.TextFormat,
                title = "Размер шрифта",
                subtitle = fontLabel,
                onClick = { showFontDialog = true }
            )

            // Home List Type
            SettingsItem(
                icon = Icons.Default.List,
                title = "Список на главной",
                subtitle = homeListLabel,
                onClick = { showHomeListDialog = true }
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "О приложении",
                color = Color.Gray,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(start = 16.dp, bottom = 8.dp)
            )

            SettingsItem(
                icon = Icons.Default.Info,
                title = "Версия 1.0",
                subtitle = "Основано на открытых данных SurvivalManual Wiki",
                onClick = { /* TODO: Open Github Link */ }
            )
        }
    }
}

@Composable
fun SettingsItem(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surface)
            .clickable(onClick = onClick)
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(24.dp)
        )
        Spacer(modifier = Modifier.width(16.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(
                text = title,
                color = MaterialTheme.colorScheme.onSurface,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = subtitle,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                fontSize = 12.sp
            )
        }
    }
}