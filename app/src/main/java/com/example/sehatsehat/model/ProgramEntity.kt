package com.example.sehatsehat.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.sehatsehat.network.TimestampTypeAdapter
//import com.google.gson.annotations.JsonAdapter

@Entity(tableName = "programs")
data class ProgramEntity(
    @PrimaryKey val id: String,
    var program_name: String,
    var pricing: Float,
//    @JsonAdapter(TimestampTypeAdapter::class)
    var createdAt: String = "",
//    @JsonAdapter(TimestampTypeAdapter::class)
    var updatedAt: String = "",
//    @JsonAdapter(TimestampTypeAdapter::class)
    var deletedAt: String? = ""
)
