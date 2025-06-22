package com.example.sehatsehat.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.example.sehatsehat.model.MealEntity

@Dao
interface MealDao {
    @Insert
    suspend fun insert(meal: MealEntity)
    @Query("SELECT * FROM meals WHERE program_id = :programId")
    suspend fun getMealsByProgramId(programId: String): List<MealEntity>
    @Query("SELECT * from meals where id=:id")
    suspend fun getMealsById(id:String):MealEntity?
    @Query("DELETE FROM meals")
    suspend fun deleteAll()
}