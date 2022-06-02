package com.example.systemposfront.bo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
class ImageProducts {
    @SerializedName("id")
    @Expose
     var id: Long? = null
    @SerializedName("urlImage")
    @Expose
     var urlImage: String? = null
   // @SerializedName("product")
   // var product:Product?=null

}