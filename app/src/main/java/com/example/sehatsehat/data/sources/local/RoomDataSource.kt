package com.example.sehatsehat.data.sources.local

import android.util.Log
import com.example.sehatsehat.data.sources.remote.DashboardDRO
import com.example.sehatsehat.model.ChatLogEntity
import com.example.sehatsehat.model.ProgramEntity
import com.example.sehatsehat.model.ProgramProgressEntity
import com.example.sehatsehat.model.UserEntity
import java.util.Date

class RoomDataSource(
    private val db:AppDatabase
):LocalDataSource{


    override suspend fun syncChatGroup(group_id: String, logs: List<ChatLogEntity>) {
        db.chatLogDao().deleteGroupChat(group_id)
        for (log in logs){
            db.chatLogDao().insert(log)
        }
    }

    override suspend fun syncProgram(programs: List<ProgramEntity>) {
        db.programDao().deleteAll()
        for (program in programs){
            db.programDao().insert(program)
        }
    }

    override suspend fun syncProgramProgress(progress: List<ProgramProgressEntity>) {
        db.programProgressDao().deleteAll()
        for(p in progress ){
            db.programProgressDao().insert(p)
        }
    }

    override suspend fun syncUser(users: List<UserEntity>) {
        db.userDao().deleteAll()
        for (user in users){
            db.userDao().insertUser(user)
        }
    }

    override suspend fun getDashboard(): DashboardDRO {
        val programs = db.programDao().getAllPrograms()
        val progress = db.programProgressDao().getAllProgramProgress()

        val availablePrograms = arrayListOf<ProgramEntity>()
        val completedPrograms = arrayListOf<ProgramEntity>()
        val inProgressPrograms = arrayListOf<ProgramEntity>()

        for (program in programs){
            var inProgress = false
            for (progressItem in progress){
                if (progressItem.program_id == program.id){
                    inProgress = true
                    if(progressItem.progress_index == progressItem.progress_list.split(",").size){
                        completedPrograms.add(program)
                    }else{
                        inProgressPrograms.add(program)
                    }
                    break
                }
                break
            }
            if(!inProgress){
                availablePrograms.add(program)
            }
        }

        return DashboardDRO(
            availablePrograms,
            completedPrograms,
            inProgressPrograms)
    }


    override suspend fun getAllChatLog(): List<ChatLogEntity> {
        return db.chatLogDao().getAll()
    }

    override suspend fun deleteChatLog(id: String): ChatLogEntity {
        val chatlog = db.chatLogDao().getChatFromId(id)
        if(chatlog != null){
            chatlog.deletedAt = Date().time
            db.chatLogDao().update(chatlog)
            return chatlog
        }else{
            throw NoSuchElementException("Chatlog with id: $id not found")
        }
    }

    override suspend fun getFromChatbotChatLog(username: String): List<ChatLogEntity> {
        Log.d("Room_check",username)
        return db.chatLogDao().getChatFromBot(username)
    }

    override suspend fun getFromGroupChatLog(group_id: String): List<ChatLogEntity> {
        return db.chatLogDao().getChatFromGroup(group_id)
    }

    override suspend fun updateChatLog(id: String, content: String): ChatLogEntity {
        val chatlog = db.chatLogDao().getChatFromId(id)
        if(chatlog != null){
            db.chatLogDao().update(chatlog.copy(content = content))
            return chatlog
        }else{
            throw NoSuchElementException("Chatlog with id: $id not found")
        }
    }

    override suspend fun insertChatLog(
        content: String,
        username: String,
        chat_group: String
    ): ChatLogEntity {
        var count = db.chatLogDao().getSize()
        val id = "CL"+(count+1).toString().padStart(5,'0')
        val chatlog = ChatLogEntity(id,chat_group,username,content)
        db.chatLogDao().insert(chatlog)
        return chatlog
    }

    // program
    override suspend fun getAllPrograms(): List<ProgramEntity> {
        return db.programDao().getAllPrograms()
    }

    override suspend fun getProgramById(id: String): ProgramEntity? {
        return db.programDao().findById(id)
    }

    override suspend fun insertProgram(program: ProgramEntity) {
        db.programDao().insert(program)
    }

    override suspend fun updateProgram(program: ProgramEntity) {
        db.programDao().update(program)
    }

    override suspend fun deleteProgram(program: ProgramEntity) {
        db.programDao().delete(program)
    }

    // user
    override suspend fun getAllUsers(): List<UserEntity> {
        return db.userDao().getAllUsers()
    }

    override suspend fun deleteUser(user: UserEntity) {
        return db.userDao().deleteUser(user)
    }
}