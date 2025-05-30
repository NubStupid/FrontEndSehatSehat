package com.example.sehatsehat.ui

import android.app.DatePickerDialog
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.example.sehatsehat.ui.theme.SehatSehatTheme
import com.example.sehatsehat.viewmodel.RegisterUiState
import com.example.sehatsehat.viewmodel.RegisterViewModel
import java.util.Calendar

class RegisterActivity : ComponentActivity() {

    private val viewModel: RegisterViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            SehatSehatTheme {
                RegisterScreen(
                    viewModel = viewModel,
                    onNavigateToLogin = {
                        // Navigasi kembali ke LoginActivity
                        startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
                        finish()
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun RegisterScreen(
    viewModel: RegisterViewModel,
    onNavigateToLogin: () -> Unit
) {
    val username by remember { derivedStateOf { viewModel.username } }
    val displayName by remember { derivedStateOf { viewModel.displayName } }
    val password by remember { derivedStateOf { viewModel.password } }
    val dob by remember { derivedStateOf { viewModel.dob } }
    val ppUrl by remember { derivedStateOf { viewModel.ppUrl } }
    val uiState by remember { derivedStateOf { viewModel.uiState } }
    val context = LocalContext.current

    // State untuk menampilkan DatePicker
    val showDatePicker = remember { mutableStateOf(false) }

    // Panggil DatePickerDialog hanya sekali saat flag true
    if (showDatePicker.value) {
        LaunchedEffect(Unit) {
            val cal = Calendar.getInstance()
            val year = cal.get(Calendar.YEAR)
            val month = cal.get(Calendar.MONTH)
            val day = cal.get(Calendar.DAY_OF_MONTH)

            DatePickerDialog(
                context,
                { _, selectedYear, selectedMonth, selectedDay ->
                    val monthStr = String.format("%02d", selectedMonth + 1)
                    val dayStr = String.format("%02d", selectedDay)
                    val dobString = "$selectedYear-$monthStr-$dayStr"
                    viewModel.onDobChange(dobString)
                    showDatePicker.value = false
                },
                year, month, day
            ).show()
        }
    }

    // Tangani uiState: Error atau Success
    LaunchedEffect(uiState) {
        when (uiState) {
            is RegisterUiState.Error -> {
                Toast.makeText(
                    context,
                    (uiState as RegisterUiState.Error).message,
                    Toast.LENGTH_SHORT
                ).show()
                Log.e("error", (uiState as RegisterUiState.Error).message)
                viewModel.resetState()
            }
            is RegisterUiState.Success -> {
                Toast.makeText(context, "Registrasi berhasil", Toast.LENGTH_SHORT).show()
                viewModel.resetState()
                onNavigateToLogin()
            }
            else -> Unit
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(text = "Register", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(24.dp))

        // Username
        OutlinedTextField(
            value = username,
            onValueChange = { viewModel.onUsernameChange(it) },
            label = { Text("Username") },
            leadingIcon = { Icon(imageVector = Icons.Default.Person, contentDescription = "Username Icon") },
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
            modifier = Modifier.fillMaxWidth(),
            enabled = uiState != RegisterUiState.Loading
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Display Name
        OutlinedTextField(
            value = displayName,
            onValueChange = { viewModel.onDisplayNameChange(it) },
            label = { Text("Display Name") },
            leadingIcon = { Icon(imageVector = Icons.Default.Person, contentDescription = "Display Name Icon") },
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
            modifier = Modifier.fillMaxWidth(),
            enabled = uiState != RegisterUiState.Loading
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Password
        OutlinedTextField(
            value = password,
            onValueChange = { viewModel.onPasswordChange(it) },
            label = { Text("Password") },
            leadingIcon = { Icon(imageVector = Icons.Default.Lock, contentDescription = "Lock Icon") },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
            modifier = Modifier.fillMaxWidth(),
            enabled = uiState != RegisterUiState.Loading
        )
        Spacer(modifier = Modifier.height(16.dp))

        // ─── Tanggal Lahir ─────────────────────────────────────────────────────────
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(enabled = uiState != RegisterUiState.Loading) {
                    showDatePicker.value = true
                }
        ) {
            OutlinedTextField(
                value = dob,
                onValueChange = { /* no-op */ },
                label = { Text("Tanggal Lahir") },
                singleLine = true,
                readOnly = true,
                enabled = false, // agar klik diarahkan ke Box
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next)
            )
        }
        Spacer(modifier = Modifier.height(16.dp))
        // ─────────────────────────────────────────────────────────────────────────────

        // Profile Picture URL
        OutlinedTextField(
            value = ppUrl,
            onValueChange = { viewModel.onPpUrlChange(it) },
            label = { Text("Profile Picture URL") },
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
            modifier = Modifier.fillMaxWidth(),
            enabled = uiState != RegisterUiState.Loading
        )
        Spacer(modifier = Modifier.height(24.dp))

        // Tombol Daftar
        Button(
            onClick = { viewModel.register() },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            enabled = uiState != RegisterUiState.Loading
        ) {
            if (uiState is RegisterUiState.Loading) {
                // Tampilkan progress kecil di dalam button
                CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Mendaftar...")
            } else {
                Text(text = "Daftar")
            }
        }

        Spacer(modifier = Modifier.height(12.dp))
        TextButton(
            onClick = onNavigateToLogin,
            enabled = uiState != RegisterUiState.Loading
        ) {
            Text(text = "Sudah punya akun? Masuk")
        }
    }
}
