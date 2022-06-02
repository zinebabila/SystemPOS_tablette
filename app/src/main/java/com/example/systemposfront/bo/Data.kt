package com.example.systemposfront.bo

import androidx.annotation.Keep
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.InternalSerializationApi
import kotlinx.serialization.SerialName
import kotlinx.serialization.json.Json
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString


@Keep
@SerialName("Data")
@Serializable
class Data
{

    @SerializedName("date_commande")
    @Expose
    var date_commande:String?=null
    @SerializedName("account_id")
    @Expose
    var account_id:Long?=0
    @SerializedName("coupon_id")
    @Expose
    var coupon_id:Long?=0
    @SerializedName("products")
    @Expose
    var products = arrayListOf<CartItem>()

    @SerializedName("somme")
    @Expose
    var somme:Double?=0.0
    @SerializedName("currency_id")
    @Expose
    var currency_id:Long?=0
    override fun toString(): String {
        return "Data(date_commande=$date_commande, account_id=$account_id, coupon_id=$coupon_id, products=$products, somme=$somme, currency_id=$currency_id)"
    }
    fun toJsonString(): String {
        return Json.encodeToString<Data>(this)
    }
}