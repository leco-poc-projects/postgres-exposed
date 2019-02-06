package co.l3co.configuration

import com.zaxxer.hikari.HikariDataSource

val DRIVER_CLASS_NAME = System.getenv("DRIVER_CLASS_NAME") ?: "org.postgresql.Driver"
val DB_URL = System.getenv("DB_URL") ?: "jdbc:postgresql://localhost:5432/hikari-post"
val DB_USER = System.getenv("DB_USER") ?: "sa"
val DB_PASSWORD = System.getenv("DB_PASSWORD") ?: "sa"
val DB_MAX_POOL = System.getenv("DB_MAX_POOL")?.toInt() ?: 10


fun hikariDataSource(name: String) = HikariDataSource().apply {
    poolName = name
    driverClassName = DRIVER_CLASS_NAME
    jdbcUrl = DB_URL
    username = DB_USER
    password = DB_PASSWORD
    connectionTimeout = 1500
    validationTimeout = 1000
    maxLifetime = 1000
    minimumIdle = 1
    maximumPoolSize = DB_MAX_POOL
    transactionIsolation = "TRANSACTION_READ_COMMITTED"
    connectionTestQuery = "SELECT 1"
    addDataSourceProperty("statement_timeout", 60000)
}