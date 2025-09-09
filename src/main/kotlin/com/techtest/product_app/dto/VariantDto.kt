package com.techtest.product_app.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class VariantDto(
    val id: Long,
    val title: String,
    val sku: String?,
    val price: String,
    val available: Boolean,
    @JsonProperty("product_id")
    val productId: Long
)