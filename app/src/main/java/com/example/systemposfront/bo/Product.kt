package com.example.systemposfront.bo

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
import kotlinx.serialization.Serializable

@Serializable
class Product {
    @SerializedName("id")
    @Expose
     var id: Long? = null
    @SerializedName("title")
    @Expose
     var title: String? = null
    @SerializedName("description")
    @Expose
     var description: String? = null

    @SerializedName("reduction")
    @Expose
     var reduction: Double? = null
    @SerializedName("prix")
    @Expose
    var prix: Double? = null
    @SerializedName("qteStock")
    @Expose
     var qteStock = 0
    @SerializedName("images")
    @Expose
     var images = ArrayList<ImageProducts>()
    @SerializedName("category")
    @Expose
     var category: Category? = null
    @SerializedName("commandLigne")
    @Expose
    var commandLigne: CommandLine? = null
   @SerializedName("merchant")
   @Expose
    var merchant: Merchant? = null
    override fun toString(): String {
       return "Product(id=$id, title=$title, description=$description, prix=$prix, reduction=$reduction, qteStock=$qteStock, images=$images, commandLigne=$commandLigne, merchant=$merchant),cat=$category"
   }


}