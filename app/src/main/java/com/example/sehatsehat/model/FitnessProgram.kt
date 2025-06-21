package com.example.sehatsehat.model

import android.os.Parcelable
import androidx.compose.ui.graphics.Color
import androidx.room.Entity
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "fitness_programs")
data class FitnessProgram(
    val id: Int,
    val title: String,
    val dateRange: String,
    val description: String,
    val duration: String,
    val progress: Float,
    val backgroundGradient: List<Long>,
    val isPurchased: Boolean = false,
    val price: Int = 0,
    val instructor: String = "Sehat Sehat Expert",
    val benefits: List<String> = listOf(
        "Burn fat effectively",
        "Increase stamina",
        "Build lean muscle",
        "Improve cardiovascular health"
    ),
    val detailedDescription: String
) : Parcelable
