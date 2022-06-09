package com.example.systemposfront.controller


import com.example.systemposfront.bo.Notification
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path

interface NotificationController {
    @GET("/notifications/getAll/{id}")
    fun getNotification(@Path("id")  id:Long): Call<ArrayList<Notification>>
}