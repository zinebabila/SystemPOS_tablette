package com.example.systemposfront.controller

import com.example.systemposfront.bo.Command
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface CommandController {
    @POST("/commands/add")
    fun addCommand(@Body post: Command) : Call<Command>

}