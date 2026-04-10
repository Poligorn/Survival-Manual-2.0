package com.survivalwiki.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.MedicalServices
import androidx.compose.material.icons.filled.Psychology
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items as lazyItems
import com.survivalwiki.data.entity.Category
import com.survivalwiki.data.entity.Article
import com.survivalwiki.ui.SurvivalViewModel
import com.survivalwiki.ui.utils.getIconForName
import com.survivalwiki.ui.screens.RecentArticleCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CatalogScreen(viewModel: SurvivalViewModel, onArticleClick: (Int) -> Unit) {
    val categories by viewModel.categories.collectAsState()
    val allArticles by viewModel.allArticles.collectAsState()
    var showFilterDialog by remember { mutableStateOf(false) }

    var selectedLevel by remember { mutableStateOf("Все") }
    var selectedSort by remember { mutableStateOf("По алфавиту") }
    var expandedSort by remember { mutableStateOf(false) }
    var expandedLevel by remember { mutableStateOf(false) }

    val isFilterActive = selectedLevel != "Все" || selectedSort != "По алфавиту"
    
    val displayArticles = remember(allArticles, selectedLevel, selectedSort) {
        var list = allArticles
        if (selectedLevel != "Все") {
            // Level is stored as something like "Уровень: Базовый", so we check contains
            list = list.filter { it.level != null && it.level.contains(selectedLevel, ignoreCase = true) }
        }
        
        list = when (selectedSort) {
            "По дате обновления" -> list.sortedByDescending { it.lastUpdated ?: "0" }
            "По времени открытия" -> list.sortedByDescending { it.lastReadTimestamp }
            else -> list.sortedBy { it.title }
        }
        list
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .padding(horizontal = 16.dp)
    ) {
        Spacer(modifier = Modifier.height(24.dp))
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Каталог",
                color = MaterialTheme.colorScheme.onBackground,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )
            Icon(
                imageVector = Icons.Default.FilterList,
                contentDescription = "Filter",
                tint = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier
                    .size(24.dp)
                    .clickable { showFilterDialog = true }
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        if (!isFilterActive) {
            LazyVerticalGrid(
                columns = GridCells.Fixed(2),
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(categories) { category ->
                    CategoryCard(category, onClick = { 
                        // Right now, onClick opens article 1, but we'll stick to what we have or you'd open article list
                        onArticleClick(category.id) 
                    })
                }
            }
        } else {
            if (displayArticles.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    Text(
                        "Ничего не найдено", 
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        fontSize = 16.sp
                    )
                }
            } else {
                LazyVerticalGrid(
                    columns = GridCells.Fixed(2),
                    horizontalArrangement = Arrangement.spacedBy(16.dp),
                    verticalArrangement = Arrangement.spacedBy(16.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(displayArticles) { article ->
                        ArticleGridCard(
                            article = article, 
                            onClick = { onArticleClick(article.id) }
                        )
                    }
                }
            }
        }
    }

    if (showFilterDialog) {
        AlertDialog(
            onDismissRequest = { showFilterDialog = false },
            title = { Text("Сортировка и фильтры", color = MaterialTheme.colorScheme.onBackground) },
            text = {
                Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
                    Text("Сортировка", color = MaterialTheme.colorScheme.onBackground, fontWeight = FontWeight.Bold)
                    ExposedDropdownMenuBox(
                        expanded = expandedSort,
                        onExpandedChange = { expandedSort = !expandedSort }
                    ) {
                        OutlinedTextField(
                            value = selectedSort,
                            onValueChange = {},
                            readOnly = true,
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedSort) },
                            modifier = Modifier.menuAnchor(),
                            colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(
                                focusedTextColor = MaterialTheme.colorScheme.onBackground,
                                unfocusedTextColor = MaterialTheme.colorScheme.onBackground
                            )
                        )
                        ExposedDropdownMenu(
                            expanded = expandedSort,
                            onDismissRequest = { expandedSort = false },
                            modifier = Modifier.background(MaterialTheme.colorScheme.surface)
                        ) {
                            listOf("По алфавиту", "По дате обновления", "По времени открытия").forEach { option ->
                                DropdownMenuItem(
                                    text = { Text(option, color = MaterialTheme.colorScheme.onSurface) },
                                    onClick = {
                                        selectedSort = option
                                        expandedSort = false
                                    }
                                )
                            }
                        }
                    }

                    Text("Уровень сложности", color = MaterialTheme.colorScheme.onBackground, fontWeight = FontWeight.Bold)
                    ExposedDropdownMenuBox(
                        expanded = expandedLevel,
                        onExpandedChange = { expandedLevel = !expandedLevel }
                    ) {
                        OutlinedTextField(
                            value = selectedLevel,
                            onValueChange = {},
                            readOnly = true,
                            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expandedLevel) },
                            modifier = Modifier.menuAnchor(),
                            colors = ExposedDropdownMenuDefaults.outlinedTextFieldColors(
                                focusedTextColor = MaterialTheme.colorScheme.onBackground,
                                unfocusedTextColor = MaterialTheme.colorScheme.onBackground
                            )
                        )
                        ExposedDropdownMenu(
                            expanded = expandedLevel,
                            onDismissRequest = { expandedLevel = false },
                            modifier = Modifier.background(MaterialTheme.colorScheme.surface)
                        ) {
                            listOf("Все", "Базовый", "Средний", "Продвинутый").forEach { option ->
                                DropdownMenuItem(
                                    text = { Text(option, color = MaterialTheme.colorScheme.onSurface) },
                                    onClick = {
                                        selectedLevel = option
                                        expandedLevel = false
                                    }
                                )
                            }
                        }
                    }
                }
            },
            confirmButton = {
                Button(
                    onClick = { showFilterDialog = false },
                    colors = ButtonDefaults.buttonColors(containerColor = MaterialTheme.colorScheme.primary)
                ) {
                    Text("Применить", color = MaterialTheme.colorScheme.onPrimary)
                }
            },
            dismissButton = {
                OutlinedButton(onClick = { showFilterDialog = false }) {
                    Text("Сбросить", color = MaterialTheme.colorScheme.onBackground)
                }
            },
            containerColor = MaterialTheme.colorScheme.surface,
            titleContentColor = MaterialTheme.colorScheme.onBackground
        )
    }
}

@Composable
fun CategoryCard(category: Category, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f) // make it square-ish based on the design
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surface)
            .clickable(onClick = onClick)
            .padding(16.dp)
    ) {
        val iconVector = getIconForName(category.iconResName)
        Icon(
            imageVector = iconVector,
            contentDescription = category.title,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(48.dp)
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = category.title,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Bold,
            fontSize = 14.sp,
            textAlign = TextAlign.Center
        )
    }
}

@Composable
fun ArticleGridCard(article: Article, onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
        modifier = Modifier
            .fillMaxWidth()
            .aspectRatio(1f) // make it square-ish based on the design
            .clip(RoundedCornerShape(16.dp))
            .background(MaterialTheme.colorScheme.surface)
            .clickable(onClick = onClick)
            .padding(16.dp)
    ) {
        val iconVector = getIconForName(article.iconResName)
        Icon(
            imageVector = iconVector,
            contentDescription = article.title,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(40.dp)
        )
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = article.title,
            color = MaterialTheme.colorScheme.onSurface,
            fontWeight = FontWeight.Bold,
            fontSize = 13.sp,
            textAlign = TextAlign.Center,
            maxLines = 2
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = article.level ?: "Статья",
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            fontSize = 11.sp,
            textAlign = TextAlign.Center
        )
    }
}