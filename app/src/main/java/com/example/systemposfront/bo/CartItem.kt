package com.example.systemposfront.bo

import androidx.annotation.Keep
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
@Keep
@Serializable
 data class CartItem(
    @SerializedName("product")
    @Expose
    var product: Product,

    @SerializedName("quantity")
    @Expose
    var quantity: Int = 0

)