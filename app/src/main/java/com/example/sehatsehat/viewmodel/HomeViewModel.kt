package com.example.sehatsehat.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sehatsehat.data.repositories.SehatRepository
import com.example.sehatsehat.model.Article
import com.example.sehatsehat.model.FitnessProgram
import com.example.sehatsehat.model.UserEntity
import kotlinx.coroutines.launch

class HomeViewModel(
    private val sehatRepository: SehatRepository
) : ViewModel() {
    private val _activeUser = MutableLiveData<UserEntity>()
    val activeUser: LiveData<UserEntity> = _activeUser

    private val _featuredArticles = MutableLiveData<List<Article>>()
    val featuredArticles: LiveData<List<Article>> = _featuredArticles

    private val _regularArticles = MutableLiveData<List<Article>>()
    val regularArticles: LiveData<List<Article>> = _regularArticles

    private val _purchasedPrograms = MutableLiveData<List<FitnessProgram>>()
    val purchasedPrograms: LiveData<List<FitnessProgram>> = _purchasedPrograms

    private val _programAvailable = MutableLiveData<List<FitnessProgram>>()
    val programAvailable: LiveData<List<FitnessProgram>> = _programAvailable

    fun init(user: UserEntity) {
        viewModelScope.launch {
            try {
                // Sinkronisasi awal
                sehatRepository.programSync()
                sehatRepository.userProgramSync()
                sehatRepository.programProgressSync()

                // Simpan user & load data
                _activeUser.value = user
                val articles = sehatRepository.getArticles()
                _featuredArticles.value = articles.take(articles.size / 2)
                _regularArticles.value = articles.drop(articles.size / 2)
                _purchasedPrograms.value = sehatRepository.getAllUserProgramForUI(user.username)
                _programAvailable.value = sehatRepository.getAllProgramForPurchase(user.username)
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Error init data", e)
            }
        }
    }

    fun fetchArticles() {
        viewModelScope.launch {
            try {
                val articles = sehatRepository.getArticles()
                _featuredArticles.value = articles.take(articles.size / 2)
                _regularArticles.value = articles.drop(articles.size / 2)
            } catch (e: Exception) {
                Log.e("HomeViewModel", "Failed to fetch articles", e)
            }
        }
    }

    /**
     * Update balance lokal tanpa panggil API
     */
    fun updateBalance(newBalance: Int) {
        _activeUser.value = _activeUser.value?.apply { balance = newBalance }
    }
}