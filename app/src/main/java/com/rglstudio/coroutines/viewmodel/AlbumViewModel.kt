package com.rglstudio.coroutines.viewmodel

import android.content.Context
import com.rglstudio.coroutines.base.BaseViewModel
import com.rglstudio.coroutines.network.NetworkConfig
import com.rglstudio.coroutines.repository.AlbumRepositoryImpl

class AlbumViewModel(context: Context) : BaseViewModel(){
    companion object{
        val FACTORY = singleArgViewModelFactory(::AlbumViewModel)
    }
    private val albumRepository : AlbumRepositoryImpl = AlbumRepositoryImpl(NetworkConfig.create(context))

    val responData = albumRepository.responData
    val errorInfo = albumRepository.errorInfo

    fun getAlbum() {
        launchDataLoad {
            albumRepository.getAlbum()
        }
    }
}