package com.example.sehatsehat.data.sources.remote

import com.example.sehatsehat.model.ChatLogEntity
import com.example.sehatsehat.model.ProgramEntity
import retrofit2.Response

class RetrofitDataSource(
    private val retrofitService:WebService
):RemoteDataSource{
    override suspend fun chatToChatbot(message: ChatBotDTO): ChatBotMessage {
        return retrofitService.chatToChatBot(message)
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

    // user
    override suspend fun getAllUsers(): UserListDRO {
        return retrofitService.getAllUsers()
    }

    override suspend fun deleteUser(username: String) {
        // Panggil endpoint DELETE → Response<Unit>
        val response: Response<Unit> = retrofitService.deleteUser(username)
        if (!response.isSuccessful) {
            throw RuntimeException("Gagal menghapus user username=$username (status code: ${response.code()})")
        }
    }
}