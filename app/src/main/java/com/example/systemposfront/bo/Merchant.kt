package com.example.systemposfront.bo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
class Merchant : Person() {

    @SerializedName("sold")
    @Expose
     var sold: Double? = null
    @SerializedName("account")
    @Expose
     var account: Account? = null
    @SerializedName("reviews")
    @Expose
     var reviews: Set<Review> = HashSet()

    @SerializedName("accountsCreated")
    @Expose
     var accountsCreated: Set<Account> = HashSet()

    @SerializedName("coupons")
    @Expose
     var coupons: Set<Coupon> = HashSet()

    @SerializedName("notifications")
    @Expose
     var notifications: Set<Notification> = HashSet()

    @SerializedName("currencies")
    @Expose
     var currencies: List<Currency> = listOf<Currency>()

    @SerializedName("products")
    @Expose
     var products: Set<Product> = HashSet()

}