package com.example.sehatsehat.data.sources.local

import com.example.sehatsehat.data.sources.DatabaseEntity

class RoomDataSource(
    private val db:AppDatabase
):LocalDataSource{



    override suspend fun getUnsynced(): DatabaseEntity {
        TODO("Not yet implemented")
    }

    override suspend fun sync(database: DatabaseEntity) {
        TODO("Not yet implemented")
    }
}