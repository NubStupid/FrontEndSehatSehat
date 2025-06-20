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

}