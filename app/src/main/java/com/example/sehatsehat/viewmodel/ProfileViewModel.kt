package com.example.sehatsehat.viewmodel

import androidx.compose.ui.text.intl.Locale
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sehatsehat.data.repositories.SehatRepository
import com.example.sehatsehat.model.UserEntity
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.Date
import kotlin.text.format

class ProfileViewModel(
    private val sehatRepository: SehatRepository
):ViewModel(){
    private val _memberSince = MutableLiveData<String>()
    val memberSince: LiveData<String> = _memberSince
    private val _activeUser = MutableLiveData<UserEntity>()
    val activeUser: LiveData<UserEntity> = _activeUser
    private val _programAssigned = MutableLiveData<Int>()
    val programAssigned: LiveData<Int> = _programAssigned

    fun init(user: UserEntity){
        viewModelScope.launch {
            _activeUser.value = user
//            val sdf = SimpleDateFormat("dd MM yyyy", java.util.Locale.getDefault())
//            val inputFormat = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'", java.util.Locale.ENGLISH)
            _memberSince.value = "Member since ${user.createdAt.split("T").get(0)}"
//
            val programList = sehatRepository.getProgramByUser(user.username)
            _programAssigned.value = programList.size

        }
    }
}