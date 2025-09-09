package com.techtest.product_app.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class VariantDto(
    val id: Long,
    val title: String,
    val sku: String?,
    val price: String,
    val available: Boolean,
    @JsonProperty("product_id")
    val productId: Long,
    @JsonProperty("requires_shipping")
    val requiresShipping: Boolean?,
    val taxable: Boolean?,
    @JsonProperty("compare_at_price")
    val compareAtPrice: String?,
    val position: Int?,
    @JsonProperty("created_at")
    val createdAt: String?,
    @JsonProperty("updated_at")
    val updatedAt: String?
)