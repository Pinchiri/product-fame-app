package com.techtest.product_app.job

import com.techtest.product_app.service.ProductService
import org.slf4j.LoggerFactory
import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Component

@Component
class ProductImportJob(
    private val productService: ProductService
) {
    
    private val logger = LoggerFactory.getLogger(ProductImportJob::class.java)
    
    @Scheduled(initialDelay = 0) // Start immediately
    fun importProducts() {
        try {
            logger.info("Starting product import from external API")
            productService.fetchAndSaveProducts()
            logger.info("Product import completed successfully")
        } catch (e: Exception) {
            logger.error("Product import failed", e)
        }
    }
}
