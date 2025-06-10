package com.example.sehatsehat.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "bookings")
data class Booking(
    @PrimaryKey val id: String = "",
    val userId: String = "",
    val sessionId: String = "",
    val bookingDate: Long = 0L,
    val status: BookingStatus = BookingStatus.CONFIRMED,
    val creditsUsed: Int = 0
)

enum class BookingStatus {
    CONFIRMED, CANCELLED, COMPLETED, NO_SHOW
}