package com.example.posts.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.posts.repo.MyRepository
import com.example.posts.retrofit.DataStates
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DetailsFragmentViewModel @Inject constructor(private val repo: MyRepository): ViewModel()
{

    interface IDetailsFragment
    {
        fun flowResponse(message: DataStates<Any>?)
    }

    fun setStateEvent(listener: IDetailsFragment ,detailsStateEvent: DetailsStateEvent, id: Int)
    {
        viewModelScope.launch {
            when(detailsStateEvent)
            {
                is DetailsStateEvent.Remove -> {
                    repo.deletePost(id)
                        .onEach { datastate ->
                            listener.flowResponse(datastate)
                        }
                        .launchIn(viewModelScope)
                }
            }
        }
    }
}


sealed class DetailsStateEvent
{
    object Remove: DetailsStateEvent()
}