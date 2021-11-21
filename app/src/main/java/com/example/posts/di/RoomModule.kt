package com.example.posts.di

import android.content.Context
import androidx.room.Room
import com.example.posts.room.PostDao
import com.example.posts.room.PostDatabase

import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RoomModule
{
    @Singleton
    @Provides
    fun providePostDatabase(@ApplicationContext context: Context): PostDatabase
    {
            return Room.databaseBuilder(context, PostDatabase::class.java, PostDatabase.DATABASE_NAME)
                .fallbackToDestructiveMigration()
                .build()
    }

    @Singleton
    @Provides
    fun providePostDao(postDatabase: PostDatabase) : PostDao
    {
        return postDatabase.postDao()
    }
}