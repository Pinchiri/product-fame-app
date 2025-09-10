package com.techtest.product_app.config

import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.context.annotation.Configuration
import java.sql.DriverManager
import jakarta.annotation.PostConstruct
import java.sql.Connection

@Configuration
class DatabaseInitializer(
    @param:Value("\${spring.datasource.username}") private val postgresUser: String,
    @param:Value("\${spring.datasource.password}") private val postgresPassword: String,
    @param:Value("\${spring.datasource.url}") private val datasourceUrl: String
) {
    
    private val logger = LoggerFactory.getLogger(DatabaseInitializer::class.java)
    
    // Extract database name from the configured datasource URL
    private val targetDatabase: String by lazy {
        datasourceUrl.substringAfterLast('/')
    }
    
    companion object {
        private const val POSTGRES_DEFAULT_URL = "jdbc:postgresql://localhost:5432/postgres"
    }
    
    @PostConstruct
    fun initDatabase() {
        createDatabaseIfNotExists()
    }
    
    private fun createDatabaseIfNotExists() {
        try {
            logger.info("Checking if database '$targetDatabase' exists...")
            
            DriverManager.getConnection(POSTGRES_DEFAULT_URL, postgresUser, postgresPassword).use { connection ->
                val checkSql = "SELECT 1 FROM pg_database WHERE datname = ?"
                connection.prepareStatement(checkSql).use { statement ->
                    statement.setString(1, targetDatabase)
                    val resultSet = statement.executeQuery()
                    
                    if (!resultSet.next()) {
                        logger.info("Database '$targetDatabase' does not exist. Creating it...")
                        createDatabase(connection)
                    } else {
                        logger.info("Database '$targetDatabase' already exists.")
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
            val createSql = "CREATE DATABASE \"$targetDatabase\""
            connection.createStatement().use { statement ->
                statement.execute(createSql)
            }
            logger.info("Successfully created database '$targetDatabase'")
        } catch (e: Exception) {
            logger.error("Failed to create database '$targetDatabase'", e)
            throw e
        }
    }
}