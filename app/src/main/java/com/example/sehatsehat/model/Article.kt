package com.example.sehatsehat.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
data class Article(
    val id: Int,
    val title: String,
    val description: String,
    val date: String,
    val imageUrl: String? = null,
    val isFeatured: Boolean = false,
    val author: String = "",
    val readTime: String = "",
    val content: String = "",
    val tags: List<String> = emptyList()
) : Parcelable