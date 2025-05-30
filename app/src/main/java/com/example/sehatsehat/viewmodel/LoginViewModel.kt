package com.example.sehatsehat.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.sehatsehat.LoginRequest
import com.example.sehatsehat.LoginResponse
import com.example.sehatsehat.data.App
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException

sealed class LoginUiState {
    object Idle : LoginUiState()
    object Loading : LoginUiState()
    data class Success(val displayName: String) : LoginUiState()
    data class Error(val message: String) : LoginUiState()
}

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val webService = App.retrofitService

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
                // 3. Panggil API login
                val response = webService.login(LoginRequest(username = email, password = password))

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        val loginBody: LoginResponse? = response.body()
                        val user = loginBody?.user
                        if (user != null) {
                            // Ambil displayName dari UserResponse
                            val displayName = user.display_name
                            uiState = LoginUiState.Success(displayName)
                        } else {
                            uiState = LoginUiState.Error("Response login tidak berisi data user")
                        }
                    } else {
                        // Ambil pesan error dari server (bila ada)
                        val err = response.errorBody()?.string()
                            ?: "Login gagal (kode ${response.code()})"
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
