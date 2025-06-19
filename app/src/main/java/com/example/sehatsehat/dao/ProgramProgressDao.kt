package com.example.sehatsehat.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.example.sehatsehat.model.ProgramEntity
import com.example.sehatsehat.model.ProgramProgressEntity

@Dao
interface ProgramProgressDao {
    @Insert
    suspend fun insert(programProgress: ProgramProgressEntity)
    @Update
    suspend fun update(programProgress: ProgramProgressEntity)
    @Delete
    suspend fun delete(programProgress: ProgramProgressEntity)

    @Query("SELECT * FROM programs_progress WHERE id = :programProgressId LIMIT 1")
    suspend fun findById(programProgressId: String): ProgramProgressEntity?

    @Query("SELECT * FROM programs_progress ORDER BY createdAt DESC")
    suspend fun getAllProgramProgress(): List<ProgramProgressEntity>

    @Query("DELETE FROM programs_progress")
    suspend fun deleteAll()

    @Query("DELETE FROM programs_progress WHERE id = :programProgressId")
    suspend fun deleteById(programProgressId: String)

    @Query("SELECT * FROM programs_progress WHERE program_id = :programId")
    suspend fun findByProgramId(programId: String): List<ProgramProgressEntity>

    @Query("SELECT * FROM programs_progress WHERE program_id = :programId ORDER BY progress_index DESC LIMIT 1")
    suspend fun findLatestByProgramId(programId: String): ProgramProgressEntity?

    @Query("SELECT * FROM programs_progress WHERE program_id = :programId ORDER BY progress_index DESC LIMIT 1")
    suspend fun getLatestProgramProgress(programId: String): ProgramProgressEntity?

    @Query("SELECT * FROM programs_progress WHERE program_id = :programId ORDER BY progress_index ASC")
    suspend fun getProgramProgressList(programId: String): List<ProgramProgressEntity>

    @Query("SELECT COUNT(*) FROM programs_progress GROUP BY program_id")
    suspend fun getProgramProgressCount(): Int
}