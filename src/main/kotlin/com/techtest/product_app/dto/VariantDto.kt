package com.techtest.product_app.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class VariantDto(
    val id: Long,
    val title: String,
    val sku: String?,
    val price: String,
    val available: Boolean,
    @get:JsonProperty("product_id")
    val productId: Long,
    @get:JsonProperty("requires_shipping")
    val requiresShipping: Boolean?,
    val taxable: Boolean?,
    @get:JsonProperty("compare_at_price")
    val compareAtPrice: String?,
    val position: Int?,
    @get:JsonProperty("created_at")
    val createdAt: String?,
    @get:JsonProperty("updated_at")
    val updatedAt: String?
)