package com.example.sehatsehat.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sehatsehat.data.repositories.SehatRepository
import com.example.sehatsehat.data.sources.remote.UserDRO
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

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun init(user: UserEntity) {
        viewModelScope.launch {
            try {
                _isLoading.value = true

                // Sinkronisasi awal
                sehatRepository.programSync()
                sehatRepository.userProgramSync()
                sehatRepository.programProgressSync()

                // Simpan user & load data
                _activeUser.value = user
                loadAllData(user.username)

            } catch (e: Exception) {
                Log.e("HomeViewModel", "Error init data", e)
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Extension function untuk konversi UserDRO ke UserEntity
    fun UserDRO.toUserEntity(): UserEntity {
        return UserEntity(
            username = this.username,
            display_name = this.display_name,
            password = this.password,
            dob = this.dob,
            role = this.role,
            pp_url = this.pp_url,
            balance = this.balance,
            createdAt = this.createdAt,
            updatedAt = this.updatedAt
        )
    }

    /**
     * Refresh semua data user setiap kali halaman dibuka
     */
    fun refreshUserData() {
        val currentUser = _activeUser.value
        if (currentUser != null) {
            viewModelScope.launch {
                try {
                    _isLoading.value = true

                    // Ambil data user terbaru dari repository
                    val updatedUser = sehatRepository.getUserProfile(currentUser.username)?.toUserEntity()
                    if (updatedUser != null) {
                        _activeUser.value = updatedUser
                        Log.d("HomeViewModel", "User data refreshed: ${updatedUser.display_name}, Balance: ${updatedUser.balance}")
                    }

                    // Sinkronisasi ulang data
                    sehatRepository.programSync()
                    sehatRepository.userProgramSync()
                    sehatRepository.programProgressSync()

                    // Reload semua data
                    loadAllData(currentUser.username)

                } catch (e: Exception) {
                    Log.e("HomeViewModel", "Error refreshing user data", e)
                } finally {
                    _isLoading.value = false
                }
            }
        }
    }

    private suspend fun loadAllData(username: String) {
        try {
            // Load articles
            val articles = sehatRepository.getArticles()
            _featuredArticles.value = articles.take(articles.size / 2)
            _regularArticles.value = articles.drop(articles.size / 2)

            // Load programs
            _purchasedPrograms.value = sehatRepository.getAllUserProgramForUI(username)
            _programAvailable.value = sehatRepository.getAllProgramForPurchase(username)

        } catch (e: Exception) {
            Log.e("HomeViewModel", "Error loading data", e)
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