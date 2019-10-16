package com.rglstudio.coroutines.network

open class DataSourceException(message: String? = null) : Exception(message)

class RemoteDataNotFoundException(msg: String) : DataSourceException(msg)