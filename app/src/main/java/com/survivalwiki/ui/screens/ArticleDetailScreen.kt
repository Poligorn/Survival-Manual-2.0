package com.survivalwiki.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import coil.request.ImageRequest
import com.survivalwiki.data.entity.Article
import com.survivalwiki.ui.SurvivalViewModel
import com.survivalwiki.ui.utils.getIconForName
import com.survivalwiki.ui.theme.PrimaryOrange
import com.survivalwiki.ui.theme.SurfaceDark

import androidx.compose.ui.unit.TextUnit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArticleDetailScreen(
    articleId: Int,
    viewModel: SurvivalViewModel,
    onBackClick: () -> Unit
) {
    val article by viewModel.getArticleById(articleId).collectAsState()
    val fontSizePref by viewModel.fontSizeFlow.collectAsState(initial = 1)
    
    val baseTextSize = when(fontSizePref) {
        0 -> 14.sp
        2 -> 20.sp
        else -> 16.sp
    }
    
    val titleSize = when(fontSizePref) {
        0 -> 24.sp
        2 -> 32.sp
        else -> 28.sp
    }

    LaunchedEffect(articleId) {
        viewModel.markArticleAsRead(articleId)
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White
                        )
                    }
                },
                actions = {
                    article?.let { currentArticle ->
                        val isBookmarked = currentArticle.isBookmarked
                        IconButton(onClick = { viewModel.toggleBookmark(currentArticle.id) }) {
                            Icon(
                                imageVector = if (isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                                contentDescription = "Bookmark",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF000000),
                    navigationIconContentColor = Color.White,
                    actionIconContentColor = MaterialTheme.colorScheme.primary
                )
            )
        },
        containerColor = Color(0xFF000000)
    ) { paddingValues ->
        if (article == null) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator(color = MaterialTheme.colorScheme.primary)
            }
        } else {
            val currentArticle = article!!
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(horizontal = 20.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Title
                item {
                    Text(
                        text = currentArticle.title,
                        color = Color.White,
                        fontSize = titleSize,
                        fontWeight = FontWeight.Bold,
                        lineHeight = titleSize * 1.2
                    )
                }

                // Tags / Categories
                item {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        // Category Chip
                        Row(
                            modifier = Modifier
                                .clip(RoundedCornerShape(16.dp))
                                .background(SurfaceDark)
                                .padding(horizontal = 12.dp, vertical = 6.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = getIconForName(currentArticle.iconResName),
                                contentDescription = null,
                                tint = MaterialTheme.colorScheme.primary,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = currentArticle.tags.split(",").firstOrNull()?.removePrefix("#")?.replaceFirstChar { it.uppercase() } ?: "Общее",
                                color = Color.White,
                                fontSize = 14.sp
                            )
                        }

                        // Level Chip
                        currentArticle.level?.let { level ->
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(16.dp))
                                    .border(1.dp, Color.DarkGray, RoundedCornerShape(16.dp))
                                    .padding(horizontal = 12.dp, vertical = 6.dp)
                            ) {
                                Text(
                                    text = level,
                                    color = Color.LightGray,
                                    fontSize = 14.sp
                                )
                            }
                        }
                    }
                }

                // Metadata (Updated time, Read time)
                item {
                    Text(
                        text = "Обновлено: ${currentArticle.lastUpdated ?: "Неизвестно"} • Время чтения: ${currentArticle.readTimeMin} мин",
                        color = Color.Gray,
                        fontSize = 12.sp
                    )
                    Spacer(modifier = Modifier.height(8.dp))
                }

                // Content Parser (Markdown-like custom logic for images and text)
                item {
                    ArticleContent(content = currentArticle.content, baseTextSize = baseTextSize)
                }
                
                item {
                    Spacer(modifier = Modifier.height(40.dp))
                }
            }
        }
    }
}

@Composable
fun ArticleContent(content: String, baseTextSize: TextUnit = 16.sp) {
    // A simple parser that looks for <image:filename.png:Caption text> tags
    // and splits the content into text blocks and image blocks.
    val imageRegex = Regex("<image:(.*?):(.*?)>")
    var lastIndex = 0

    val matchResults = imageRegex.findAll(content)

    for (match in matchResults) {
        val textBefore = content.substring(lastIndex, match.range.first)
        if (textBefore.isNotBlank()) {
            ParsedText(text = textBefore, baseTextSize = baseTextSize)
            Spacer(modifier = Modifier.height(16.dp))
        }

        val filename = match.groupValues[1]
        val caption = match.groupValues[2]

        ArticleImage(filename = filename, caption = caption)
        Spacer(modifier = Modifier.height(16.dp))

        lastIndex = match.range.last + 1
    }

    val remainingText = content.substring(lastIndex)
    if (remainingText.isNotBlank()) {
        ParsedText(text = remainingText, baseTextSize = baseTextSize)
    }
}

@Composable
fun ParsedText(text: String, baseTextSize: TextUnit = 16.sp) {
    // Very basic markdown parser for Headings (##) and bold text.
    // For a real app, use a dedicated Markdown library like compose-markdown.
    val lines = text.trim().split("\n")
    
    Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
        for (line in lines) {
            val hashMatch = Regex("^#{1,6}\\s+(.*)").find(line)
            when {
                hashMatch != null -> {
                    val level = line.takeWhile { it == '#' }.length
                    val content = hashMatch.groupValues[1]
                    val sizeMultiplier = 1.0f + (0.1f * (6 - level)) // Smaller level number = larger text
                    
                    Spacer(modifier = Modifier.height(if (level <= 2) 12.dp else 8.dp))
                    Text(
                        text = content,
                        color = Color.White,
                        fontSize = baseTextSize * sizeMultiplier,
                        fontWeight = FontWeight.Bold
                    )
                }
                line.isNotBlank() -> {
                    Text(
                        text = line,
                        color = Color.LightGray,
                        fontSize = baseTextSize,
                        lineHeight = baseTextSize * 1.5
                    )
                }
            }
        }
    }
}

@Composable
fun ArticleImage(filename: String, caption: String) {
    val context = LocalContext.current
    
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(SurfaceDark),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Coil can load files from assets using the file:///android_asset/ path
        AsyncImage(
            model = ImageRequest.Builder(context)
                .data("file:///android_asset/images/$filename")
                .crossfade(true)
                .build(),
            contentDescription = caption,
            contentScale = ContentScale.FillWidth,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentHeight() // Maintains aspect ratio
        )
        
        if (caption.isNotBlank()) {
            Text(
                text = caption,
                color = Color.Gray,
                fontSize = 12.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(12.dp)
            )
        }
    }
}