package com.techtest.product_app.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class ProductDto(
    val id: Long,
    val title: String,
    val handle: String,
    val vendor: String?,
    @JsonProperty("product_type")
    val productType: String?,
    @JsonProperty("created_at")
    val createdAt: String?,
    @JsonProperty("updated_at")
    val updatedAt: String?,
    val variants: List<VariantDto> = emptyList()
)