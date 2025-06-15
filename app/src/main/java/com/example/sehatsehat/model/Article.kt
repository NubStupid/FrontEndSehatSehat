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
    val author: String = "Dr. Smith",
    val readTime: String = "5 min read",
    val content: String = "Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua. Ut enim ad minim veniam, quis nostrud exercitation ullamco laboris nisi ut aliquip ex ea commodo consequat.",
    val tags: List<String> = listOf("Health", "Fitness", "Wellness")
) : Parcelable