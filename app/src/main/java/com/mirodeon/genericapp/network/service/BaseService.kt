package com.mirodeon.genericapp.network.service

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

open class BaseService(private val baseUrl: String) {

    fun getRetrofit(): Retrofit {
        val okBuilder = OkHttpClient().newBuilder().apply {
            connectTimeout(60, TimeUnit.SECONDS)
            callTimeout(60, TimeUnit.SECONDS)
            readTimeout(60, TimeUnit.SECONDS)
            writeTimeout(60, TimeUnit.SECONDS)
        }

        val gsonBuilder = GsonBuilder()
            .setDateFormat("yyyy-MM-dd'T'HH:mm:ssX")
            .create()


        return Retrofit.Builder()
            .baseUrl(baseUrl)
            .addConverterFactory(GsonConverterFactory.create(gsonBuilder))
            .client(okBuilder.build())
            .build()
    }
}