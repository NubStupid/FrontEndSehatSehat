package com.example.sehatsehat.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sehatsehat.SehatApplication
import com.example.sehatsehat.model.ProgramEntity
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue

//StateFlow
//
//Ini adalah API dari Kotlin Coroutines untuk merepresentasikan state yang bisa di‑observe secara reaktif.
//
//Bedanya dengan MutableState di Compose, StateFlow bisa dipakai di banyak lapis (ViewModel, Repository, dll), dan bisa di‑collect dalam Composable dengan collectAsState() untuk mendapatkan pembaruan.
//
//editUiState: StateFlow<EditProgramUiState>
//
//Merupakan aliran (flow) dari EditProgramUiState, yang menampung:
//
//program: ProgramEntity? → data program yang sedang diedit
//
//isLoading: Boolean → flag loading saat fetch/update
//
//error: String? → pesan error jika ada
//
//Kita simpan di AdminListProgramViewModel sebagai _editUiState (private MutableStateFlow) dan expose sebagai read‑only StateFlow.

class AdminListProgramViewModel : ViewModel() {
    var programs by mutableStateOf<List<ProgramEntity>>(emptyList())
        private set
    var isLoading by mutableStateOf(false)
        private set
    var errorMessage by mutableStateOf<String?>(null)
        private set

    private val _editUiState = MutableStateFlow(EditProgramUiState())
    val editUiState: StateFlow<EditProgramUiState> = _editUiState.asStateFlow()

    init { fetchPrograms() }

    fun fetchPrograms() {
        viewModelScope.launch {
            isLoading = true; errorMessage = null
            try {
                val dro = SehatApplication.retrofitService.getAllPrograms()
                programs = dro.programs.filter { it.deletedAt == null }
            } catch (e: Exception) {
                errorMessage = e.localizedMessage ?: "Unknown error"
            } finally {
                isLoading = false
            }
        }
    }

    fun deleteProgram(id: String) {
        viewModelScope.launch {
            isLoading = true; errorMessage = null
            try {
                SehatApplication.retrofitService.deleteProgram(id)
                fetchPrograms()
            } catch (e: Exception) {
                errorMessage = e.localizedMessage ?: "Failed to delete program"
            } finally {
                isLoading = false
            }
        }
    }

    fun loadProgramForEdit(id: String) {
        viewModelScope.launch {
            _editUiState.value = EditProgramUiState(isLoading = true)
            try {
                val dro = SehatApplication.retrofitService.getProgramById(id)
                _editUiState.value = EditProgramUiState(
                    program = dro.program,
                    isLoading = false
                )
            } catch (e: Exception) {
                _editUiState.value = EditProgramUiState(
                    error = e.localizedMessage ?: "Gagal memuat program",
                    isLoading = false
                )
            }
        }
    }
}
