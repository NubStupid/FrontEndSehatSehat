package com.example.sehatsehat.data.repositories

import com.example.sehatsehat.data.sources.remote.DashboardDRO
import com.example.sehatsehat.data.sources.remote.LoginDRO
import com.example.sehatsehat.data.sources.remote.LoginDTO
import com.example.sehatsehat.data.sources.remote.PaymentRequest
import com.example.sehatsehat.data.sources.remote.PaymentResponse
import com.example.sehatsehat.data.sources.remote.RegisterDRO
import com.example.sehatsehat.data.sources.remote.UserDRO
import com.example.sehatsehat.data.sources.remote.UserDTO
import com.example.sehatsehat.data.sources.remote.userTopUpResponse
import com.example.sehatsehat.data.sources.remote.userUpdateProfileResponse
import com.example.sehatsehat.model.Article
import com.example.sehatsehat.model.ChatLogEntity
import com.example.sehatsehat.model.FitnessProgram
import com.example.sehatsehat.model.MealEntity
import com.example.sehatsehat.model.ProgramEntity
import com.example.sehatsehat.model.ProgramProgressEntity
import com.example.sehatsehat.model.ReportItem
import com.example.sehatsehat.model.UserEntity
import com.example.sehatsehat.model.UserPogramEntity
import com.example.sehatsehat.model.WorkoutEntity
import retrofit2.Response
import retrofit2.Call

interface SehatRepository{
    suspend fun chatGroupSync(group_id: String)
    suspend fun programSync()
    suspend fun userSync()
    suspend fun programProgressSync()
    suspend fun userProgramSync()
    suspend fun workoutSync()
    suspend fun mealSync()

    suspend fun getArticles():List<Article>

    suspend fun getProgramDashboard():DashboardDRO

    suspend fun getAllChatLog():List<ChatLogEntity>
    suspend fun getFromGroupChatLog(group_id:String): List<ChatLogEntity>
    suspend fun getFromChatbotChatLog(username:String): List<ChatLogEntity>
    suspend fun getFromCSChatLog(username:String): List<ChatLogEntity>
    suspend fun insertChatLog(content:String, username:String,chat_group:String)
    suspend fun updateChatLog(id:String, content:String)
    suspend fun deleteChatLog(id:String)

    suspend fun chatToChatbot(content:String,username:String,chat_group: String)
    suspend fun chatToCustomerService(content:String,username:String,chat_group: String)

    suspend fun registerUser(user: UserDTO): RegisterDRO
    suspend fun loginUser(credentials: LoginDTO): LoginDRO

    suspend fun getAllPrograms(): List<ProgramEntity>
    suspend fun getProgramById(id: String): ProgramEntity?
    suspend fun insertProgram(program: ProgramEntity)
    suspend fun updateProgram(program: ProgramEntity)
    suspend fun deleteProgram(program: ProgramEntity)
    suspend fun getProgramByUser(username: String): List<ProgramEntity>
    suspend fun getAllUserProgramForUI(username: String):List<FitnessProgram>
    suspend fun getAllProgramForPurchase(username:String):List<FitnessProgram>
    suspend fun getProgramProgressById(id:String):ProgramProgressEntity?
    suspend fun getUserProgramByProgramId(id:String):UserPogramEntity?

    suspend fun getAllUsers(): List<UserEntity>
    suspend fun getUserProfile(username: String): UserDRO
    suspend fun deleteUser(user: UserEntity)

    suspend fun userTopUp(username: String, amount: Int): userTopUpResponse
    suspend fun userUpdateProfile(username: String, display_name: String, password: String, dob: String): userUpdateProfileResponse

    suspend fun getWorkoutById(id:String):WorkoutEntity?
    suspend fun getMealById(id: String):MealEntity?
    suspend fun incrementProgress(progress_id:String)

    suspend fun createPaymentTransaction(request: PaymentRequest): PaymentResponse

    suspend fun insertUrl(url:String)
    suspend fun deleteUrl()
    suspend fun getUrl():String?

    suspend fun getReportByRange(start: String, end: String): Response<List<ReportItem>>
}