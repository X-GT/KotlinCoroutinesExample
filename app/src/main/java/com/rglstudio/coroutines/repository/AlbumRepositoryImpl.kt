package com.rglstudio.coroutines.repository

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.rglstudio.coroutines.base.BaseRepository
import com.rglstudio.coroutines.data.ResponAlbum
import com.rglstudio.coroutines.network.ApiService
import com.rglstudio.coroutines.network.RemoteDataNotFoundException
import com.rglstudio.coroutines.network.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class AlbumRepositoryImpl(private val apiService: ApiService) : BaseRepository(), AlbumRepository {
    override suspend fun getAlbum() {
        withContext(Dispatchers.IO) {
            try {
                val result = apiCall {
                    apiService.getAlbum().await()
                }

                when (result) {
                    is Result.Success -> {
                        val errorList = result.data
                        responData.postValue(errorList)
                    }
                    is Result.Error ->
                        errorInfo.postValue(result.exception.message)
                }

            } catch (error: RemoteDataNotFoundException) {
                Log.e("Error", error.message!!)
            }
        }
    }

    val responData : MutableLiveData<List<ResponAlbum>> by lazy {
        MutableLiveData<List<ResponAlbum>>()
    }

    val errorInfo : MutableLiveData<String> by lazy {
        MutableLiveData<String>()
    }
}