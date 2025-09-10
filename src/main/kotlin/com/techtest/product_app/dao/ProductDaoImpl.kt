package com.techtest.product_app.dao

import com.techtest.product_app.model.Product
import com.techtest.product_app.util.Constants.ProductColumns
import com.techtest.product_app.util.Constants.ParamNames
import org.springframework.jdbc.core.simple.JdbcClient
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.stereotype.Repository
import java.sql.ResultSet

@Repository
class ProductDaoImpl(private val jdbcClient: JdbcClient) : ProductDao {
    
    override fun findAll(): List<Product> {
        return jdbcClient.sql(FIND_ALL_SQL)
        .query(PRODUCT_ROW_MAPPER)
        .list()
    }
    
    override fun findById(id: Long): Product? {
        return jdbcClient.sql(FIND_BY_ID_SQL)
        .params(mapOf(ProductColumns.ID to id))
        .query(PRODUCT_ROW_MAPPER)
        .optional()
        .orElse(null)
    }
    
    override fun findByExtId(extId: Long): Product? {
        return jdbcClient.sql(FIND_BY_EXT_ID_SQL)
        .params(mapOf(ParamNames.EXT_ID to extId))
        .query(PRODUCT_ROW_MAPPER)
        .optional()
        .orElse(null)
    }
    
    override fun save(product: Product): Product {
        val keyHolder = GeneratedKeyHolder()
        
        jdbcClient.sql(INSERT_SQL)
        .params(mapOf(
            ParamNames.EXT_ID to product.extId,
            ProductColumns.TITLE to product.title,
            ProductColumns.HANDLE to product.handle,
            ProductColumns.VENDOR to product.vendor,
            ParamNames.CREATED_AT to product.createdAt
        ))
        .update(keyHolder, ProductColumns.ID)
        
        val generatedId = keyHolder.keys?.get(ProductColumns.ID) as Long? ?: throw RuntimeException("Failed to get generated ID")
        return product.copy(id = generatedId)
    }
    
    override fun update(product: Product): Product {
        jdbcClient.sql(UPDATE_SQL)
        .params(mapOf(
            ProductColumns.TITLE to product.title,
            ProductColumns.HANDLE to product.handle,
            ProductColumns.VENDOR to product.vendor,
            ProductColumns.ID to product.id
        ))
        .update()
        
        return product
    }
    
    override fun deleteById(id: Long) {
        jdbcClient.sql(DELETE_BY_ID_SQL)
        .params(mapOf(ProductColumns.ID to id))
        .update()
    }

    companion object {
        private const val FIND_ALL_SQL = """
            SELECT id, ext_id, title, handle, vendor, created_at 
            FROM products 
            ORDER BY created_at DESC
        """

        private const val FIND_BY_ID_SQL = """
            SELECT id, ext_id, title, handle, vendor, created_at 
            FROM products 
            WHERE id = :id
        """

        private const val FIND_BY_EXT_ID_SQL = """
            SELECT id, ext_id, title, handle, vendor, created_at 
            FROM products 
            WHERE ext_id = :extId
        """

        private const val INSERT_SQL = """
            INSERT INTO products (ext_id, title, handle, vendor, created_at) 
            VALUES (:extId, :title, :handle, :vendor, :createdAt)
        """

        private const val UPDATE_SQL = """
            UPDATE products 
            SET title = :title, handle = :handle, vendor = :vendor 
            WHERE id = :id
        """

        private const val DELETE_BY_ID_SQL = "DELETE FROM products WHERE id = :id"

        private val PRODUCT_ROW_MAPPER: (ResultSet, Int) -> Product = { resultSet, _ ->
            Product(
                id = resultSet.getLong(ProductColumns.ID),
                extId = resultSet.getLong(ProductColumns.EXT_ID),
                title = resultSet.getString(ProductColumns.TITLE),
                handle = resultSet.getString(ProductColumns.HANDLE),
                vendor = resultSet.getString(ProductColumns.VENDOR),
                createdAt = resultSet.getTimestamp(ProductColumns.CREATED_AT).toLocalDateTime()
            )
        }
    }
}