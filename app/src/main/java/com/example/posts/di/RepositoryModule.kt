package com.example.posts.di

import com.example.posts.repo.MyRepository
import com.example.posts.retrofit.IRetrofit
import com.example.posts.retrofit.NetworkEntityMapper
import com.example.posts.room.PostDao
import com.example.posts.room.RoomEntityMapper
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule
{
    @Singleton
    @Provides
    fun providesRepository(webInterface: IRetrofit, postDao: PostDao, roomEntityMapper: RoomEntityMapper, networkEntityMapper: NetworkEntityMapper): MyRepository
    {
        return MyRepository(webInterface,postDao, roomEntityMapper, networkEntityMapper)
    }
}