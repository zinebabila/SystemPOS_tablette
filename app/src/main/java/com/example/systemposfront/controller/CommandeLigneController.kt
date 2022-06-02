package com.example.systemposfront.controller

import com.example.systemposfront.bo.Command
import com.example.systemposfront.bo.CommandLine
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface CommandeLigneController {
    @POST("/commandslines/add")
    fun addCommandLine(@Body post: CommandLine) : Call<CommandLine>
}