package com.example.sehatsehat.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "workout_sessions")
data class WorkoutSession(
    @PrimaryKey val id: String = "",
    val title: String = "",
    val time: String = "",
    val credits: Int = 0,
    val date: String = "",
    val duration: Int = 0, // in minutes
    val difficulty: String = "Medium",
    val category: String = "",
    val description: String = "",
    val instructorName: String = "",
    val imageUrl: String? = null,
    val isBooked: Boolean = false,
    val maxParticipants: Int = 0,
    val currentParticipants: Int = 0
)