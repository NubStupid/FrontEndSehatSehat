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
    val backgroundGradient: List<Long>, // store raw color values
    val isPurchased: Boolean = false,
    val price: String = "Rp 150.000",
    val rating: Float = 4.8f,
    val totalUsers: Int = 1234,
    val instructor: String = "John Doe",
    val benefits: List<String> = listOf(
        "Burn fat effectively",
        "Increase stamina",
        "Build lean muscle",
        "Improve cardiovascular health"
    ),
    val detailedDescription: String = "This comprehensive program combines high-intensity interval training with strength exercises..."
) : Parcelable
