package com.example.sehatsehat.data.sources.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.sehatsehat.dao.ChatLogDao
import com.example.sehatsehat.dao.MealDao
import com.example.sehatsehat.dao.PaymentURLDao
import com.example.sehatsehat.dao.ProgramDao
import com.example.sehatsehat.dao.ProgramProgressDao
import com.example.sehatsehat.dao.UserDao
import com.example.sehatsehat.dao.UserProgramDao
import com.example.sehatsehat.dao.WorkoutDao
import com.example.sehatsehat.model.ChatLogEntity
import com.example.sehatsehat.model.MealEntity
import com.example.sehatsehat.model.PaymentURL
import com.example.sehatsehat.model.ProgramEntity
import com.example.sehatsehat.model.ProgramProgressEntity
import com.example.sehatsehat.model.UserEntity
import com.example.sehatsehat.model.UserPogramEntity
import com.example.sehatsehat.model.WorkoutEntity

@Database(entities = [ChatLogEntity::class, UserEntity::class, ProgramEntity::class,ProgramProgressEntity::class,UserPogramEntity::class,WorkoutEntity::class,MealEntity::class,PaymentURL::class], version = 4)
abstract class AppDatabase: RoomDatabase() {
    abstract fun chatLogDao(): ChatLogDao
    abstract fun programDao(): ProgramDao
    abstract fun userDao(): UserDao
    abstract fun programProgressDao():ProgramProgressDao
    abstract fun userProgramDao():UserProgramDao
    abstract fun workoutDao():WorkoutDao
    abstract fun mealDao():MealDao
    abstract fun paymentURLDao():PaymentURLDao

    companion object{
        @Volatile
        private var INSTANCE: AppDatabase? = null
        fun getInstance(context: Context):AppDatabase{
            return INSTANCE?: synchronized(this){
                INSTANCE?: Room.databaseBuilder(
                    context,AppDatabase::class.java,"mdp_sehat"
                ).fallbackToDestructiveMigration(false)
                    .build().also { INSTANCE = it }
            }
        }
    }
}