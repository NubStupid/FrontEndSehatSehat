package com.example.sehatsehat.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sehatsehat.SehatApplication
import com.example.sehatsehat.data.sources.remote.UpdateRoleRequest
import com.example.sehatsehat.data.sources.remote.UserListDRO
import com.example.sehatsehat.model.UserEntity
import kotlinx.coroutines.launch

data class UserListUiState(
    val users: List<UserEntity> = emptyList(),
    val isLoading: Boolean = false,
    val error: String? = null,
    val isUpdatingRole: Boolean = false,
    val isDeleting: Boolean = false,
    val successMessage: String? = null
)

class AdminUserListViewModel : ViewModel() {
    var uiState by mutableStateOf(UserListUiState())
        private set

    init {
        fetchUsers()
    }

    fun fetchUsers() {
        viewModelScope.launch {
            uiState = uiState.copy(isLoading = true, error = null)
            try {
                val dro: UserListDRO = SehatApplication.retrofitService.getAllUsers()
                uiState = uiState.copy(
                    users = dro.users,
                    isLoading = false,
                    error = null
                )
            } catch (e: Exception) {
                uiState = uiState.copy(
                    error = e.localizedMessage ?: "Gagal memuat data user",
                    isLoading = false
                )
            }
        }
    }

    fun updateUserRole(username: String, newRole: String) {
        viewModelScope.launch {
            uiState = uiState.copy(isUpdatingRole = true, error = null, successMessage = null)
            try {
                val response = SehatApplication.retrofitService.updateUserRole(username, newRole)

                if (response.success) {
                    uiState = uiState.copy(
                        users = uiState.users.map { user ->
                            if (user.username == username) {
                                user.copy(role = newRole)
                            } else user
                        },
                        successMessage = response.message,
                        isUpdatingRole = false
                    )
                } else {
                    uiState = uiState.copy(
                        error = "Gagal mengubah role: ${response.message}",
                        isUpdatingRole = false
                    )
                }
            } catch (e: Exception) {
                uiState = uiState.copy(
                    error = e.localizedMessage ?: "Gagal mengubah role user",
                    isUpdatingRole = false
                )
            }
        }
    }

    fun deleteUser(username: String) {
        viewModelScope.launch {
            uiState = uiState.copy(isDeleting = true, error = null, successMessage = null)
            try {
                val response = SehatApplication.retrofitService.deleteUser(username)
                if (response.isSuccessful) {
                    // Remove user from local list
                    uiState = uiState.copy(
                        users = uiState.users.filter { it.username != username },
                        successMessage = "User @$username berhasil dihapus",
                        isDeleting = false
                    )
                } else {
                    uiState = uiState.copy(
                        error = "Gagal menghapus user: ${response.code()} - ${response.message()}",
                        isDeleting = false
                    )
                }
            } catch (e: Exception) {
                uiState = uiState.copy(
                    error = e.localizedMessage ?: "Gagal menghapus user",
                    isDeleting = false
                )
            }
        }
    }

    fun clearMessages() {
        uiState = uiState.copy(error = null, successMessage = null)
    }
}