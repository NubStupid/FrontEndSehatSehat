package com.example.sehatsehat.ui.customer

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
import androidx.compose.foundation.shape.CircleShape
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
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sehatsehat.ui.theme.SehatSehatTheme

class CustomerHomepageActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val displayName = intent.getStringExtra("display_name") ?: "Hanvy Hendrawan"

        setContent {
            SehatSehatTheme {
                var currentScreen by remember { mutableStateOf<Screen>(Screen.Home) }

                when (currentScreen) {
                    is Screen.Home -> CustomerHomepage(
                        displayName = displayName,
                        onNavigateToProgram = { program -> currentScreen = Screen.ProgramDetail(program) },
                        onNavigateToArticle = { article -> currentScreen = Screen.ArticleDetail(article) }
                    )
                    is Screen.ProgramDetail -> ProgramDetailScreen(
                        program = (currentScreen as Screen.ProgramDetail).program,
                        onBackClick = { currentScreen = Screen.Home },
                        onPurchaseClick = { program ->
                            // Handle purchase logic here
                            currentScreen = Screen.Home
                        }
                    )
                    is Screen.ArticleDetail -> ArticleDetailScreen(
                        article = (currentScreen as Screen.ArticleDetail).article,
                        onBackClick = { currentScreen = Screen.Home }
                    )
                }
            }
        }
    }
}

sealed class Screen {
    object Home : Screen()
    data class ProgramDetail(val program: FitnessProgram) : Screen()
    data class ArticleDetail(val article: Article) : Screen()
}

data class FitnessProgram(
    val id: Int,
    val title: String,
    val dateRange: String,
    val description: String,
    val duration: String,
    val progress: Float,
    val backgroundGradient: List<Color>,
    val isPurchased: Boolean = false,
    val price: String = "Rp 150.000",
    val rating: Float = 4.8f,
    val totalUsers: Int = 1234,
    val instructor: String = "John Doe",
    val benefits: List<String> = listOf(),
    val detailedDescription: String = ""
)

data class Article(
    val id: Int,
    val title: String,
    val description: String,
    val date: String,
    val imageUrl: String? = null,
    val isFeatured: Boolean = false,
    val author: String = "Dr. Smith",
    val readTime: String = "5 min read",
    val content: String = "",
    val tags: List<String> = listOf()
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomerHomepage(
    displayName: String,
    onNavigateToProgram: (FitnessProgram) -> Unit,
    onNavigateToArticle: (Article) -> Unit
) {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Schedule", "Program")

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .statusBarsPadding()
    ) {
        item {
            // Header Section
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Customer Non Expert Home User",
                    color = Color.Gray,
                    fontSize = 14.sp
                )
                Text(
                    text = "Customer Non Expert Home User",
                    color = Color.Gray,
                    fontSize = 14.sp
                )
            }
        }

        item {
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
                            text = "Status",
                            color = Color.Gray,
                            fontSize = 12.sp
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
}

@Composable
fun ArticlesContent(onNavigateToArticle: (Article) -> Unit) {
    // Enhanced dummy articles data
    val featuredArticles = listOf(
        Article(
            id = 1,
            title = "The Ultimate Guide to Healthy Living",
            description = "Comprehensive guide covering nutrition, exercise, and mental wellness for a balanced lifestyle.",
            date = "Today",
            isFeatured = true,
            author = "Dr. Sarah Johnson",
            readTime = "8 min read",
            content = "Lorem ipsum dolor sit amet, consectetur adipiscing elit...",
            tags = listOf("Health", "Lifestyle", "Wellness")
        ),
        Article(
            id = 2,
            title = "Benefits of Morning Exercise",
            description = "Discover why morning workouts can boost your energy throughout the day",
            date = "Yesterday",
            isFeatured = true,
            author = "Mike Chen",
            readTime = "5 min read",
            content = "Lorem ipsum dolor sit amet, consectetur adipiscing elit...",
            tags = listOf("Exercise", "Morning", "Energy")
        ),
        Article(
            id = 3,
            title = "Healthy Diet Tips for Busy People",
            description = "Essential nutrition guidelines for maintaining a balanced diet despite a hectic schedule",
            date = "2 days ago",
            isFeatured = true,
            author = "Dr. Emma Wilson",
            readTime = "6 min read",
            content = "Lorem ipsum dolor sit amet, consectetur adipiscing elit...",
            tags = listOf("Nutrition", "Diet", "Busy Life")
        )
    )

    val regularArticles = listOf(
        Article(
            id = 4,
            title = "Mental Health and Physical Fitness Connection",
            description = "Understanding how physical activity impacts mental wellbeing and cognitive function",
            date = "3 days ago",
            author = "Dr. James Mitchell",
            readTime = "7 min read",
            content = "Lorem ipsum dolor sit amet, consectetur adipiscing elit...",
            tags = listOf("Mental Health", "Fitness")
        ),
        Article(
            id = 5,
            title = "Hydration: The Key to Optimal Performance",
            description = "Learn about proper hydration strategies for different activities and climates",
            date = "4 days ago",
            author = "Lisa Rodriguez",
            readTime = "4 min read",
            content = "Lorem ipsum dolor sit amet, consectetur adipiscing elit...",
            tags = listOf("Hydration", "Performance")
        ),
        Article(
            id = 6,
            title = "Sleep Optimization for Athletes",
            description = "Essential sleep strategies for muscle recovery and peak performance",
            date = "5 days ago",
            author = "Dr. Mark Thompson",
            readTime = "6 min read",
            content = "Lorem ipsum dolor sit amet, consectetur adipiscing elit...",
            tags = listOf("Sleep", "Recovery", "Athletes")
        ),
        Article(
            id = 7,
            title = "Home Workout Equipment Guide",
            description = "Complete guide to building an effective home gym on any budget",
            date = "1 week ago",
            author = "Alex Johnson",
            readTime = "9 min read",
            content = "Lorem ipsum dolor sit amet, consectetur adipiscing elit...",
            tags = listOf("Home Gym", "Equipment")
        )
    )

    Column {
        // Featured Articles - Horizontal Scrollable
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            contentPadding = PaddingValues(horizontal = 4.dp)
        ) {
            items(featuredArticles) { article ->
                FeaturedArticleCard(
                    article = article,
                    onClick = { onNavigateToArticle(article) }
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Regular Articles - Vertical Scrollable
        LazyColumn(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.height(400.dp)
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
fun FeaturedArticleCard(
    article: Article,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(280.dp)
            .height(160.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFF2D3748))
    ) {
        Box(
            modifier = Modifier.fillMaxSize()
        ) {
            // Background gradient
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color(0xFF667EEA),
                                Color(0xFF764BA2)
                            )
                        )
                    )
            )

            // Content overlay
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color.Transparent,
                                Color.Black.copy(alpha = 0.6f)
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
                    // Read time badge
                    Surface(
                        modifier = Modifier.align(Alignment.Start),
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

                    // Text content at bottom
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
fun RegularArticleCard(
    article: Article,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onClick() },
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
            // Article image placeholder
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(Color(0xFFFFB6C1), Color(0xFFFF69B4))
                        )
                    ),
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

            // Article content
            Column(
                modifier = Modifier.weight(1f)
            ) {
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

            // Arrow icon
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
    // Enhanced program list with purchase status
    val programs = listOf(
        FitnessProgram(
            id = 1,
            title = "Slim Fit Project",
            dateRange = "10/02/2026 - 10/03/2026",
            description = "Medium Intensity training with a combination of HIIT to burn fat and increase stamina in a short time.",
            duration = "15 Hours 30 Minutes",
            progress = 0.7f,
            backgroundGradient = listOf(Color(0xFF6B46C1), Color(0xFF9333EA)),
            isPurchased = true,
            price = "Rp 150.000",
            rating = 4.8f,
            totalUsers = 1234,
            instructor = "Sarah Johnson",
            benefits = listOf("Burn fat effectively", "Increase stamina", "Build lean muscle", "Improve cardiovascular health"),
            detailedDescription = "This comprehensive program combines high-intensity interval training with strength exercises to maximize fat burning while preserving muscle mass. Perfect for those looking to achieve a lean, toned physique."
        ),
        FitnessProgram(
            id = 2,
            title = "Muscle Building Pro",
            dateRange = "15/02/2026 - 15/04/2026",
            description = "High intensity strength training program designed to build lean muscle mass and improve overall body composition.",
            duration = "25 Hours 45 Minutes",
            progress = 0.0f,
            backgroundGradient = listOf(Color(0xFF059669), Color(0xFF10B981)),
            isPurchased = false,
            price = "Rp 200.000",
            rating = 4.9f,
            totalUsers = 856,
            instructor = "Mike Chen",
            benefits = listOf("Build muscle mass", "Increase strength", "Improve body composition", "Enhanced metabolism"),
            detailedDescription = "An advanced muscle building program that focuses on progressive overload and proper nutrition guidance to help you build significant muscle mass and strength."
        ),
        FitnessProgram(
            id = 3,
            title = "Cardio Blast",
            dateRange = "01/03/2026 - 30/03/2026",
            description = "Intensive cardiovascular training to improve heart health and burn maximum calories in minimum time.",
            duration = "12 Hours 20 Minutes",
            progress = 0.0f,
            backgroundGradient = listOf(Color(0xFFDC2626), Color(0xFFEF4444)),
            isPurchased = false,
            price = "Rp 120.000",
            rating = 4.7f,
            totalUsers = 2341,
            instructor = "Lisa Rodriguez",
            benefits = listOf("Improve cardiovascular health", "Burn calories fast", "Increase endurance", "Boost metabolism"),
            detailedDescription = "High-energy cardio workouts designed to maximize calorie burn and improve cardiovascular fitness in the shortest time possible."
        ),
        FitnessProgram(
            id = 4,
            title = "Flexibility Master",
            dateRange = "05/03/2026 - 05/05/2026",
            description = "Comprehensive flexibility and mobility program combining yoga, stretching, and movement patterns.",
            duration = "18 Hours 15 Minutes",
            progress = 0.0f,
            backgroundGradient = listOf(Color(0xFF7C3AED), Color(0xFF8B5CF6)),
            isPurchased = false,
            price = "Rp 100.000",
            rating = 4.6f,
            totalUsers = 1567,
            instructor = "Emma Wilson",
            benefits = listOf("Improve flexibility", "Reduce injury risk", "Better posture", "Stress relief"),
            detailedDescription = "A comprehensive flexibility program that combines various stretching techniques, yoga poses, and mobility exercises to improve your range of motion and overall movement quality."
        )
    )

    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(bottom = 16.dp),
        modifier = Modifier.height(500.dp)
    ) {
        items(programs) { program ->
            ProgramCard(
                program = program,
                onClick = { onNavigateToProgram(program) }
            )
        }
    }
}

@Composable
fun ProgramCard(
    program: FitnessProgram,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp)
            .clickable { onClick() },
        shape = RoundedCornerShape(20.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    if (program.isPurchased) {
                        Brush.horizontalGradient(program.backgroundGradient)
                    } else {
                        Brush.horizontalGradient(
                            listOf(Color.Gray.copy(alpha = 0.3f), Color.Gray.copy(alpha = 0.5f))
                        )
                    }
                )
        ) {
            Row(
                modifier = Modifier.fillMaxSize()
            ) {
                // Left side content
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .padding(20.dp),
                    verticalArrangement = Arrangement.SpaceBetween
                ) {
                    // Top section - Title and Date
                    Column {
                        Text(
                            text = program.title,
                            color = if (program.isPurchased) Color.White else Color.Gray,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            lineHeight = 20.sp
                        )
                        Text(
                            text = if (program.isPurchased) program.dateRange else program.price,
                            color = if (program.isPurchased) Color.White.copy(alpha = 0.9f) else Color.Gray.copy(alpha = 0.7f),
                            fontSize = 11.sp,
                            modifier = Modifier.padding(top = 2.dp)
                        )
                    }

                    // Middle section - Description
                    Text(
                        text = program.description,
                        color = if (program.isPurchased) Color.White.copy(alpha = 0.95f) else Color.Gray.copy(alpha = 0.8f),
                        fontSize = 11.sp,
                        lineHeight = 14.sp,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )

                    // Bottom section - Duration and Progress or Rating
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
                            Row(
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    Icons.Default.Star,
                                    contentDescription = "Rating",
                                    tint = Color(0xFFFFB800),
                                    modifier = Modifier.size(16.dp)
                                )
                                Text(
                                    text = "${program.rating} (${program.totalUsers} users)",
                                    color = Color.Gray,
                                    fontSize = 12.sp,
                                    modifier = Modifier.padding(start = 4.dp)
                                )
                            }
                        }
                    }
                }

                // Right side - Arrow or Plus button
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProgramDetailScreen(
    program: FitnessProgram,
    onBackClick: () -> Unit,
    onPurchaseClick: (FitnessProgram) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .statusBarsPadding()
    ) {
        item {
            // Header with back button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.Black
                    )
                }
                Text(
                    text = "Program Details",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }

        item {
            // Program Hero Card
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(200.dp),
                shape = RoundedCornerShape(20.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Brush.horizontalGradient(program.backgroundGradient))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(24.dp),
                        verticalArrangement = Arrangement.SpaceBetween
                    ) {
                        Column {
                            Text(
                                text = program.title,
                                color = Color.White,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold
                            )
                            Text(
                                text = "by ${program.instructor}",
                                color = Color.White.copy(alpha = 0.9f),
                                fontSize = 14.sp,
                                modifier = Modifier.padding(top = 4.dp)
                            )
                        }

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.Bottom
                        ) {
                            Column {
                                Text(
                                    text = program.duration,
                                    color = Color.White,
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                                Row(
                                    verticalAlignment = Alignment.CenterVertically,
                                    modifier = Modifier.padding(top = 4.dp)
                                ) {
                                    Icon(
                                        Icons.Default.Star,
                                        contentDescription = "Rating",
                                        tint = Color(0xFFFFB800),
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Text(
                                        text = "${program.rating} (${program.totalUsers})",
                                        color = Color.White,
                                        fontSize = 12.sp,
                                        modifier = Modifier.padding(start = 4.dp)
                                    )
                                }
                            }

                            if (!program.isPurchased) {
                                Surface(
                                    shape = RoundedCornerShape(20.dp),
                                    color = Color(0xFFFFB800)
                                ) {
                                    Text(
                                        text = program.price,
                                        color = Color.White,
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        item {
            // Program Details Section
            Column(
                modifier = Modifier.padding(horizontal = 16.dp)
            ) {
                Text(
                    text = "About This Program",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Text(
                    text = program.detailedDescription,
                    fontSize = 14.sp,
                    lineHeight = 20.sp,
                    modifier = Modifier.padding(bottom = 16.dp)
                )

                Text(
                    text = "Key Benefits",
                    fontSize = 18.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Column(
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(bottom = 16.dp)
                ) {
                    program.benefits.forEach { benefit ->
                        Row(
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                Icons.Default.CheckCircle,
                                contentDescription = "Benefit",
                                tint = Color(0xFF10B981),
                                modifier = Modifier.size(20.dp)
                            )
                            Text(
                                text = benefit,
                                fontSize = 14.sp,
                                modifier = Modifier.padding(start = 8.dp)
                            )
                        }
                    }
                }
            }
        }

        item {
            // Action Button
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Button(
                    onClick = { onPurchaseClick(program) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(50.dp),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (program.isPurchased) Color(0xFF10B981) else Color(0xFFFFB800)
                    )
                ) {
                    Text(
                        text = if (program.isPurchased) "Start Program" else "Purchase Now",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class, ExperimentalLayoutApi::class)
@Composable
fun ArticleDetailScreen(
    article: Article,
    onBackClick: () -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .statusBarsPadding()
    ) {
        item {
            // Header with back button
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        Icons.Default.ArrowBack,
                        contentDescription = "Back",
                        tint = Color.Black
                    )
                }
                Text(
                    text = "Article Details",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(start = 8.dp)
                )
            }
        }

        item {
            // Article Hero Image
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .background(
                        Brush.horizontalGradient(
                            colors = listOf(Color(0xFFFFB6C1), Color(0xFFFF69B4))
                        )
                    ),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.Menu,
                    contentDescription = "Article",
                    tint = Color.White,
                    modifier = Modifier.size(48.dp)
                )
            }
        }

        item {
            // Article Content
            Column(
                modifier = Modifier.padding(16.dp)
            ) {
                Text(
                    text = article.title,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(bottom = 8.dp)
                )

                Row(
                    modifier = Modifier.padding(bottom = 16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "By ${article.author}",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                    Text(
                        text = " • ${article.readTime}",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                    Text(
                        text = " • ${article.date}",
                        fontSize = 14.sp,
                        color = Color.Gray
                    )
                }

                // Tags
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(bottom = 16.dp)
                ) {
                    article.tags.forEach { tag ->
                        Surface(
                            shape = RoundedCornerShape(20.dp),
                            color = Color(0xFFE2E8F0)
                        ) {
                            Text(
                                text = tag,
                                fontSize = 12.sp,
                                color = Color(0xFF4A5568),
                                modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                            )
                        }
                    }
                }

                Text(
                    text = article.content,
                    fontSize = 16.sp,
                    lineHeight = 24.sp,
                    modifier = Modifier.padding(bottom = 16.dp)
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewCustomerHomepage() {
    SehatSehatTheme {
        CustomerHomepage(
            displayName = "Hanvy Hendrawan",
            onNavigateToProgram = {},
            onNavigateToArticle = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewProgramDetailScreen() {
    SehatSehatTheme {
        ProgramDetailScreen(
            program = FitnessProgram(
                id = 1,
                title = "Slim Fit Project",
                dateRange = "10/02/2026 - 10/03/2026",
                description = "Medium Intensity training with a combination of HIIT to burn fat and increase stamina in a short time.",
                duration = "15 Hours 30 Minutes",
                progress = 0.7f,
                backgroundGradient = listOf(Color(0xFF6B46C1), Color(0xFF9333EA)),
                isPurchased = true,
                price = "Rp 150.000",
                rating = 4.8f,
                totalUsers = 1234,
                instructor = "John Doe",
                benefits = listOf("Burn fat effectively", "Increase stamina", "Build lean muscle", "Improve cardiovascular health"),
                detailedDescription = "This comprehensive program combines high-intensity interval training with strength exercises to maximize fat burning while preserving muscle mass. Perfect for those looking to achieve a lean, toned physique."
            ),
            onBackClick = {},
            onPurchaseClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewArticleDetailScreen() {
    SehatSehatTheme {
        ArticleDetailScreen(
            article = Article(
                id = 1,
                title = "The Ultimate Guide to Healthy Living",
                description = "Comprehensive guide covering nutrition, exercise, and mental wellness for a balanced lifestyle.",
                date = "Today",
                isFeatured = true,
                author = "Dr. Sarah Johnson",
                readTime = "8 min read",
                content = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat. Duis aute irure dolor in reprehenderit in voluptate velit esse cillum dolore eu fugiat nulla pariatur. Excepteur sint occaecat cupidatat non proident, sunt in culpa qui officia deserunt mollit anim id est laborum.",
                tags = listOf("Health", "Lifestyle", "Wellness")
            ),
            onBackClick = {}
        )
    }
}