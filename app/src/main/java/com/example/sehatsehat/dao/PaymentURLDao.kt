package com.example.sehatsehat.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import com.example.sehatsehat.model.PaymentURL

@Dao
interface PaymentURLDao {
    @Insert
    suspend fun insert(paymentURL: PaymentURL)
    @Query("DELETE FROM payment_url")
    suspend fun deleteAll()
    @Query("SELECT * FROM payment_url LIMIT 1")
    suspend fun getURL(): PaymentURL?

}