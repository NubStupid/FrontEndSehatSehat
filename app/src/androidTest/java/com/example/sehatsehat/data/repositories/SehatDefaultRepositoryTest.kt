package com.example.sehatsehat.data.repositories

import com.example.sehatsehat.model.ChatLogEntity
import com.example.sehatsehat.data.sources.local.LocalDataSource
import com.example.sehatsehat.data.sources.remote.RemoteDataSource
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.mockk
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*

import org.junit.Before
import org.junit.Test

class SehatDefaultRepositoryTest {
    lateinit var SehatDefaultRepository:SehatRepository
    lateinit var localDataSource:LocalDataSource
    lateinit var remoteDataSource: RemoteDataSource
    lateinit var dummyChatStorage: MutableList<ChatLogEntity>

    @Before
    fun setUp() {
        localDataSource = mockk<LocalDataSource>()
        remoteDataSource = mockk<RemoteDataSource>()
        SehatDefaultRepository = SehatDefaultRepository(localDataSource,remoteDataSource)
        dummyChatStorage = mutableListOf(
            ChatLogEntity("CL00001","GC00001","john_doe","Hello, how can I help you today"),
            ChatLogEntity("CL00002","GC00001","john_doe","Hello")
        )
    }

    @Test
    fun getAllChatLog()= runTest{
        coEvery { localDataSource.getAllChatLog() } returns dummyChatStorage.toList()
        val result = SehatDefaultRepository.getAllChatLog()
        assertEquals(dummyChatStorage.size,result.size)
        coVerify { localDataSource.getAllChatLog() }
    }



}