package com.rglstudio.coroutines.network

import android.content.Context
import android.util.Log
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import com.rglstudio.coroutines.BuildConfig
import com.rglstudio.coroutines.util.InternetUtil
import okhttp3.Cache
import okhttp3.CacheControl
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit

object NetworkConfig {
    private fun provideCache(context: Context): Cache? {
        var cache: Cache? = null
        try {
            cache = Cache(
                File(context.cacheDir, "http-cache"),
                (10 * 1024 * 1024).toLong()
            ) // 10 MB
        } catch (e: Exception) {
            Log.e("Error", e.toString())
        }
        return cache
    }

    private fun provideCacheInterceptor(context: Context): Interceptor {
        return Interceptor { chain ->
            val response = chain.proceed(chain.request())
            val cacheControl: CacheControl =
                if (InternetUtil.checkInternetConnection(context)) {
                    CacheControl.Builder()
                        .maxAge(1, TimeUnit.MINUTES)
                        .build()
                } else {
                    CacheControl.Builder()
                        .maxStale(7, TimeUnit.DAYS)
                        .build()
                }

            response.newBuilder()
                .removeHeader("Pragma")
                .removeHeader("Cache-Control")
                .header("Cache-Control", cacheControl.toString())
                .build()
        }
    }

    private fun provideOfflineCacheInterceptor(context: Context): Interceptor {
        return Interceptor { chain ->
            var request = chain.request()
            if (!InternetUtil.checkInternetConnection(context)) {
                val cacheControl = CacheControl.Builder()
                    .maxStale(7, TimeUnit.DAYS)
                    .build()

                request = request.newBuilder()
                    .removeHeader("Pragma")
                    .removeHeader("Cache-Control")
                    .cacheControl(cacheControl)
                    .build()
            }
            chain.proceed(request)
        }
    }

    private fun provideInterceptor(context: Context) : OkHttpClient{
        val httpLoggingInterceptor = HttpLoggingInterceptor()
        httpLoggingInterceptor.level =
            if (BuildConfig.DEBUG)
                HttpLoggingInterceptor.Level.BODY
            else
                HttpLoggingInterceptor.Level.NONE

        val okhttp = OkHttpClient.Builder()
            .addInterceptor(httpLoggingInterceptor)
            .cache(provideCache(context))
            .addNetworkInterceptor(provideCacheInterceptor(context))
            .addInterceptor(provideOfflineCacheInterceptor(context))
            .connectTimeout(1, TimeUnit.MINUTES)
            .writeTimeout(1, TimeUnit.MINUTES)
            .retryOnConnectionFailure(true)
            .build()

        return okhttp
    }

    fun create(context: Context): ApiService {
        val retrofit = Retrofit.Builder()
            .baseUrl("https://jsonplaceholder.typicode.com/")
            .client(provideInterceptor(context))
            .addConverterFactory(GsonConverterFactory.create())
            .addCallAdapterFactory(CoroutineCallAdapterFactory())
            .build()

        return retrofit.create(ApiService::class.java)
    }
}