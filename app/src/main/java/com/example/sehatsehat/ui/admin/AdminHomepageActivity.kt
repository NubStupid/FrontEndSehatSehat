package com.example.sehatsehat.ui.admin

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.List
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.State
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.lifecycle.liveData
import androidx.lifecycle.viewmodel.compose.viewModel
import co.yml.charts.common.extensions.isNotNull
import co.yml.charts.common.model.PlotType
import co.yml.charts.ui.piechart.charts.PieChart
import co.yml.charts.ui.piechart.models.PieChartConfig
import co.yml.charts.ui.piechart.models.PieChartData
import com.example.sehatsehat.SehatViewModelFactory
import com.example.sehatsehat.model.ProgramEntity
import com.example.sehatsehat.ui.LoginActivity
import com.example.sehatsehat.ui.customer.ChatbotActivity
import com.example.sehatsehat.viewmodel.AdminHomepageViewModel

class AdminHomepageActivity : ComponentActivity() {
    val vm by viewModels <AdminHomepageViewModel>(){ SehatViewModelFactory }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        vm.init()
        setContent {
            val completedState:State<Int?> = vm.completedProgramCountLiveData.observeAsState()
            val ongoingState:State<Int?> = vm.ongoingProgramCountLiveData.observeAsState()
            val availableState:State<Int?> = vm.availableProgramCountLiveData.observeAsState()
            val completed = completedState.value
            val ongoing = ongoingState.value
            val available = availableState.value

            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(MaterialTheme.colorScheme.background)
            ) {
                TopBar()
                NavigationSection()
                Text("Program Progress Chart")
                YChartScreen(completed?:0, ongoing?:0, available?:0)
                // Jika perlu, tambahkan konten lain di bawah tombol (misalnya daftar program)
            }
        }
    }

    @Composable
    private fun TopBar() {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(MaterialTheme.colorScheme.primaryContainer)
                .padding(vertical = 16.dp, horizontal = 12.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Admin Dashboard",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            IconButton(
                onClick = {
                    // Logout: kembali ke LoginActivity dan finish activity ini
                    startActivity(Intent(this@AdminHomepageActivity, LoginActivity::class.java))
                    finish()
                }
            ) {
                Icon(
                    imageVector = Icons.Outlined.Close,
                    contentDescription = "Logout",
                    tint = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }
    }

    @Composable
    private fun NavigationSection() {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            BigNavButton(
                modifier = Modifier.weight(1f),
                icon = Icons.Outlined.List,
                title = "List Program",
                backgroundColor = Color(0xFF00AA13),
                onClick = {
                    startActivity(
                        Intent(
                            this@AdminHomepageActivity,
                            AdminListProgramActivity::class.java
                        )
                    )
                }
            )
            BigNavButton(
                modifier = Modifier.weight(1f),
                icon = Icons.Outlined.DateRange,
                title = "Chatbot",
                backgroundColor = Color(0xFF00AA13),
                onClick = {
                    startActivity(
                        Intent(
                            this@AdminHomepageActivity,
                            ChatbotActivity::class.java
                        )
                    )
                }
            )
            BigNavButton(
                modifier = Modifier.weight(1f),
                icon = Icons.Outlined.Person,
                title = "List User",
                backgroundColor = Color(0xFF00AA13),
                onClick = {
                    startActivity(
                        Intent(
                            this@AdminHomepageActivity,
                            AdminListUserActivity::class.java
                        )
                    )
                }
            )
            BigNavButton(
                modifier = Modifier.weight(1f),
                icon = Icons.Outlined.DateRange,
                title = "Report",
                backgroundColor = Color(0xFF00AA13),
                onClick = {
                    startActivity(
                        Intent(
                            this@AdminHomepageActivity,
                            AdminReportActivity::class.java
                        )
                    )
                }
            )
        }
    }

    @Composable
    private fun BigNavButton(
        modifier: Modifier = Modifier,        // ← tambahkan parameter
        icon: ImageVector,
        title: String,
        backgroundColor: Color,
        onClick: () -> Unit
    ) {
        Card(
            modifier = modifier                // ← gunakan modifier di sini
                .height(160.dp)
                .shadow(elevation = 6.dp, shape = RoundedCornerShape(12.dp))
                .clickable { onClick() },
            shape = RoundedCornerShape(12.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.White)
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box(
                    modifier = Modifier
                        .size(72.dp)
                        .background(color = backgroundColor, shape = RoundedCornerShape(36.dp)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = title,
                        tint = Color.White,
                        modifier = Modifier.size(36.dp)
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))
                Text(
                    text = title,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = Color.Black
                )
            }
        }
    }


    @Composable
    fun YChartScreen(completed:Int, ongoing:Int, available:Int) {

        // 1. Define the data for the pie chart.
        // We create three slices of equal proportion (1f) to mimic the Y-Chart structure.
        val pieChartData = PieChartData(
            slices = listOf(
                PieChartData.Slice(
                    label = "Completed",
                    value = completed.toFloat(), // Equal proportion
                    color = Color(0xFFB39DDB) // Light Purple
                ),
                PieChartData.Slice(
                    label = "Ongoing",
                    value = ongoing.toFloat(), // Equal proportion
                    color = Color(0xFF81C784) // Light Green
                ),
                PieChartData.Slice(
                    label = "Available",
                    value = available.toFloat(), // Equal proportion
                    color = Color(0xFF64B5F6) // Light Blue
                )
            ),
            plotType = PlotType.Donut
            // The plotType can be Donut or Pie.
        )

        // 2. Configure the appearance of the pie chart.
        val pieChartConfig = PieChartConfig(
            // Basic chart properties
            strokeWidth = 120f,
            isAnimationEnable = true,
            animationDuration = 800,
            showSliceLabels = true,
            sliceLabelTextSize = 16.sp,
            sliceLabelTextColor = Color.Black,

            // We disable the legend since our data is displayed directly.
            isSumVisible = false,
            isClickOnSliceEnabled = false
        )

        // 3. Display the chart and the data.
        // We use a Box to overlay the data points on top of the chart sections.
        // This part is a bit tricky as we manually place the text.
        // For a real app, you might calculate positions based on angles.
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            contentAlignment = Alignment.Center
        ) {
            // The PieChart from the library
            PieChart(
                modifier = Modifier.fillMaxSize(),
                pieChartData = pieChartData,
                pieChartConfig = pieChartConfig
            )
        }
    }

}
