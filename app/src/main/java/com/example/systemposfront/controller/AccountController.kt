package com.example.systemposfront.controller

import com.example.systemposfront.bo.Account
import com.example.systemposfront.bo.JwtRequest
import com.example.systemposfront.bo.JwtResponse
import com.example.systemposfront.bo.Product
import okhttp3.RequestBody

import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET

import retrofit2.http.POST
import retrofit2.http.Path

interface AccountController {

    @POST("/authenticate")
   fun putNewDataOnDb(@Body account: Account):Call<Account>
    @POST("/authenticate")
     fun login(@Body post: JwtRequest) : Call<JwtResponse>
    @GET("/Account/getOne/{id}")
    fun getAccount(@Path("id") id:Long): Call<Account>







}