package com.example.posts.repo

import com.example.posts.model.Post
import com.example.posts.retrofit.DataStates
import kotlinx.coroutines.flow.Flow

interface IRepository
{
    suspend fun getPosts() : Flow<DataStates<List<Post>>>

    suspend fun createPost(post: Post): Flow<DataStates<Post>>

    suspend fun deletePost(id: Int): Flow<DataStates<retrofit2.Response<Post>>>

    suspend fun deleteAllCache(): Flow<DataStates<Boolean>>
}