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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sehatsehat.SehatViewModelFactory
import com.example.sehatsehat.model.UserEntity
import com.example.sehatsehat.ui.LoginActivity
import com.example.sehatsehat.ui.customer.ChatbotActivity
import com.example.sehatsehat.viewmodel.AdminHomepageViewModel
import co.yml.charts.common.model.PlotType
import co.yml.charts.ui.piechart.charts.PieChart
import co.yml.charts.ui.piechart.models.PieChartConfig
import co.yml.charts.ui.piechart.models.PieChartData

class AdminHomepageActivity : ComponentActivity() {
    private val vm by viewModels<AdminHomepageViewModel> { SehatViewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        vm.init()
        val activeUser = intent.getParcelableExtra<UserEntity>("active_user")
        if (activeUser != null) {
            setContent {
                val scrollState = rememberScrollState()
                val completed = vm.completedProgramCountLiveData.observeAsState(0).value
                val ongoing   = vm.ongoingProgramCountLiveData.observeAsState(0).value
                val available = vm.availableProgramCountLiveData.observeAsState(0).value

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(MaterialTheme.colorScheme.background)
                ) {
                    TopBar()

                    // Navigation section scrollable if needed
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .verticalScroll(scrollState)
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        NavButton(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(60.dp),
                            icon = Icons.Outlined.List,
                            title = "List Program",
                            onClick = {
                                startActivity(Intent(this@AdminHomepageActivity, AdminListProgramActivity::class.java))
                            }
                        )
                        NavButton(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(60.dp),
                            icon = Icons.Outlined.DateRange,
                            title = "Chatbot",
                            onClick = {
                                Intent(this@AdminHomepageActivity, ChatbotActivity::class.java).also {
                                    it.putExtra("active_user", activeUser)
                                    startActivity(it)
                                }
                            }
                        )
                        NavButton(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(60.dp),
                            icon = Icons.Outlined.Person,
                            title = "List User",
                            onClick = {
                                startActivity(Intent(this@AdminHomepageActivity, AdminListUserActivity::class.java))
                            }
                        )
                        NavButton(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(60.dp),
                            icon = Icons.Outlined.DateRange,
                            title = "Report",
                            onClick = {
                                startActivity(Intent(this@AdminHomepageActivity, AdminReportActivity::class.java))
                            }
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = "Program Progress Chart",
                        style = MaterialTheme.typography.titleMedium,
                        modifier = Modifier.padding(start = 16.dp)
                    )

                    // Chart takes remaining space
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f)
                            .padding(16.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        YChartScreen(completed, ongoing, available)
                    }
                }
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
    private fun NavButton(
        modifier: Modifier = Modifier,
        icon: ImageVector,
        title: String,
        onClick: () -> Unit
    ) {
        Card(
            modifier = modifier
                .shadow(elevation = 4.dp, shape = RoundedCornerShape(8.dp))
                .clickable { onClick() },
            shape = RoundedCornerShape(8.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = title,
                    tint = MaterialTheme.colorScheme.onPrimaryContainer,
                    modifier = Modifier.size(28.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleMedium,
                    color = MaterialTheme.colorScheme.onPrimaryContainer
                )
            }
        }
    }

    @Composable
    private fun YChartScreen(completed: Int, ongoing: Int, available: Int) {
        val pieData = PieChartData(
            slices = listOf(
                PieChartData.Slice("Completed", completed.toFloat(), color = MaterialTheme.colorScheme.secondary),
                PieChartData.Slice("Ongoing", ongoing.toFloat(), color = MaterialTheme.colorScheme.tertiary),
                PieChartData.Slice("Available", available.toFloat(), color = MaterialTheme.colorScheme.primary)
            ),
            plotType = PlotType.Donut
        )
        val config = PieChartConfig(
            strokeWidth = 100f,
            isAnimationEnable = true,
            animationDuration = 600,
            showSliceLabels = true,
            sliceLabelTextSize = 14.sp,
            sliceLabelTextColor = MaterialTheme.colorScheme.onBackground,
            isSumVisible = false,
            isClickOnSliceEnabled = false
        )
        PieChart(
            pieChartData = pieData,
            pieChartConfig = config,
            modifier = Modifier.fillMaxSize()
        )
    }
}
