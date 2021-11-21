package com.example.posts.retrofit

import com.example.posts.model.Post
import retrofit2.Response
import retrofit2.http.*

interface IRetrofit
{
    @GET("posts")
    suspend fun getPosts() : List<NetworkEntity>?

    @POST("posts")
    suspend fun createPost(@Body post: NetworkEntity): Response<NetworkEntity>

    @DELETE("posts/{id}")
    suspend fun deletePost(@Path("id") id: Int): Response<Post>
}