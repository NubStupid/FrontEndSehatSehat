package com.example.sehatsehat.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "user_programs")
data class UserPogramEntity(
    @PrimaryKey val id: String,
    val program_id: String,
    val username: String,
    val expires_in:Long = System.currentTimeMillis(),
    val chat_group_id:String,
    val program_progress_id:String,
    val createdAt: Long = System.currentTimeMillis(),
    val updatedAt: Long = System.currentTimeMillis(),
)