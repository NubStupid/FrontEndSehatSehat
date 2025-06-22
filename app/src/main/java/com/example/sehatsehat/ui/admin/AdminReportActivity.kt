package com.example.sehatsehat.ui.admin

import android.app.DatePickerDialog
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.DateRange
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sehatsehat.ui.theme.SehatSehatTheme
import com.example.sehatsehat.viewmodel.AdminReportViewModel
import java.text.SimpleDateFormat
import java.util.*

class AdminReportActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SehatSehatTheme {
                AdminReportScreen(onBackClick = { finish() })
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminReportScreen(
    onBackClick: () -> Unit,
    viewModel: AdminReportViewModel = viewModel()
) {
    // State for date filters
    val dateFormat = remember { SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()) }
    val displayDateFormat = remember { SimpleDateFormat("dd MMM yyyy", Locale.getDefault()) }
    val calendar = remember { Calendar.getInstance() }

    var startDate by remember { mutableStateOf(dateFormat.format(calendar.time)) }
    var endDate by remember { mutableStateOf(dateFormat.format(calendar.time)) }

    // Initial load
    LaunchedEffect(Unit) {
        viewModel.loadReportByRange(startDate, endDate)
    }

    val items by viewModel.reportItems.collectAsState()
    val totalRevenue by remember(items) {
        mutableStateOf(items.sumOf { it.totalRevenue })
    }

    Log.e("item reports", items.toString())

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Laporan") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            // Filter section
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Text(
                        text = "Filter Tanggal",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 16.dp)
                    )

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        DatePickerField(
                            modifier = Modifier.weight(1f),
                            label = "Tanggal Mulai",
                            date = startDate,
                            displayFormat = displayDateFormat,
                            onDateSelected = { newDate -> startDate = newDate }
                        )
                        DatePickerField(
                            modifier = Modifier.weight(1f),
                            label = "Tanggal Akhir",
                            date = endDate,
                            displayFormat = displayDateFormat,
                            onDateSelected = { newDate -> endDate = newDate }
                        )
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Button(
                        onClick = { viewModel.loadReportByRange(startDate, endDate) },
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text("Tampilkan Laporan")
                    }
                }
            }

            // Header row
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(MaterialTheme.colorScheme.surfaceVariant)
                    .padding(vertical = 12.dp, horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Text(
                    text = "Program",
                    modifier = Modifier.weight(3f),
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.labelMedium
                )
                Text(
                    text = "Tanggal",
                    modifier = Modifier.weight(2f),
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.labelMedium
                )
                Text(
                    text = "Jumlah",
                    modifier = Modifier.weight(1f),
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.End,
                    style = MaterialTheme.typography.labelMedium
                )
                Text(
                    text = "Subtotal",
                    modifier = Modifier.weight(2f),
                    fontWeight = FontWeight.Bold,
                    textAlign = TextAlign.End,
                    style = MaterialTheme.typography.labelMedium
                )
            }
            HorizontalDivider()

            LazyColumn(modifier = Modifier.weight(1f)) {
                items(items) { item ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 12.dp, horizontal = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = item.programName,
                            modifier = Modifier.weight(3f),
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            text = item.purchaseDate,
                            modifier = Modifier.weight(2f),
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            text = item.buyerCount.toString(),
                            modifier = Modifier.weight(1f),
                            textAlign = TextAlign.End,
                            style = MaterialTheme.typography.bodyMedium
                        )
                        Text(
                            text = "Rp${"%,.0f".format(item.totalRevenue)}",
                            modifier = Modifier.weight(2f),
                            textAlign = TextAlign.End,
                            fontWeight = FontWeight.Medium,
                            style = MaterialTheme.typography.bodyMedium
                        )
                    }
                    HorizontalDivider()
                }
            }

            // Total section
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer
                )
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.End
                ) {
                    Text(
                        text = "Total Pendapatan: Rp${"%,.0f".format(totalRevenue)}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onPrimaryContainer
                    )
                }
            }
        }
    }
}

@Composable
private fun DatePickerField(
    modifier: Modifier = Modifier,
    label: String,
    date: String,
    displayFormat: SimpleDateFormat,
    onDateSelected: (String) -> Unit
) {
    val context = LocalContext.current
    val calendar = Calendar.getInstance()
    val dateFormat = remember { SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()) }

    // Parse existing date
    runCatching {
        val parsedDate = dateFormat.parse(date)
        parsedDate?.let { calendar.time = it }
    }

    val year = calendar.get(Calendar.YEAR)
    val month = calendar.get(Calendar.MONTH)
    val day = calendar.get(Calendar.DAY_OF_MONTH)

    // Display formatted date
    val displayDate = runCatching {
        val parsedDate = dateFormat.parse(date)
        parsedDate?.let { displayFormat.format(it) } ?: date
    }.getOrElse { date }

    // Function to show date picker
    val showDatePicker = {
        DatePickerDialog(
            context,
            { _, selectedYear, selectedMonth, selectedDay ->
                calendar.set(selectedYear, selectedMonth, selectedDay)
                onDateSelected(dateFormat.format(calendar.time))
            },
            year,
            month,
            day
        ).show()
    }

    Box(
        modifier = modifier.clickable { showDatePicker() }
    ) {
        OutlinedTextField(
            value = displayDate,
            onValueChange = { },
            label = { Text(label) },
            readOnly = true,
            enabled = false, // Disable to prevent keyboard popup
            trailingIcon = {
                IconButton(onClick = { showDatePicker() }) {
                    Icon(
                        imageVector = Icons.Default.DateRange,
                        contentDescription = "Pilih tanggal",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = OutlinedTextFieldDefaults.colors(
                disabledBorderColor = MaterialTheme.colorScheme.outline,
                disabledTextColor = MaterialTheme.colorScheme.onSurface,
                disabledLabelColor = MaterialTheme.colorScheme.onSurfaceVariant,
                disabledTrailingIconColor = MaterialTheme.colorScheme.primary
            )
        )
    }
}