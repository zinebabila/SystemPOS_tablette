package com.example.systemposfront.controller

import com.example.systemposfront.bo.Command
import com.example.systemposfront.bo.Product
import retrofit2.Call
import retrofit2.http.*

interface ProductController {
    @Headers("Content-Type: application/json", "Accept: application/json")
    @GET("/products/getAll")
    fun getProducts(): Call<ArrayList<Product>>
    @GET("/products/getOne/{id}")
    fun getProduct(@Path("id") id:Long): Call<Product>
    @POST("/products")
    fun addProduct(@Body post: Product): Call<Product>
    @GET("/products/getCat/{id}")
     fun getProductCat(@Path("id") id:Long): Call<ArrayList<Product>>

}