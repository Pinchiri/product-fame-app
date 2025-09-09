package com.techtest.product_app.service

import com.techtest.product_app.dto.ProductDto
import com.techtest.product_app.model.Product

interface ProductService {
    fun getAllProducts(): List<Product>
    fun getProductById(id: Long): Product?
    fun getAllProductsWithVariants(): List<ProductDto>
    fun fetchAndSaveProducts()
}