package com.example.sehatsehat.ui.admin

import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sehatsehat.ui.admin.ui.theme.SehatSehatTheme
import com.example.sehatsehat.viewmodel.AdminEditProgramViewModel

class AdminEditProgramActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val programId = intent.getStringExtra("PROGRAM_ID") ?: return finish()

        setContent {
            SehatSehatTheme {
                AdminEditProgramScreen(
                    programId = programId,
                    onBack = { finish() }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminEditProgramScreen(
    programId: String,
    onBack: () -> Unit,
    editVm: AdminEditProgramViewModel = viewModel()
) {
    val context = LocalContext.current
    val uiState by editVm.uiState.collectAsState()

    // State untuk form input
    var name by remember { mutableStateOf("") }
    var pricing by remember { mutableStateOf("") }
    var estimated_time by remember { mutableStateOf("") }
    var focused_at by remember { mutableStateOf("") }
    var exercisesCsv by remember { mutableStateOf("") }
    var mealsCsv by remember { mutableStateOf("") }
    var ingredients by remember { mutableStateOf("") }
    var fat by remember { mutableStateOf("") }
    var calories by remember { mutableStateOf("") }
    var protein by remember { mutableStateOf("") }

    val scrollState = rememberScrollState()

    // Load program data saat composable pertama kali dibuat
    LaunchedEffect(programId) {
        editVm.loadProgram(programId)
    }

    // Populate fields ketika data sudah dimuat
    LaunchedEffect(uiState.program, uiState.exercisesCsv, uiState.mealsCsv) {
        uiState.program?.let { program ->
            if (name.isEmpty()) { // Hanya isi jika masih kosong
                name = program.program_name
                pricing = program.pricing.toString()
                Log.d("EditScreen", "Program fields populated: name=$name, pricing=$pricing")
            }
        }

        if (exercisesCsv.isEmpty() && uiState.exercisesCsv.isNotEmpty()) {
            exercisesCsv = uiState.exercisesCsv
            Log.d("EditScreen", "Exercises populated: $exercisesCsv")
        }

        if (mealsCsv.isEmpty() && uiState.mealsCsv.isNotEmpty()) {
            mealsCsv = uiState.mealsCsv
            Log.d("EditScreen", "Meals populated: $mealsCsv")
        }

        if (estimated_time.isEmpty() && uiState.estimatedTime.isNotEmpty()) {
            estimated_time = uiState.estimatedTime
            Log.d("EditScreen", "Estimated time populated: $estimated_time")
        }

        if (focused_at.isEmpty() && uiState.focusedAt.isNotEmpty()) {
            focused_at = uiState.focusedAt
            Log.d("EditScreen", "Focused at populated: $focused_at")
        }

        if (ingredients.isEmpty() && uiState.ingredients.isNotEmpty()) {
            ingredients = uiState.ingredients
            Log.d("EditScreen", "Ingredients populated: $ingredients")
        }

        if (fat.isEmpty() && uiState.fat.isNotEmpty()) {
            fat = uiState.fat
            Log.d("EditScreen", "Fat populated: $fat")
        }

        if (calories.isEmpty() && uiState.calories.isNotEmpty()) {
            calories = uiState.calories
            Log.d("EditScreen", "Calories populated: $calories")
        }

        if (protein.isEmpty() && uiState.protein.isNotEmpty()) {
            protein = uiState.protein
            Log.d("EditScreen", "Protein populated: $protein")
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Edit Program") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primary,
                    titleContentColor = Color.White,
                    navigationIconContentColor = Color.White
                )
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
                .verticalScroll(scrollState)
        ) {
            when {
                uiState.isLoading -> {
                    CircularProgressIndicator(
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                uiState.error != null -> {
                    Column(
                        modifier = Modifier
                            .align(Alignment.Center)
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Error: ${uiState.error}",
                            color = MaterialTheme.colorScheme.error
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = { editVm.loadProgram(programId) }) {
                            Text("Coba Lagi")
                        }
                    }
                }
                uiState.program != null -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        OutlinedTextField(
                            value = name,
                            onValueChange = { name = it },
                            label = { Text("Nama Program") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )

                        OutlinedTextField(
                            value = pricing,
                            onValueChange = {
                                // Hanya izinkan input angka dan titik desimal
                                if (it.isEmpty() || it.matches(Regex("^\\d*\\.?\\d*$"))) {
                                    pricing = it
                                }
                            },
                            label = { Text("Harga") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )

                        OutlinedTextField(
                            value = exercisesCsv,
                            onValueChange = {
                                exercisesCsv = it
                            },
                            label = { Text("Exercises (dipisahkan koma)") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )

                        OutlinedTextField(
                            value = estimated_time,
                            onValueChange = {
                                estimated_time = it
                            },
                            label = { Text("Waktu") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )

                        OutlinedTextField(
                            value = focused_at,
                            onValueChange = {
                                focused_at = it
                            },
                            label = { Text("Fokus") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )

                        OutlinedTextField(
                            value = mealsCsv,
                            onValueChange = {
                                mealsCsv = it
                            },
                            label = { Text("Meals (dipisahkan koma)") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )

                        OutlinedTextField(
                            value = ingredients,
                            onValueChange = {
                                ingredients = it
                            },
                            label = { Text("Bahan Meal") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )

                        OutlinedTextField(
                            value = fat,
                            onValueChange = {
                                fat = it
                            },
                            label = { Text("Fat") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )

                        OutlinedTextField(
                            value = calories,
                            onValueChange = {
                                calories = it
                            },
                            label = { Text("Kalori") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )

                        OutlinedTextField(
                            value = protein,
                            onValueChange = {
                                protein = it
                            },
                            label = { Text("Protein") },
                            modifier = Modifier.fillMaxWidth(),
                            singleLine = true
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = {
                                val pricingValue = pricing.toFloatOrNull()
                                if (name.isNotBlank() && pricingValue != null && pricingValue > 0) {
                                    editVm.updateProgram(
                                        id = programId,
                                        program_name = name,
                                        pricing = pricingValue,
                                        exercises = exercisesCsv,
                                        estimated_time = estimated_time,
                                        focused_at = focused_at,
                                        meals = mealsCsv,
                                        ingredients = ingredients,
                                        fat = fat,
                                        calories = calories,
                                        protein = protein
                                    ) {
                                        Toast.makeText(
                                            context,
                                            "Program berhasil diperbarui",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                        onBack()
                                    }
                                } else {
                                    Toast.makeText(
                                        context,
                                        "Mohon isi semua field dengan benar",
                                        Toast.LENGTH_SHORT
                                    ).show()
                                }
                            },
                            modifier = Modifier.fillMaxWidth(),
                            enabled = !uiState.isLoading && name.isNotBlank() && pricing.isNotBlank()
                        ) {
                            if (uiState.isLoading) {
                                CircularProgressIndicator(
                                    modifier = Modifier.size(16.dp),
                                    color = Color.White
                                )
                            } else {
                                Text("Simpan Perubahan")
                            }
                        }
                    }
                }
            }
        }
    }
}