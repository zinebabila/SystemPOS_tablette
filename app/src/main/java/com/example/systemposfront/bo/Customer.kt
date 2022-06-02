package com.example.systemposfront.bo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
class Customer {
    @SerializedName("reviews")
    @Expose
    private var reviews: Set<Review> = HashSet()
    @SerializedName("payments")
    @Expose
    private var payments: Set<Payment> = HashSet()


}