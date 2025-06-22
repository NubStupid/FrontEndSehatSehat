package com.example.sehatsehat.data.sources.local

import com.example.sehatsehat.data.sources.remote.DashboardDRO
import com.example.sehatsehat.model.ChatLogEntity
import com.example.sehatsehat.model.MealEntity
import com.example.sehatsehat.model.ProgramEntity
import com.example.sehatsehat.model.ProgramProgressEntity
import com.example.sehatsehat.model.UserEntity
import com.example.sehatsehat.model.UserPogramEntity
import com.example.sehatsehat.model.WorkoutEntity

interface LocalDataSource {
//    suspend fun getAll():List<Todo>
//    suspend fun getById(id:String): Todo?
//    suspend fun insert(content:String): Todo
//    suspend fun update(id:String, content:String, completed:Boolean): Todo
//    suspend fun delete(id:String): Todo

    suspend fun getAllChatLog():List<ChatLogEntity>
    suspend fun getFromGroupChatLog(group_id:String): List<ChatLogEntity>
    suspend fun getFromChatbotChatLog(username:String): List<ChatLogEntity>
    suspend fun getFromCSChatLog(username:String): List<ChatLogEntity>
    suspend fun insertChatLog(content:String, username:String,chat_group:String): ChatLogEntity
    suspend fun updateChatLog(id:String, content:String): ChatLogEntity
    suspend fun deleteChatLog(id:String): ChatLogEntity

    suspend fun syncChatGroup(group_id:String,logs:List<ChatLogEntity>)
    suspend fun syncProgram(programs:List<ProgramEntity>)
    suspend fun syncUser(users:List<UserEntity>)
    suspend fun syncProgramProgress(progress:List<ProgramProgressEntity>)
    suspend fun syncUserProgram(userPrograms:List<UserPogramEntity>)
    suspend fun syncWorkout(workouts:List<WorkoutEntity>)
    suspend fun syncMeal(meals:List<MealEntity>)

    suspend fun getDashboard(): DashboardDRO

    // Program
    suspend fun getAllPrograms(): List<ProgramEntity>
    suspend fun getProgramById(id: String): ProgramEntity?
    suspend fun insertProgram(program: ProgramEntity)
    suspend fun updateProgram(program: ProgramEntity)
    suspend fun deleteProgram(program: ProgramEntity)
    suspend fun getProgramByUser(username: String): List<ProgramEntity>
    suspend fun getProgramPurchasable(username:String): List<ProgramEntity>
    suspend fun getUserProgramById(id:String):UserPogramEntity?
    suspend fun getUserProgramByProgramId(id:String):UserPogramEntity?
    suspend fun getProgramProgressById(id:String):ProgramProgressEntity?

    // user
    suspend fun getAllUsers(): List<UserEntity>
    suspend fun deleteUser(user: UserEntity)


    suspend fun getWorkoutById(id:String):WorkoutEntity?
    suspend fun getMealById(id: String):MealEntity?
    suspend fun incrementProgress(progress:ProgramProgressEntity)
}