package com.example.sehatsehat.data.repositories

import android.util.Log
import com.example.sehatsehat.model.ChatLogEntity
import com.example.sehatsehat.data.sources.local.LocalDataSource
import com.example.sehatsehat.data.sources.remote.ChatBotDTO
import com.example.sehatsehat.data.sources.remote.ChatLogDTO
import com.example.sehatsehat.data.sources.remote.DashboardDRO
import com.example.sehatsehat.data.sources.remote.LoginDRO
import com.example.sehatsehat.data.sources.remote.LoginDTO
import com.example.sehatsehat.data.sources.remote.ProgramDRO
import com.example.sehatsehat.data.sources.remote.ProgramListDRO
import com.example.sehatsehat.data.sources.remote.RegisterDRO
import com.example.sehatsehat.data.sources.remote.RemoteDataSource
import com.example.sehatsehat.data.sources.remote.UserDRO
import com.example.sehatsehat.data.sources.remote.UserDTO
import com.example.sehatsehat.data.sources.remote.UserListDRO
import com.example.sehatsehat.data.sources.remote.userTopUpResponse
import com.example.sehatsehat.data.sources.remote.userUpdateProfileResponse
import com.example.sehatsehat.model.Article
import com.example.sehatsehat.model.FitnessProgram
import com.example.sehatsehat.model.ProgramEntity
import com.example.sehatsehat.model.UserEntity
import com.example.sehatsehat.ui.customer.ProfileActivity
import java.util.Date
import java.util.UUID

class SehatDefaultRepository (
    private val localDataSource:LocalDataSource,
    private val remoteDataSource:RemoteDataSource
):SehatRepository{

    override suspend fun chatGroupSync(group_id: String) {
        Log.d("Test_Sync_group_id",group_id)
        val clientChatGroup = localDataSource.getFromGroupChatLog(group_id)
        Log.d("Test_Sync_clientCG",clientChatGroup.size.toString())
        val serverChatGroup = remoteDataSource.syncChatGroup(group_id,clientChatGroup)
        Log.d("Test_Sync_serverCG",serverChatGroup.chats.size.toString())
        localDataSource.syncChatGroup(group_id,serverChatGroup.chats)
    }

    override suspend fun getProgramByUser(username: String): List<ProgramEntity> {
        return remoteDataSource.getProgramByUser(username)
    }

    override suspend fun programSync() {
        val serverProgram = remoteDataSource.syncProgram()
        localDataSource.syncProgram(serverProgram.programs)
    }

    override suspend fun userSync() {
        val serverUser = remoteDataSource.syncUser()
        localDataSource.syncUser(serverUser.users)
    }

    override suspend fun programProgressSync() {
        val serverProgress = remoteDataSource.syncProgramProgress()
        localDataSource.syncProgramProgress(serverProgress.progress)
    }

    override suspend fun userProgramSync() {
        val userPrograms = remoteDataSource.syncUserProgram()
        localDataSource.syncUserProgram(userPrograms.userPrograms)
    }

    override suspend fun getArticles(): List<Article> {
        return remoteDataSource.getArticles()
    }

    override suspend fun getProgramDashboard(): DashboardDRO {
        val dro = localDataSource.getDashboard()
        return dro
    }

    override suspend fun getAllChatLog(): List<ChatLogEntity> {
        return localDataSource.getAllChatLog()
    }

    override suspend fun deleteChatLog(id: String) {
        val chatlog = localDataSource.deleteChatLog(id)
    }

    override suspend fun getFromChatbotChatLog(username: String): List<ChatLogEntity> {
        return localDataSource.getFromChatbotChatLog(username)
    }

    override suspend fun getFromCSChatLog(username: String): List<ChatLogEntity> {
        return localDataSource.getFromCSChatLog(username)
    }

    override suspend fun getFromGroupChatLog(group_id: String): List<ChatLogEntity> {
        TODO("Not yet implemented")
    }

    override suspend fun updateChatLog(id: String, content: String) {
        TODO("Not yet implemented")
    }

    override suspend fun insertChatLog(
        content: String,
        username: String,
        chat_group: String
    ){
        localDataSource.insertChatLog(content, username, chat_group)
        remoteDataSource.insertChatLog(ChatLogDTO(content, username, chat_group))
    }


    override suspend fun chatToChatbot(content: String, username: String, chat_group: String) {
        insertChatLog(content,username, chat_group)
        val message = remoteDataSource.chatToChatbot(ChatBotDTO(content))
        insertChatLog(message.response,"Chatbot",chat_group)
    }

    override suspend fun chatToCustomerService(
        content: String,
        username: String,
        chat_group: String
    ) {
        insertChatLog(content,username, chat_group)
        val message = remoteDataSource.chatToCustomerService(ChatBotDTO(content))
        insertChatLog(message.response,"Chatbot",chat_group)
    }

    // ==== (2) Auth / User ====
    override suspend fun registerUser(user: UserDTO): RegisterDRO {
        return remoteDataSource.registerUser(user)
    }

    override suspend fun loginUser(credentials: LoginDTO): LoginDRO {
        return remoteDataSource.loginUser(credentials)
    }

    //    user crud
    override suspend fun getAllUsers(): List<UserEntity> {
        // Cek local pertama
        val localList = localDataSource.getAllUsers()
        return if (localList.isNotEmpty()) {
            localList
        } else {
            // Jika kosong, fetch dari server
            val dro: UserListDRO = remoteDataSource.getAllUsers()
            val dtoList: List<UserEntity> = dro.users
            // Simpan semua ke local
//            dtoList.forEach { localDataSource.insertUser(it) }
            dtoList
        }
    }

    override suspend fun getUserProfile(username: String): UserDRO {
        return remoteDataSource.getuserProfile(username)
    }

//    override suspend fun getUserByUsername(username: String): UserEntity? {
//        val localProg = localDataSource.getUserByUsername(username)
//        return if (localProg != null) {
//            localProg
//        } else {
//            val dro: UserDRO = remoteDataSource.getUserByUsername(username)
//            val userDTO: UserEntity = dro.user ?: return null
//            localDataSource.insertProgram(userDTO)
//            userDTO
//        }
//    }

//    override suspend fun insertUser(user: UserEntity) {
//        // Kirim ke server
//        val dro: UserDRO = remoteDataSource.insertUser(user)
//        val returned: ProgramEntity = dro.user ?: return
//        // Simpan versi server (agar ID atau field lain sesuai backend)
//        localDataSource.insertUser(returned)
//    }

    override suspend fun deleteUser(user: UserEntity) {
        // Hapus di server
        remoteDataSource.deleteUser(user.username)
        // Hapus di local
        localDataSource.deleteUser(user)
    }

    override suspend fun userTopUp(username: String, amount: Int): userTopUpResponse {
        return remoteDataSource.userTopUp(username, amount)
    }

    override suspend fun userUpdateProfile(
        username: String,
        display_name: String,
        password: String,
        dob: String
    ): userUpdateProfileResponse {
        return remoteDataSource.userUpdateProfile(username, display_name, password, dob)
    }

    // ==== (3) Program CRUD ====
    override suspend fun getAllPrograms(): List<ProgramEntity> {
        // Cek local pertama
        val localList = localDataSource.getAllPrograms()
        return if (localList.isNotEmpty()) {
            localList
        } else {
            // Jika kosong, fetch dari server
            val dro: ProgramListDRO = remoteDataSource.getAllPrograms()
            val dtoList: List<ProgramEntity> = dro.programs
            // Simpan semua ke local
            dtoList.forEach { localDataSource.insertProgram(it) }
            dtoList
        }
    }

    override suspend fun getProgramById(id: String): ProgramEntity? {
        val localProg = localDataSource.getProgramById(id)
        return if (localProg != null) {
            localProg
        } else {
            val dro: ProgramDRO = remoteDataSource.getProgramById(id)
            val progDTO: ProgramEntity = dro.program ?: return null
            localDataSource.insertProgram(progDTO)
            progDTO
        }
    }

    override suspend fun insertProgram(program: ProgramEntity) {
        // Kirim ke server
        val dro: ProgramDRO = remoteDataSource.insertProgram(program)
        val returned: ProgramEntity = dro.program ?: return
        // Simpan versi server (agar ID atau field lain sesuai backend)
        localDataSource.insertProgram(returned)
    }

    override suspend fun updateProgram(program: ProgramEntity) {
        // Kirim update ke server
        val dro: ProgramDRO = remoteDataSource.updateProgram(program.id, program)
        val returned: ProgramEntity = dro.program ?: return
        // Update di local
        localDataSource.updateProgram(returned)
    }

    override suspend fun deleteProgram(program: ProgramEntity) {
        // Hapus di server
        remoteDataSource.deleteProgram(program.id)
        // Hapus di local
        localDataSource.deleteProgram(program)
    }

    override suspend fun getAllUserProgramForUI(username: String): List<FitnessProgram> {
        val user_programs = localDataSource.getProgramByUser(username)
        val ui_list = arrayListOf<FitnessProgram>()
        var id = 1
//        Log.d("luar","luar")
        for(up in user_programs){
            val upEntity = localDataSource.getUserProgramByProgramId(up.id)
//            Log.d("upe",upEntity.toString())
            if(upEntity != null){
//                Log.d("upp","upp")
                val programProgress = localDataSource.getProgramProgressById(upEntity.program_progress_id)
                if(programProgress != null){
//                    Log.d("ui","ui")
                    val progress = (programProgress.progress_index / programProgress.progress_list.length).toFloat()
                    val dateRange = "${Date(upEntity.createdAt)} - ${Date(upEntity.expires_in)}"
                    val programs_UI = FitnessProgram(id,up.program_name,dateRange,"Sehat Sehat's Program named ${up.program_name}","",progress,listOf(0xFF6B46C1, 0xFF9333EA),true,up.pricing.toInt(), detailedDescription = "Program ${up.program_name} has a detailed program about ${programProgress.progress_list_type} to help achieve your goals")
                    ui_list.add(programs_UI)
                    id++
                }
            }
        }
        return ui_list
    }

    override suspend fun getAllProgramForPurchase(username: String): List<FitnessProgram> {
        val user_programs = localDataSource.getProgramByUser(username)
        val programs = localDataSource.getAllPrograms()

        val ui_list = arrayListOf<FitnessProgram>()
        var id = 1
        for(up in programs){
            if(user_programs.contains(up)){
                continue
            }
            val programs_UI = FitnessProgram(id,up.program_name,"Around 30 days","Sehat Sehat's Program named ${up.program_name}","",0f,listOf(0xFF059669, 0xFF10B981),false,up.pricing.toInt(), detailedDescription = "Program ${up.program_name} has a detailed program to help achieve your goals")
            id++
            ui_list.add(programs_UI)
        }
        return ui_list
    }
}