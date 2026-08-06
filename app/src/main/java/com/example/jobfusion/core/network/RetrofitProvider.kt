package com.example.jobfusion.core.network

import com.example.jobfusion.data.auth.remote.AuthApi
import com.example.jobfusion.jobseeker.data.remote.PreferencesApi
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitProvider {
    private const val DEFAULT_BASE_URL = "https://api.jobfusion.dev/"

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY
    }

    private val httpClient = OkHttpClient.Builder()
        .connectTimeout(30, TimeUnit.SECONDS)
        .readTimeout(30, TimeUnit.SECONDS)
        .writeTimeout(30, TimeUnit.SECONDS)
        .addInterceptor(loggingInterceptor)
        .build()

    private val retrofit: Retrofit = Retrofit.Builder()
        .baseUrl(DEFAULT_BASE_URL)
        .client(httpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val authApi: AuthApi by lazy { retrofit.create(AuthApi::class.java) }

    val preferencesApi: PreferencesApi by lazy { retrofit.create(PreferencesApi::class.java) }
}
