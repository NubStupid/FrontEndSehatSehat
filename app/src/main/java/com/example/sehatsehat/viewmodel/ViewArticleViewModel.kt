package com.example.sehatsehat.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.sehatsehat.data.sources.remote.ArticleJSON

class ViewArticleViewModel : ViewModel() {

    private val _article = MutableLiveData<ArticleJSON>()
    val article: LiveData<ArticleJSON> get() = _article

    fun setArticle(article: ArticleJSON) {
        _article.value = article
    }
}
