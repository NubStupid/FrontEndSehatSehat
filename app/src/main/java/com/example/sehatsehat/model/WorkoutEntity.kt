package com.example.sehatsehat.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "workouts")
data class WorkoutEntity(
    @PrimaryKey val id: String,
    val workout_title: String,
    val estimated_time: Int,
    val focused_at: String,
    val program_id: String,
    val createdAt: String? = null,
    val updatedAt: String? = null,
    val deletedAt: String? = null,
    val expert_username: String? = "Sehat Sehat Expert"
)
