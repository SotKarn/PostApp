package com.example.posts.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.posts.model.Post
import com.example.posts.repo.IRepository
import com.example.posts.repo.MyRepository
import com.example.posts.retrofit.DataStates
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PostFragmentViewModel @Inject constructor(private val repo: MyRepository): ViewModel()
{
    private val _datastate: MutableLiveData<DataStates<List<Post>>> = MutableLiveData()
    val dataState: LiveData<DataStates<List<Post>>>
        get() = _datastate


    fun setStateEvent(postFragmentEvent: PostFragmentEvent)
    {
        viewModelScope.launch {
            when(postFragmentEvent)
            {
                is PostFragmentEvent.GetPostEvents -> {
                    repo.getPosts()
                        .onEach { dataState->
                            _datastate.value = dataState
                        }
                        .launchIn(viewModelScope)
                }
            }
        }
    }
}

sealed class PostFragmentEvent
{
    object GetPostEvents: PostFragmentEvent()
}