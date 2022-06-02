package com.example.systemposfront.controller

import com.example.systemposfront.bo.Category
import com.example.systemposfront.bo.Product

import retrofit2.Call
import retrofit2.http.*

interface CategorieController {
    @GET("/categories/getAll")
    fun getCategorie(): Call<List<Category>>
    @GET("/categories/getOne/{id}")
    fun  getProductCat(@Path("id")  idCat:Long): Call<Category>
    @POST("/categories")
    fun addCat(@Body post: Category): Call<Category>

}