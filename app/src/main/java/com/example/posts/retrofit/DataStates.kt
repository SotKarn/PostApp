package com.example.posts.retrofit

sealed class DataStates<out R>
{
    data class Success<out T>(val data: T?): DataStates<T>()
    data class Error(val exception: Exception): DataStates<Nothing>()
}