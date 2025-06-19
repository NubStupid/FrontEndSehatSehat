package com.example.sehatsehat.data.sources.local

import com.example.sehatsehat.model.ChatLogEntity
import io.mockk.coEvery
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*

import org.junit.Before
import org.junit.Test

class RoomDataSourceTest {
    lateinit var localDataSource:LocalDataSource
    lateinit var dummyChatStorage: ArrayList<ChatLogEntity>
    @Before
    fun setUp() {
        localDataSource = mockk<LocalDataSource>()
        dummyChatStorage = arrayListOf(
            ChatLogEntity("CL00001","GC00001","john_doe","Hello, how can I help you today"),
            ChatLogEntity("CL00002","GC00001","john_doe","Hello")
        )
    }

    @Test
    fun insertChatLog()= runTest{
        val contentSlot = slot<String>()
        val usernameSlot = slot<String>()
        val chat_groupSlot = slot<String>()
        coEvery { localDataSource.insertChatLog(capture(contentSlot),capture(usernameSlot),capture(chat_groupSlot)) } answers {
            ChatLogEntity("CL00003",chat_groupSlot.captured,usernameSlot.captured,contentSlot.captured)
        }

        val result = localDataSource.insertChatLog("Hello, how can I help you today","john_doe","GC00001")
        assertEquals("Hello, how can I help you today",result.content)

    }
}