package com.example.sehatsehat.ui

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MailOutline
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import com.example.sehatsehat.ui.customer.CustomerHomepageActivity
import com.example.sehatsehat.ui.theme.SehatSehatTheme
import com.example.sehatsehat.viewmodel.LoginUiState
import com.example.sehatsehat.viewmodel.LoginViewModel

class LoginActivity : ComponentActivity() {

    // Cukup pakai default factory, karena LoginViewModel hanya butuh Application
    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            SehatSehatTheme {
                LoginScreen(
                    viewModel = viewModel,
                    onNavigateToRegister = {
                        startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
                    },
                    onLoginSuccess = { displayName ->
                        // Pindah ke CustomerHomepageActivity
                        val intent = Intent(this@LoginActivity, CustomerHomepageActivity::class.java)
                        intent.putExtra("display_name", displayName)
                        startActivity(intent)
                        finish()
                    }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginScreen(
    viewModel: LoginViewModel,
    onNavigateToRegister: () -> Unit,
    onLoginSuccess: (String) -> Unit
) {
    val email = viewModel.email
    val password = viewModel.password
    val isPasswordVisible = viewModel.isPasswordVisible
    val uiState = viewModel.uiState
    val context = LocalContext.current

    // Tangani perubahan uiState: Error atau Success
    LaunchedEffect(uiState) {
        when (uiState) {
            is LoginUiState.Error -> {
                Toast.makeText(context, uiState.message, Toast.LENGTH_SHORT).show()
                Log.e("error", uiState.message)
                viewModel.resetState()
            }
            is LoginUiState.Success -> {
                Toast.makeText(context, "Login berhasil! Selamat datang ${uiState.displayName}", Toast.LENGTH_SHORT).show()
                viewModel.resetState()
                onLoginSuccess(uiState.displayName)
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
        Text(text = "Login", style = MaterialTheme.typography.headlineSmall)
        Spacer(modifier = Modifier.height(24.dp))

        OutlinedTextField(
            value = email,
            onValueChange = { viewModel.onEmailChange(it) },
            label = { Text("Username") },
            leadingIcon = { Icon(imageVector = Icons.Default.MailOutline, contentDescription = "User Icon") },
            singleLine = true,
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Next),
            modifier = Modifier.fillMaxWidth(),
            enabled = uiState !is LoginUiState.Loading
        )
        Spacer(modifier = Modifier.height(16.dp))

        OutlinedTextField(
            value = password,
            onValueChange = { viewModel.onPasswordChange(it) },
            label = { Text("Password") },
            leadingIcon = { Icon(imageVector = Icons.Default.Lock, contentDescription = "Lock Icon") },
            singleLine = true,
            visualTransformation = if (isPasswordVisible) VisualTransformation.None else PasswordVisualTransformation(),
            keyboardOptions = KeyboardOptions.Default.copy(imeAction = ImeAction.Done),
            modifier = Modifier.fillMaxWidth(),
            enabled = uiState !is LoginUiState.Loading
        )
        Spacer(modifier = Modifier.height(24.dp))

        Button(
            onClick = { viewModel.login() },
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            enabled = uiState !is LoginUiState.Loading
        ) {
            if (uiState is LoginUiState.Loading) {
                CircularProgressIndicator(modifier = Modifier.size(20.dp), strokeWidth = 2.dp)
                Spacer(modifier = Modifier.width(8.dp))
                Text(text = "Masuk...")
            } else {
                Text(text = "Masuk")
            }
        }

        Spacer(modifier = Modifier.height(12.dp))
        TextButton(
            onClick = onNavigateToRegister,
            enabled = uiState !is LoginUiState.Loading
        ) {
            Text(text = "Belum punya akun? Daftar di sini")
        }
    }
}