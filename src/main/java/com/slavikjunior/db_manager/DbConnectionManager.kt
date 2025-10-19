package com.slavikjunior.db_manager

import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

fun getConnection(
    dataBaseName: String,
    user: String = "postgres",
    // todo пароль убрать потом в синглтон ключей
    password: String = "7913"
): Connection {
    try {
        Class.forName("org.postgresql.Driver")
    } catch (e: ClassNotFoundException) {
        println("Driver postgresql not found")
    }
    try {
        val connection = DriverManager.getConnection(
            "jdbc:postgresql://localhost:5432/$dataBaseName", user, password
        )
        println("Connected to PostgreSQL database successfully")
        return connection
    } catch (e: SQLException) {
        throw SQLException("Database connection error")
    }
}