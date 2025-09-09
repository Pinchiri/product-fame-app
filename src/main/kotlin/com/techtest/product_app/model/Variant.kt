package com.techtest.product_app.model

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.math.BigDecimal
import java.time.LocalDateTime

@Table("variants")
data class Variant(
    @Id
    val id: Long? = null,
    val productId: Long,
    val extId: Long,
    val title: String,
    val sku: String?,
    val price: BigDecimal,
    val available: Boolean = true,
    val createdAt: LocalDateTime = LocalDateTime.now()
)