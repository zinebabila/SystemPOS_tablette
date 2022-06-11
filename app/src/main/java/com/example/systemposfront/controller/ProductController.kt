package com.example.systemposfront.controller


import com.example.systemposfront.bo.Product
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ProductController {
    /*  @Headers("Content-Type: application/json", "Accept: application/json")
      @GET("/products/getAll")
      fun getProducts(@Query("id")  id: RequestBody,
                      @Query("page") page: RequestBody,
                      @Query("size")  size: RequestBody,
                      @Query("sorted")  sorted: RequestBody,
                      @Query("reverse") type: RequestBody
      ): Call<ArrayList<Product>>
      @GET("/products/getOne/{id}")
      fun getProduct(@Path("id") id:Long): Call<Product>

      @GET("/products/getAllByCat")
      fun getProductCat(@Query("id")  id: RequestBody,
                        @Query("idCat")  idCat: RequestBody,
                        @Query("page") page: RequestBody,
                        @Query("size")  size: RequestBody,
                        @Query("sorted")  sorted: RequestBody,
                        @Query("reverse") type: RequestBody
      ): Call<ArrayList<Product>>*/
    @Headers("Content-Type: application/json", "Accept: application/json")
    @GET("/products/getAll/{id}")
    fun getProducts(@Path("id") id:Long): Call<ArrayList<Product>>
    @GET("/products/getOne/{id}")
    fun getProduct(@Path("id") id:Long): Call<Product>
    @GET("/products/getCat")
    fun getProductCat(@Path("id") id:Long,@Path("idcat") idcat:Long): Call<ArrayList<Product>>
    @Multipart
    @POST("/products/add")
    fun addProduct(@Part  file: MultipartBody.Part,
                   @Part("title")  title: RequestBody,
                   @Part("description")  description: RequestBody,
                   @Part("reduction")  reduction: RequestBody,
                   @Part("qteStock")  qteStock: RequestBody,
                   @Part("price")  price: RequestBody,
                   @Part("categoryID")  IdC: RequestBody,
                   @Part("merchant_Account_ID")  IdM: RequestBody
    ): Call<Product>

}