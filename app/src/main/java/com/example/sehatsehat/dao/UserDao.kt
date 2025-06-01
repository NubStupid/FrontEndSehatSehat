package com.example.sehatsehat.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.sehatsehat.model.UserEntity

@Dao
interface UserDao {
    @Query("select * from users")
    suspend fun getAll():List<UserEntity>
    @Query("select * from users where username=:username")
    suspend fun findByUsername(username:String):UserEntity?
    @Insert
    suspend fun insert(user: UserEntity)
//    @Update
//    suspend fun update(user: User)
//    @Delete
//    suspend fun delete(user: User)
}