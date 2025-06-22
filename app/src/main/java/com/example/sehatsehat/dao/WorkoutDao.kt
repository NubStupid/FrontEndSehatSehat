package com.example.sehatsehat.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.sehatsehat.model.WorkoutEntity

@Dao
interface WorkoutDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(workout: WorkoutEntity)
    @Query("SELECT * FROM workouts WHERE program_id = :programId")
    suspend fun getWorkoutsByProgramId(programId: String): List<WorkoutEntity>
    @Query("SELECT * FROM workouts WHERE id = :workoutId")
    suspend fun getWorkoutById(workoutId: String): WorkoutEntity?
    @Query("DELETE FROM workouts")
    suspend fun deleteAll()

}