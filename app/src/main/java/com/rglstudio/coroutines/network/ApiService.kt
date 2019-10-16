package com.rglstudio.coroutines.network

import com.rglstudio.coroutines.data.ResponAlbum
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.GET

interface ApiService {
    @GET("photos")
    fun getAlbum() : Deferred<Response<List<ResponAlbum>>>
}