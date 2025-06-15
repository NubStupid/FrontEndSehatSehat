package com.example.sehatsehat.ui.customer

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sehatsehat.R
import com.example.sehatsehat.model.Article
import com.example.sehatsehat.model.FitnessProgram
import com.example.sehatsehat.ui.theme.SehatSehatTheme

class HomeActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val displayName = intent.getStringExtra("display_name") ?: "Hanvy Hendrawan"

        setContent {
            SehatSehatTheme {
                HomeScreen(
                    displayName = displayName,
                    onNavigateToProgram = { program ->
                        startActivity(ViewProgramActivity.newIntent(this, program))
                    },
                    onNavigateToArticle = { article ->
                        startActivity(ArticleDetailActivity.newIntent(this, article))
                    },
                    onNavigateToProfile = {
                        startActivity(ProfileActivity.newIntent(this, displayName))
                    }
                )
            }
        }
    }

    companion object {
        fun newIntent(context: Context, displayName: String? = null): Intent {
            return Intent(context, HomeActivity::class.java).apply {
                displayName?.let { putExtra("display_name", it) }
            }
        }
    }
}

@Composable
fun HomeScreen(
    displayName: String,
    onNavigateToProgram: (FitnessProgram) -> Unit,
    onNavigateToArticle: (Article) -> Unit,
    onNavigateToProfile: () -> Unit
) {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Schedule", "Program")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
    ) {
        // Header Section
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Welcome back,",
                color = Color.Gray,
                fontSize = 14.sp
            )
            IconButton(onClick = onNavigateToProfile) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_launcher_foreground),
                    contentDescription = "Profile",
                    tint = Color(0xFFFFB800)
                )
            }
        }

        // User Profile Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {
                // Profile Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.Top
                ) {
                    Column {
                        Text(
                            text = displayName,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black
                        )
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(top = 4.dp)
                        ) {
                            Icon(
                                Icons.Default.Star,
                                contentDescription = "Wallet",
                                tint = Color.Gray,
                                modifier = Modifier.size(16.dp)
                            )
                            Text(
                                text = "Wallet: Rp. 000.000,00",
                                color = Color.Gray,
                                fontSize = 12.sp,
                                modifier = Modifier.padding(start = 4.dp)
                            )
                        }
                    }
                    Text(
                        text = "Active",
                        color = Color(0xFF10B981),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Tab Row
                TabRow(
                    selectedTabIndex = selectedTab,
                    modifier = Modifier.fillMaxWidth(),
                    containerColor = Color.Transparent,
                    indicator = { tabPositions ->
                        TabRowDefaults.Indicator(
                            modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                            color = Color(0xFFFFB800)
                        )
                    }
                ) {
                    tabs.forEachIndexed { index, title ->
                        Tab(
                            selected = selectedTab == index,
                            onClick = { selectedTab = index },
                            text = {
                                Text(
                                    text = title,
                                    color = if (selectedTab == index) Color(0xFFFFB800) else Color.Gray,
                                    fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal
                                )
                            }
                        )
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Content based on selected tab
                when (selectedTab) {
                    0 -> ArticlesContent(onNavigateToArticle = onNavigateToArticle)
                    1 -> ProgramContent(onNavigateToProgram = onNavigateToProgram)
                }
            }
        }
    }
}

@Composable
fun ArticlesContent(onNavigateToArticle: (Article) -> Unit) {
    val featuredArticles = listOf(
        Article(
            id = 1,
            title = "The Ultimate Guide to Healthy Living",
            description = "Comprehensive guide covering nutrition, exercise, and mental wellness",
            date = "Today",
            isFeatured = true,
            author = "Dr. Sarah Johnson",
            readTime = "8 min read"
        ),
        Article(
            id = 2,
            title = "Benefits of Morning Exercise",
            description = "Discover why morning workouts can boost your energy",
            date = "Yesterday",
            isFeatured = true,
            author = "Mike Chen",
            readTime = "5 min read"
        ),
        Article(
            id = 5,
            title = "Sleep and Recovery for Athletes",
            description = "How proper sleep enhances athletic performance",
            date = "2 days ago",
            isFeatured = true,
            author = "Dr. Lisa Wong",
            readTime = "6 min read"
        ),
        Article(
            id = 6,
            title = "Nutrition for Muscle Growth",
            description = "Essential foods to support your muscle building journey",
            date = "3 days ago",
            isFeatured = true,
            author = "Dr. Mark Taylor",
            readTime = "7 min read"
        ),
        Article(
            id = 7,
            title = "Yoga for Stress Relief",
            description = "Simple yoga poses to reduce stress and anxiety",
            date = "4 days ago",
            isFeatured = true,
            author = "Emma Rodriguez",
            readTime = "5 min read"
        )
    )

    val regularArticles = listOf(
        Article(
            id = 3,
            title = "Healthy Diet Tips for Busy People",
            description = "Essential nutrition guidelines for busy schedules",
            date = "5 days ago",
            author = "Dr. Emma Wilson",
            readTime = "6 min read"
        ),
        Article(
            id = 4,
            title = "Mental Health and Fitness Connection",
            description = "Understanding how physical activity impacts mental wellbeing",
            date = "1 week ago",
            author = "Dr. James Mitchell",
            readTime = "7 min read"
        ),
        Article(
            id = 8,
            title = "Home Workout Essentials",
            description = "Effective exercises you can do without equipment",
            date = "1 week ago",
            author = "Coach David Lee",
            readTime = "5 min read"
        ),
        Article(
            id = 9,
            title = "Hydration and Performance",
            description = "Why water intake is crucial for your workouts",
            date = "2 weeks ago",
            author = "Dr. Anna Park",
            readTime = "4 min read"
        ),
        Article(
            id = 10,
            title = "Avoiding Workout Injuries",
            description = "Tips to prevent common exercise-related injuries",
            date = "2 weeks ago",
            author = "Dr. Robert Chen",
            readTime = "6 min read"
        )
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Text(
            text = "Featured Articles",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Horizontal scrolling for featured articles
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(bottom = 16.dp),
            modifier = Modifier.height(180.dp)
        ) {
            items(featuredArticles) { article ->
                FeaturedArticleCard(
                    article = article,
                    onClick = { onNavigateToArticle(article) }
                )
            }
        }

        Text(
            text = "Latest Articles",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Vertical scrolling for regular articles
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.height(300.dp) // Fixed height for vertical scrolling
        ) {
            items(regularArticles) { article ->
                RegularArticleCard(
                    article = article,
                    onClick = { onNavigateToArticle(article) }
                )
            }
        }
    }
}

@Composable
fun FeaturedArticleCard(article: Article, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .width(280.dp)
            .height(160.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(16.dp)
    ) {
        Box(modifier = Modifier.fillMaxSize()) {
            Image(
                painter = painterResource(id = R.drawable.ic_launcher_background), // Replace with your image
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier.fillMaxSize()
            )

            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.Black.copy(alpha = 0.7f)
                            )
                        )
                    )
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Surface(
                        shape = RoundedCornerShape(20.dp),
                        color = Color.White.copy(alpha = 0.2f)
                    ) {
                        Text(
                            text = article.readTime,
                            color = Color.White,
                            fontSize = 10.sp,
                            modifier = Modifier.padding(horizontal = 12.dp, vertical = 4.dp)
                        )
                    }

                    Column {
                        Text(
                            text = article.title,
                            color = Color.White,
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = article.description,
                            color = Color.White.copy(alpha = 0.8f),
                            fontSize = 12.sp,
                            maxLines = 2,
                            overflow = TextOverflow.Ellipsis,
                            modifier = Modifier.padding(top = 4.dp)
                        )
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(top = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "By ${article.author}",
                                color = Color.White.copy(alpha = 0.7f),
                                fontSize = 10.sp
                            )
                            Text(
                                text = article.date,
                                color = Color.White.copy(alpha = 0.6f),
                                fontSize = 10.sp
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun RegularArticleCard(article: Article, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF8F9FA)),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color(0xFFFFB800)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.Menu,
                    contentDescription = "Article",
                    tint = Color.White,
                    modifier = Modifier.size(24.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = article.title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
                Text(
                    text = article.description,
                    fontSize = 12.sp,
                    color = Color.Gray,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis,
                    modifier = Modifier.padding(top = 4.dp)
                )
                Row(
                    modifier = Modifier.padding(top = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = article.author,
                        fontSize = 11.sp,
                        color = Color.Gray,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = " • ${article.readTime}",
                        fontSize = 11.sp,
                        color = Color.Gray
                    )
                    Text(
                        text = " • ${article.date}",
                        fontSize = 11.sp,
                        color = Color.Gray
                    )
                }
            }

            Icon(
                Icons.Default.ArrowForward,
                contentDescription = "Read more",
                tint = Color.Gray,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
fun ProgramContent(onNavigateToProgram: (FitnessProgram) -> Unit) {
    val programs = listOf(
        FitnessProgram(
            id = 1,
            title = "Slim Fit Project",
            dateRange = "10/02/2026 - 10/03/2026",
            description = "Medium Intensity training with HIIT to burn fat",
            duration = "15 Hours 30 Minutes",
            progress = 0.7f,
            backgroundGradient = listOf(0xFF6B46C1, 0xFF9333EA),
            isPurchased = true
        ),
        FitnessProgram(
            id = 2,
            title = "Muscle Building Pro",
            dateRange = "15/02/2026 - 15/04/2026",
            description = "High intensity strength training program",
            duration = "25 Hours 45 Minutes",
            progress = 0.0f,
            backgroundGradient = listOf(0xFF059669, 0xFF10B981),
            isPurchased = false
        )
    )

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Text(
            text = "Available Programs",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            modifier = Modifier.padding(bottom = 8.dp)
        )

        // Using Column instead of LazyColumn for programs
        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            programs.forEach { program ->
                ProgramCard(
                    program = program,
                    onClick = { onNavigateToProgram(program) }
                )
            }
        }
    }
}

@Composable
fun ProgramCard(program: FitnessProgram, onClick: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp)
            .clickable(onClick = onClick),
        shape = RoundedCornerShape(20.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    if (program.isPurchased) {
                        Brush.horizontalGradient(
                            program.backgroundGradient.map { Color(it) }
                        )
                    } else {
                        Brush.horizontalGradient(
                            listOf(Color.Gray.copy(alpha = 0.3f), Color.Gray.copy(alpha = 0.5f)))
                    }
                )
        ) {
            Row(modifier = Modifier.fillMaxSize()) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(20.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    Column {
                        Text(
                            text = program.title,
                            color = if (program.isPurchased) Color.White else Color.Gray,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            lineHeight = 20.sp
                        )
                        Text(
                            text = if (program.isPurchased) program.dateRange else "Rp 150.000",
                            color = if (program.isPurchased) Color.White.copy(alpha = 0.9f) else Color.Gray.copy(alpha = 0.7f),
                            fontSize = 11.sp,
                            modifier = Modifier.padding(top = 2.dp)
                        )
                    }

                    Text(
                        text = program.description,
                        color = if (program.isPurchased) Color.White.copy(alpha = 0.95f) else Color.Gray.copy(alpha = 0.8f),
                        fontSize = 11.sp,
                        lineHeight = 14.sp,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )

                    Column {
                        if (program.isPurchased) {
                            Text(
                                text = program.duration,
                                color = Color.White,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                modifier = Modifier.padding(bottom = 6.dp)
                            )
                            LinearProgressIndicator(
                                progress = { program.progress },
                                modifier = Modifier
                                    .fillMaxWidth(0.85f)
                                    .height(4.dp)
                                    .clip(RoundedCornerShape(2.dp)),
                                color = Color(0xFFFFB800),
                                trackColor = Color.White.copy(alpha = 0.3f),
                            )
                        } else {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    Icons.Default.Star,
                                    contentDescription = "Rating",
                                    tint = Color(0xFFFFB800),
                                    modifier = Modifier.size(16.dp)
                                )
                                Text(
                                    text = "4.8 (1234 users)",
                                    color = Color.Gray,
                                    fontSize = 12.sp,
                                    modifier = Modifier.padding(start = 4.dp)
                                )
                            }
                        }
                    }
                }

                Box(
                    modifier = Modifier
                        .width(70.dp)
                        .fillMaxHeight()
                        .background(
                            if (program.isPurchased) Color(0xFFFFB800) else Color.Gray.copy(alpha = 0.6f)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        if (program.isPurchased) Icons.Default.PlayArrow else Icons.Default.Add,
                        contentDescription = if (program.isPurchased) "Start Program" else "Purchase Program",
                        tint = Color.White,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
        }
    }
}

