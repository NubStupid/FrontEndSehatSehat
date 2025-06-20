package com.example.sehatsehat.ui.admin

import com.squareup.moshi.FromJson
import com.squareup.moshi.ToJson
import java.util.Date

class TimestampDateAdapter {
    @FromJson
    fun fromJson(json: Long): Date {
        return Date(json)
    }

    @ToJson
    fun toJson(value: Date): Long {
        return value.time
    }
}
