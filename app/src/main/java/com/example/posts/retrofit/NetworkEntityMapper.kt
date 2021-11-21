package com.example.posts.retrofit

import com.example.posts.model.Post
import com.example.posts.utils.EntityMapper
import javax.inject.Inject

class NetworkEntityMapper @Inject constructor(): EntityMapper<NetworkEntity, Post>
{
    override fun mapFromEntity(entity: NetworkEntity): Post
    {
        return Post(
            id = entity.id,
            userId = entity.userId,
            title = entity.title,
            body = entity.body
        )
    }

    override fun mapToEntity(domainModel: Post): NetworkEntity {
        return NetworkEntity(
            id = domainModel.id,
            userId = domainModel.userId,
            title = domainModel.title,
            body = domainModel.body
        )
    }

    fun mapFromEntityList(entities: List<NetworkEntity>): List<Post>
    {
        return entities.map { mapFromEntity(it)}
    }
}