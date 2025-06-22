package com.example.sehatsehat.ui.admin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.core.view.WindowCompat
import com.example.sehatsehat.ui.admin.ui.theme.SehatSehatTheme
import com.example.sehatsehat.viewmodel.AdminAddProgramViewModel

class AdminAddProgramActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)
        setContent {
            SehatSehatTheme {
                AdminAddProgramScreen(
                    onBackClick    = { finish() },
                    onSubmitSuccess = { finish() }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminAddProgramScreen(
    onBackClick: () -> Unit,
    onSubmitSuccess: () -> Unit,
    vm: AdminAddProgramViewModel = viewModel()
) {
    val programName by remember { derivedStateOf { vm.programName } }
    val pricing     by remember { derivedStateOf { vm.pricing     } }
    val isLoading   by remember { derivedStateOf { vm.isLoading   } }
    val errorMessage by remember { derivedStateOf { vm.errorMessage } }

    var exercises by remember { mutableStateOf("") }
    var meals by remember { mutableStateOf("") }
    var estimated_time by remember { mutableStateOf("") }
    var focused_at by remember { mutableStateOf("") }
    var ingredients by remember { mutableStateOf("") }
    var fat by remember { mutableStateOf("") }
    var calories by remember { mutableStateOf("") }
    var protein by remember { mutableStateOf("") }

    val scrollState = rememberScrollState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Tambah Program") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Kembali")
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(scrollState)
                .padding(16.dp)
        ) {
            Column(
                verticalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.align(Alignment.TopCenter)
            ) {
                OutlinedTextField(
                    value = programName,
                    onValueChange = vm::onProgramNameChange,
                    label = { Text("Nama Program") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = pricing,
                    onValueChange = vm::onPricingChange,
                    label = { Text("Harga (dalam Rupiah)") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth()
                )

                errorMessage?.let { msg ->
                    Text(
                        text = msg,
                        color = MaterialTheme.colorScheme.error,
                        fontSize = 14.sp
                    )
                }

                OutlinedTextField(
                    value = exercises,
                    onValueChange = { exercises = it },
                    label = { Text("Exercises (dipisahkan koma)") },
                    singleLine = false,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = estimated_time,
                    onValueChange = { estimated_time = it },
                    label = { Text("Waktu (dalam menit)") },
                    singleLine = false,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = focused_at,
                    onValueChange = { focused_at = it },
                    label = { Text("Fokus/Instruksi (dipisahkan koma)") },
                    singleLine = false,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = meals,
                    onValueChange = { meals = it },
                    label = { Text("Meals (dipisahkan koma)") },
                    singleLine = false,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = ingredients,
                    onValueChange = { ingredients = it },
                    label = { Text("Bahan Meal (dipisahkan koma)") },
                    singleLine = false,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = calories,
                    onValueChange = { calories = it },
                    label = { Text("Kalori (dalam kal)") },
                    singleLine = false,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = fat,
                    onValueChange = { fat = it },
                    label = { Text("Fat (dalam gram)") },
                    singleLine = false,
                    modifier = Modifier.fillMaxWidth()
                )

                OutlinedTextField(
                    value = protein,
                    onValueChange = { protein = it },
                    label = { Text("Protein (dalam gram)") },
                    singleLine = false,
                    modifier = Modifier.fillMaxWidth()
                )

                Button(
                    onClick = { vm.submit(exercises, estimated_time, focused_at, meals, ingredients, fat, calories, protein, onSubmitSuccess) },
                    modifier = Modifier.fillMaxWidth(),
                    enabled = !isLoading
                ) {
                    if (isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(20.dp),
                            strokeWidth = 2.dp
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                    Text("Simpan")
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewAdminAddProgram() {
    SehatSehatTheme {
        AdminAddProgramScreen(onBackClick = {}, onSubmitSuccess = {})
    }
}
