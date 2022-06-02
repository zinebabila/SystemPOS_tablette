package com.example.systemposfront.bo

import com.google.gson.annotations.Expose
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import java.util.*
@Serializable
class Coupon {

    @SerialName("id")
    @Expose
     var id: Long? = null
    @SerialName("code")
    @Expose
    private var code: String? = null
    @SerialName("reduction")
    @Expose
     var reduction: Double? = null
    @SerialName("expirationDate")
    @Expose
     var expirationDate: String? = null
    @SerialName("maxnum_Uses")
    @Expose
    var maxnum_Uses: Int? = null
    @SerialName("num_Uses")
    @Expose
    var num_Uses: Int? = null
    @SerialName("merchantCreatorCoupon")
    @Expose
    private var merchantCreatorCoupon: Merchant? = null
    @SerialName("commands")
    @Expose
    private var commands: Set<Command?> = HashSet()
}