package com.techtest.product_app.dao

import com.techtest.product_app.model.Product
import org.springframework.jdbc.core.simple.JdbcClient
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.stereotype.Repository

@Repository
class ProductDaoImpl(private val jdbcClient: JdbcClient) : ProductDao {
    
    override fun findAll(): List<Product> {
        return jdbcClient.sql("""
            SELECT id, ext_id, title, handle, vendor, created_at 
            FROM products 
            ORDER BY created_at DESC
        """)
        .query { rs, _ ->
            Product(
                id = rs.getLong("id"),
                extId = rs.getLong("ext_id"),
                title = rs.getString("title"),
                handle = rs.getString("handle"),
                vendor = rs.getString("vendor"),
                createdAt = rs.getTimestamp("created_at").toLocalDateTime()
            )
        }
        .list()
    }
    
    override fun findById(id: Long): Product? {
        return jdbcClient.sql("""
            SELECT id, ext_id, title, handle, vendor, created_at 
            FROM products 
            WHERE id = ?
        """)
        .param(id)
        .query { rs, _ ->
            Product(
                id = rs.getLong("id"),
                extId = rs.getLong("ext_id"),
                title = rs.getString("title"),
                handle = rs.getString("handle"),
                vendor = rs.getString("vendor"),
                createdAt = rs.getTimestamp("created_at").toLocalDateTime()
            )
        }
        .optional()
        .orElse(null)
    }
    
    override fun findByExtId(extId: Long): Product? {
        return jdbcClient.sql("""
            SELECT id, ext_id, title, handle, vendor, created_at 
            FROM products 
            WHERE ext_id = ?
        """)
        .param(extId)
        .query { rs, _ ->
            Product(
                id = rs.getLong("id"),
                extId = rs.getLong("ext_id"),
                title = rs.getString("title"),
                handle = rs.getString("handle"),
                vendor = rs.getString("vendor"),
                createdAt = rs.getTimestamp("created_at").toLocalDateTime()
            )
        }
        .optional()
        .orElse(null)
    }
    
    override fun save(product: Product): Product {
        val keyHolder = GeneratedKeyHolder()
        
        jdbcClient.sql("""
            INSERT INTO products (ext_id, title, handle, vendor, created_at) 
            VALUES (?, ?, ?, ?, ?)
        """)
        .param(product.extId)
        .param(product.title)
        .param(product.handle)
        .param(product.vendor)
        .param(product.createdAt)
        .update(keyHolder)
        
        val generatedId = keyHolder.key?.toLong() ?: throw RuntimeException("Failed to get generated ID")
        return product.copy(id = generatedId)
    }
    
    override fun update(product: Product): Product {
        jdbcClient.sql("""
            UPDATE products 
            SET title = ?, handle = ?, vendor = ? 
            WHERE id = ?
        """)
        .param(product.title)
        .param(product.handle)
        .param(product.vendor)
        .param(product.id)
        .update()
        
        return product
    }
    
    override fun deleteById(id: Long) {
        jdbcClient.sql("DELETE FROM products WHERE id = ?")
        .param(id)
        .update()
    }
}