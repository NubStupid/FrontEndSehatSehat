package com.example.sehatsehat.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sehatsehat.SehatApplication
import com.example.sehatsehat.data.repositories.SehatDefaultRepository
import com.example.sehatsehat.data.repositories.SehatRepository
import com.example.sehatsehat.data.sources.remote.DashboardDRO
import com.example.sehatsehat.model.ProgramEntity
import kotlinx.coroutines.launch

class AdminHomepageViewModel(
    private val sehatRepository:SehatRepository
):ViewModel(){
    private val _availableProgram = MutableLiveData<List<ProgramEntity>>()
    val availableProgram: LiveData<List<ProgramEntity>> = _availableProgram

    private val _ongoingProgram = MutableLiveData<List<ProgramEntity>>()
    val ongoingProgram: LiveData<List<ProgramEntity>> = _ongoingProgram

    private val _completedProgram = MutableLiveData<List<ProgramEntity>>()
    val completedProgram: LiveData<List<ProgramEntity>> = _completedProgram

    private val availableProgramCount = MutableLiveData<Int>()
    val availableProgramCountLiveData: LiveData<Int> = availableProgramCount

    private val ongoingProgramCount = MutableLiveData<Int>()
    val ongoingProgramCountLiveData: LiveData<Int> = ongoingProgramCount

    private val completedProgramCount = MutableLiveData<Int>()
    val completedProgramCountLiveData: LiveData<Int> = completedProgramCount



    fun init(){
        viewModelScope.launch {
            sehatRepository.programSync()
            sehatRepository.userSync()
            sehatRepository.programProgressSync()

            val chartData:DashboardDRO = sehatRepository.getProgramDashboard()
            availableProgramCount.value = chartData.available.size
            ongoingProgramCount.value = chartData.ongoing.size
            completedProgramCount.value = chartData.completed.size
            _availableProgram.value = chartData.available
            _ongoingProgram.value = chartData.ongoing
            _completedProgram.value = chartData.completed
        }
    }
}