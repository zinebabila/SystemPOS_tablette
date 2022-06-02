package com.example.systemposfront.bo

import kotlinx.serialization.Serializable

@Serializable
class Review {

    private var id: Long? = null
    private var rate = 0

    private var comment: String? = null

    private var customer: Customer? = null

    private var merchant: Merchant? = null

}