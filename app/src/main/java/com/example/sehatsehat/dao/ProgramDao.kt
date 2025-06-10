package com.example.sehatsehat.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import androidx.room.Delete
import com.example.sehatsehat.model.ProgramEntity

@Dao
interface ProgramDao {
    @Query("SELECT * FROM programs ORDER BY createdAt DESC")
    suspend fun getAllPrograms(): List<ProgramEntity>

    @Query("SELECT * FROM programs WHERE id = :programId LIMIT 1")
    suspend fun findById(programId: String): ProgramEntity?

    @Insert
    suspend fun insert(program: ProgramEntity)

    @Update
    suspend fun update(program: ProgramEntity)

    @Delete
    suspend fun delete(program: ProgramEntity)
}
