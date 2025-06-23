package com.example.sehatsehat.ui.customer

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
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
import androidx.compose.ui.platform.LocalContext
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

    // Launcher untuk PaymentActivity
    private val paymentLauncher = registerForActivityResult(
        ActivityResultContracts.StartActivityForResult()
    ) { result ->
        if (result.resultCode == Activity.RESULT_OK) {
            val newBal = result.data?.getIntExtra("new_balance", vm.activeUser.value?.balance ?: 0)
            newBal?.let { vm.updateBalance(it) }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val activeUser = intent.getParcelableExtra<UserEntity>("active_user")
            ?: return finish()
        vm.init(activeUser)

        setContent {
            SehatSehatTheme {
                HomeScreen(
                    viewModel = vm,
                    onNavigateToProgram = { program ->
                        Intent(this, PaymentActivity::class.java).also {
                            it.putExtra("program", program)
                            it.putExtra("active_user", vm.activeUser.value)
                            paymentLauncher.launch(it)
                        }
                    },
                    onNavigateToPurchasedProgram = { program ->
                        Intent(this, ViewProgramActivity::class.java).also {
                            it.putExtra("program", program)
                            it.putExtra("active_user", vm.activeUser.value)
                            startActivity(it)
                        }
                    },
                    onNavigateToArticle = { article ->
                        Intent(this, ViewArticleActivity::class.java).also {
                            it.putExtra("title", article.title)
                            it.putExtra("description", article.description)
                            it.putExtra("content", article.content)
                            it.putExtra("date", article.date)
                            it.putExtra("author", article.author)
                            startActivity(it)
                        }
                    },
                    onNavigateToProfile = {
                        Intent(this, ProfileActivity::class.java).also {
                            it.putExtra("active_user", vm.activeUser.value)
                            startActivity(it)
                        }
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
        viewModel: HomeViewModel,
        onNavigateToProgram: (FitnessProgram) -> Unit,
        onNavigateToPurchasedProgram: (FitnessProgram) -> Unit,
        onNavigateToArticle: (Article) -> Unit,
        onNavigateToProfile: () -> Unit
    ) {
        val user by viewModel.activeUser.observeAsState()
        val featured by viewModel.featuredArticles.observeAsState(emptyList())
        val regular by viewModel.regularArticles.observeAsState(emptyList())
        val purchased by viewModel.purchasedPrograms.observeAsState(emptyList())
        val available by viewModel.programAvailable.observeAsState(emptyList())

        val context = LocalContext.current

        var selectedTab by remember { mutableStateOf(0) }
        val tabs = listOf("Articles", "Program")

        LaunchedEffect(Unit) {
            viewModel.refreshUserData()
        }

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
                        0 -> ArticlesContent(vm = viewModel) { article ->
                            navigateToArticle(context, article)
                        }
                        1 -> ProgramContent(
                            onNavigateToProgram = onNavigateToProgram,
                            onNavigateToPurchasedProgram = onNavigateToPurchasedProgram
                        )
                    }
                }
            }
        }
    }

    @Composable
    fun ArticlesContent(vm: HomeViewModel, onNavigateToArticle: (Article) -> Unit) {
        LaunchedEffect(Unit) {
            vm.fetchArticles()
        }

        val featuredArticlesObs: State<List<Article>?> = vm.featuredArticles.observeAsState()
        val featuredArticles = featuredArticlesObs.value

        val regularArticlesObs: State<List<Article>?> = vm.regularArticles.observeAsState()
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
                                    text = "By ${article.author.take(32)}",
                                    color = Color.White.copy(alpha = 0.7f),
                                    fontSize = 10.sp
                                )
                                Text(
                                    text = article.date.split("T").get(0),
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

    fun navigateToArticle(context: Context, article: Article) {
        val intent = Intent(context, ViewArticleActivity::class.java).apply {
            putExtra("title", article.title)
            putExtra("description", article.description)
            putExtra("content", article.content)
            putExtra("author", article.author)
            putExtra("date", article.date)
        }
        context.startActivity(intent)
    }

    @Composable
    fun ProgramContent(
        onNavigateToProgram: (FitnessProgram) -> Unit,
        onNavigateToPurchasedProgram: (FitnessProgram) -> Unit
    ) {
        val purchasedObs:State<List<FitnessProgram>?> = vm.purchasedPrograms.observeAsState()
        val purchasedPrograms = purchasedObs.value
        val availableObs:State<List<FitnessProgram>?> = vm.programAvailable.observeAsState()
        val availablePrograms = availableObs.value

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp)
        ) {
            Text(
                text = "Purchased Programs",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            LazyColumn(
                modifier = Modifier
                    .weight(1f, fill = false)
                    .fillMaxWidth()
                    .background(Color.LightGray.copy(alpha = 0.3f)),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(
                    items = purchasedPrograms.orEmpty(),
                    key = { program -> program.id }
                ) { program ->
                    ProgramCard(
                        program = program,
                        onClick = { onNavigateToPurchasedProgram(program) } // Use different callback for purchased programs
                    )
                }
            }

            Text(
                text = "Available Programs",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(bottom = 8.dp)
            )

            LazyColumn(
                modifier = Modifier
                    .weight(1f, fill = false)
                    .fillMaxWidth()
                    .background(Color.LightGray.copy(alpha = 0.3f)),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(
                    items = availablePrograms.orEmpty(),
                    key = { program -> program.id }
                ) { program ->
                    ProgramCard(
                        program = program,
                        onClick = { onNavigateToProgram(program) } // Use payment callback for available programs
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
                                text = if (program.isPurchased) program.dateRange else parseBalanceToIDR(program.price),
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