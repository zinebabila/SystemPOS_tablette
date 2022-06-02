package com.example.systemposfront.bo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
class Category {
    @SerializedName("id")
    @Expose
     var id: Long? = null
    @SerializedName("nameCategory")
    @Expose
     var nameCategory: String? = null
    @SerializedName("products")
    @Expose
     var products= listOf<Product>()
    override fun toString(): String {
        return nameCategory!!
    }


}