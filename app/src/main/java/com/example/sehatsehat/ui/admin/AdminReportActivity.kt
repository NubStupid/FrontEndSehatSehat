package com.example.sehatsehat.ui.admin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sehatsehat.ui.theme.SehatSehatTheme
import com.example.sehatsehat.viewmodel.AdminReportViewModel

class AdminReportActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SehatSehatTheme { AdminReportScreen(onBackClick = {finish()}) }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminReportScreen(onBackClick: () -> Unit, viewModel: AdminReportViewModel = viewModel()) {
    LaunchedEffect(Unit) {
        viewModel.loadMonthlyReport()
    }

    val items by viewModel.reportItems.collectAsState()
    var totalRevenue by remember { mutableStateOf(0.0) }

    // Hitung total pendapatan ketika data berubah
    LaunchedEffect(items) {
        totalRevenue = items.sumOf { it.totalRevenue }
    }

    Scaffold(
        topBar = { TopAppBar(
            title = { Text("Laporan Bulanan") },
            navigationIcon = {
                IconButton(onClick = onBackClick) {
                    Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                }
            },
            colors = TopAppBarDefaults.topAppBarColors(
                containerColor = MaterialTheme.colorScheme.primary,
                titleContentColor = Color.White,
                navigationIconContentColor = Color.White
            )
        ) }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Header Tabel
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFEEEEEE))
                    .padding(vertical = 8.dp, horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text("Program", modifier = Modifier.weight(3f), fontWeight = FontWeight.SemiBold)
                Text("Tanggal", modifier = Modifier.weight(2f), fontWeight = FontWeight.SemiBold)
                Text("Pembeli", modifier = Modifier.weight(1f), fontWeight = FontWeight.SemiBold, textAlign = TextAlign.End)
                Text("Pendapatan", modifier = Modifier.weight(2f), fontWeight = FontWeight.SemiBold, textAlign = TextAlign.End)
            }

            Divider()

            // Baris isi
            LazyColumn(modifier = Modifier.weight(1f)) {
                items(items) { item ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp, horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(item.programName, modifier = Modifier.weight(3f))
                        Text(item.purchaseDate, modifier = Modifier.weight(2f))
                        Text(item.buyerCount.toString(), modifier = Modifier.weight(1f), textAlign = TextAlign.End)
                        Text("Rp ${"%,.0f".format(item.totalRevenue)}", modifier = Modifier.weight(2f), textAlign = TextAlign.End)
                    }
                    Divider()
                }
            }

            // Footer total pendapatan
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color(0xFFF5F5F5))
                    .padding(16.dp),
                horizontalArrangement = Arrangement.End
            ) {
                Text(
                    text = "Total Pendapatan: Rp ${"%,.0f".format(totalRevenue)}",
                    style = MaterialTheme.typography.titleMedium,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}