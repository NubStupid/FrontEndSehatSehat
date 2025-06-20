package com.example.sehatsehat.ui.admin

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sehatsehat.model.UserEntity
import com.example.sehatsehat.ui.admin.ui.theme.SehatSehatTheme
import com.example.sehatsehat.viewmodel.AdminUserListViewModel
import java.text.SimpleDateFormat
import java.util.*

class AdminListUserActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            SehatSehatTheme {
                AdminUserListScreen(onBackClick = { finish() })
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AdminUserListScreen(
    onBackClick: () -> Unit,
    viewModel: AdminUserListViewModel = viewModel()
) {
    val context = LocalContext.current
    val uiState = viewModel.uiState

    // Show success or error messages
    LaunchedEffect(uiState.successMessage) {
        uiState.successMessage?.let { message ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
            viewModel.clearMessages()
        }
    }

    LaunchedEffect(uiState.error) {
        uiState.error?.let { error ->
            Toast.makeText(context, error, Toast.LENGTH_LONG).show()
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Kelola User") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(Icons.Default.ArrowBack, contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                uiState.isLoading -> {
                    CircularProgressIndicator(Modifier.align(Alignment.Center))
                }
                uiState.error != null && uiState.users.isEmpty() -> {
                    ErrorView(uiState.error) { viewModel.fetchUsers() }
                }
                uiState.users.isEmpty() -> {
                    Text("Tidak ada user", Modifier.align(Alignment.Center))
                }
                else -> {
                    UserList(
                        users = uiState.users,
                        onRoleChange = { username, newRole ->
                            viewModel.updateUserRole(username, newRole)
                        },
                        onDelete = { username ->
                            viewModel.deleteUser(username)
                        },
                        isUpdatingRole = uiState.isUpdatingRole,
                        isDeleting = uiState.isDeleting
                    )
                }
            }

            // Show loading overlay when updating role or deleting
            if (uiState.isUpdatingRole || uiState.isDeleting) {
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(Color.Black.copy(alpha = 0.3f)),
                    contentAlignment = Alignment.Center
                ) {
                    Card(
                        modifier = Modifier.padding(16.dp),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.padding(16.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            CircularProgressIndicator(modifier = Modifier.size(24.dp))
                            Spacer(modifier = Modifier.width(16.dp))
                            Text(
                                text = when {
                                    uiState.isUpdatingRole -> "Mengubah role..."
                                    uiState.isDeleting -> "Menghapus user..."
                                    else -> "Memproses..."
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun ErrorView(message: String, onRetry: () -> Unit) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Error: $message",
            color = MaterialTheme.colorScheme.error,
            modifier = Modifier.padding(16.dp)
        )
        Spacer(Modifier.height(12.dp))
        Button(onClick = onRetry) {
            Text("Coba Lagi")
        }
    }
}

@Composable
fun UserList(
    users: List<UserEntity>,
    onRoleChange: (String, String) -> Unit,
    onDelete: (String) -> Unit,
    isUpdatingRole: Boolean,
    isDeleting: Boolean
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(users) { user ->
            UserCard(
                user = user,
                onRoleChange = { newRole -> onRoleChange(user.username, newRole) },
                onDelete = { onDelete(user.username) },
                isProcessing = isUpdatingRole || isDeleting
            )
        }
    }
}

@Composable
fun UserCard(
    user: UserEntity,
    onRoleChange: (String) -> Unit,
    onDelete: () -> Unit,
    isProcessing: Boolean
) {
    var showRoleDialog by remember { mutableStateOf(false) }
    var showConfirmDelete by remember { mutableStateOf(false) }

    Card(
        Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp)),
        elevation = CardDefaults.cardElevation(4.dp)
    ) {
        Column(Modifier.padding(16.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Icon(
                    Icons.Default.Person,
                    contentDescription = null,
                    modifier = Modifier
                        .size(40.dp)
                        .background(
                            MaterialTheme.colorScheme.primary.copy(alpha = 0.1f),
                            RoundedCornerShape(20.dp)
                        )
                        .padding(8.dp)
                )
                Spacer(Modifier.width(12.dp))
                Column(Modifier.weight(1f)) {
                    Text(
                        text = user.display_name,
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                    Text(
                        text = "@${user.username}",
                        fontSize = 14.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f)
                    )
                    Text(
                        text = "Bergabung: ${formatDate(user.createdAt.toLong())}",
                        fontSize = 12.sp,
                        color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f)
                    )
                }
                RoleChip(user.role)
            }

            Spacer(Modifier.height(12.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = { showRoleDialog = true },
                    modifier = Modifier.weight(1f),
                    enabled = !isProcessing
                ) {
                    Text("Ubah Role")
                }
                OutlinedButton(
                    onClick = { showConfirmDelete = true },
                    modifier = Modifier.weight(1f),
                    enabled = !isProcessing,
                    colors = ButtonDefaults.outlinedButtonColors(
                        contentColor = Color.Red
                    )
                ) {
                    Text("Hapus")
                }
            }
        }
    }

    if (showRoleDialog) {
        RoleDialog(
            currentRole = user.role,
            onSelect = { newRole ->
                onRoleChange(newRole)
                showRoleDialog = false
            },
            onDismiss = { showRoleDialog = false }
        )
    }

    if (showConfirmDelete) {
        ConfirmDeleteDialog(
            username = user.username,
            onConfirm = {
                onDelete()
                showConfirmDelete = false
            },
            onCancel = { showConfirmDelete = false }
        )
    }
}

@Composable
fun RoleChip(role: String) {
    Surface(
        color = when(role) {
            "admin" -> Color.Red.copy(0.1f)
            "trainer" -> Color.Blue.copy(0.1f)
            else -> Color.Green.copy(0.1f)
        },
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier.padding(start = 8.dp)
    ) {
        Text(
            text = role.uppercase(),
            modifier = Modifier.padding(8.dp, 4.dp),
            fontSize = 12.sp,
            fontWeight = FontWeight.Medium,
            color = when(role) {
                "admin" -> Color.Red
                "trainer" -> Color.Blue
                else -> Color.Green
            }
        )
    }
}

@Composable
fun RoleDialog(
    currentRole: String,
    onSelect: (String) -> Unit,
    onDismiss: () -> Unit
) {
    val roles = listOf("customer", "trainer", "admin")

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Pilih Role") },
        text = {
            Column {
                roles.forEach { role ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        RadioButton(
                            selected = currentRole == role,
                            onClick = { onSelect(role) }
                        )
                        Spacer(Modifier.width(8.dp))
                        Text(role.replaceFirstChar { it.uppercase() })
                    }
                }
            }
        },
        confirmButton = {
            TextButton(onClick = onDismiss) {
                Text("Batal")
            }
        }
    )
}

@Composable
fun ConfirmDeleteDialog(
    username: String,
    onConfirm: () -> Unit,
    onCancel: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onCancel,
        title = { Text("Konfirmasi Hapus") },
        text = { Text("Yakin hapus user @$username?") },
        confirmButton = {
            TextButton(onClick = onConfirm) {
                Text("Hapus", color = Color.Red)
            }
        },
        dismissButton = {
            TextButton(onClick = onCancel) {
                Text("Batal")
            }
        }
    )
}