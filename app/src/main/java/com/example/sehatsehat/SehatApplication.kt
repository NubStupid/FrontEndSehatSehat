package com.example.sehatsehat

import android.app.Application
import com.example.sehatsehat.data.repositories.SehatDefaultRepository
import com.example.sehatsehat.data.repositories.SehatRepository
import com.example.sehatsehat.data.sources.local.AppDatabase
import com.example.sehatsehat.data.sources.local.RoomDataSource
import com.example.sehatsehat.data.sources.remote.RetrofitDataSource
import com.example.sehatsehat.data.sources.remote.WebService
import com.example.sehatsehat.network.TimestampTypeAdapter
//import com.google.gson.GsonBuilder
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class SehatApplication : Application() {
    //    companion object{
//        lateinit var db:AppDatabase
//    }
//
//    override fun onCreate() {
//        super.onCreate()
//
//        db = AppDatabase.getInstance(baseContext)
//    }
    lateinit var sehatRepository: SehatRepository
    companion object{
        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        val retrofit = Retrofit.Builder().addConverterFactory(
            MoshiConverterFactory.create(moshi)
        ).baseUrl("http://192.168.1.8:3000/").build()
        val retrofitService = retrofit.create(WebService::class.java)
        lateinit var db:AppDatabase
    }
    //10.0.2.2:3000
    //10.10.3.28:3000
//    10.10.4.202

    override fun onCreate() {
        super.onCreate()
        baseContext.deleteDatabase("mdp_sehat")

        db =AppDatabase.getInstance(baseContext)
        sehatRepository = SehatDefaultRepository(
            localDataSource = RoomDataSource(db),
            remoteDataSource = RetrofitDataSource(retrofitService)
        )
    }
}