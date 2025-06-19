package com.example.sehatsehat.model

import androidx.room.Entity
import androidx.room.PrimaryKey


@Entity(tableName = "programs_progress")
data class ProgramProgressEntity(
    @PrimaryKey val id: String,
    val program_id: String,
    val progress_index: Int,
    val progress_list:String,
    val progress_list_type:String,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
    val deletedAt: Long? = System.currentTimeMillis()
)
