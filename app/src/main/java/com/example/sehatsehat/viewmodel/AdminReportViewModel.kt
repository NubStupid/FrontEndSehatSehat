package com.example.sehatsehat.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sehatsehat.SehatApplication
import com.example.sehatsehat.model.ReportItem
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class AdminReportViewModel : ViewModel() {
    private val _reportItems = MutableStateFlow<List<ReportItem>>(emptyList())
    val reportItems: StateFlow<List<ReportItem>> = _reportItems

    fun loadMonthlyReport() {
        viewModelScope.launch {
            try {
                val response = SehatApplication.retrofitService.getMonthlyReport()
                if (response.isSuccessful) {
                    _reportItems.value = response.body() ?: emptyList()
                } else {
                    // Tangani error responsif dari server
                    _reportItems.value = emptyList()
                }
            } catch (e: Exception) {
                // Log atau tangani exception
                _reportItems.value = emptyList()
            }
        }
    }
}