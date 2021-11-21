package com.example.posts.room

import androidx.room.*

@Dao
interface PostDao
{
        @Insert(onConflict = OnConflictStrategy.REPLACE)
        suspend fun insert(postEntity: CachedEntity): Long

        @Query("SELECT * FROM posts")
        suspend fun getAllPosts(): List<CachedEntity>?

        @Delete
        suspend fun delete(postEntity: CachedEntity)

        @Query("DELETE FROM posts")
        suspend fun deleteAll()

}