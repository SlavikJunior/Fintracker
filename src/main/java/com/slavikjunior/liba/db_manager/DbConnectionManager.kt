package com.slavikjunior.liba.db_manager

import com.slavikjunior.liba.exceptions.DbAccessException
import com.slavikjunior.liba.exceptions.DriverNotFoundException
import com.slavikjunior.secrets.Keys
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

object DbConnectionManager {
    private var host: String = Keys.host
    private var port: String = Keys.port
    private var databaseName: String = Keys.databaseName

    init {
        loadDriver()
    }

    fun configure(
        host: String = Keys.host,
        port: String = Keys.port,
        databaseName: String = Keys.databaseName
    ) {
        this.host = host
        this.port = port
        this.databaseName = databaseName
    }

    @Throws(DbAccessException::class)
    fun getConnection(): Connection {
        try {
            val url = "jdbc:postgresql://$host:$port/$databaseName"
            val conn = DriverManager.getConnection(url, Keys.user, Keys.password)
            return conn
        } catch (e: SQLException) {
            throw DbAccessException("Failed to connect to DB.", e)
        }
    }

    private fun loadDriver() {
        try {
            Class.forName("org.postgresql.Driver")
        } catch (e: ClassNotFoundException) {
            throw DriverNotFoundException("PostgreSQL JDBC driver not found.")
        }
    }
}