package com.example.systemposfront.bo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
class Currency {

@SerializedName("id")
@Expose
     var id: Long? = null
    @SerializedName("currencyName")
    @Expose
     var currencyName: String? = null
    @SerializedName("percentageToDollar")
    @Expose
     var percentageToDollar: Double? = null
    @SerializedName("payments")
    @Expose

     var payments: Set<Payment?> = HashSet()
    @SerializedName("imageCurrency")
    @Expose
     var imageCurrency:String?=null
    @SerializedName("merchants")
    @Expose

    private var merchants: Set<Merchant?> = HashSet()
}