package com.techtest.product_app.config

import org.slf4j.LoggerFactory
import org.springframework.context.annotation.Configuration
import java.sql.DriverManager
import jakarta.annotation.PostConstruct
import java.sql.Connection

@Configuration
class DatabaseInitializer {
    
    private val logger = LoggerFactory.getLogger(DatabaseInitializer::class.java)
    
    companion object {
        private const val POSTGRES_DEFAULT_URL = "jdbc:postgresql://localhost:5432/postgres"
        private const val TARGET_DATABASE = "product-db"
        private const val POSTGRES_USER = "postgres"
        private const val POSTGRES_PASSWORD = "postgres"
    }
    
    @PostConstruct
    fun initDatabase() {
        createDatabaseIfNotExists()
    }
    
    private fun createDatabaseIfNotExists() {
        try {
            logger.info("Checking if database '$TARGET_DATABASE' exists...")
            
            DriverManager.getConnection(POSTGRES_DEFAULT_URL, POSTGRES_USER, POSTGRES_PASSWORD).use { connection ->
                val checkSql = "SELECT 1 FROM pg_database WHERE datname = ?"
                connection.prepareStatement(checkSql).use { statement ->
                    statement.setString(1, TARGET_DATABASE)
                    val resultSet = statement.executeQuery()
                    
                    if (!resultSet.next()) {
                        logger.info("Database '$TARGET_DATABASE' does not exist. Creating it...")
                        createDatabase(connection)
                    } else {
                        logger.info("Database '$TARGET_DATABASE' already exists.")
                    }
                }
            }
        } catch (e: Exception) {
            logger.error("Error during database initialization", e)
            throw RuntimeException("Failed to initialize database: ${e.message}", e)
        }
    }
    
    private fun createDatabase(connection: Connection) {
        try {
            // PostgreSQL requires autocommit for CREATE DATABASE
            connection.autoCommit = true
            val createSql = "CREATE DATABASE \"$TARGET_DATABASE\""
            connection.createStatement().use { statement ->
                statement.execute(createSql)
            }
            logger.info("Successfully created database '$TARGET_DATABASE'")
        } catch (e: Exception) {
            logger.error("Failed to create database '$TARGET_DATABASE'", e)
            throw e
        }
    }
}