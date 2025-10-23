@file:JvmName(name = "Exceptions")

package com.slavikjunior.liba.exceptions

import java.sql.SQLException

class DbAccessException(message: String) : SQLException(message) {
    constructor(message: String, cause: Throwable) : this( message + " " + cause.message + " " + cause.cause)
}

class DbConnectionException(message: String) : SQLException(message) {
    constructor(message: String, cause: Throwable) : this( message + " " + cause.message + " " + cause.cause)
}

class DriverNotFoundException(message: String) : ClassNotFoundException(message)

class ExecuteSQLException(message: String) : SQLException(message)

class NotNullableColumnException(message: String) : SQLException(message)

