package com.example.sehatsehat.ui.admin

import android.os.Bundle
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
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.sehatsehat.SehatApplication
import com.example.sehatsehat.data.sources.remote.UserListDRO
import com.example.sehatsehat.model.UserEntity
import com.example.sehatsehat.ui.admin.ui.theme.SehatSehatTheme
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.*

// ViewModel untuk User List
data class UiState(
    val users: List<UserEntity> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null
)

class AdminUserListViewModel : ViewModel() {
    var uiState by mutableStateOf(UiState())
        private set

    init {
        fetchUsers()
    }

    fun fetchUsers() {
        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true, error = null)
            try {
                val dro: UserListDRO = SehatApplication.retrofitService.getAllUsers()
                uiState = uiState.copy(users = dro.users)
            } catch (e: Exception) {
                uiState = uiState.copy(error = e.localizedMessage ?: "Unknown error")
            } finally {
                uiState = uiState.copy(isLoading = false)
            }
        }
    }

    fun deleteUser(username: String) {
        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true, error = null)
            try {
                val response = SehatApplication.retrofitService.deleteUser(username)
                if (response.isSuccessful) {
                    fetchUsers()
                } else {
                    uiState = uiState.copy(error = "Gagal menghapus: ${response.code()}")
                }
            } catch (e: Exception) {
                uiState = uiState.copy(error = e.localizedMessage ?: "Unknown error")
            } finally {
                uiState = uiState.copy(isLoading = false)
            }
        }
    }

    fun updateUserRole(username: String, newRole: String) {
        viewModelScope.launch {
            // TODO: panggil API update role jika ada
            uiState = uiState.copy(
                users = uiState.users.map { user ->
                    if (user.username == username) user.copy(role = newRole) else user
                }
            )
        }
    }
}

// Activity + Screen + Composables
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
    val (users, isLoading, error) = remember { derivedStateOf { Triple(
        viewModel.uiState.users,
        viewModel.uiState.isLoading,
        viewModel.uiState.error
    ) } }.value

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
                isLoading -> CircularProgressIndicator(Modifier.align(Alignment.Center))
                error != null -> ErrorView(error!!) { viewModel.fetchUsers() }
                users.isEmpty() -> Text("Tidak ada user", Modifier.align(Alignment.Center))
                else -> UserList(users, onRoleChange = { r -> viewModel.updateUserRole(r.first, r.second) }, onDelete = { viewModel.deleteUser(it) })
            }
        }
    }
}

@Composable
fun ErrorView(message: String, onRetry: () -> Unit) {
    Column(
//        Modifier.align(Alignment.CenterHorizontally),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Error: $message", color = MaterialTheme.colorScheme.error)
        Spacer(Modifier.height(12.dp))
        Button(onClick = onRetry) { Text("Coba Lagi") }
    }
}

@Composable
fun UserList(
    users: List<UserEntity>,
    onRoleChange: (Pair<String, String>) -> Unit,
    onDelete: (String) -> Unit
) {
    LazyColumn(
        Modifier.fillMaxSize().padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(users) { user ->
            UserCard(user, onRoleChange = { newRole -> onRoleChange(user.username to newRole) }, onDelete = onDelete)
        }
    }
}

@Composable
fun UserCard(
    user: UserEntity,
    onRoleChange: (String) -> Unit,
    onDelete: (String) -> Unit
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
                    Icons.Default.Person, null,
                    Modifier
                        .size(40.dp)
                        .background(MaterialTheme.colorScheme.primary.copy(alpha = 0.1f), RoundedCornerShape(20.dp))
                        .padding(8.dp)
                )
                Spacer(Modifier.width(12.dp))
                Column(Modifier.weight(1f)) {
                    Text(user.display_name, fontWeight = FontWeight.Bold, fontSize = 16.sp, maxLines = 1, overflow = TextOverflow.Ellipsis)
                    Text("@${user.username}", fontSize = 14.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.7f))
                    Text("Bergabung: ${formatDate(user.createdAt.toLong())}", fontSize = 12.sp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.5f))
                }
                RoleChip(user.role)
            }
            Spacer(Modifier.height(12.dp))
            Row(Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                OutlinedButton({ showRoleDialog = true }, Modifier.weight(1f)) { Text("Ubah Role") }
                OutlinedButton({ showConfirmDelete = true }, Modifier.weight(1f), colors = ButtonDefaults.outlinedButtonColors(contentColor = Color.Red)) { Text("Hapus") }
            }
        }
    }

    if (showRoleDialog) RoleDialog(user.role, onSelect = { onRoleChange(it); showRoleDialog = false }, onDismiss = { showRoleDialog = false })
    if (showConfirmDelete) ConfirmDeleteDialog(user.username, onConfirm = { onDelete(user.username); showConfirmDelete = false }, onCancel = { showConfirmDelete = false })
}

@Composable fun RoleChip(role: String) = Surface(
    color = when(role){"admin"->Color.Red.copy(.1f)"trainer"->Color.Blue.copy(.1f)else->Color.Green.copy(.1f)},
    shape=RoundedCornerShape(16.dp),modifier=Modifier.padding(start=8.dp)){
    Text(role.uppercase(), Modifier.padding(8.dp,4.dp), fontSize=12.sp, fontWeight=FontWeight.Medium,
        color=when(role){"admin"->Color.Red "trainer"->Color.Blue else->Color.Green})
}

@Composable
fun RoleDialog(current: String, onSelect: (String)->Unit, onDismiss: ()->Unit) {
    AlertDialog(onDismissRequest=onDismiss,title={Text("Pilih Role")},text={Column{listOf("customer","trainer","admin").forEach{role->Row(Modifier.fillMaxWidth().padding(vertical=4.dp),verticalAlignment=Alignment.CenterVertically){RadioButton(selected=current==role,onClick={onSelect(role)});Spacer(Modifier.width(8.dp));Text(role.replaceFirstChar{it.uppercase()})}}}},confirmButton={TextButton(onClick=onDismiss){Text("Batal")}})
}

@Composable
fun ConfirmDeleteDialog(username:String,onConfirm:()->Unit,onCancel:()->Unit){
    AlertDialog(
        onDismissRequest=onCancel,
        title={Text("Konfirmasi Hapus")},
        text={Text("Yakin hapus user @$username?")},
        confirmButton={TextButton(onClick=onConfirm){Text("Hapus",color=Color.Red)}},
        dismissButton={TextButton(onClick=onCancel){Text("Batal")}}
    )
}

//fun formatDate(timestamp: Long): String {
//    val sdf = SimpleDateFormat("dd MMM yyyy", Locale("id"))
//    return sdf.format(Date(timestamp))
//}

@Preview(showBackground = true)
@Composable
fun AdminUserListPreview() {
    SehatSehatTheme { AdminUserListScreen(onBackClick = {}) }
}
