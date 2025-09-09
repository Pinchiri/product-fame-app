# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

A **Product Catalog Application** built with modern Spring Boot 3.5.5 and Kotlin 2.2.10. This is a web application designed to fetch, store, and display product data from an external API (famme.no/products.json) with a dynamic frontend using HTMX for seamless user interactions.

## Tech Stack

### Core Framework
- **Spring Boot 3.5.5** with Spring Web MVC
- **Kotlin 2.2.10** (K2 compiler enabled)
- **Java 17** (bytecode target, runtime can be JDK 23/25+)
- **Gradle 8.14.3** with Kotlin DSL

### Database & Persistence  
- **PostgreSQL** (production database)
- **Spring Data JDBC** (not JPA - uses JdbcClient for modern data access)
- **Flyway 11.11.2** for database migrations

### Frontend & UI
- **Thymeleaf** for server-side rendering
- **HTMX 2.x** (`htmx-spring-boot-thymeleaf:4.0.1`) for dynamic updates
- **Web Awesome** components for professional UI styling

### Testing
- **JUnit 5** with Spring Boot Test
- **Kotlin Test** integration

## Essential Development Commands

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

# Database migration (handled automatically on startup via Flyway)
# No separate command needed - migrations run when application starts
```

## Project Structure

```
src/main/kotlin/com/techtest/product_app/
├── ProductAppApplication.kt           # Main Spring Boot application class
├── dto/                              # Data Transfer Objects
│   ├── ProductDto.kt                 # JSON API response mapping
│   └── VariantDto.kt                 # Product variant representation
└── model/                            # Database entities
    ├── Product.kt                    # @Table("products") - main entity
    └── Variant.kt                    # @Table("variants") - related entity

src/main/resources/
├── application.properties            # App configuration (minimal)
└── db/migration/                     # Flyway SQL migrations
    └── V1__Create_products_and_variants.sql

src/test/kotlin/com/techtest/product_app/
└── ProductAppApplicationTests.kt     # Basic integration test
```

## Database Schema

**Normalized relational design** with proper foreign key relationships:

- **products** table: id, ext_id (unique), title, handle, vendor, created_at
- **variants** table: id, product_id (FK), ext_id, title, sku, price_cents, available, created_at

Uses **BIGSERIAL** primary keys and includes performance indexes on ext_id fields for API lookups.

## Key Architecture Patterns

### Data Layer
- **Spring Data JDBC** with JdbcClient (modern Spring Boot 3.x approach)
- **Entity classes** use `@Table` annotations, not JPA
- **Flyway migrations** handle schema versioning automatically
- **Price storage**: Stored as integers in cents to avoid floating-point issues

### Application Layer  
- **Scheduled jobs** with `@Scheduled(initialDelay=0)` for immediate startup execution
- **External API integration** fetching from famme.no/products.json (limited to 50 products)
- **DTO pattern** for clean separation between API responses and database entities

### Presentation Layer
- **Thymeleaf** templates for server-side rendering  
- **HTMX integration** for dynamic updates without full page reloads
- **Web Awesome** design system for consistent, professional styling

## Development Notes

### Package Structure
- Base package: `com.techtest.product_app` (note: underscore, not hyphen)
- Original package name `com.techtest.product-app` was invalid and corrected

### Database Configuration
- Flyway automatically runs migrations on application startup
- PostgreSQL connection configured via Spring Boot properties
- No separate migration command needed - handled by framework

### Missing Components (Implementation Status)
Based on analysis, the following components are **not yet implemented**:
- Controllers/REST endpoints (no @Controller or @RestController found)
- Service layer classes (no @Service annotations found) 
- Repository/DAO classes (no @Repository annotations found)
- Thymeleaf templates (no templates/ directory found)
- Scheduled job implementation (mentioned in requirements but not found)

### External Dependencies
- **htmx-spring-boot-thymeleaf**: Provides HTMX integration with Thymeleaf
- **PostgreSQL driver 42.7.7**: Latest stable JDBC driver
- **Flyway**: Database migration tool with PostgreSQL support
- **Jackson**: For JSON processing (imported in DTOs)

## Development Workflow

1. **Database First**: Schema defined in Flyway migrations
2. **Entity Mapping**: Kotlin data classes map to database tables  
3. **API Integration**: DTOs handle external JSON API responses
4. **HTMX Frontend**: Dynamic UI updates without JavaScript
5. **Gradle Build**: Standard Spring Boot build process

This is a **modern Spring Boot application** following current best practices with Kotlin, JDBC (not JPA), and HTMX for a smooth developer and user experience.

## IMPORTANT EVERY TIME YOU PROCESS A PROMPT
1. Initial Analysis and Planning
   First think through the problem, read the codebase for relevant files, and write a plan to todo.md.
2. Todo List Structure
   The plan should have a list of todo items that you can check off as you complete them.
3. Plan Verification
   Before you begin working, check in with me and I will verify the plan.
4. Task Execution
   Then, begin working on the todo items, marking them as complete as you go.
5. Communication
   Please every step of the way just give me a high level explanation of what changes you made.
6. Simplicity Principle
   Make every task and code change you do as simple as possible. We want to avoid making any massive or complex changes. Every change should impact as little code as possible. Everything is about simplicity.
7. Process Documentation
   Every time you perform actions related to the project, append your actions to activity.md and read that file whenever you find it necessary to assist you. Please include every prompt I give. Add documentation to the code focusing on why is the code being added or for which case instead of reexplaining what it does
8. Review Process
   Finally, add a review section to the todo.md file with a summary of the changes you made and any other relevant information.
9. Git Repository
   Every time you make successful changes please push the changes to the current git repository. Dont add yourself as coauthor in the commit message