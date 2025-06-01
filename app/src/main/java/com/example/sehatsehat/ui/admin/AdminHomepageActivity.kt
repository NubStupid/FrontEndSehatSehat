package com.example.sehatsehat.ui.admin

import android.os.Bundle
import android.view.LayoutInflater
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Send
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.outlined.AccountBox
import androidx.compose.material.icons.outlined.DateRange
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Send
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material.icons.outlined.ShoppingCart
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import co.yml.charts.common.model.PlotType
import co.yml.charts.ui.piechart.charts.PieChart
import co.yml.charts.ui.piechart.models.PieChartConfig
import co.yml.charts.ui.piechart.models.PieChartData
import com.example.sehatsehat.R

class AdminHomepageActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Column(modifier = Modifier.fillMaxSize()) {
                AdminTopBar()
                Text("My Chart", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                AnyChartComposable()
            }
        }
    }

   @Composable
   fun AdminTopBar() {
       var expanded by remember { mutableStateOf(false) }

       Row(
           horizontalArrangement = Arrangement.SpaceBetween,
           verticalAlignment = Alignment.CenterVertically,
           modifier = Modifier.fillMaxWidth().background(
               Color.Gray
           ),
       ){
           Text("Admin Homepage", modifier = Modifier.weight(1f).padding(start = 12.dp,top = 24.dp), fontSize = 16.sp, fontWeight = FontWeight.Bold)
           Box(
               modifier = Modifier.weight(1f).padding(top = 24.dp),
               contentAlignment = Alignment.CenterEnd
           ) {
               IconButton(onClick = { expanded = !expanded }) {
                   Icon(Icons.Default.MoreVert, contentDescription = "More options")
               }
               DropdownMenu(
                   expanded = expanded,
                   onDismissRequest = { expanded = false },
                   modifier = Modifier.padding(0.dp)
               ) {
                   // First section
                   DropdownMenuItem(
                       text = { Text("Admin Homepage", fontWeight = FontWeight.Bold) },
                       leadingIcon = { Icon(painterResource(R.drawable.dashboard), contentDescription = null,   modifier = Modifier.size(24.dp)) },
                       onClick = { /* Do something... */ }
                   )

                   HorizontalDivider()


                   Text(
                       text = "Credentials",
                       fontWeight = FontWeight.SemiBold,
                       modifier = Modifier.padding(8.dp)
                   )

                   DropdownMenuItem(
                       text = { Text("Programs") },
                       leadingIcon = { Icon(Icons.Outlined.DateRange, contentDescription = null) },
                       onClick = { /* Do something... */ }
                   )


                   // Second section
                   DropdownMenuItem(
                       text = { Text("Users") },
                       leadingIcon = { Icon(Icons.Outlined.Person, contentDescription = null) },
                       onClick = { /* Do something... */ }
                   )

                   DropdownMenuItem(
                       text = { Text("Transactions") },
                       leadingIcon = { Icon(Icons.Outlined.ShoppingCart, contentDescription = null) },
                       onClick = { /* Do something... */ }
                   )

                   HorizontalDivider()

                   // Third section
                   DropdownMenuItem(
                       text = { Text("Logout", color = Color.Red) },
                       leadingIcon = { Icon(Icons.Outlined.Info, contentDescription = null, tint = Color.Red) },
                       onClick = { /* Do something... */ },
                       modifier = Modifier.background(Color.Red.copy(alpha = 0.05f)).padding(0.dp)
                   )

                   HorizontalDivider()
               }
           }
       }

   }


    @Composable
    fun AnyChartComposable() {
        val pieChartData = PieChartData(
            slices = listOf(
                PieChartData.Slice("SciFi", 65f, Color(0xFF333333)),
                PieChartData.Slice("Comedy", 35f, Color(0xFF666a86)),
                PieChartData.Slice("Drama", 10f, Color(0xFF95B8D1)),
                PieChartData.Slice("Romance", 40f, Color(0xFFF53844))
            ), plotType = PlotType.Pie
        )

        val pieChartConfig = PieChartConfig(
            isAnimationEnable = true,
            showSliceLabels = true,
            animationDuration = 1500,
            labelVisible = true,
            labelType = PieChartConfig.LabelType.VALUE
        )

        PieChart(
            modifier = Modifier
                .width(400.dp)
                .height(400.dp),
            pieChartData,
            pieChartConfig
        )

    }
}