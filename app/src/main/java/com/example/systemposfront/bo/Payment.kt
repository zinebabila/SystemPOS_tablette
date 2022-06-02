package com.example.systemposfront.bo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
class Payment {
@SerializedName("id")
@Expose
     var id: Long? = null
    @SerializedName("somme")
    @Expose
     var somme: Double? = null
    @SerializedName("customer")
    @Expose
     var customer: Customer? = null
    @SerializedName("currency")
    @Expose
     var currency: Currency? = null
    @SerializedName("command")
    @Expose
     var command: Command? = null
}