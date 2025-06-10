package com.example.sehatsehat.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
//import com.example.sehatsehat.data.converters.ListStringConverter
//import com.example.sehatsehat.data.converters.WorkoutSessionConverter

@Entity(tableName = "fitness_programs")
//@TypeConverters(ListStringConverter::class, WorkoutSessionConverter::class)
data class FitnessProgram(
    @PrimaryKey val id: String = "",
    val title: String = "",
    val dateRange: String = "",
    val description: String = "",
    val duration: String = "",
    val totalHours: Double = 0.0,
    val progress: Float = 0f,
    val backgroundGradient: List<String> = emptyList(),
    val difficulty: String = "Medium",
    val category: String = "",
    val instructorName: String = "",
    val price: Double = 0.0,
    val isEnrolled: Boolean = false,
    val sessions: List<WorkoutSession> = emptyList()
)