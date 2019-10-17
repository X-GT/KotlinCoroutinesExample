package com.rglstudio.coroutines.repository

interface AlbumRepository {
    suspend fun getAlbum()
}