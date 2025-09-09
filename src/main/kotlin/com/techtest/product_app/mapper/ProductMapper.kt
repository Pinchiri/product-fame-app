package com.techtest.product_app.mapper

import com.techtest.product_app.dto.ProductDto
import com.techtest.product_app.dto.VariantDto
import com.techtest.product_app.model.Product
import com.techtest.product_app.model.Variant
import com.techtest.product_app.util.DateTimeUtils
import java.math.BigDecimal
import java.time.LocalDateTime

// DTO -> Entity mappings
fun ProductDto.toProduct(): Product {
    return Product(
        extId = this.id,
        title = this.title,
        handle = this.handle,
        vendor = this.vendor,
        createdAt = DateTimeUtils.parseIsoDateTime(this.createdAt)
    )
}

fun VariantDto.toVariant(productId: Long): Variant {
    return Variant(
        productId = productId,
        extId = this.id,
        title = this.title,
        sku = this.sku,
        price = this.price.toBigDecimalOrNull() ?: BigDecimal.ZERO,
        available = this.available,
        createdAt = DateTimeUtils.parseIsoDateTime(this.createdAt)
    )
}

// Entity -> DTO mappings
fun Product.toDto(variants: List<VariantDto> = emptyList()): ProductDto {
    return ProductDto(
        id = this.extId,
        title = this.title,
        handle = this.handle,
        vendor = this.vendor,
        productType = null,
        createdAt = this.createdAt.toString(),
        updatedAt = null,
        variants = variants
    )
}

fun Variant.toDto(): VariantDto {
    return VariantDto(
        id = this.extId,
        title = this.title,
        sku = this.sku,
        price = this.price.toString(),
        available = this.available,
        productId = this.productId,
        requiresShipping = null,
        taxable = null,
        compareAtPrice = null,
        position = null,
        createdAt = this.createdAt.toString(),
        updatedAt = null
    )
}