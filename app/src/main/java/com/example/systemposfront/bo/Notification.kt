package com.example.systemposfront.bo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
class Notification {
    @SerializedName("id")
    @Expose
    private var id: Long? = null

    @SerializedName("description")
    @Expose
    var description: String? = null

    @SerializedName("dateNotification")
    @Expose
    var dateNotification: String? = null

    @SerializedName("product")
    @Expose
    var product: Product? = null

    @SerializedName("review")
    @Expose
    var review: Review? = null

    @SerializedName("visited")
    @Expose
    var visited = false



}