package com.example.systemposfront.controller

import com.example.systemposfront.bo.Category
import com.example.systemposfront.bo.Currency
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface CurrencyController {
    @GET("/currencies/getAll")
    fun getCurrency(): Call<List<Currency>>
    @GET("/currencies/getOne/{id}")
    fun  getOneCurrency(@Path("id")  idCat:Long): Call<Currency>
}