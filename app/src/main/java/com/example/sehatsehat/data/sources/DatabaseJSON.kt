package com.example.sehatsehat.data.sources

import com.example.sehatsehat.data.sources.remote.ChatLogJson

data class DatabaseJSON(
    val chatLogRecord: List<ChatLogJson>
)
