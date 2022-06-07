package com.example.systemposfront.interfaces

import com.example.systemposfront.BuildConfig
import com.google.gson.Gson
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.converter.scalars.ScalarsConverterFactory

interface AuthenEnd {
    companion object {

        private val gson: Gson = GsonBuilder()
            .setLenient()
            .create()

        private var BASE_URL = "http://192.168.86.32:9090"

        val retrofit: Retrofit = Retrofit.Builder()
            .addConverterFactory(ScalarsConverterFactory.create())
            .addConverterFactory(GsonConverterFactory.create(gson))
            .baseUrl(BASE_URL)
            .build()


    }


}