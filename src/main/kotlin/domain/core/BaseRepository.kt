package com.khrix.domain.core

interface BaseRepository<Model> :
    BaseCreateReturn<Model>,
    BaseCreate<Model>,
    BaseRead<Model>,
    BaseUpdate<Model>,
    BaseDelete


interface BaseCreateReturn<Model> {
    suspend fun createRead(data: Model): Model
}

interface BaseCreate<Model> {
    suspend fun create(data: Model): Int
}

interface BaseRead<Model> {
    suspend fun read(id: Int): Model?
}

interface BaseUpdate<Model> {
    suspend fun update(id: Int, data: Model)
}

interface BaseDelete {
    suspend fun delete(id: Int)
}
