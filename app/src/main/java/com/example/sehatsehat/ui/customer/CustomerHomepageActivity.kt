package com.example.sehatsehat.ui.customer

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.AccountWallet
//import androidx.compose.material.icons.filled.CalendarToday
//import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Star
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
                CustomerHomepage(displayName = displayName)
            }
        }
    }
}

data class FitnessProgram(
    val title: String,
    val dateRange: String,
    val description: String,
    val duration: String,
    val progress: Float,
    val backgroundGradient: List<Color>
)

data class ScheduleItem(
    val day: String,
    val date: String,
    val fullDate: String, // Format: "2026-03-01" untuk filtering
    val isToday: Boolean = false
)

data class WorkoutCard(
    val title: String,
    val time: String,
    val credits: String,
    val date: String, // Format: "2026-03-01" untuk filtering
    val imageRes: Int? = null
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomerHomepage(displayName: String) {
    var selectedTab by remember { mutableStateOf(0) }
    val tabs = listOf("Schedule", "Program")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.White)
            .statusBarsPadding()
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
                text = "Home User",
                color = Color.Gray,
                fontSize = 14.sp
            )
            Text(
                text = "Home User",
                color = Color.Gray,
                fontSize = 14.sp
            )
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
                    0 -> ScheduleContent()
                    1 -> ProgramContent()
                }
            }
        }
    }
}

@Composable
fun ScheduleContent() {
    // State untuk selected date
    var selectedDate by remember { mutableStateOf("2026-03-02") } // Default ke hari Tue

    Column {
        // Schedule Days Row - Horizontal Scrollable
        val scheduleItems = listOf(
            ScheduleItem("Mon", "01", "2026-03-01"),
            ScheduleItem("Tue", "02", "2026-03-02", true), // Default selected
            ScheduleItem("Wed", "03", "2026-03-03"),
            ScheduleItem("Thu", "04", "2026-03-04"),
            ScheduleItem("Fri", "05", "2026-03-05"),
            ScheduleItem("Sat", "06", "2026-03-06"),
            ScheduleItem("Sun", "07", "2026-03-07"),
            ScheduleItem("Mon", "08", "2026-03-08"),
            ScheduleItem("Tue", "09", "2026-03-09"),
            ScheduleItem("Wed", "10", "2026-03-10"),
            ScheduleItem("Thu", "11", "2026-03-11"),
            ScheduleItem("Fri", "12", "2026-03-12"),
            ScheduleItem("Sat", "13", "2026-03-13"),
            ScheduleItem("Sun", "14", "2026-03-14")
        )

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            contentPadding = PaddingValues(horizontal = 4.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            items(scheduleItems) { item ->
                ScheduleDayCard(
                    item = item,
                    isSelected = selectedDate == item.fullDate,
                    onDateSelected = { selectedDate = item.fullDate }
                )
            }
        }

        Spacer(modifier = Modifier.height(20.dp))

        // Workout Cards - Vertical Scrollable (Expanded data with dates)
        val allWorkoutCards = listOf(
            WorkoutCard("Cardio Morning", "08.00 am - 09.00 am", "26 Credit", "2026-03-01"),
            WorkoutCard("Upper Body Ignite", "10.00 am - 11.00 am", "25 Credit", "2026-03-01"),
            WorkoutCard("Yoga Flow", "07.00 am - 08.00 am", "20 Credit", "2026-03-02"),
            WorkoutCard("Cardio Evening", "05.00 pm - 06.00 pm", "26 Credit", "2026-03-02"),
            WorkoutCard("HIIT Training", "06.00 pm - 07.00 pm", "30 Credit", "2026-03-02"),
            WorkoutCard("Strength Training", "09.00 am - 10.00 am", "28 Credit", "2026-03-03"),
            WorkoutCard("Pilates", "10.00 am - 11.00 am", "22 Credit", "2026-03-03"),
            WorkoutCard("CrossFit", "07.00 pm - 08.00 pm", "35 Credit", "2026-03-04"),
            WorkoutCard("Swimming", "11.00 am - 12.00 pm", "24 Credit", "2026-03-04"),
            WorkoutCard("Boxing", "08.00 pm - 09.00 pm", "32 Credit", "2026-03-05"),
            WorkoutCard("Zumba Dance", "07.00 pm - 08.00 pm", "18 Credit", "2026-03-05"),
            WorkoutCard("Full Body Workout", "09.00 am - 10.30 am", "35 Credit", "2026-03-06"),
            WorkoutCard("Meditation", "06.00 am - 07.00 am", "15 Credit", "2026-03-07"),
            WorkoutCard("Cycling Class", "08.00 am - 09.00 am", "25 Credit", "2026-03-08")
        )

        // Filter workout cards berdasarkan selected date
        val filteredWorkoutCards = allWorkoutCards.filter { it.date == selectedDate }

        // Show filtered content or empty state
        if (filteredWorkoutCards.isNotEmpty()) {
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxHeight()
            ) {
                items(filteredWorkoutCards) { card ->
                    WorkoutCardItem(card)
                }
            }
        } else {
            // Empty state when no workouts for selected date
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(top = 40.dp),
                contentAlignment = Alignment.TopCenter
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Icon(
                        Icons.Default.Star,
                        contentDescription = "No workouts",
                        tint = Color.Gray,
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "No workouts scheduled",
                        color = Color.Gray,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium
                    )
                    Text(
                        text = "for this date",
                        color = Color.Gray,
                        fontSize = 14.sp,
                        modifier = Modifier.padding(top = 4.dp)
                    )
                }
            }
        }
    }
}

@Composable
fun ProgramContent() {
    // Expanded program list for demonstration - nanti bisa diintegrasikan dengan backend
    val programs = listOf(
        FitnessProgram(
            title = "Slim Fit Project",
            dateRange = "10/02/2026 - 10/03/2026",
            description = "Medium Intensity training with a combination of HIIT to burn fat and increase stamina in a short time.",
            duration = "15 Hours 30 Minutes",
            progress = 0.7f,
            backgroundGradient = listOf(Color(0xFF6B46C1), Color(0xFF9333EA))
        ),
        FitnessProgram(
            title = "Muscle Building Pro",
            dateRange = "15/02/2026 - 15/04/2026",
            description = "High intensity strength training program designed to build lean muscle mass and improve overall body composition.",
            duration = "25 Hours 45 Minutes",
            progress = 0.4f,
            backgroundGradient = listOf(Color(0xFF059669), Color(0xFF10B981))
        ),
        FitnessProgram(
            title = "Cardio Blast",
            dateRange = "01/03/2026 - 30/03/2026",
            description = "Intensive cardiovascular training to improve heart health and burn maximum calories in minimum time.",
            duration = "12 Hours 20 Minutes",
            progress = 0.8f,
            backgroundGradient = listOf(Color(0xFFDC2626), Color(0xFFEF4444))
        ),
        FitnessProgram(
            title = "Flexibility Master",
            dateRange = "05/03/2026 - 05/05/2026",
            description = "Comprehensive flexibility and mobility program combining yoga, stretching, and movement patterns.",
            duration = "18 Hours 15 Minutes",
            progress = 0.3f,
            backgroundGradient = listOf(Color(0xFF7C3AED), Color(0xFF8B5CF6))
        ),
        FitnessProgram(
            title = "Athletic Performance",
            dateRange = "10/03/2026 - 10/06/2026",
            description = "Sport-specific training program to enhance athletic performance, speed, agility, and power.",
            duration = "30 Hours 10 Minutes",
            progress = 0.6f,
            backgroundGradient = listOf(Color(0xFF0891B2), Color(0xFF06B6D4))
        ),
        FitnessProgram(
            title = "Weight Loss Journey",
            dateRange = "15/03/2026 - 15/06/2026",
            description = "Complete weight loss program combining cardio, strength training, and nutritional guidance.",
            duration = "35 Hours 30 Minutes",
            progress = 0.5f,
            backgroundGradient = listOf(Color(0xFFB91C1C), Color(0xFFDC2626))
        ),
        FitnessProgram(
            title = "Core Strength Plus",
            dateRange = "20/03/2026 - 20/04/2026",
            description = "Focused core strengthening program to improve stability, posture, and functional movement.",
            duration = "20 Hours 25 Minutes",
            progress = 0.9f,
            backgroundGradient = listOf(Color(0xFF9333EA), Color(0xFFA855F7))
        ),
        FitnessProgram(
            title = "Beginner Fitness",
            dateRange = "25/03/2026 - 25/05/2026",
            description = "Perfect starting program for fitness beginners with gradual progression and proper form focus.",
            duration = "22 Hours 40 Minutes",
            progress = 0.2f,
            backgroundGradient = listOf(Color(0xFF059669), Color(0xFF34D399))
        )
    )

    // Fully scrollable LazyColumn
    LazyColumn(
        verticalArrangement = Arrangement.spacedBy(16.dp),
        contentPadding = PaddingValues(bottom = 16.dp),
        modifier = Modifier.fillMaxHeight()
    ) {
        items(programs) { program ->
            ProgramCard(program)
        }
    }
}

@Composable
fun ScheduleDayCard(
    item: ScheduleItem,
    isSelected: Boolean,
    onDateSelected: () -> Unit
) {
    Card(
        modifier = Modifier
            .width(60.dp)
            .height(80.dp),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(
            containerColor = when {
                isSelected -> Color(0xFFFFB800) // Selected: Yellow
                item.isToday -> Color(0xFF6B46C1) // Today: Purple
                else -> Color(0xFFF5F5F5) // Default: Light gray
            }
        ),
        onClick = onDateSelected
    ) {
        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = item.day,
                color = if (isSelected || item.isToday) Color.White else Color.Black,
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = item.date,
                color = if (isSelected || item.isToday) Color.White else Color.Black,
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}

@Composable
fun WorkoutCardItem(card: WorkoutCard) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(100.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = Color(0xFFF5F5F5))
    ) {
        Row(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Workout Image Placeholder
            Box(
                modifier = Modifier
                    .size(70.dp)
                    .clip(RoundedCornerShape(12.dp))
                    .background(Color.Gray.copy(alpha = 0.3f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.Star,
                    contentDescription = "Workout",
                    tint = Color.Gray,
                    modifier = Modifier.size(30.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = card.title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
                Text(
                    text = card.time,
                    fontSize = 12.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(top = 4.dp)
                )
                Text(
                    text = card.credits,
                    fontSize = 12.sp,
                    color = Color.Gray,
                    modifier = Modifier.padding(top = 2.dp)
                )
            }

            // Play Button
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .clip(CircleShape)
                    .background(Color(0xFFFFB800)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    Icons.Default.PlayArrow,
                    contentDescription = "Play",
                    tint = Color.White,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

@Composable
fun ProgramCard(program: FitnessProgram) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .height(140.dp),
        shape = RoundedCornerShape(20.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.horizontalGradient(program.backgroundGradient)
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
                            color = Color.White,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            lineHeight = 20.sp
                        )
                        Text(
                            text = program.dateRange,
                            color = Color.White.copy(alpha = 0.9f),
                            fontSize = 11.sp,
                            modifier = Modifier.padding(top = 2.dp)
                        )
                    }

                    // Middle section - Description
                    Text(
                        text = program.description,
                        color = Color.White.copy(alpha = 0.95f),
                        fontSize = 11.sp,
                        lineHeight = 14.sp,
                        modifier = Modifier.padding(vertical = 4.dp)
                    )

                    // Bottom section - Duration and Progress
                    Column {
                        Text(
                            text = program.duration,
                            color = Color.White,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            modifier = Modifier.padding(bottom = 6.dp)
                        )

                        // Progress Bar using LinearProgressIndicator
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

                // Right side - Arrow button
                Box(
                    modifier = Modifier
                        .width(70.dp)
                        .fillMaxHeight()
                        .background(Color(0xFFFFB800)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.PlayArrow,
                        contentDescription = "Start Program",
                        tint = Color.White,
                        modifier = Modifier.size(28.dp)
                    )
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun CustomerHomepagePreview() {
    SehatSehatTheme {
        CustomerHomepage(displayName = "Hanvy Hendrawan")
    }
}