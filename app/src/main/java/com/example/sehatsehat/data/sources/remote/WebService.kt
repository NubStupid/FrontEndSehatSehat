package com.example.sehatsehat.data.sources.remote

import com.example.sehatsehat.model.ProgramEntity
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface WebService {
//    CHATBOT
    @POST("/api/v1/chatbot")
    suspend fun chatToChatBot(@Body body: ChatBotDTO):ChatBotMessage

    @POST("/api/v1/chat")
    suspend fun addChatLog(@Body body:ChatLogDTO):ChatLogDRO

    @GET("/api/v1/chat/{chat_group_id}")
    suspend fun getChatfromGroup(@Path("chat_group_id") chat_group_id:String):ChatLogFromGroupDRO

    @POST("/api/v1/chat/sync")
    suspend fun syncChatGroup(@Body body:SyncChatGroupDTO):ChatLogFromGroupDRO

    // Ubah return type menjadi RegisterDRO (bukan Response<RegisterDRO>)
    @POST("/api/v1/register")
    suspend fun register(
        @Body body: UserDTO
    ): RegisterDRO

    // Ubah return type menjadi LoginDRO (bukan Response<LoginDRO>)
    @POST("/api/v1/login")
    suspend fun login(@Body body: LoginDTO): LoginDRO

    // ===================== PROGRAM CRUD =====================

    /**
     * GET semua program. → ProgramListDRO
     */
    @GET("/api/v1/programs")
    suspend fun getAllPrograms(): ProgramListDRO

    /**
     * GET detail program per ID. → ProgramDRO
     */
    @GET("/api/v1/programs/{id}")
    suspend fun getProgramById(
        @Path("id") id: String
    ): ProgramDRO

    /**
     * POST create program. Body: ProgramEntity ( dianggap JSON sesuai field ProgramEntity ).
     * Response: ProgramDRO
     */
    @POST("/api/v1/programs")
    suspend fun insertProgram(
        @Body body: ProgramEntity
    ): ProgramDRO

    /**
     * PUT update program per ID. Body: ProgramEntity.
     * Response: ProgramDRO
     */
    @PUT("/api/v1/programs/{id}")
    suspend fun updateProgram(
        @Path("id") id: String,
        @Body body: ProgramEntity
    ): ProgramDRO

    /**
     * DELETE program per ID. Response: empty (Response<Unit>).
     */
    @DELETE("/api/v1/programs/{id}")
    suspend fun deleteProgram(
        @Path("id") id: String
    ): Response<Unit>

    // user
    @GET("/api/v1/users")
    suspend fun getAllUsers(): UserListDRO

    @DELETE("/api/v1/users/{username}")
    suspend fun deleteUser(
        @Path("username") username: String
    ): Response<Unit>
}