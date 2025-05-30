package com.example.sehatsehat.model

import kotlinx.serialization.Serializable

@Serializable
data class UserRequest(
    val username: String,
    val display_name: String,
    var password: String,
    var dob: String,            // format "yyyy-MM-dd"
    var role: String,
    var pp_url: String
)

@Serializable
data class RegisterResponse(
    val message: String,
    val user: UserResponse? = null
)

@Serializable
data class UserResponse(
    val username: String,
    val display_name: String,
    val password: String,
    val dob: String,
    val role: String,
    val pp_url: String,
    val createdAt: String,
    val updatedAt: String
)
