package com.example.systemposfront.bo

import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

@Serializable
class Account {
     @SerializedName("id")
     var id: Long? = null
     @SerializedName("email")
     var email: String? = null
     @SerializedName("password")
     var password: String? = null
     @SerializedName("type")
     var type = 0.toChar()
     @SerializedName("commands")
     val commands: Set<Command?>? = null
     @SerializedName("merchantCreator")
     val merchantCreator: Merchant? = null
    // @SerializedName("merchantOwner")
    // val merchantOwner: Merchant? = null
     override fun toString(): String {
          return "Account(id=$id, email=$email, password=$password, type=$type)"
     }
     fun toJsonString(): String {
          return Json.encodeToString<Account>(this)
     }
}