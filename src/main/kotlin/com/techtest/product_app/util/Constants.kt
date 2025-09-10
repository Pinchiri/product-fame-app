package com.techtest.product_app.util

object Constants {
    
    // Product table column names
    object ProductColumns {
        const val ID = "id"
        const val EXT_ID = "ext_id"
        const val TITLE = "title"
        const val HANDLE = "handle"
        const val VENDOR = "vendor"
        const val CREATED_AT = "created_at"
    }
    
    // Variant table column names
    object VariantColumns {
        const val ID = "id"
        const val PRODUCT_ID = "product_id"
        const val EXT_ID = "ext_id"
        const val TITLE = "title"
        const val SKU = "sku"
        const val PRICE = "price"
        const val AVAILABLE = "available"
        const val CREATED_AT = "created_at"
    }
    
    // Query parameter names
    object ParamNames {
        const val ID = "id"
        const val EXT_ID = "extId"
        const val PRODUCT_ID = "productId"
        const val CREATED_AT = "createdAt"
    }
    
    // Template paths
    object Templates {
        const val INDEX = "index"
        const val PRODUCT_ROWS_FRAGMENT = "fragments/product-rows :: rows"
    }
    
    // Model attribute names
    object ModelAttributes {
        const val ROWS = "rows"
    }
    
    // General constants
    object General {
        const val DEFAULT_VENDOR = "Manual Entry"
        const val SEPARATOR = "-"
        const val SPACE = " "
    }
}