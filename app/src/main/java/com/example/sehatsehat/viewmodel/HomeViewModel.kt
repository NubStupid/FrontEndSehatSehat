package com.example.sehatsehat.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.sehatsehat.data.repositories.SehatRepository
import com.example.sehatsehat.model.Article
import com.example.sehatsehat.model.UserEntity
import kotlinx.coroutines.launch

class HomeViewModel(
    private val sehatRepository: SehatRepository
):ViewModel(){
    private val _activeUser = MutableLiveData<UserEntity>()
    val activeUser: LiveData<UserEntity>
        get() = _activeUser

    private val _featuredArticles = MutableLiveData<List<Article>>()
    val featuredArticles: LiveData<List<Article>>
        get() = _featuredArticles

    private val _regularArticles = MutableLiveData<List<Article>>()
    val regularArticles: LiveData<List<Article>>
        get() = _regularArticles


    fun init(user: UserEntity){
        viewModelScope.launch {
            _activeUser.value = user
            val articles = sehatRepository.getArticles()
            Log.d("a", articles.size.toString())
            _featuredArticles.value = articles.take(articles.size/2)
            _regularArticles.value = articles.drop(articles.size/2)
        }

    }
}