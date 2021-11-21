package com.example.posts.room

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [CachedEntity::class], version = 1, exportSchema = false)
abstract class PostDatabase: RoomDatabase()
{
    abstract fun postDao(): PostDao

    companion object
    {
      val DATABASE_NAME: String = "posts_db"
    }
}