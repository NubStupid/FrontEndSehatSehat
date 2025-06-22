package com.example.sehatsehat.ui.customer

import android.os.Bundle
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.livedata.observeAsState
import com.example.sehatsehat.SehatViewModelFactory
import com.example.sehatsehat.ui.theme.SehatSehatTheme
import com.example.sehatsehat.viewmodel.CustomerTopupViewModel

class CustomerTopupActivity : ComponentActivity() {
    private val vm: CustomerTopupViewModel by viewModels { SehatViewModelFactory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        val username = intent.getStringExtra("USERNAME") ?: ""
        setContent {
            SehatSehatTheme {
                CustomerTopupScreen(
                    username = username,
                    viewModel = vm,
                    onBack = { finish() }
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CustomerTopupScreen(
    username: String,
    viewModel: CustomerTopupViewModel,
    onBack: () -> Unit
) {
    // Observe LiveData from ViewModel
    val isLoading by viewModel.isLoading.observeAsState(false)
    val amount by viewModel.amountInput.observeAsState(0)
    val successMsg by viewModel.successMessage.observeAsState()
    val errorMsg by viewModel.errorMessage.observeAsState()

    var textState by remember { mutableStateOf(TextFieldValue(amount.toString())) }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Top Up Saldo") },
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
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            OutlinedTextField(
                value = textState,
                onValueChange = { newVal ->
                    if (newVal.text.all { it.isDigit() }) {
                        textState = newVal
                        viewModel.updateAmount(newVal.text.toIntOrNull() ?: 0)
                    }
                },
                label = { Text("Jumlah Top Up") },
                placeholder = { Text("Contoh: 50000") },
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.fillMaxWidth()
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = { viewModel.sendTopUp(username) },
                enabled = !isLoading && textState.text.isNotBlank(),
                modifier = Modifier.fillMaxWidth()
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        modifier = Modifier
                            .size(20.dp)
                            .padding(end = 8.dp),
                        strokeWidth = 2.dp
                    )
                    Text("Memproses...")
                } else {
                    Text("Top Up")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            successMsg?.let { msg ->
                Text(msg, color = MaterialTheme.colorScheme.primary)
            }
            errorMsg?.let { err ->
                Text(err, color = MaterialTheme.colorScheme.error)
            }
        }
    }
}
