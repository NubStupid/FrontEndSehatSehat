package com.example.sehatsehat.ui.customer

import android.content.Context
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.example.sehatsehat.model.FitnessProgram
import com.example.sehatsehat.ui.theme.SehatSehatTheme

class ListProgramActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            SehatSehatTheme {
                ListProgramScreen(
                    onBackClick = { finish() },
                    onProgramClick = { program ->
                        startActivity(ViewProgramActivity.newIntent(this, program))
                    }
                )
            }
        }
    }

    companion object {
        fun newIntent(context: Context): Intent {
            return Intent(context, ListProgramActivity::class.java)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListProgramScreen(
    onBackClick: () -> Unit,
    onProgramClick: (FitnessProgram) -> Unit
) {
    val programs = listOf(
        FitnessProgram(
            id = 1,
            title = "Slim Fit Project",
            dateRange = "10/02/2026 - 10/03/2026",
            description = "Medium Intensity training with HIIT to burn fat",
            duration = "15 Hours 30 Minutes",
            progress = 0.7f,
            backgroundGradient = listOf(0xFF6B46C1, 0xFF9333EA),
            detailedDescription = "Medium Intensity training with HIIT to burn fat"
        ),
        FitnessProgram(
            id = 2,
            title = "Muscle Building Pro",
            dateRange = "15/02/2026 - 15/04/2026",
            description = "High intensity strength training program",
            duration = "25 Hours 45 Minutes",
            progress = 0.0f,
            backgroundGradient = listOf(0xFF059669, 0xFF10B981),
            detailedDescription = "High intensity strength training program"
        ),
        FitnessProgram(
            id = 3,
            title = "Cardio Blast",
            dateRange = "01/03/2026 - 30/03/2026",
            description = "Intensive cardiovascular training",
            duration = "12 Hours 20 Minutes",
            progress = 0.0f,
            backgroundGradient = listOf(0xFFDC2626, 0xFFEF4444),
            detailedDescription = "High intensity strength training program"
        )
    )

    var searchQuery by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("All Programs") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                actions = {
                    IconButton(onClick = { /* Optional search icon action */ }) {
                        Icon(Icons.Default.Search, contentDescription = "Search")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White,
                    titleContentColor = Color.Black
                )
            )
        }
    ) { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .padding(16.dp)
        ) {
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                modifier = Modifier.fillMaxWidth(),
                placeholder = { Text("Search programs...") },
                leadingIcon = {
                    Icon(Icons.Default.Search, contentDescription = "Search")
                },
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color(0xFFFFB800),
                    unfocusedBorderColor = Color.Gray
                )
            )

            Spacer(modifier = Modifier.height(16.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(programs.filter {
                    it.title.contains(searchQuery, ignoreCase = true) ||
                            it.description.contains(searchQuery, ignoreCase = true)
                }) { program ->
//                    ProgramCard(
//                        program = program,
//                        onClick = { onProgramClick(program) }
//                    )
                }
            }
        }
    }
}

// You must implement or import ProgramCard Composable separately.
