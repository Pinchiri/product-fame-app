package com.techtest.product_app.dao

import com.techtest.product_app.model.Product

interface ProductDao {
    fun findAll(): List<Product>
    fun findById(id: Long): Product?
    fun findByExtId(extId: Long): Product?
    fun save(product: Product): Product
    fun update(product: Product): Product
    fun deleteById(id: Long)
}