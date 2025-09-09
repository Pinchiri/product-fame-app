package com.techtest.product_app.dao

import com.techtest.product_app.model.Variant
import org.springframework.jdbc.core.simple.JdbcClient
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.stereotype.Repository

@Repository
class VariantDaoImpl(private val jdbcClient: JdbcClient) : VariantDao {
    
    override fun findByProductId(productId: Long): List<Variant> {
        return jdbcClient.sql("""
            SELECT id, product_id, ext_id, title, sku, price, available, created_at 
            FROM variants 
            WHERE product_id = ?
            ORDER BY created_at ASC
        """)
        .param(productId)
        .query { rs, _ ->
            Variant(
                id = rs.getLong("id"),
                productId = rs.getLong("product_id"),
                extId = rs.getLong("ext_id"),
                title = rs.getString("title"),
                sku = rs.getString("sku"),
                price = rs.getBigDecimal("price"),
                available = rs.getBoolean("available"),
                createdAt = rs.getTimestamp("created_at").toLocalDateTime()
            )
        }
        .list()
    }
    
    override fun findByExtId(extId: Long): Variant? {
        return jdbcClient.sql("""
            SELECT id, product_id, ext_id, title, sku, price, available, created_at 
            FROM variants 
            WHERE ext_id = ?
        """)
        .param(extId)
        .query { rs, _ ->
            Variant(
                id = rs.getLong("id"),
                productId = rs.getLong("product_id"),
                extId = rs.getLong("ext_id"),
                title = rs.getString("title"),
                sku = rs.getString("sku"),
                price = rs.getBigDecimal("price"),
                available = rs.getBoolean("available"),
                createdAt = rs.getTimestamp("created_at").toLocalDateTime()
            )
        }
        .optional()
        .orElse(null)
    }
    
    override fun save(variant: Variant): Variant {
        val keyHolder = GeneratedKeyHolder()
        
        jdbcClient.sql("""
            INSERT INTO variants (product_id, ext_id, title, sku, price, available, created_at) 
            VALUES (?, ?, ?, ?, ?, ?, ?)
        """)
        .param(variant.productId)
        .param(variant.extId)
        .param(variant.title)
        .param(variant.sku)
        .param(variant.price)
        .param(variant.available)
        .param(variant.createdAt)
        .update(keyHolder, "id")
        
        val generatedId = keyHolder.keys?.get("id") as Long? ?: throw RuntimeException("Failed to get generated ID")
        return variant.copy(id = generatedId)
    }
    
    override fun update(variant: Variant): Variant {
        jdbcClient.sql("""
            UPDATE variants 
            SET title = ?, sku = ?, price = ?, available = ? 
            WHERE id = ?
        """)
        .param(variant.title)
        .param(variant.sku)
        .param(variant.price)
        .param(variant.available)
        .param(variant.id)
        .update()
        
        return variant
    }
    
    override fun deleteByProductId(productId: Long) {
        jdbcClient.sql("DELETE FROM variants WHERE product_id = ?")
        .param(productId)
        .update()
    }
}