package com.example.sehatsehat.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.sehatsehat.SehatApplication
import com.example.sehatsehat.data.sources.remote.LoginDRO
import com.example.sehatsehat.data.sources.remote.LoginDTO
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException

// Tambahkan dua jenis Success untuk membedakan navigasi admin vs customer
sealed class LoginUiState {
    object Idle : LoginUiState()
    object Loading : LoginUiState()
    data class SuccessAdmin(val displayName: String) : LoginUiState()
    data class SuccessCustomer(val displayName: String) : LoginUiState()
    data class SuccessExpert(val displayName: String) : LoginUiState()
    data class Error(val message: String) : LoginUiState()
}

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val webService = SehatApplication.retrofitService

    var email by mutableStateOf("")
        private set

    var password by mutableStateOf("")
        private set

    var isPasswordVisible by mutableStateOf(false)
        private set

    var uiState by mutableStateOf<LoginUiState>(LoginUiState.Idle)
        private set

    fun onEmailChange(newEmail: String) {
        email = newEmail.trim()
        if (uiState is LoginUiState.Error) {
            uiState = LoginUiState.Idle
        }
    }

    fun onPasswordChange(newPassword: String) {
        password = newPassword
        if (uiState is LoginUiState.Error) {
            uiState = LoginUiState.Idle
        }
    }

    fun onTogglePasswordVisibility() {
        isPasswordVisible = !isPasswordVisible
    }

    fun login() {
        // 1. Validasi input
        if (email.isBlank()) {
            uiState = LoginUiState.Error("Username tidak boleh kosong")
            return
        }
        if (password.isBlank()) {
            uiState = LoginUiState.Error("Password tidak boleh kosong")
            return
        }

        // 2. Set loading
        uiState = LoginUiState.Loading

        viewModelScope.launch(Dispatchers.IO) {
            try {
                // 3. Panggil API login → mengembalikan LoginDRO langsung
                val loginBody: LoginDRO =
                    webService.login(LoginDTO(username = email, password = password))

                withContext(Dispatchers.Main) {
                    val user = loginBody.user
                    if (user != null) {
                        // Cek role: "admin" atau "customer"
                        val displayName = user.display_name
                        when (user.role) {
                            "admin" -> uiState = LoginUiState.SuccessAdmin(displayName)
                            "customer" -> uiState = LoginUiState.SuccessCustomer(displayName)
                            "expert" -> uiState = LoginUiState.SuccessExpert(displayName)
                            else -> uiState = LoginUiState.Error("Role tidak dikenali")
                        }
                    } else {
                        // Jika server mengembalikan pesan error di field message
                        val err = loginBody.message.ifBlank {
                            "Login gagal: data user tidak tersedia."
                        }
                        uiState = LoginUiState.Error(err)
                    }
                }
            } catch (e: HttpException) {
                withContext(Dispatchers.Main) {
                    uiState = LoginUiState.Error("Server error: ${e.message()}")
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    uiState = LoginUiState.Error("Gagal login: ${e.localizedMessage}")
                }
            }
        }
    }

    fun resetState() {
        uiState = LoginUiState.Idle
    }
}
