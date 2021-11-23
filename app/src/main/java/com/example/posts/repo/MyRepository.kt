package com.example.posts.repo

import android.util.Log
import com.example.posts.model.Post
import com.example.posts.retrofit.DataStates
import com.example.posts.retrofit.IRetrofit
import com.example.posts.retrofit.NetworkEntity
import com.example.posts.retrofit.NetworkEntityMapper
import com.example.posts.room.CachedEntity
import com.example.posts.room.PostDao
import com.example.posts.room.RoomEntityMapper
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import retrofit2.Response
import java.lang.Exception
import java.net.UnknownHostException

private const val TAG = "MyRepository"
class MyRepository  constructor(
    private val webInterface: IRetrofit,
    private val postDao: PostDao,
    private val roomEntityMapper: RoomEntityMapper,
    private val networkEntityMapper: NetworkEntityMapper
): IRepository
{

    override suspend fun getPosts(): Flow<DataStates<List<Post>>> = flow {
        try
        {
            // Get all posts from web
            val response: List<NetworkEntity>? = webInterface.getPosts()
            var posts: List<Post>

            // Get all room entities
            val cachedPosts: List<CachedEntity>? = postDao.getAllPosts()


            response?.let { networkList ->

                //Convert NetworkEntities to local
                posts = networkEntityMapper.mapFromEntityList(networkList)

                if(!cachedPosts.isNullOrEmpty())
                {
                       val cachedLocalPosts:List<Post> = roomEntityMapper.mapFromEntityList(cachedPosts)

                       // Find differences between cache and network posts
                       val newCachedPosts: List<Post> = cachedLocalPosts.asSequence().minus(posts.toSet()).map{ it }.toList()

                       // If cache has more items than the web => add the new items to web
                       if (newCachedPosts.isNotEmpty())
                       {
                           newCachedPosts.forEach { newCachedPost ->
                               Log.e(TAG, " add newWebPosts id: " + newCachedPost.id.toString() )
                               webInterface.createPost(networkEntityMapper.mapToEntity(newCachedPost))
                           }
                       }
                       // If cache has no new items, we want to check if the web has more to add them in cache
                       else
                       {
                           val newWebPosts: List<Post> = posts.asSequence().minus(cachedLocalPosts.toSet()).map{ it }.toList()

                           //if web has more items, then remove them
                           if(newWebPosts.isNotEmpty())
                           {
                               newWebPosts.forEach { newWebPost ->
                                    Log.e(TAG, " delete newWebPosts id: " + newWebPost.id.toString() )
                                    webInterface.deletePost(newWebPost.id)
                               }
                           }
                       }
                    emit(DataStates.Success(roomEntityMapper.mapFromEntityList(cachedPosts)))
                }
                else
                {
                    posts.forEach { post->
                          postDao.insert(roomEntityMapper.mapToEntity(post))
                    }
                    emit(DataStates.Success(posts))
                }


            } ?: run {

                if(!cachedPosts.isNullOrEmpty())
                {
                    emit(DataStates.Success(roomEntityMapper.mapFromEntityList(cachedPosts)))
                }
            }

        }catch (e: UnknownHostException)
        {
            emit(DataStates.Error(e))
            val cachedPosts = postDao.getAllPosts()

            cachedPosts?.let {
                emit(DataStates.Success(roomEntityMapper.mapFromEntityList(cachedPosts)))
            }
        }
        catch (e: Exception)
        {
            emit(DataStates.Error(e))
        }
    }


     override suspend fun createPost(post: Post): Flow<DataStates<Post>> = flow {

         try
         {
             val networkEntity = networkEntityMapper.mapToEntity(post)
             val responseBody: Response<NetworkEntity> = webInterface.createPost(networkEntity)
             if(responseBody.isSuccessful)
             {
                 emit(DataStates.Success(networkEntityMapper.mapFromEntity(responseBody.body()!!)))
             }

             //Add to Room
             postDao.insert(roomEntityMapper.mapToEntity(post))
         }
         catch (e: UnknownHostException)
         {
             //Add to Room
             postDao.insert(roomEntityMapper.mapToEntity(post))

             emit(DataStates.Error(e))
         }

     }


     override suspend fun deletePost(id: Int): Flow<DataStates<Response<Post>>> = flow {
//        // emit(DataStates.Loading)
         try
         {
             val responseBody: Response<Post> = webInterface.deletePost(id)

             val deletedPost: CachedEntity? = postDao.getAllPosts()!!.find { it.id == id }
             if(deletedPost != null)
             {
                 postDao.delete(deletedPost)
             }

             emit(DataStates.Success(responseBody))
         }
         catch (e:UnknownHostException)
         {
             val deletedPost: CachedEntity? = postDao.getAllPosts()!!.find { it.id == id }
             if(deletedPost != null)
             {
                 postDao.delete(deletedPost)
             }
             emit(DataStates.Error(e))
         }


     }

    override suspend fun deleteAllCache(): Flow<DataStates<Boolean>> = flow{
        try{
            postDao.deleteAll()
            emit(DataStates.Success(true))
        }catch (e: Exception)
        {
            emit(DataStates.Error(e))
        }

    }
}

