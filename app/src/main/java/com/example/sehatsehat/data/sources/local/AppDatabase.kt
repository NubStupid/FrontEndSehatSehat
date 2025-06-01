package com.example.sehatsehat.data.sources.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [ChatLogEntity::class], version = 1)
abstract class AppDatabase: RoomDatabase() {
    abstract fun chatLogDao():ChatLogDao

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