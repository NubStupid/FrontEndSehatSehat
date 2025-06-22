package com.example.sehatsehat.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("meals")
data class MealEntity(
    @PrimaryKey val id: String,
    val meal_name: String,
    val ingredients: String,
    val calories: Float,
    val fat: Float,
    val protein: Float,
    val program_id: String,
    val createdAt: String? = null,
    val updatedAt: String? = null,
    val deletedAt: String? = null
)
