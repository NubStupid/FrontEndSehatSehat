package com.example.sehatsehat.ui.customer

import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.sehatsehat.SehatApplication
import com.example.sehatsehat.ui.theme.SehatSehatTheme
import com.example.sehatsehat.viewmodel.CustomerUpdateProfileViewModel

class CustomerUpdateProfileActivity : ComponentActivity() {
    private val viewModel: CustomerUpdateProfileViewModel by viewModels {
        object : ViewModelProvider.Factory {
            override fun <T : ViewModel> create(modelClass: Class<T>): T {
                @Suppress("UNCHECKED_CAST")
                return CustomerUpdateProfileViewModel(
                    SehatApplication.retrofitService
                ) as T
            }
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val username = intent.getStringExtra("USERNAME") ?: return
        Log.e("username", username)
        viewModel.init(username)

        setContent {
            SehatSehatTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    CustomerUpdateProfileScreen(
                        viewModel = viewModel,
                        onBack = { finish() }
                    )
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomerUpdateProfileScreen(
    viewModel: CustomerUpdateProfileViewModel,
    onBack: () -> Unit
) {
    val isLoading by viewModel.isLoading.observeAsState(false)
    val displayName by viewModel.displayName.observeAsState("")
    val password by viewModel.password.observeAsState("")
    val dob by viewModel.dob.observeAsState("")
    val successMsg by viewModel.successMessage.observeAsState()
    val errorMsg by viewModel.errorMessage.observeAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Update Profile") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
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
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (isLoading) CircularProgressIndicator(Modifier.align(Alignment.CenterHorizontally))
            errorMsg?.let { Text(it, color = MaterialTheme.colorScheme.error) }

            OutlinedTextField(
                value = displayName,
                onValueChange = viewModel::onDisplayNameChange,
                label = { Text("Display Name") },
                modifier = Modifier.fillMaxWidth()
            )
            OutlinedTextField(
                value = password,
                onValueChange = viewModel::onPasswordChange,
                label = { Text("Password") },
                modifier = Modifier.fillMaxWidth(),
                visualTransformation = PasswordVisualTransformation()
            )
            OutlinedTextField(
                value = dob,
                onValueChange = viewModel::onDobChange,
                label = { Text("Date of Birth") },
                modifier = Modifier.fillMaxWidth(),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Text)
            )

            Button(
                onClick = { viewModel.updateProfile() },
                enabled = !isLoading,
                modifier = Modifier.fillMaxWidth()
            ) {
                if (isLoading) {
                    CircularProgressIndicator(Modifier.size(20.dp).padding(end = 8.dp), strokeWidth = 2.dp)
                    Text("Saving...")
                } else Text("Save")
            }

            successMsg?.let { Text(it, color = MaterialTheme.colorScheme.primary) }
        }
    }
}