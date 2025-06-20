package com.example.sehatsehat.model

data class WorkoutEntity(
    val id: String,
    val workout_title: String,
    val estimated_time: Int,
    val focused_at: String,
    val program_id: String,
    val createdAt: String? = null,
    val updatedAt: String? = null,
    val deletedAt: String? = null
)
