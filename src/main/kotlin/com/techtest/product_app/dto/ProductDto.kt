package com.techtest.product_app.dto

import com.fasterxml.jackson.annotation.JsonProperty

data class ProductDto(
    val id: Long,
    val title: String,
    val handle: String,
    val vendor: String?,
    val variants: List<VariantDto>
)