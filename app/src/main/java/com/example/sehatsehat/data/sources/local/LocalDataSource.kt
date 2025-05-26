package com.example.sehatsehat.data.sources.local

import com.example.sehatsehat.data.sources.DatabaseEntity

interface LocalDataSource {
//    suspend fun getAll():List<Todo>
//    suspend fun getById(id:String): Todo?
//    suspend fun insert(content:String): Todo
//    suspend fun update(id:String, content:String, completed:Boolean): Todo
//    suspend fun delete(id:String): Todo
    suspend fun getUnsynced():DatabaseEntity
    suspend fun sync(database:DatabaseEntity)
}