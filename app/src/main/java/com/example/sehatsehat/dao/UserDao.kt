package com.example.sehatsehat.dao

import androidx.room.*
import com.example.sehatsehat.model.ProgramEntity
import com.example.sehatsehat.model.UserEntity
import kotlinx.coroutines.flow.Flow
import retrofit2.http.GET

@Dao
interface UserDao {
    @Query("SELECT * FROM users")
    suspend fun getAllUsers(): List<UserEntity>

    @Delete
    suspend fun deleteUser(userEntity: UserEntity)

    @Query("DELETE FROM users")
    suspend fun deleteAll()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUser(userEntity: UserEntity)


//    @Query("SELECT * FROM users WHERE id = :userId")
//    suspend fun getUserById(userId: String): UserEntity?

//    @Query("SELECT * FROM users WHERE email = :email")
//    suspend fun getUserByEmail(email: String): UserEntity?

//    @Insert(onConflict = OnConflictStrategy.REPLACE)
//    suspend fun insertUser(user: UserEntity)

//    @Update
//    suspend fun updateUser(user: UserEntity)

//    @Query("UPDATE users SET walletBalance = :balance WHERE id = :userId")
//    suspend fun updateWalletBalance(userId: String, balance: Double)
//
//    @Query("SELECT walletBalance FROM users WHERE id = :userId")
//    fun getWalletBalanceFlow(userId: String): Flow<Double>
}