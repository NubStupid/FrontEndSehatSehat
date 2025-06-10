package com.example.sehatsehat.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "programs")
data class ProgramEntity(
    @PrimaryKey val id: String,
    val program_name: String,
    val pricing: Float,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val deletedAt: Long = System.currentTimeMillis()
)
