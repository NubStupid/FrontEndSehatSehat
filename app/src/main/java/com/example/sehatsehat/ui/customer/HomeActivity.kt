package com.example.sehatsehat.ui.customer

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
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
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sehatsehat.R
import com.example.sehatsehat.SehatViewModelFactory
import com.example.sehatsehat.model.Article
import com.example.sehatsehat.model.FitnessProgram
import com.example.sehatsehat.model.UserEntity
import com.example.sehatsehat.ui.theme.SehatSehatTheme
import com.example.sehatsehat.viewmodel.HomeViewModel
import java.text.NumberFormat
import java.util.Currency
import java.util.Locale

class HomeActivity : ComponentActivity() {
    val vm by viewModels<HomeViewModel> { SehatViewModelFactory }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val activeUser = intent.getParcelableExtra<UserEntity>("active_user")?: UserEntity("","","","","","",0,"","")
        vm.init(activeUser)
        setContent {
            SehatSehatTheme {
                HomeScreen(
                    displayName = activeUser.display_name,
                    onNavigateToProgram = { program ->
                        startActivity(ViewProgramActivity.newIntent(this, program))
                    },
                    onNavigateToArticle = { article ->
                        startActivity(ArticleDetailActivity.newIntent(this, article))
                    },
                    onNavigateToProfile = {
                        val intent = Intent(this, ProfileActivity::class.java)
                        intent.putExtra("display_name", activeUser.display_name)
                        intent.putExtra("active_user", activeUser)
                        startActivity(intent)
                    }
                )
            }
        }
    }

    fun parseBalanceToIDR(balance:Int):String{
        val formatter = NumberFormat.getCurrencyInstance(Locale("in","ID"))
        formatter.currency = Currency.getInstance("IDR")
        formatter.maximumFractionDigits = 0
        return formatter.format(balance)
    }

    companion object {
        fun newIntent(context: Context, displayName: String? = null): Intent {
            return Intent(context, HomeActivity::class.java).apply {
                displayName?.let { putExtra("display_name", it) }
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
        val tabs = listOf("Articles", "Program")

        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
        ) {
            // Header Section
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "",
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
                            val userObs:State<UserEntity?> = vm.activeUser.observeAsState()
                            val user = userObs.value

                            Text(
                                text = user?.display_name?:"Not rendered yet!",
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
                                    text = "Wallet: ${parseBalanceToIDR(user?.balance?:0)}",
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
        val featuredArticlesObs:State<List<Article>?> = vm.featuredArticles.observeAsState()
        val featuredArticles = featuredArticlesObs.value

        val regularArticlesObs:State<List<Article>?> = vm.regularArticles.observeAsState()
        val regularArticles = regularArticlesObs.value

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
                items(featuredArticles.orEmpty()) { article ->
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
                items(regularArticles.orEmpty()) { article ->
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
                    }
                    Row(
                        modifier = Modifier.padding(top = 8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = " • ${article.date.split("T").get(0)}",
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
        val purchasedObs:State<List<FitnessProgram>?> = vm.purchasedPrograms.observeAsState()
        val purchasedPrograms = purchasedObs.value
        val availableObs:State<List<FitnessProgram>?> = vm.programAvailable.observeAsState()
        val availablePrograms = availableObs.value

//        val programs = listOf(
//            FitnessProgram(
//                id = 1,
//                title = "Slim Fit Project",
//                dateRange = "10/02/2026 - 10/03/2026",
//                description = "Medium",
//                duration = "15 Hours 30 Minutes",
//                progress = 0.7f,
//                backgroundGradient = listOf(0xFF6B46C1, 0xFF9333EA),
//                isPurchased = true,
//                detailedDescription = "DWNDQJDNQWDQWD"
//            ),
//            FitnessProgram(
//                id = 2,
//                title = "Muscle Building Pro",
//                dateRange = "15/02/2026 - 15/04/2026",
//                description = "High intensity strength training program",
//                duration = "25 Hours 45 Minutes",
//                progress = 0.0f,
//                backgroundGradient = listOf(0xFF059669, 0xFF10B981),
//                isPurchased = false,
//                detailedDescription = "High intensity strength training program"
//            )
//        )

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
//            Text(
//                text = "Available Programs",
//                fontSize = 18.sp,
//                fontWeight = FontWeight.Bold,
//                modifier = Modifier.padding(bottom = 8.dp)
//            )
//
//            // Using Column instead of LazyColumn for programs
//            Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
//                programs.forEach { program ->
//                    ProgramCard(
//                        program = program,
//                        onClick = { onNavigateToProgram(program) }
//                    )
//                }
//            }
            Text(
                text = "Purchased Programs",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp) // Keep this padding for the title
            )

            LazyColumn(
                modifier = Modifier
                    .weight(1f, fill = false) // Takes available space, shrinks if content is smaller
                    .fillMaxWidth()
                    .background(Color.LightGray.copy(alpha = 0.3f)), // Just for visualization
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(
                    items = purchasedPrograms.orEmpty(),
                    key = { program -> program.id } // Optional: Provide a stable key for better performance
                ) { program ->
                    ProgramCard(
                        program = program,
                        onClick = { onNavigateToProgram(program) }
                    )

                }
            }
            Text(
                text = "Available Programs",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp) // Keep this padding for the title
            )

            LazyColumn(
                modifier = Modifier
                    .weight(1f, fill = false) // Takes available space, shrinks if content is smaller
                    .fillMaxWidth()
                    .background(Color.LightGray.copy(alpha = 0.3f)), // Just for visualization
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(
                    items = availablePrograms.orEmpty(),
                    key = { program -> program.id } // Optional: Provide a stable key for better performance
                ) { program ->
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

}
