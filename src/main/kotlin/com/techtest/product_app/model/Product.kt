package com.techtest.product_app.model

import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Table
import java.time.LocalDateTime

@Table("products")
data class Product(
    @Id
    val id: Long? = null,
    val extId: Long,
    val title: String,
    val handle: String,
    val vendor: String?,
    val createdAt: LocalDateTime = LocalDateTime.now()
)