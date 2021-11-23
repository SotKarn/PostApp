package com.example.posts.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
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
class MainViewModel
@Inject constructor(
     private val repo: MyRepository
): ViewModel()
{
    private val _datastate: MutableLiveData<DataStates<Boolean>> = MutableLiveData()
    val dataState: LiveData<DataStates<Boolean>>
        get() = _datastate


    fun setStateEvent(mainStateEvent: MainStateEvent)
    {
        viewModelScope.launch {
            when(mainStateEvent)
            {
                is MainStateEvent.DeleteAll -> {
                    repo.deleteAllCache()
                        .onEach { dataState->
                            _datastate.value = dataState
                        }
                        .launchIn(viewModelScope)
                }
            }
        }
    }
}


sealed class MainStateEvent
{
    object DeleteAll: MainStateEvent()
}