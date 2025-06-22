package com.example.sehatsehat.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity("payment_url")
data class PaymentURL(
    @PrimaryKey(true) val id:Int =1,
    val url:String
)