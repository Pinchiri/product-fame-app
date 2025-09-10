package com.techtest.product_app.controller

import com.techtest.product_app.dao.ProductDao
import com.techtest.product_app.model.Product
import com.techtest.product_app.service.ProductService
import com.techtest.product_app.util.Constants.Templates
import com.techtest.product_app.util.Constants.ModelAttributes
import com.techtest.product_app.util.Constants.General
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestParam

@Controller
class ProductController(
    private val productService: ProductService,
    private val productDao: ProductDao
) {
    
    @GetMapping("/")
    fun home(model: Model): String {
        return Templates.INDEX
    }
    
    @GetMapping("/products")
    fun productsTableBody(model: Model): String {
        val productsWithVariants = productService.getAllProductsWithVariants()
        model.addAttribute(ModelAttributes.ROWS, productsWithVariants)
        return Templates.PRODUCT_ROWS_FRAGMENT
    }
    
    @PostMapping("/products")
    fun addProduct(
        @RequestParam title: String,
        @RequestParam(required = false) handle: String?,
        @RequestParam(required = false) vendor: String?,
        model: Model
    ): String {
        val newProduct = Product(
            extId = System.currentTimeMillis(),
            title = title,
            handle = handle ?: title.lowercase().replace(General.SPACE, General.SEPARATOR),
            vendor = vendor ?: General.DEFAULT_VENDOR
        )
        productDao.save(newProduct)
        
        val productsWithVariants = productService.getAllProductsWithVariants()
        model.addAttribute(ModelAttributes.ROWS, productsWithVariants)
        return Templates.PRODUCT_ROWS_FRAGMENT
    }
}