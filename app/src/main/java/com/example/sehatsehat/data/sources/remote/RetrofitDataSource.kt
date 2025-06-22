package com.example.sehatsehat.data.sources.remote

import com.example.sehatsehat.model.Article
import com.example.sehatsehat.model.ChatLogEntity
import com.example.sehatsehat.model.ProgramEntity
import com.example.sehatsehat.model.ReportItem
import retrofit2.Response
import java.util.UUID

class RetrofitDataSource(
    private val retrofitService:WebService
):RemoteDataSource{
    override suspend fun chatToChatbot(message: ChatBotDTO): ChatBotMessage {
        return retrofitService.chatToChatBot(message)
    }

    override suspend fun chatToCustomerService(message: ChatBotDTO): ChatBotMessage {
        return retrofitService.chatToCustomerService(message)
    }

    override suspend fun insertChatLog(chatlog: ChatLogDTO): ChatLogDRO {
        return retrofitService.addChatLog(chatlog)
    }

    override suspend fun getFromGroupChatLog(group_id: String): ChatLogFromGroupDRO {
        return  retrofitService.getChatfromGroup(group_id)
    }

    override suspend fun syncChatGroup(group_id: String, logs: List<ChatLogEntity>):ChatLogFromGroupDRO {
        return retrofitService.syncChatGroup(SyncChatGroupDTO(group_id,logs))
    }

    override suspend fun syncProgram(): ProgramListDRO {
        return retrofitService.syncProgram()
    }

    override suspend fun syncUser(): UserListDRO {
        return retrofitService.syncUser()
    }

    override suspend fun syncProgramProgress(): ProgramProgressDRO {
        return retrofitService.syncProgramProgress()
    }

    override suspend fun syncUserProgram(): UserProgramListDRO {
        return retrofitService.syncUserProgram()
    }

    override suspend fun syncWorkout(): WorkoutListDRO {
        return retrofitService.syncWorkout()
    }

    override suspend fun syncMeal(): MealListDRO {
        return retrofitService.syncMeal()
    }

    override suspend fun getArticles(): List<Article> {
        val response = retrofitService.getArticles()
        val returned = arrayListOf<Article>()
        if (response.isSuccessful) {
            val articles = response.body()?.response
            if (articles != null) {
                articles.map {
                    returned.add(Article(
                        id = UUID.randomUUID().variant(),
                        author = it.author,
                        title = it.title,
                        description = it.description,
                        date = it.publishedAt,
                        content = it.content,
                    ))
                }
            } else {
                throw RuntimeException("Gagal mengambil data dari server")
            }
        }
        return returned
    }
    // ======================================
    // 2) Auth / User
    // ======================================
    override suspend fun registerUser(user: UserDTO): RegisterDRO {
        return retrofitService.register(user)
    }

    override suspend fun loginUser(credentials: LoginDTO): LoginDRO {
        return retrofitService.login(credentials)
    }

    // ======================================
    // 3) Program CRUD
    // ======================================
    override suspend fun getAllPrograms(): ProgramListDRO {
        return retrofitService.getAllPrograms()
    }

    override suspend fun getProgramById(id: String): ProgramDRO {
        return retrofitService.getProgramById(id)
    }

    override suspend fun insertProgram(program: ProgramEntity): ProgramDRO {
        return retrofitService.insertProgram(program)
    }

    override suspend fun updateProgram(id: String, program: ProgramEntity): ProgramDRO {
        return retrofitService.updateProgram(id, program)
    }

    override suspend fun deleteProgram(id: String) {
        val response: Unit = retrofitService.deleteProgram(id)
    }

    override suspend fun getProgramByUser(username: String): List<ProgramEntity> {
        return retrofitService.getProgramByUser(getUserProgramDTO(username))
    }

    override suspend fun incrementProgress(progress_id: String): ProgramProgressSingleDRO {
        return retrofitService.incrementProgress(progress_id)
    }

    // user
    override suspend fun getAllUsers(): UserListDRO {
        return retrofitService.getAllUsers()
    }

    override suspend fun getuserProfile(username: String): UserDRO {
        return retrofitService.getUserProfile(username)
    }

    override suspend fun deleteUser(username: String) {
        // Panggil endpoint DELETE → Response<Unit>
        val response: Response<Unit> = retrofitService.deleteUser(username)
        if (!response.isSuccessful) {
            throw RuntimeException("Gagal menghapus user username=$username (status code: ${response.code()})")
        }
    }

    override suspend fun userTopUp(username: String, amount: Int): userTopUpResponse {
        return retrofitService.userTopUp(username, amount)
    }

    override suspend fun userUpdateProfile(
        username: String,
        display_name: String,
        password: String,
        dob: String
    ): userUpdateProfileResponse {
        return retrofitService.userUpdateProfile(username, display_name, password, dob)
    }

    override suspend fun getReportByRange(start: String, end: String): Response<List<ReportItem>> {
        return retrofitService.getReportByRange(start, end)
    }
}