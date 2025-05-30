package com.example.sehatsehat.data

import android.app.Application
import com.example.sehatsehat.WebService
import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

class App:Application() {
    companion object{
        lateinit var instance: App
        var db:AppDatabase? = null
        val moshi = Moshi.Builder().add(KotlinJsonAdapterFactory()).build()
        val retrofit = Retrofit.Builder().addConverterFactory(
            MoshiConverterFactory.create(moshi)
        ).baseUrl("http://10.13.3.139:3000/").build()
        val retrofitService = retrofit.create(WebService::class.java)
    }

    override fun onCreate() {
        super.onCreate()
        instance = this
////        baseContext.deleteDatabase("<nama database>")
        db = AppDatabase.getInstance(this)
    }
}