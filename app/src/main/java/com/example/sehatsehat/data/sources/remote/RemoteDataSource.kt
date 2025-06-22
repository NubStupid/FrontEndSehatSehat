package com.example.sehatsehat.data.sources.remote

import com.example.sehatsehat.model.Article
import com.example.sehatsehat.model.ChatLogEntity
import com.example.sehatsehat.model.ProgramEntity
import com.example.sehatsehat.model.ReportItem
import com.example.sehatsehat.model.UserPogramEntity
import retrofit2.Response

interface RemoteDataSource {
    suspend fun chatToChatbot(message:ChatBotDTO):ChatBotMessage
    suspend fun chatToCustomerService(message:ChatBotDTO):ChatBotMessage

    suspend fun insertChatLog(chatlog:ChatLogDTO):ChatLogDRO
    suspend fun getFromGroupChatLog(group_id:String):ChatLogFromGroupDRO
    suspend fun syncChatGroup(group_id:String, logs:List<ChatLogEntity>):ChatLogFromGroupDRO
    suspend fun syncProgram():ProgramListDRO
    suspend fun syncUser():UserListDRO
    suspend fun syncProgramProgress():ProgramProgressDRO
    suspend fun syncUserProgram():UserProgramListDRO
    suspend fun syncWorkout():WorkoutListDRO
    suspend fun syncMeal():MealListDRO

    suspend fun getArticles():List<Article>

    // ==== Auth / User ====
    suspend fun registerUser(user: UserDTO): RegisterDRO
    suspend fun loginUser(credentials: LoginDTO): LoginDRO

    // ==== Program CRUD ====
    suspend fun getAllPrograms(): ProgramListDRO
    suspend fun getProgramById(id: String): ProgramDRO
    suspend fun insertProgram(program: ProgramEntity): ProgramDRO
    suspend fun updateProgram(id: String, program: ProgramEntity): ProgramDRO
    suspend fun deleteProgram(id: String)
    suspend fun getProgramByUser(username: String):List<ProgramEntity>
    suspend fun incrementProgress(progress_id: String): ProgramProgressSingleDRO

    // user
    suspend fun getAllUsers(): UserListDRO
    suspend fun getuserProfile(username: String): UserDRO
    suspend fun deleteUser(username: String)
    suspend fun userTopUp(username: String, amount: Int): userTopUpResponse
    suspend fun userUpdateProfile(username: String, display_name: String, password: String, dob: String): userUpdateProfileResponse

    // report
    suspend fun getReportByRange(start: String, end: String): Response<List<ReportItem>>
}