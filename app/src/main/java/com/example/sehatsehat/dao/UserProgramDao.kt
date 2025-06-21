package com.example.sehatsehat.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.sehatsehat.model.UserPogramEntity

@Dao
interface UserProgramDao {
    @Insert
    suspend fun insertUserProgram(userProgram: UserPogramEntity)
    @Update
    suspend fun updateUserProgram(userProgram: UserPogramEntity)
    @Delete
    suspend fun deleteUserProgram(userProgram: UserPogramEntity)
    @Query("SELECT * from user_programs WHERE id = :id")
    suspend fun getUserProgramById(id: String): UserPogramEntity?

    @Query("SELECT * from user_programs WHERE program_id =:id")
    suspend fun getUserProgramByProgramId(id:String):UserPogramEntity?

    @Query("DELETE FROM user_programs")
    suspend fun deleteAll()
    @Query("SELECT * from user_programs WHERE username =:username")
    suspend fun getUserProgramByUsername(username:String):List<UserPogramEntity>

    @Query("SELECT * from user_programs WHERE username !=:username")
    suspend fun getUserProgramPurchasableByUsername(username:String):List<UserPogramEntity>


}