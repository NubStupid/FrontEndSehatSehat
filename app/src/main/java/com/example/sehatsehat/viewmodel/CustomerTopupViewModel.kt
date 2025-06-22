package com.example.sehatsehat.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sehatsehat.data.repositories.SehatRepository
import kotlinx.coroutines.launch

class CustomerTopupViewModel(
    private val sehatRepository: SehatRepository
) : ViewModel() {

    private val _isLoading = MutableLiveData(false)
    val isLoading: LiveData<Boolean> = _isLoading

    private val _amountInput = MutableLiveData<Int>()
    val amountInput: LiveData<Int> = _amountInput

    private val _successMessage = MutableLiveData<String?>()
    val successMessage: LiveData<String?> = _successMessage

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> = _errorMessage

    init {
        _amountInput.value = 0
        _successMessage.value = null
        _errorMessage.value = null
    }

    fun updateAmount(newAmount: Int) {
        _amountInput.value = newAmount
        Log.d("TopupViewModel", "updateAmount: $newAmount")
    }

    fun sendTopUp(username: String) {
        val amount = _amountInput.value ?: run {
            _errorMessage.value = "Jumlah tidak valid"
            return
        }

        viewModelScope.launch {
            _isLoading.value = true
            _successMessage.value = null
            _errorMessage.value = null

            try {
                val response = sehatRepository.userTopUp(username, amount)
                if (response.success) {
                    _successMessage.value = response.message
                    Log.d("TopupViewModel", "Top-up sukses: ${response.message}")
                } else {
                    _errorMessage.value = response.message
                    Log.d("TopupViewModel", "Top-up gagal: ${response.message}")
                }
            } catch (e: Exception) {
                _errorMessage.value = e.localizedMessage ?: "Unknown error"
                Log.e("TopupViewModel", "Exception saat top-up", e)
            } finally {
                _isLoading.value = false
            }
        }
    }
}
