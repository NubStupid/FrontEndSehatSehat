package com.example.sehatsehat.data.sources.local

import org.junit.Assert.*

import org.junit.Before
import org.junit.Test

class ChatLogEntityTest {

    lateinit var test_Chatlog:ChatLogEntity
    @Before
    fun setUp() {
        test_Chatlog = ChatLogEntity("CL00001","GC00001","john_doe","Hello, how can I help you today")
    }

    @Test
    fun testChatLogDefaultTimestamps(){
        val createdAt = test_Chatlog.createdAt
        val updatedAt = test_Chatlog.updatedAt
        val deletedAt = test_Chatlog.deletedAt
        assertNotNull(createdAt)
        assertNotNull(updatedAt)
        assertNull(deletedAt)
    }

    @Test(expected = IllegalArgumentException::class)
    fun testUsernameEmpty(){
        ChatLogEntity("CL00001","GC00001","","Hello, how can I help you today")
    }

    @Test(expected = IllegalArgumentException::class)
    fun testContentEmpty(){
        ChatLogEntity("CL00001","GC00001","john_doe","")
    }

    @Test(expected = IllegalArgumentException::class)
    fun testChatGroupIdEmpty(){
        ChatLogEntity("CL00001","","john_doe","Hello, how can I help you today")
    }

}