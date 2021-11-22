package com.example.posts.repo


import com.example.posts.model.Post
import com.example.posts.retrofit.DataStates
import com.example.posts.retrofit.IRetrofit
import com.example.posts.retrofit.NetworkEntity
import com.example.posts.room.CachedEntity
import com.example.posts.room.PostDao
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response


class FakeRepository  constructor(
    private val webInterface: IRetrofit,
    private val postDao: PostDao,
    private val roomEntityMapper: FakeRoomMapper,
    private val networkEntityMapper: FakeNetworkMapper
): IRepository
{
    override suspend fun getPosts(): Flow<DataStates<List<Post>>> = flow {
        val response:List<NetworkEntity>? = webInterface.getPosts()

        response.let {
            val local = networkEntityMapper.mapFromEntityList(it!!)
            emit(DataStates.Success(local))
        }

    }

    override suspend fun createPost(post: Post): Flow<DataStates<Post>> = flow {
        val entity = roomEntityMapper.mapToEntity(post)
        postDao.insert(entity)
    }

    override suspend fun deletePost(id: Int): Flow<DataStates<Response<Post>>> = flow {
        val deletedPost: CachedEntity? = postDao.getAllPosts()?.find { it.id == id }
        if(deletedPost != null)
        {
            postDao.delete(deletedPost)
        }

    }

    override suspend fun deleteAllCache(): Flow<DataStates<Boolean>> = flow{
         postDao.deleteAll()
    }

}