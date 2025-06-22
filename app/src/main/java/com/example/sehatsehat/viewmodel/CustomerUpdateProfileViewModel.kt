package com.example.sehatsehat.viewmodel

import android.util.Log
import androidx.lifecycle.*
import com.example.sehatsehat.data.sources.remote.UserDRO
import com.example.sehatsehat.data.sources.remote.WebService
import com.example.sehatsehat.data.sources.remote.userUpdateProfileResponse
import kotlinx.coroutines.launch

class CustomerUpdateProfileViewModel(
    private val apiService: WebService
) : ViewModel() {

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _profile = MutableLiveData<UserDRO?>()
    val profile: LiveData<UserDRO?> = _profile

    private val _displayName = MutableLiveData<String>()
    val displayName: LiveData<String> = _displayName

    private val _password = MutableLiveData<String>()
    val password: LiveData<String> = _password

    private val _dob = MutableLiveData<String>()
    val dob: LiveData<String> = _dob

    private val _successMessage = MutableLiveData<String?>()
    val successMessage: LiveData<String?> = _successMessage

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    fun init(username: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                Log.e("username di view model", username)
                val user: UserDRO = apiService.getUserProfile(username)
                _profile.value = user
                _displayName.value = user.display_name
                _password.value = user.password
                _dob.value = user.dob
            } catch (e: Exception) {
                _errorMessage.value = e.localizedMessage ?: "Failed to load profile"
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun onDisplayNameChange(newName: String) {
        _displayName.value = newName
    }

    fun onPasswordChange(newPassword: String) {
        _password.value = newPassword
    }

    fun onDobChange(newDob: String) {
        _dob.value = newDob
    }

    fun updateProfile() {
        val user = _profile.value ?: return
        val name = _displayName.value.orEmpty()
        val pass = _password.value.orEmpty()
        val birth = _dob.value.orEmpty()

        viewModelScope.launch {
            _isLoading.value = true
            _successMessage.value = null
            _errorMessage.value = null
            try {
                val resp: userUpdateProfileResponse = apiService.userUpdateProfile(
                    user.username,
                    name,
                    pass,
                    birth
                )
                if (resp.success) _successMessage.value = resp.message
                else _errorMessage.value = resp.message
            } catch (e: Exception) {
                _errorMessage.value = e.localizedMessage ?: "Failed to update profile"
            } finally {
                _isLoading.value = false
            }
        }
    }
}