package com.example.sehatsehat.model

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize
import kotlinx.serialization.Serializable

@Parcelize
@Entity("users")
data class UserEntity(
    @PrimaryKey(autoGenerate = false) val username: String,
    var display_name: String,
    var password: String,
    var dob: String,
    var role: String,
    var pp_url: String,
    var balance:Int,
    var createdAt: String,
    var updatedAt: String
):Parcelable
