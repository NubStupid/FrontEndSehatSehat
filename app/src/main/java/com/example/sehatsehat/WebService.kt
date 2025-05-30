package com.example.sehatsehat

import com.example.sehatsehat.model.UserRequest
import com.example.sehatsehat.model.UserResponse
import kotlinx.serialization.Serializable
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

@Serializable
data class RegisterResponse(
    val message: String,
    val user: UserResponse? = null
)

@Serializable
data class LoginRequest(
    val username: String,
    val password: String
)

@Serializable
data class LoginResponse(
    val message: String,
    val user: UserResponse? = null
)

interface WebService {
    @POST("register")
    suspend fun register(@Body user: UserRequest): Response<RegisterResponse>

    @POST("login")
    suspend fun login(@Body loginRequest: LoginRequest): Response<LoginResponse>
}
