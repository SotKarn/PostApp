package com.example.posts.viewmodels


import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.posts.model.Post
import com.example.posts.repo.FakeNetworkMapper
import com.example.posts.repo.FakeRepository
import com.example.posts.repo.FakeRoomMapper
import com.example.posts.retrofit.DataStates
import com.example.posts.retrofit.IRetrofit
import com.example.posts.retrofit.NetworkEntity
import com.example.posts.room.PostDao
import dagger.hilt.android.testing.HiltAndroidTest
import kotlinx.coroutines.*
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.setMain
import org.junit.Assert.*
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Spy
import org.mockito.junit.MockitoJUnitRunner


@RunWith(MockitoJUnitRunner::class)
class PostFragmentViewModelTest {

    @get:Rule
    var rule: TestRule = InstantTaskExecutorRule()

    @Mock
    lateinit var dao: PostDao

    @Mock
    lateinit var roomEntityMapper: FakeRoomMapper

    @Mock
    lateinit var webinterface: IRetrofit

    @Spy
    lateinit var networkEntityMapper: FakeNetworkMapper

    private lateinit var viewModel: FakePostFragmentViewModel



    @ExperimentalCoroutinesApi
    @Before
    fun setUp() {
        Dispatchers.setMain(Dispatchers.Unconfined)
        viewModel = FakePostFragmentViewModel(FakeRepository(webinterface, dao, roomEntityMapper, networkEntityMapper) )
    }
    @ExperimentalCoroutinesApi
    @After
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `check insert from viewModel`() = runBlocking {

        val dummyNetworkList = listOf(NetworkEntity(1, 11, "test title", "test body"),
                                        NetworkEntity(2, 22, "test title2", "test body2"))

        val expectedList: DataStates.Success<List<Post>> = DataStates.Success(listOf(Post(1, 11, "test title", "test body"),
                                    Post(2, 22, "test title2", "test body2")))

        Mockito.`when`(webinterface.getPosts()).thenReturn(dummyNetworkList)

        viewModel.setStateEvent(PostFragmentEvent.GetPostEvents)

        assertEquals(expectedList, viewModel.dataState.value)
    }
}

class FakePostFragmentViewModel (private val repo: FakeRepository): ViewModel()
{
    private val _datastate: MutableLiveData<DataStates<List<Post>>> = MutableLiveData()
    val dataState: LiveData<DataStates<List<Post>>>
        get() = _datastate


    fun setStateEvent(postFragmentEvent: PostFragmentEvent)
    {
        viewModelScope.launch {
            when(postFragmentEvent)
            {
                PostFragmentEvent.GetPostEvents -> {
                    repo.getPosts()
                        .onEach { dataState->
                            _datastate.value = dataState
                        }
                        .launchIn(viewModelScope)}
                }
            }
    }

}


