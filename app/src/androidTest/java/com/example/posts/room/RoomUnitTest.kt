package com.example.posts.room

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.matcher.ViewMatchers.assertThat
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.*
import org.hamcrest.MatcherAssert
import org.hamcrest.Matchers.hasSize
import org.junit.After
import org.junit.Before
import org.junit.Test

class RoomUnitTest
{

    private lateinit var dao: PostDao
    private lateinit var db: PostDatabase
    private val dummyPost = CachedEntity(1, 1, "test title", "test body")
    private val dummyPost2 = CachedEntity(2, 2, "test title 2", "test body 2")
    private val dummyPost3 = CachedEntity(3, 3, "test title 3", "test body 3")

    @Before
    fun setUp()
    {
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(context, PostDatabase::class.java)
            .build()
        dao = db.postDao()
    }

    @After
    fun tearDown() {
        db.close()
    }

    @Test
    fun checkIfItemsAreInserted() = runBlocking()
    {

        //Insert
        dao.insert(dummyPost)
        dao.insert(dummyPost2)
        dao.insert(dummyPost3)

        //Get Posts from database
        val cachedPostList = dao.getAllPosts()

        //Check
        assertThat(cachedPostList, hasSize(3))
        assertThat(cachedPostList, hasItems(dummyPost, dummyPost2, dummyPost3))
    }

    @Test
    fun checkItemDelete() = runBlocking()
    {

        dao.insert(dummyPost)
        dao.delete(dummyPost)
        val cachedPostList = dao.getAllPosts()
        MatcherAssert.assertThat(cachedPostList, not(hasItem(dummyPost)))
    }

    @Test
    fun checkDeleteAll() = runBlocking()
    {

        //Insert two items
        dao.insert(dummyPost)
        dao.insert(dummyPost2)

        //Get all items
        var cachedPostList = dao.getAllPosts()
        MatcherAssert.assertThat(cachedPostList, hasItem(dummyPost))

        //Delete all items
        dao.deleteAll()
        cachedPostList = dao.getAllPosts()
        MatcherAssert.assertThat(cachedPostList, hasSize(0))
    }
}