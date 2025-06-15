package com.example.sehatsehat.data.sources.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.sehatsehat.dao.ChatLogDao
import com.example.sehatsehat.dao.ProgramDao
import com.example.sehatsehat.dao.UserDao
import com.example.sehatsehat.model.ChatLogEntity
import com.example.sehatsehat.model.ProgramEntity
import com.example.sehatsehat.model.UserEntity

@Database(entities = [ChatLogEntity::class, UserEntity::class, ProgramEntity::class], version = 3)
abstract class AppDatabase: RoomDatabase() {
    abstract fun chatLogDao(): ChatLogDao
    abstract fun programDao(): ProgramDao
    abstract fun userDao(): UserDao

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