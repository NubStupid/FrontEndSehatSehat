package com.example.sehatsehat.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
data class Article(
    val title: String,
    val description: String,
    val content: String = "",
    val author: String = "",
    val date: String,
) : Parcelable