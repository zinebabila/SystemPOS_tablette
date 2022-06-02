package com.example.systemposfront.controller

import com.example.systemposfront.bo.Command
import com.example.systemposfront.bo.Data
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface DataController {
    @POST("/data/add")
    fun addCommand(@Body post: Data) : Call<Command>
}