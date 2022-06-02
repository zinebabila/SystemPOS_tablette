package com.example.systemposfront.bo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

abstract class Person {
    @SerializedName("id")
    @Expose
     var id: Long? = null
    @SerializedName("firstName")
    @Expose
     var firstName: String? = null
    @SerializedName("lastName")
    @Expose
     var lastName: String? = null
    @SerializedName("numTel")
    @Expose
     var numTel: String? = null
    @SerializedName("urlImage")
    @Expose
     var urlImage: String? = null
}