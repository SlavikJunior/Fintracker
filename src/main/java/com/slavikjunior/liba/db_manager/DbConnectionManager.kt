package com.slavikjunior.liba.db_manager

import com.slavikjunior.liba.exceptions.DbAccessException
import com.slavikjunior.liba.exceptions.DbConnectionException
import com.slavikjunior.liba.exceptions.DriverNotFoundException
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

class DbConnectionManager(
    private val databaseName: String,
    private val user: String,
    private val password: String,
) {
    private val defaultHost = "localhost"
    private val defaultPort = "5432"

    // Храним текущее соединение
    private var connection: Connection? = null

    // Последние успешные параметры подключения
    private var host: String = defaultHost
    private var port: String = defaultPort

    init {
        loadDriver()
    }

    /**
     * Возвращает активное соединение.
     * Если оно закрыто или отсутствует — создаёт новое.
     */
    @JvmOverloads
    @Throws(DbConnectionException::class)
    fun useConnection(host: String = this.host, port: String = this.port): Connection {
        try {
            if (!connectionIsAlive()) {
                this.connection = createConnection(host, port)
                this.host = host
                this.port = port
            }
            return this.connection!!
        } catch (e: SQLException) {
            throw DbConnectionException("Failed to obtain a valid database connection: ${e.message}", e)
        }
    }

    /**
     * Принудительно закрывает соединение (если оно открыто).
     */
    @Throws(DbAccessException::class)
    fun closeConnection() {
        try {
            connection?.let {
                if (!it.isClosed) {
                    it.close()
                    println("✅ Connection closed successfully")
                }
            }
            connection = null
        } catch (e: SQLException) {
            throw DbAccessException("Failed to close database connection: ${e.message}", e)
        }
    }

    /**
     * Проверяет живое ли соединение.
     * isValid(timeout) надёжнее, чем isClosed.
     */
    private fun connectionIsAlive(): Boolean {
        return try {
            connection != null && connection!!.isValid(2)
        } catch (e: SQLException) {
            false
        }
    }

    /**
     * Создаёт новое соединение с заданными параметрами.
     */
    @Throws(DbAccessException::class)
    private fun createConnection(host: String, port: String): Connection {
        return try {
            val url = "jdbc:postgresql://$host:$port/$databaseName"
            val conn = DriverManager.getConnection(url, user, password)
            println("✅ Connected to PostgreSQL database at $host:$port/$databaseName")
            conn
        } catch (e: SQLException) {
            throw DbAccessException("Failed to connect to database at $host:$port/$databaseName: ${e.message}", e)
        }
    }

    /**
     * Загружает драйвер PostgreSQL.
     */
    @Throws(DriverNotFoundException::class)
    private fun loadDriver() {
        try {
            Class.forName("org.postgresql.Driver")
        } catch (e: ClassNotFoundException) {
            throw DriverNotFoundException("PostgreSQL JDBC driver not found. Add 'org.postgresql:postgresql' to dependencies.")
        }
    }
}