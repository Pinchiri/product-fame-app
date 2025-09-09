package com.techtest.product_app.dao

import com.techtest.product_app.model.Variant

interface VariantDao {
    fun findByProductId(productId: Long): List<Variant>
    fun findByExtId(extId: Long): Variant?
    fun save(variant: Variant): Variant
    fun update(variant: Variant): Variant
    fun deleteByProductId(productId: Long)
}