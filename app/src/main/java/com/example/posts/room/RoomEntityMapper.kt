package com.example.posts.room

import com.example.posts.model.Post
import com.example.posts.utils.EntityMapper

class RoomEntityMapper : EntityMapper<CachedEntity, Post>
{
    override fun mapFromEntity(entity: CachedEntity): Post {
        return Post(
            id = entity.id,
            userId = entity.userId,
            title = entity.title,
            body = entity.body
        )
    }

    override fun mapToEntity(domainModel: Post): CachedEntity {
        return CachedEntity(
            id = domainModel.id,
            userId = domainModel.userId,
            title = domainModel.title,
            body = domainModel.body
        )
    }

    fun mapFromEntityList(entities: List<CachedEntity>): List<Post>
    {
        return entities.map { mapFromEntity(it)}
    }

}