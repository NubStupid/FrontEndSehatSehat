package com.example.sehatsehat.data.sources.remote

import com.example.sehatsehat.model.MealEntity
import com.example.sehatsehat.model.ProgramEntity
import com.example.sehatsehat.model.ProgramProgressEntity
import com.example.sehatsehat.model.ReportItem
import com.example.sehatsehat.model.WorkoutEntity
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.DELETE
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.PUT
import retrofit2.http.Path

interface WebService {

    @GET("/api/v1/news")
    suspend fun getArticles(): Response<ArticlesDRO>
//    CHATBOT
    @POST("/api/v1/chatbot")
    suspend fun chatToChatBot(@Body body: ChatBotDTO):ChatBotMessage

    @POST("/api/v1/customer_service")
    suspend fun chatToCustomerService(@Body body: ChatBotDTO):ChatBotMessage

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

    @POST("/api/v1/programs/sync")
    suspend fun syncProgram():ProgramListDRO

    @POST("/api/v1/programs/progress/sync")
    suspend fun syncProgramProgress():ProgramProgressDRO


    @GET("/api/v1/programs")
    suspend fun getAllPrograms(): ProgramListDRO

    @POST("/api/v1/programs/user/sync")
    suspend fun syncUserProgram():UserProgramListDRO

    /**
     * GET detail program per ID. → ProgramDRO
     */
    @GET("/api/v1/programs/{id}")
    suspend fun getProgramById(
        @Path("id") id: String
    ): ProgramDRO

    @POST("/api/v1/programs/user")
    suspend fun getProgramByUser(
        @Body body: getUserProgramDTO
    ): List<ProgramEntity>

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
    suspend fun deleteProgram(@Path("id") id: String): Unit

    // user
    @GET("/api/v1/users")
    suspend fun getAllUsers(): UserListDRO

    @POST("/api/v1/users/sync")
    suspend fun syncUser(): UserListDRO


    @DELETE("/api/v1/users/{username}")
    suspend fun deleteUser(
        @Path("username") username: String
    ): Response<Unit>

    @POST("/meals")
    suspend fun insertMeal(@Body meal: MealEntity): Response<Unit>

    @POST("/workouts")
    suspend fun insertWorkout(@Body workout: WorkoutEntity): Response<Unit>

    @GET("/api/v1/programs/{program_id}/meals")
    suspend fun getMealsByProgramId(
        @Path("program_id") programId: String
    ): MealListDRO

    @GET("/api/v1/programs/{program_id}/workouts")
    suspend fun getWorkoutsByProgramId(
        @Path("program_id") programId: String
    ): WorkoutListDRO

    @PUT("/api/v1/users/{username}/{role}")
    suspend fun updateUserRole(
        @Path("username") username: String,
        @Path("role") role: String
    ): UpdateRoleResponse

    @GET("/api/v1/reports/monthly-purchases")
    suspend fun getMonthlyReport(): Response<List<ReportItem>>

    @GET("/api/v1/reports/{start}/{end}")
    suspend fun getReportByRange(
        @Path("start") start: String,
        @Path("end") end: String
    ): Response<List<ReportItem>>

    @PUT("/api/v1/users/{username}/topup/{amount}")
    suspend fun userTopUp(
        @Path("username") username: String,
        @Path("amount") amount: Int
    ): userTopUpResponse

    @GET("/api/v1/users/{username}")
    suspend fun getUserProfile(
        @Path("username") username: String
    ): UserDRO

    @PUT("/api/v1/users/{username}/updateProfile/{display_name}/{password}/{dob}")
    suspend fun userUpdateProfile(
        @Path("username") username: String,
        @Path("display_name") display_name: String,
        @Path("password") password: String,
        @Path("dob") dob: String
    ): userUpdateProfileResponse

    @PUT("/api/v1/programs/progress/{progress_id}")
    suspend fun incrementProgress(
        @Path("progress_id") progress_id: String
    ): ProgramProgressSingleDRO

    @POST("/api/v1/workout/sync")
    suspend fun syncWorkout(): WorkoutListDRO

    @POST("/api/v1/meal/sync")
    suspend fun syncMeal(): MealListDRO
}