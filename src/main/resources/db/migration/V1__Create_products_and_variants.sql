-- Create products table
CREATE TABLE products (
    id BIGSERIAL PRIMARY KEY,
    ext_id BIGINT UNIQUE NOT NULL,           -- JSON: "id": 14976808092031
    title TEXT NOT NULL,                     -- JSON: "title": "Wrap Cardigan"
    handle TEXT NOT NULL,                    -- JSON: "handle": "wrap-cardigan"
    vendor TEXT,                             -- JSON: "vendor": "Famme"
    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP
);

-- Create variants table with foreign key to products
CREATE TABLE variants (
    id BIGSERIAL PRIMARY KEY,
    product_id BIGINT NOT NULL REFERENCES products(id) ON DELETE CASCADE,
    ext_id BIGINT NOT NULL,                  -- JSON: "id": 55187702776191
    title TEXT NOT NULL,                     -- JSON: "title": "White / XS"
    sku TEXT,                                -- JSON: "sku": "WRCN-WH-XS"
    price DECIMAL(10,2) NOT NULL,            -- JSON: "price": "599.00" -> 599.00
    available BOOLEAN DEFAULT true,          -- JSON: "available": true
    created_at TIMESTAMPTZ DEFAULT CURRENT_TIMESTAMP
);

-- Create index on product_id for better JOIN performance
CREATE INDEX idx_variants_product_id ON variants(product_id);

-- Create index on ext_id for both tables for API lookups
CREATE INDEX idx_products_ext_id ON products(ext_id);
CREATE INDEX idx_variants_ext_id ON variants(ext_id);