package com.slavikjunior.dao

import com.slavikjunior.db_manager.getConnection
import java.sql.Connection
import java.sql.PreparedStatement
import java.sql.SQLException

private const val USER_DATABASE_NAME = "users"
val connection = getConnection(USER_DATABASE_NAME)

// todo почитать про исключения в котлине
//@Throws(SQLException::class)
fun userIsExists(login: String): Boolean {
    if (!isConnectionEstablished())
        throw SQLException()
    connection as Connection
    val ps: PreparedStatement = connection.prepareStatement("SELECT 1 FROM users WHERE login=?")
    ps.setString(1, login)
    val rs = ps.executeQuery()
    return rs.next()
}

//@Throws(SQLException::class)
fun createUser(login: String, password: String, email: String) {
    if (!isConnectionEstablished())
        throw SQLException()
    connection as Connection
    val ps: PreparedStatement = connection.prepareStatement("INSERT INTO users (login, password, email) VALUES (?, ?, ?)")
    ps.setString(1, login)
    ps.setString(2, password)
    ps.setString(3, email)
    ps.executeUpdate()
}

//@Throws(SQLException::class)
fun checkCredentials(username: String, password: String): Boolean {
    if (!isConnectionEstablished())
        throw SQLException()
    connection as Connection
    val ps: PreparedStatement = connection.prepareStatement("SELECT * FROM users WHERE username=? AND password=?")
    ps.setString(1, username)
    ps.setString(2, password)
    val rs = ps.executeQuery()
    return rs.next()
}

private fun isConnectionEstablished() = if (connection != null) true else false