package com.example.sehatsehat.data.repositories

import com.example.sehatsehat.model.ChatLogEntity
import com.example.sehatsehat.data.sources.local.LocalDataSource
import com.example.sehatsehat.data.sources.remote.DashboardDRO
import com.example.sehatsehat.data.sources.remote.RemoteDataSource
import com.example.sehatsehat.model.Article
import com.example.sehatsehat.model.ProgramEntity
import io.mockk.coEvery
import io.mockk.coVerify
import io.mockk.confirmVerified
import io.mockk.mockk
import io.mockk.slot
import kotlinx.coroutines.test.runTest
import org.junit.Assert.*

import org.junit.Before
import org.junit.Test
import java.util.Date

class SehatDefaultRepositoryTest {
    lateinit var SehatDefaultRepository:SehatRepository
    lateinit var localDataSource:LocalDataSource
    lateinit var remoteDataSource: RemoteDataSource
    lateinit var dummyChatStorage: ArrayList<ChatLogEntity>
    lateinit var dummyArticles: List<Article>
    lateinit var dummyPrograms: List<ProgramEntity>
    lateinit var dummyDashboardDRO: DashboardDRO
    @Before
    fun setUp() {
        localDataSource = mockk<LocalDataSource>()
        remoteDataSource = mockk<RemoteDataSource>()
        SehatDefaultRepository = SehatDefaultRepository(localDataSource,remoteDataSource)
        dummyChatStorage = arrayListOf(
            ChatLogEntity("CL00001","GC00001","john_doe","Hello, how can I help you today"),
            ChatLogEntity("CL00002","GC00001","john_doe","Hello") ,
            ChatLogEntity("CL00003", "GC00002", "john_doe", "Meeting at 3 PM.")
        )
        dummyArticles = listOf(
            Article(1, "Title 1", "Content 1", "url1"),
            Article(2, "Title 2", "Content 2", "url2")
        )
        dummyPrograms = listOf(
            ProgramEntity(
                id = "prog_yoga_001",
                program_name = "Morning Yoga Flow",
                pricing = 0.0f, // Free program
                createdAt = "", // "2024-07-13TXX:XX:XX.XXX_Z"
                updatedAt = ""
            ),
            ProgramEntity(
                id = "prog_hiit_002",
                program_name = "30-Min HIIT Blast",
                pricing = 9.99f,
                createdAt = "",
                updatedAt = ""
            ),
            ProgramEntity(
                id = "prog_strength_003",
                program_name = "Strength Builder Weekly",
                pricing = 19.99f,
                createdAt = "",
                updatedAt = ""
            ),
            ProgramEntity(
                id = "prog_meditation_004",
                program_name = "Guided Meditation Series",
                pricing = 4.99f,
                createdAt = "",
                updatedAt = "",
                deletedAt = "" // Example of a "deleted" program
            ),
            ProgramEntity(
                id = "prog_core_005",
                program_name = "Core Crusher Challenge",
                pricing = 7.50f,
                createdAt = "",
                updatedAt = ""
            )
        )
        dummyDashboardDRO = DashboardDRO( dummyPrograms.take(2), listOf(dummyPrograms.get(2)) , dummyPrograms.takeLast(2))
    }

    @Test
    fun getAllChatLog()= runTest{
        coEvery { localDataSource.getAllChatLog() } returns dummyChatStorage.toList()
        val result = SehatDefaultRepository.getAllChatLog()
        assertEquals(dummyChatStorage.size,result.size)
        coVerify { localDataSource.getAllChatLog() }
    }

    @Test
    fun getArticles()= runTest {
        coEvery { remoteDataSource.getArticles() } returns dummyArticles
        val result = SehatDefaultRepository.getArticles()
        assertEquals(dummyArticles.size, result.size)
        coVerify { remoteDataSource.getArticles() }
    }

    @Test
    fun getProgramDashboard()= runTest {
        coEvery { localDataSource.getDashboard() } returns dummyDashboardDRO
        val result = SehatDefaultRepository.getProgramDashboard()
        assertEquals(result.available.size,2)
        assertEquals(result.ongoing.size,1)
        assertEquals(result.completed.size,2)
    }




}