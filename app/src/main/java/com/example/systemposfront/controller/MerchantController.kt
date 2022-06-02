package com.example.systemposfront.controller

import com.example.systemposfront.bo.Merchant
import com.example.systemposfront.bo.Product
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path

interface MerchantController {
    @Headers("Content-Type: application/json", "Accept: application/json")
    @GET("/merchants/getOne/{id}")
    fun getMerchant(@Path("id") id:Long): Call<Merchant>
}