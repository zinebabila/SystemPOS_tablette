package com.example.systemposfront.interfaces

import android.content.Context
import android.content.SharedPreferences
import com.example.systemposfront.BuildConfig
import com.example.systemposfront.controller.AccountController
import com.example.systemposfront.security.TokenManager
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory


interface AccountEnd {
    companion object {

        private val gson: Gson = GsonBuilder()
            .setLenient()
            .create()

        var authToken:String?=null
        private var BASE_URL = "http://192.168.86.23:9090"

        var retrofit: Retrofit = Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .client(OkHttpClient.Builder().addInterceptor { chain ->
                val request = chain.request().newBuilder().addHeader("Authorization", "Bearer "+ authToken).build()
                chain.proceed(request)
            }.build())
            .build()


    }




}