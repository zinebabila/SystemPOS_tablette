package com.example.systemposfront.bo

import kotlinx.serialization.Serializable

@Serializable
class Review {

    private var id: Long? = null
    private var rate = 0.0

    private var comment: String? = null

    var customer: Customer? = null

    private var merchant: Merchant? = null

}