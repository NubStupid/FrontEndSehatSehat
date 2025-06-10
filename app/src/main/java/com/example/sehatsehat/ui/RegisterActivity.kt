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
    val showDatePicker = remember { mutableStateOf(false) }

    if (showDatePicker.value) {
        LaunchedEffect(Unit) {
            val cal = Calendar.getInstance()
            DatePickerDialog(
                context,
                { _, year, month, day ->
                    val dobString = "%04d-%02d-%02d".format(year, month + 1, day)
                    viewModel.onDobChange(dobString)
                    showDatePicker.value = false
                },
                cal.get(Calendar.YEAR), cal.get(Calendar.MONTH), cal.get(Calendar.DAY_OF_MONTH)
            ).show()
        }
    }

    // Handle UI state
    LaunchedEffect(uiState) {
        when (uiState) {
            is RegisterUiState.Error -> {
                Toast.makeText(context, (uiState as RegisterUiState.Error).message, Toast.LENGTH_SHORT).show()
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
        Text(
            text = "Daftar",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        OutlinedTextField(
            value = username,
            onValueChange = { viewModel.onUsernameChange(it) },
            label = { Text("Username") },
            leadingIcon = { Icon(imageVector = Icons.Default.Person, contentDescription = null) },
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
            modifier = Modifier.fillMaxWidth(),
            enabled = uiState != RegisterUiState.Loading
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = displayName,
            onValueChange = { viewModel.onDisplayNameChange(it) },
            label = { Text("Display Name") },
            leadingIcon = { Icon(imageVector = Icons.Default.Person, contentDescription = null) },
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
            modifier = Modifier.fillMaxWidth(),
            enabled = uiState != RegisterUiState.Loading
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { viewModel.onPasswordChange(it) },
            label = { Text("Password") },
            leadingIcon = { Icon(imageVector = Icons.Default.Lock, contentDescription = null) },
            singleLine = true,
            visualTransformation = PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
            modifier = Modifier.fillMaxWidth(),
            enabled = uiState != RegisterUiState.Loading
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Tanggal Lahir
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .clickable(enabled = uiState != RegisterUiState.Loading) {
                    showDatePicker.value = true
                }
        ) {
            OutlinedTextField(
                value = dob,
                onValueChange = {},
                label = { Text("Tanggal Lahir") },
                readOnly = true,
                singleLine = true,
                enabled = false,
                modifier = Modifier.fillMaxWidth()
            )
        }
        Spacer(modifier = Modifier.height(16.dp))

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

        Button(
            onClick = { viewModel.register() },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            enabled = uiState != RegisterUiState.Loading
        ) {
            if (uiState is RegisterUiState.Loading) {
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
