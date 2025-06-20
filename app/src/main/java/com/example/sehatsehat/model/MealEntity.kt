package com.example.sehatsehat.model

data class MealEntity(
    val id: String,
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
