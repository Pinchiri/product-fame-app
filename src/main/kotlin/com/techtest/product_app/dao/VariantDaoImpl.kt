package com.techtest.product_app.dao

import com.techtest.product_app.model.Variant
import com.techtest.product_app.util.Constants.VariantColumns
import com.techtest.product_app.util.Constants.ParamNames
import org.springframework.jdbc.core.simple.JdbcClient
import org.springframework.jdbc.support.GeneratedKeyHolder
import org.springframework.stereotype.Repository

@Repository
class VariantDaoImpl(private val jdbcClient: JdbcClient) : VariantDao {
    
    companion object {
        private const val FIND_BY_PRODUCT_ID_SQL = """
            SELECT id, product_id, ext_id, title, sku, price, available, created_at 
            FROM variants 
            WHERE product_id = :productId
            ORDER BY created_at ASC
        """
        
        private const val FIND_BY_EXT_ID_SQL = """
            SELECT id, product_id, ext_id, title, sku, price, available, created_at 
            FROM variants 
            WHERE ext_id = :extId
        """
        
        private const val INSERT_SQL = """
            INSERT INTO variants (product_id, ext_id, title, sku, price, available, created_at) 
            VALUES (:productId, :extId, :title, :sku, :price, :available, :createdAt)
        """
        
        private const val UPDATE_SQL = """
            UPDATE variants 
            SET title = :title, sku = :sku, price = :price, available = :available 
            WHERE id = :id
        """
        
        private const val DELETE_BY_PRODUCT_ID_SQL = "DELETE FROM variants WHERE product_id = :productId"
        
        private val VARIANT_ROW_MAPPER: (java.sql.ResultSet, Int) -> Variant = { resultSet, _ ->
            Variant(
                id = resultSet.getLong(VariantColumns.ID),
                productId = resultSet.getLong(VariantColumns.PRODUCT_ID),
                extId = resultSet.getLong(VariantColumns.EXT_ID),
                title = resultSet.getString(VariantColumns.TITLE),
                sku = resultSet.getString(VariantColumns.SKU),
                price = resultSet.getBigDecimal(VariantColumns.PRICE),
                available = resultSet.getBoolean(VariantColumns.AVAILABLE),
                createdAt = resultSet.getTimestamp(VariantColumns.CREATED_AT).toLocalDateTime()
            )
        }
    }
    
    override fun findByProductId(productId: Long): List<Variant> {
        return jdbcClient.sql(FIND_BY_PRODUCT_ID_SQL)
        .params(mapOf(ParamNames.PRODUCT_ID to productId))
        .query(VARIANT_ROW_MAPPER)
        .list()
    }
    
    override fun findByExtId(extId: Long): Variant? {
        return jdbcClient.sql(FIND_BY_EXT_ID_SQL)
        .params(mapOf(ParamNames.EXT_ID to extId))
        .query(VARIANT_ROW_MAPPER)
        .optional()
        .orElse(null)
    }
    
    override fun save(variant: Variant): Variant {
        val keyHolder = GeneratedKeyHolder()
        
        jdbcClient.sql(INSERT_SQL)
        .params(mapOf(
            ParamNames.PRODUCT_ID to variant.productId,
            ParamNames.EXT_ID to variant.extId,
            VariantColumns.TITLE to variant.title,
            VariantColumns.SKU to variant.sku,
            VariantColumns.PRICE to variant.price,
            VariantColumns.AVAILABLE to variant.available,
            ParamNames.CREATED_AT to variant.createdAt
        ))
        .update(keyHolder, VariantColumns.ID)
        
        val generatedId = keyHolder.keys?.get(VariantColumns.ID) as Long? ?: throw RuntimeException("Failed to get generated ID")
        return variant.copy(id = generatedId)
    }
    
    override fun update(variant: Variant): Variant {
        jdbcClient.sql(UPDATE_SQL)
        .params(mapOf(
            VariantColumns.TITLE to variant.title,
            VariantColumns.SKU to variant.sku,
            VariantColumns.PRICE to variant.price,
            VariantColumns.AVAILABLE to variant.available,
            VariantColumns.ID to variant.id
        ))
        .update()
        
        return variant
    }
    
    override fun deleteByProductId(productId: Long) {
        jdbcClient.sql(DELETE_BY_PRODUCT_ID_SQL)
        .params(mapOf(ParamNames.PRODUCT_ID to productId))
        .update()
    }
}