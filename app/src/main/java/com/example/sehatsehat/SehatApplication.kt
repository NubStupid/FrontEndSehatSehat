package com.example.sehatsehat

import android.app.Application
import com.example.sehatsehat.data.sources.local.AppDatabase
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

//        db = AppDatabase.getInstance(baseContext)
//    }

    companion object{
        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        val retrofit = Retrofit.Builder().addConverterFactory(
            MoshiConverterFactory.create(moshi)
        ).baseUrl("http://10.0.2.2:3000/").build()
//        val retrofitService = retrofit.create(WebService::class.java)
        lateinit var db:AppDatabase
    }

    override fun onCreate() {
        super.onCreate()
//        baseContext.deleteDatabase("mdp_sehat")
        db =AppDatabase.getInstance(baseContext)
    }
}