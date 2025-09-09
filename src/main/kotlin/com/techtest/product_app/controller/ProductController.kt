package com.techtest.product_app.controller

import com.techtest.product_app.dao.ProductDao
import com.techtest.product_app.model.Product
import com.techtest.product_app.service.ProductService
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
        // Página principal con botón y zonas HTMX
        return "index"
    }
    
    @GetMapping("/products")
    fun productsTableBody(model: Model): String {
        val productsWithVariants = productService.getAllProductsWithVariants()
        model.addAttribute("rows", productsWithVariants)
        return "fragments/product-rows :: rows"
    }
    
    @PostMapping("/products")
    fun addProduct(
        @RequestParam title: String,
        @RequestParam(required = false) handle: String?,
        model: Model
    ): String {
        // Crear producto simple para demostración
        val newProduct = Product(
            extId = System.currentTimeMillis(), // Temporal para demo
            title = title,
            handle = handle ?: title.lowercase().replace(" ", "-"),
            vendor = "Manual Entry"
        )
        productDao.save(newProduct)
        
        // Retornar fragment actualizado usando ProductDto con variants
        val productsWithVariants = productService.getAllProductsWithVariants()
        model.addAttribute("rows", productsWithVariants)
        return "fragments/product-rows :: rows"
    }
    
    @PostMapping("/products/refresh")
    fun refreshProductsFromApi(model: Model): String {
        productService.fetchAndSaveProducts()
        
        // Retornar fragment actualizado usando ProductDto con variants
        val productsWithVariants = productService.getAllProductsWithVariants()
        model.addAttribute("rows", productsWithVariants)
        return "fragments/product-rows :: rows"
    }
}