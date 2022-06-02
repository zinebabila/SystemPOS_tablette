package com.example.systemposfront.controller

import com.example.systemposfront.bo.Coupon
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path


interface CouponController {
    @GET("/coupons/getCode/{code}")
    fun getCouponCode(@Path("code") code: String?): Call<Coupon>

}