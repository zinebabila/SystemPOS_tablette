package com.example.systemposfront.bo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
class CommandLine {
    @SerializedName("id")
    @Expose
     var id: Long? = null
    @SerializedName("qte")
    @Expose
     var qte = 0
    @SerializedName("product")
    @Expose
     var product: Product? = null
    @SerializedName("command")
    @Expose
     var command: Command? = null


}