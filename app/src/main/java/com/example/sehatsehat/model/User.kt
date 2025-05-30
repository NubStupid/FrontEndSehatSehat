package com.example.sehatsehat.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.serialization.Serializable

@Serializable
@Entity("users")
data class User(
    @PrimaryKey(autoGenerate = false) val username: String,
    var display_name: String,
    var password: String,
    var dob: String,
    var role: String,
    var pp_url: String,
    var createdAt: String,
    var updatedAt: String
)
