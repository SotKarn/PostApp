package com.example.posts.repo


import com.example.posts.model.Post
import com.example.posts.retrofit.DataStates
import com.example.posts.retrofit.IRetrofit
import com.example.posts.retrofit.NetworkEntity
import com.example.posts.room.PostDao
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Assert.assertEquals
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.times
import org.mockito.Spy
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class RepositoryTest
{

    @Mock
    lateinit var dao: PostDao

    @Mock
    lateinit var roomEntityMapper: FakeRoomMapper

    @Mock
    lateinit var webinterface: IRetrofit

    @Spy
    lateinit var networkEntityMapper: FakeNetworkMapper


    private lateinit var repo: FakeRepository


    @Before
    fun setUp()
    {
        repo = FakeRepository(webinterface, dao, roomEntityMapper, networkEntityMapper)
    }

    @Test
    fun testingSuccessRepositoryFlow() {
        runBlocking {
            val networkEntities = listOf(NetworkEntity(1, 2, "test1", "test2"))
            val expected = DataStates.Success(listOf(Post(1, 2, "test1", "test2")))

            Mockito.`when`(webinterface.getPosts()).thenReturn(networkEntities)

            val datastate: DataStates<List<Post>> = repo.getPosts().first()
            assertEquals(expected, datastate )
            Mockito.verify(webinterface, times(1)).getPosts()
            Mockito.verify(networkEntityMapper, times(1)).mapFromEntityList(networkEntities)
        }
    }
}