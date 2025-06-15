// File: LoginActivity.kt
package com.example.sehatsehat.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBox
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.sehatsehat.ui.admin.AdminHomepageActivity
import com.example.sehatsehat.ui.customer.HomeActivity
import com.example.sehatsehat.ui.expert.ExpertHomepageActivity
import com.example.sehatsehat.ui.theme.SehatSehatTheme
import com.example.sehatsehat.viewmodel.LoginUiState
import com.example.sehatsehat.viewmodel.LoginViewModel

class LoginActivity : ComponentActivity() {

    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        setContent {
            SehatSehatTheme {
                LoginWithBottomNav(
                    viewModel = viewModel,
                    onLoginAdmin = { displayName ->
                        Toast.makeText(
                            this,
                            "Login admin berhasil! Selamat datang $displayName",
                            Toast.LENGTH_SHORT
                        ).show()
                        startActivity(Intent(this, AdminHomepageActivity::class.java))
                        finish()
                    },
                    onLoginCustomer = { displayName ->
                        Toast.makeText(
                            this,
                            "Login customer berhasil! Selamat datang $displayName",
                            Toast.LENGTH_SHORT
                        ).show()
                        val intent = Intent(this, HomeActivity::class.java)
                        intent.putExtra("display_name", displayName)
                        startActivity(intent)
                        finish()
                    },
                    onLoginExpert = { displayName ->
                        Toast.makeText(
                            this,
                            "Login expert berhasil! Selamat datang $displayName",
                            Toast.LENGTH_SHORT
                        ).show()
                        val intent = Intent(this, ExpertHomepageActivity::class.java)
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
fun LoginWithBottomNav(
    viewModel: LoginViewModel,
    onLoginAdmin: (String) -> Unit,
    onLoginCustomer: (String) -> Unit,
    onLoginExpert: (String) -> Unit
) {
    val uiState = viewModel.uiState
    val context = LocalContext.current

    // State untuk bottom navigation
    var selectedTab by remember { mutableStateOf(BottomNavItem.Login) }

    // Tangani perubahan uiState: Error / SuccessAdmin / SuccessCustomer
    LaunchedEffect(uiState) {
        when (uiState) {
            is LoginUiState.Error -> {
                Toast.makeText(context, uiState.message, Toast.LENGTH_SHORT).show()
                viewModel.resetState()
            }
            is LoginUiState.SuccessAdmin -> {
                viewModel.resetState()
                onLoginAdmin(uiState.displayName)
            }
            is LoginUiState.SuccessCustomer -> {
                viewModel.resetState()
                onLoginCustomer(uiState.displayName)
            }
            is LoginUiState.SuccessExpert -> {
                viewModel.resetState()
                onLoginExpert(uiState.displayName)
            }
            else -> Unit
        }
    }

    Scaffold(
        bottomBar = {
            NavigationBar(
                containerColor = MaterialTheme.colorScheme.surface,
                tonalElevation = 4.dp
            ) {
                BottomNavItem.values().forEach { item ->
                    val isSelected = item == selectedTab
                    val tint by animateColorAsState(
                        if (isSelected) MaterialTheme.colorScheme.primary else Color.Gray
                    )
                    NavigationBarItem(
                        icon = {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = item.title,
                                tint = tint
                            )
                        },
                        label = {
                            Text(
                                text = item.title,
                                fontSize = if (isSelected) 12.sp else 10.sp,
                                color = tint
                            )
                        },
                        selected = isSelected,
                        onClick = {
                            selectedTab = item
                            when (item) {
                                BottomNavItem.Login -> {
                                    // nothing extra
                                }
                                BottomNavItem.Register -> {
                                    context.startActivity(Intent(context, RegisterActivity::class.java))
                                }
                            }
                        }
                    )
                }
            }
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            LoginContent(viewModel = viewModel)
        }
    }
}

enum class BottomNavItem(val title: String, val icon: androidx.compose.ui.graphics.vector.ImageVector) {
    Login("Login", Icons.Default.PlayArrow),
    Register("Daftar", Icons.Default.Add)
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun LoginContent(viewModel: LoginViewModel) {
    val email = viewModel.email
    val password = viewModel.password
    val isPasswordVisible = viewModel.isPasswordVisible
    val uiState = viewModel.uiState

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 24.dp),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Login",
            style = MaterialTheme.typography.headlineSmall,
            modifier = Modifier.padding(bottom = 24.dp)
        )

        OutlinedTextField(
            value = email,
            onValueChange = { viewModel.onEmailChange(it) },
            label = { Text("Username") },
            leadingIcon = { Icon(imageVector = Icons.Default.Email, contentDescription = null) },
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
            leadingIcon = { Icon(imageVector = Icons.Default.Info, contentDescription = null) },
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
    }
}
