package com.example.posts.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.posts.model.Post
import com.example.posts.repo.MyRepository
import com.example.posts.retrofit.DataStates
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class CreatePostFragmentViewModel @Inject constructor(private val repo: MyRepository): ViewModel()
{

    interface ICreatePost
    {
        fun sendResponse(datastate: DataStates<Any>?)
    }

    fun setStateEvent(listener: ICreatePost, createPostEvent: CreatePostEvent, post: Post?)
    {
        viewModelScope.launch {
            when(createPostEvent)
            {
                is CreatePostEvent.CreatePost -> {
                    if (post != null)
                    {
                        repo.createPost(post)
                            .onEach { dataState->
                                listener.sendResponse(dataState)
                            }
                            .launchIn(viewModelScope)
                    }
                }
            }
        }
    }
}


sealed class CreatePostEvent
{
    object CreatePost: CreatePostEvent()
}