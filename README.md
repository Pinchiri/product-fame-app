# Product Catalog Application

A modern **Product Catalog Application** built with Spring Boot 3.5.5 and Kotlin 2.2.10. This web application fetches, stores, and displays product data from an external API with a dynamic frontend using HTMX for seamless user interactions.

## 🚀 Tech Stack

- **Framework**: Spring Boot 3.5.5 with Spring Web MVC
- **Language**: Kotlin 2.2.10 (K2 compiler enabled)
- **Java**: Java 17 (bytecode target, runtime can be JDK 23/25+)
- **Build Tool**: Gradle 8.14.3 with Kotlin DSL
- **Database**: PostgreSQL with Spring Data JDBC
- **Migrations**: Flyway 11.11.2
- **Frontend**: Thymeleaf + HTMX 2.x + Web Awesome components
- **Testing**: JUnit 5 with Spring Boot Test

## 📋 Prerequisites

Before running this application, make sure you have:

- **Java 17** or higher
- **PostgreSQL** server running on localhost:5432
- **Git** (for cloning the repository)

### PostgreSQL Setup

1. Install PostgreSQL on your system
2. Start PostgreSQL service
3. The application will automatically create the `product-db` database on first run
4. Default credentials used:
   - Username: `postgres`
   - Password: `postgres`
   - Port: `5432`

> **Note**: You can customize database credentials in `src/main/resources/application.yaml`

## 🛠️ Getting Started

### 1. Clone the Repository

```bash
git clone <repository-url>
cd product-app
```

### 2. Build the Application

```bash
./gradlew build
```

### 3. Run the Application

```bash
./gradlew bootRun
```

The application will start on `http://localhost:8080`

### 4. Verify Installation

- The application automatically fetches and imports up to 50 products from `https://famme.no/products.json` on startup
- Database migrations are applied automatically via Flyway
- Check the logs to ensure successful startup and data import

## 📁 Project Structure

```
src/main/kotlin/com/techtest/product_app/
├── ProductAppApplication.kt           # Main Spring Boot application
├── controller/                       # Web controllers (Thymeleaf + HTMX)
│   └── ProductController.kt          
├── service/                          # Business logic layer
│   ├── ProductService.kt             # Interface
│   └── ProductServiceImpl.kt         # Implementation with RestClient
├── job/                              # Scheduled tasks
│   └── ProductImportJob.kt           # Product import from external API
├── dao/                              # Data access layer
│   ├── ProductDao.kt                 # Product DAO interface
│   ├── ProductDaoImpl.kt             # Product CRUD with JdbcClient
│   ├── VariantDao.kt                 # Variant DAO interface  
│   └── VariantDaoImpl.kt             # Variant CRUD with JdbcClient
├── dto/                              # Data Transfer Objects
│   ├── ProductDto.kt                 # External API response mapping
│   └── VariantDto.kt                 # Product variant representation
├── model/                            # Database entities
│   ├── Product.kt                    # @Table("products")
│   └── Variant.kt                    # @Table("variants") 
└── config/                           # Configuration classes
    ├── DatabaseInitializer.kt        # Auto database creation
    └── RestClientConfig.kt           # HTTP client configuration

src/main/resources/
├── application.yaml                  # Application configuration
└── db/migration/                     # Flyway SQL migrations
    └── V1__Create_products_and_variants.sql
```

## 🗄️ Database Schema

The application uses a normalized relational design:

- **products** table: `id`, `ext_id` (unique), `title`, `handle`, `vendor`, `created_at`
- **variants** table: `id`, `product_id` (FK), `ext_id`, `title`, `sku`, `price` (DECIMAL), `available`, `created_at`

## 🔧 Configuration

### Database Configuration

Edit `src/main/resources/application.yaml`:

```yaml
spring:
  datasource:
    url: jdbc:postgresql://localhost:5432/product-db
    username: postgres
    password: postgres
```

### External API Configuration

The application fetches products from `https://famme.no/products.json` by default. To change this, modify:

```kotlin
// In ProductServiceImpl.kt
private const val EXTERNAL_API_URL = "https://your-api-endpoint.com/products.json"
private const val MAX_PRODUCTS_TO_IMPORT = 50
```

## 🚀 Development Commands

```bash
# Build the application
./gradlew build

# Run the application
./gradlew bootRun

# Run tests
./gradlew test

# Clean build artifacts  
./gradlew clean

# Check for dependency updates
./gradlew dependencyUpdates
```

## 🧪 Testing

Run the test suite:

```bash
./gradlew test
```

## 📝 Features

- ✅ **Automatic Database Setup**: Creates database and runs migrations on startup
- ✅ **External API Integration**: Fetches products from external JSON API
- ✅ **Scheduled Data Import**: Automatic product import on application start
- ✅ **RESTful Architecture**: Clean separation of concerns with DAO pattern
- ✅ **Modern HTTP Client**: Uses Spring Boot 3.2+ RestClient
- ✅ **Database Migrations**: Flyway for version-controlled schema changes
- ✅ **HTMX Integration**: Dynamic frontend without JavaScript complexity

## 🤝 Contributing

1. Fork the repository
2. Create your feature branch (`git checkout -b feature/amazing-feature`)
3. Commit your changes (`git commit -m 'Add some amazing feature'`)
4. Push to the branch (`git push origin feature/amazing-feature`)
5. Open a Pull Request

## 📄 License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.

---

**Built with ❤️ using Spring Boot 3.5.5 and Kotlin 2.2.10**