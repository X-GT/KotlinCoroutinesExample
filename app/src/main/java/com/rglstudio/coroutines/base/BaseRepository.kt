package com.rglstudio.coroutines.base

import com.rglstudio.coroutines.network.RemoteDataNotFoundException
import com.rglstudio.coroutines.network.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import org.json.JSONObject
import retrofit2.HttpException
import retrofit2.Response

open class BaseRepository {
    suspend fun<T : Any> apiCall(call: suspend()-> Response<T>) : Result<T> =
        withContext(Dispatchers.IO){
            val response = call.invoke()
            if (response.isSuccessful)
                Result.Success(response.body()!!)
            else {
                try {
                    val jObjError = JSONObject(response.errorBody()!!.string())
                    Result.Error(
                        RemoteDataNotFoundException(
                            jObjError.getJSONObject("error")
                                .getString("message")
                        )
                    )
                } catch (ex: HttpException) {
                    Result.Error(RemoteDataNotFoundException(ex.message()))
                } catch (ex: Throwable) {
                    Result.Error(RemoteDataNotFoundException(ex.localizedMessage!!))
                }
        }
    }
}