package com.example.systemposfront.bo

import kotlinx.serialization.Serializable

@Serializable
class Notification {


    private var id: Long? = null
    private var description: String? = null
    private var merchant: Merchant? = null
}