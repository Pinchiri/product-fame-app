package com.techtest.product_app.service

import com.techtest.product_app.dto.ProductDto
import com.techtest.product_app.dto.ProductsResponse
import com.techtest.product_app.mapper.toDto
import com.techtest.product_app.mapper.toProduct
import com.techtest.product_app.mapper.toVariant
import com.techtest.product_app.model.Product
import com.techtest.product_app.model.Variant
import com.techtest.product_app.dao.ProductDao
import com.techtest.product_app.dao.VariantDao
import com.techtest.product_app.dto.VariantDto
import org.slf4j.LoggerFactory
import org.springframework.stereotype.Service
import org.springframework.web.client.RestTemplate
import java.time.LocalDateTime

@Service
class ProductServiceImpl(
    private val productDao: ProductDao,
    private val variantDao: VariantDao,
    private val restTemplate: RestTemplate = RestTemplate()
) : ProductService {
    
    private val logger = LoggerFactory.getLogger(ProductServiceImpl::class.java)
    
    companion object {
        private const val EXTERNAL_API_URL = "https://famme.no/products.json"
        private const val MAX_PRODUCTS_TO_IMPORT = 50
    }
    
    override fun getAllProducts(): List<Product> {
        return productDao.findAll()
    }
    
    override fun getProductById(id: Long): Product? {
        return productDao.findById(id)
    }
    
    override fun getAllProductsWithVariants(): List<ProductDto> {
        val products = productDao.findAll()
        return products.mapNotNull { product ->
            product.id?.let { productId ->
                val variants = variantDao.findByProductId(productId)
                val variantDtos = variants.map { it.toDto() }
                product.toDto(variantDtos)
            }
        }
    }
    
    override fun fetchAndSaveProducts() {
        try {
            logger.info("Fetching products from external API: $EXTERNAL_API_URL")
            
            val apiResponse = fetchProductsFromExternalApi()
            val productsToImport = limitProductsForImport(apiResponse)
            
            importProducts(productsToImport)
            
            logger.info("Successfully imported ${productsToImport.size} products")
        } catch (e: Exception) {
            logger.error("Failed to fetch and save products from external API", e)
            throw e
        }
    }
    
    private fun fetchProductsFromExternalApi(): ProductsResponse? {
        return restTemplate.getForObject(EXTERNAL_API_URL, ProductsResponse::class.java)
    }
    
    private fun limitProductsForImport(apiResponse: ProductsResponse?): List<ProductDto> {
        val products = apiResponse?.products ?: emptyList()
        return products.take(MAX_PRODUCTS_TO_IMPORT)
    }
    
    private fun importProducts(productsToImport: List<ProductDto>) {
        productsToImport.forEach { productDto ->
            try {
                saveProductWithVariants(productDto)
            } catch (e: Exception) {
                logger.warn("Failed to import product with external ID: ${productDto.id}", e)
            }
        }
    }
    
    private fun saveProductWithVariants(productDto: ProductDto) {
        val savedProduct = saveOrUpdateProduct(productDto)
        saveNewVariants(productDto.variants, savedProduct)
    }
    
    private fun saveOrUpdateProduct(productDto: ProductDto): Product {
        val existingProduct = productDao.findByExtId(productDto.id)
        
        return if (existingProduct != null) {
            updateExistingProduct(existingProduct, productDto)
        } else {
            createNewProduct(productDto)
        }
    }
    
    private fun updateExistingProduct(existingProduct: Product, productDto: ProductDto): Product {
        val updatedProduct = existingProduct.copy(
            title = productDto.title,
            vendor = productDto.vendor
        )
        return productDao.update(updatedProduct)
    }
    
    private fun createNewProduct(productDto: ProductDto): Product {
        val newProduct = productDto.toProduct()
        return productDao.save(newProduct)
    }
    
    private fun saveNewVariants(variantDtos: List<VariantDto>, savedProduct: Product) {
        variantDtos.forEach { variantDto ->
            if (isNewVariant(variantDto.id)) {
                saveNewVariant(variantDto, savedProduct)
            }
        }
    }
    
    private fun isNewVariant(variantExtId: Long): Boolean {
        return variantDao.findByExtId(variantExtId) == null
    }
    
    private fun saveNewVariant(variantDto: VariantDto, product: Product) {
        val productId = product.id ?: throw IllegalStateException("Product must have an ID")
        val newVariant = variantDto.toVariant(productId)
        variantDao.save(newVariant)
    }
}