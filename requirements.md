# Product Catalog App Requirements

## Tech Stack

### Core Framework
- **Spring Boot 3.5.5** (latest stable)
- **Kotlin 2.2.10** (latest stable with K2 compiler)
- **Gradle 9.0.0** (latest stable with Kotlin DSL)
- **JDK 17** (bytecode target) running on **JDK 23/25+** (runtime)

### Database
- **PostgreSQL** with **JDBC Driver 42.7.7**
- **Flyway 11.x** for database migrations
- **JdbcClient** (new in Spring Boot 3) for database access

### Frontend
- **Thymeleaf** for server-side rendering
- **HTMX 2.0.6** for dynamic updates without page reload
- **Web Awesome** (Web Components) for UI components
- **Design tokens** from Web Awesome for consistent styling (colors, spacing, etc.)

### Scheduled Jobs
- **@Scheduled** with `initialDelay=0` for immediate startup execution

## Application Features

### Core Functionality
1. **Product Display Page** with:
   - Header section
   - "Load Products" button
   - Products table (appears after loading)
   - "Add Product" form (appears after table is loaded)

2. **Dynamic Interactions** (using HTMX):
   - Button click fetches products from database → updates table without page reload
   - Form submission adds new product → updates table without page reload

3. **Data Population**:
   - Scheduled job fetches from `https://famme.no/products.json`
   - Limits to **50 products maximum**
   - Runs immediately on application startup (`initialDelay=0`)

## Database Design

### Option A: Normalized Tables (Recommended)
```sql
products(
  id BIGSERIAL PK, 
  ext_id TEXT UNIQUE, 
  title TEXT, 
  handle TEXT, 
  created_at TIMESTAMPTZ
)

variants(
  id BIGSERIAL PK, 
  product_id BIGINT FK, 
  ext_id TEXT, 
  sku TEXT, 
  price_cents INT
)
```

### Data Strategy
- **Limit fields**: Choose 3-5 most relevant fields from JSON response
- **One-to-many relationship**: products → variants
- **Clean SQL operations**: JOINs, counts, easy listing/adding

## UI Requirements

### Components (Web Awesome)
- Styled buttons for actions
- Professional table layout
- Form components
- Consistent design tokens for colors and spacing

### User Experience
- Clean, professional appearance
- No full page reloads (HTMX-powered)
- Immediate feedback on actions
- Responsive design

## Technical Implementation Notes

### Database Access
- Use **JdbcClient** (Spring Boot 3.x feature)
- Flyway migrations for schema setup
- Foreign key relationships for data integrity

### API Integration
- Scheduled job fetches from external API
- Transform JSON to database format
- Handle one product → multiple variants mapping

### HTMX Integration
- `hx-get` for loading products
- `hx-post` for adding products
- Target-specific table updates
- No JavaScript required

## Development Priorities

1. **Latest stable versions** for all dependencies
2. **Clean database design** with proper relationships  
3. **Modern Spring Boot features** (JdbcClient, etc.)
4. **Professional UI** with Web Awesome components
5. **Smooth UX** with HTMX interactions