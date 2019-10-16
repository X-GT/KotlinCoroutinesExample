package com.rglstudio.coroutines.base

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import kotlinx.coroutines.*
import kotlin.coroutines.CoroutineContext

open class BaseViewModel : ViewModel() {
    private val parentJob = Job()
    private val coroutineContext : CoroutineContext get() = parentJob + Dispatchers.Default
    private val scope = CoroutineScope(coroutineContext)

    private var mloading: MutableLiveData<Boolean> = MutableLiveData()
    val loading: LiveData<Boolean>
        get() = mloading

    fun cancelRequests() = coroutineContext.cancel()

    fun launchDataLoad(block: suspend () -> Unit): Job {
        return scope.launch {
            try {
                mloading.postValue(true)
                block()
            }
            finally {
                mloading.postValue(false)
            }
        }
    }
}