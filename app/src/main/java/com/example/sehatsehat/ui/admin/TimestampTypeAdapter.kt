package com.example.sehatsehat.network

import com.google.gson.TypeAdapter
import com.google.gson.stream.JsonReader
import com.google.gson.stream.JsonToken
import com.google.gson.stream.JsonWriter
import java.text.SimpleDateFormat
import java.util.*

class TimestampTypeAdapter : TypeAdapter<Long>() {
    private val fmt = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.US).apply {
        timeZone = TimeZone.getTimeZone("UTC")
    }

    override fun read(reader: JsonReader): Long? {
        return when (reader.peek()) {
            JsonToken.NUMBER -> reader.nextLong()
            JsonToken.STRING -> {
                val str = reader.nextString()
                fmt.parse(str)?.time
            }
            JsonToken.NULL -> {
                reader.nextNull()
                null
            }
            else -> null
        }
    }

    override fun write(writer: JsonWriter, value: Long?) {
        if (value == null) {
            writer.nullValue()
        } else {
            // Kirim sebagai number (long)
            writer.value(value)
        }
    }
}
