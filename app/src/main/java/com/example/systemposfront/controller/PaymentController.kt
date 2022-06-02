package com.example.systemposfront.controller

import com.example.systemposfront.bo.Payment
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.POST

interface PaymentController {
    @POST("/Payment/add")
    fun addPayment(@Body post: Payment) : Call<Payment>
}