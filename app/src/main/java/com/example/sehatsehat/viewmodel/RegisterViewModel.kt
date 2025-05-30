package com.example.sehatsehat.viewmodel

import android.app.Application
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.sehatsehat.data.App
import com.example.sehatsehat.data.AppDatabase
import com.example.sehatsehat.model.UserRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import java.util.Calendar
import java.util.Locale

sealed class RegisterUiState {
    object Idle : RegisterUiState()
    object Loading : RegisterUiState()
    object Success : RegisterUiState()
    data class Error(val message: String) : RegisterUiState()
}

class RegisterViewModel(application: Application) : AndroidViewModel(application) {

    private val webService = App.retrofitService

    var username by mutableStateOf("")
        private set

    var displayName by mutableStateOf("")
        private set

    var password by mutableStateOf("")
        private set

    // Kita simpan dob sebagai String "yyyy-MM-dd"
    var dob by mutableStateOf("")
        private set

    var ppUrl by mutableStateOf("") // profile picture URL
        private set

    var uiState by mutableStateOf<RegisterUiState>(RegisterUiState.Idle)
        private set

    fun onUsernameChange(newUsername: String) {
        username = newUsername.trim()
        if (uiState is RegisterUiState.Error) uiState = RegisterUiState.Idle
    }

    fun onDisplayNameChange(newDisplayName: String) {
        displayName = newDisplayName.trim()
        if (uiState is RegisterUiState.Error) uiState = RegisterUiState.Idle
    }

    fun onPasswordChange(newPassword: String) {
        password = newPassword
        if (uiState is RegisterUiState.Error) uiState = RegisterUiState.Idle
    }

    fun onDobChange(newDob: String) {
        dob = newDob
        if (uiState is RegisterUiState.Error) uiState = RegisterUiState.Idle
    }

    fun onPpUrlChange(newPpUrl: String) {
        ppUrl = newPpUrl.trim()
        if (uiState is RegisterUiState.Error) uiState = RegisterUiState.Idle
    }

    fun register() {
        // 1. Validasi input sederhana
        if (username.isBlank()) {
            uiState = RegisterUiState.Error("Username tidak boleh kosong")
            return
        }
        if (displayName.isBlank()) {
            uiState = RegisterUiState.Error("Display name tidak boleh kosong")
            return
        }
        if (password.isBlank()) {
            uiState = RegisterUiState.Error("Password tidak boleh kosong")
            return
        }
        if (dob.isBlank()) {
            uiState = RegisterUiState.Error("Tanggal lahir tidak boleh kosong")
            return
        }
        if (ppUrl.isBlank()) {
            uiState = RegisterUiState.Error("Profile picture URL tidak boleh kosong")
            return
        }

        // 2. Pastikan format dob valid: "yyyy-MM-dd"
        if (!isValidDateFormat(dob)) {
            uiState = RegisterUiState.Error("Format tanggal lahir harus yyyy-MM-dd")
            return
        }

        // 3. Set loading
        uiState = RegisterUiState.Loading

        // 4. Jalankan jaringan di IO
        viewModelScope.launch(Dispatchers.IO) {
            try {
                // Bangun request body
                val userRequest = UserRequest(
                    username = username,
                    display_name = displayName,
                    password = password,
                    dob = dob,
                    role = "customer",
                    pp_url = ppUrl
                )

                // Panggil API
                val response = webService.register(userRequest)

                withContext(Dispatchers.Main) {
                    if (response.isSuccessful) {
                        // Misalnya server mengembalikan { message: "...", user: {...} }
                        uiState = RegisterUiState.Success
                    } else {
                        // Ambil pesan error jika ada di errorBody
                        val err = response.errorBody()?.string()
                            ?: "Registrasi gagal (kode ${response.code()})"
                        uiState = RegisterUiState.Error(err)
                    }
                }
            } catch (e: HttpException) {
                withContext(Dispatchers.Main) {
                    uiState = RegisterUiState.Error("Server error: ${e.message()}")
                }
            } catch (e: Exception) {
                withContext(Dispatchers.Main) {
                    uiState = RegisterUiState.Error("Gagal registrasi: ${e.localizedMessage}")
                }
            }
        }
    }

    private fun isValidDateFormat(dateStr: String): Boolean {
        // Cek format "yyyy-MM-dd" sederhana
        val parts = dateStr.split("-")
        if (parts.size != 3) return false
        return try {
            val year = parts[0].toInt()
            val month = parts[1].toInt()
            val day = parts[2].toInt()
            val cal = Calendar.getInstance(Locale.getDefault())
            cal.isLenient = false
            cal.set(year, month - 1, day)
            cal.time // akses untuk memicu exception jika invalid
            true
        } catch (t: Throwable) {
            false
        }
    }

    fun resetState() {
        uiState = RegisterUiState.Idle
    }
}
