@file:JvmName(name = "Exceptions")

package com.slavikjunior.liba.exceptions

import java.sql.SQLException

class DbAccessException(message: String) : SQLException(message)

class DbConnectionException(message: String) : SQLException(message)

class DriverNotFoundException(message: String) : ClassNotFoundException(message)

class ExecuteSQLException(message: String) : SQLException(message)

class NotNullableColumnException(message: String) : SQLException(message)

