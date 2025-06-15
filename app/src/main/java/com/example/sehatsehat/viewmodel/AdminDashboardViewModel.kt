package com.example.sehatsehat.ui.admin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sehatsehat.SehatApplication
import com.example.sehatsehat.data.sources.remote.ProgramListDRO
import com.example.sehatsehat.model.ProgramEntity
import com.example.sehatsehat.model.UserEntity
import com.example.sehatsehat.ui.admin.ui.theme.SehatSehatTheme
import kotlinx.coroutines.launch

// --- VIEWMODEL ---
class AdminDashboardViewModel : ViewModel() {
    var programs    by mutableStateOf<List<ProgramEntity>>(emptyList())
        private set

    var users       by mutableStateOf<List<UserEntity>>(emptyList())
        private set

    var isLoading   by mutableStateOf(false)
        private set

    var errorMessage by mutableStateOf<String?>(null)
        private set

    init {
        fetchAll()
    }

    fun fetchAll() {
        viewModelScope.launch {
            isLoading = true
            errorMessage = null
            try {
                // 1) fetch programs
                val progDro = SehatApplication.retrofitService.getAllPrograms()  // ProgramListDRO
                programs = progDro.programs

//                // 2) fetch users
//                val userDro = SehatApplication.retrofitService.getAllUsers()     // UserListDRO
//                users = userDro.users

            } catch (e: Exception) {
                errorMessage = e.localizedMessage
            } finally {
                isLoading = false
            }
        }
    }
}

// --- ACTIVITY ---
class AdminDashboardActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SehatSehatTheme {
                AdminDashboardScreen(
                    onBack = { finish() }
                )
            }
        }
    }
}

// --- UI HELPERS ---
@Composable
fun formatCurrency(idr: Float): String =
    java.text.NumberFormat.getCurrencyInstance(java.util.Locale("id","ID"))
        .format(idr)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminDashboardScreen(
    onBack: () -> Unit,
    vm: AdminDashboardViewModel = viewModel()
) {
    val programs    = vm.programs
    val users       = vm.users
    val isLoading   = vm.isLoading
    val error       = vm.errorMessage

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Admin Dashboard") },
                navigationIcon = {
                    IconButton(onClick = onBack) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Kembali")
                    }
                }
            )
        },
        floatingActionButton = {
            FloatingActionButton(onClick = { vm.fetchAll() }) {
                Icon(Icons.Default.Add, contentDescription = "Refresh")
            }
        }
    ) { padding ->
        Box(Modifier.fillMaxSize().padding(padding)) {
            when {
                isLoading -> {
                    CircularProgressIndicator(Modifier.align(Alignment.Center))
                }
                error != null -> {
                    Text(
                        text = "Error: $error",
                        color = MaterialTheme.colorScheme.error,
                        modifier = Modifier.align(Alignment.Center)
                    )
                }
                else -> {
                    Column(Modifier.fillMaxSize().padding(16.dp)) {
                        Text("Daftar Program", style = MaterialTheme.typography.titleLarge)
                        LazyColumn(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth(),
                            contentPadding = PaddingValues(vertical = 8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(programs) { p ->
                                ListItem(
                                    leadingContent = {
                                        Icon(Icons.Default.AccountCircle, contentDescription = null)
                                    },
                                    headlineContent = { Text(p.program_name) },
                                    supportingContent = {
                                        Text(formatCurrency(p.pricing))
                                    }
                                )
                            }
                        }

                        Spacer(Modifier.height(16.dp))

                        Text("Daftar User", style = MaterialTheme.typography.titleLarge)
                        LazyColumn(
                            modifier = Modifier
                                .weight(1f)
                                .fillMaxWidth(),
                            contentPadding = PaddingValues(vertical = 8.dp),
                            verticalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            items(users) { u ->
                                ListItem(
                                    leadingContent = {
                                        Icon(Icons.Default.AccountCircle, contentDescription = null)
                                    },
                                    headlineContent = { Text(u.display_name) },
                                    supportingContent = { Text(u.role) }
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}
