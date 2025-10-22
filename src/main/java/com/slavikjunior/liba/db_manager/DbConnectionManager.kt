package com.slavikjunior.liba.db_manager

import com.slavikjunior.liba.exceptions.DbAccessException
import com.slavikjunior.liba.exceptions.DbConnectionException
import com.slavikjunior.liba.exceptions.DriverNotFoundException
import java.sql.Connection
import java.sql.DriverManager
import java.sql.SQLException

class DbConnectionManager(
    private val dataBaseName: String,
    private val user: String,
    private val password: String
) {

    private var connection: Connection = getConnectionOnLocalhostAndStandardPort()
    private var host: String? = null
    private var port: String? = null

    @Throws(DbConnectionException::class)
    fun useConnection(host: String = "localhost", port: String = "5432"): Connection {
        try {
            if (!connectionIsAlive()) {
                if (this.host == null && this.port == null)
                    return getConnection(host, port)
                else
                    return getConnectionOnLocalhostAndStandardPort()
            }
            return connection
        } catch (e: SQLException) {
            throw DbConnectionException("Miss database connection or database access error occurs")
        }
    }

    @Throws(DbConnectionException::class)
    fun useConnectionOnLocalHostAndStandardPort(): Connection {
        try {
            if (!connectionIsAlive())
                return getConnectionOnLocalhostAndStandardPort()
            return connection
        } catch (e: SQLException) {
            throw DbConnectionException("Miss database connection or database access error occurs")
        }
    }

    @Throws(DbAccessException::class, DriverNotFoundException::class)
    fun closeConnection(connection: Connection) {
        try {
            connection.close()
        } catch (e: SQLException) {
            throw DbAccessException("Database access error occurs")
        }
    }

    // todo сделать приватным
    @Throws(DbAccessException::class)
    fun getConnection(host: String = "localhost", port: String = "5432"): Connection {
        try {
            Class.forName("org.postgresql.Driver")
        } catch (e: ClassNotFoundException) {
            throw DriverNotFoundException("Driver postgresql not found")
        }
        try {
            connection = DriverManager.getConnection(
                "jdbc:postgresql://$host:$port/$dataBaseName", user, password
            )
            setUpHostAndPort(host, port)
            return connection
        } catch (e: SQLException) {
            throw DbAccessException("Database access error occurs or the url is null")
        }
    }

    @Throws(DbAccessException::class, DriverNotFoundException::class)
    private fun getConnectionOnLocalhostAndStandardPort(): Connection {
        try {
            Class.forName("org.postgresql.Driver")
        } catch (e: ClassNotFoundException) {
            throw DriverNotFoundException("Driver postgresql not found")
        }
        try {
            setUpHostAndPort("localhost", "5432")
            connection = DriverManager.getConnection(
                "jdbc:postgresql://$host:$port/$dataBaseName", user, password
            )
            return connection
        } catch (e: SQLException) {
            throw DbAccessException("Database access error occurs or the url is null")
        }
    }

    private fun setUpHostAndPort(host: String, port: String) {
        if (this.host == null && this.port == null) {
            this.host = host
            this.port = port
        }
    }

    @Throws(DbAccessException::class)
    private fun connectionIsAlive(): Boolean {
        try {
            return !connection.isClosed
        } catch (e: SQLException) {
            throw DbAccessException("Database access error occurs")
        }
    }
}