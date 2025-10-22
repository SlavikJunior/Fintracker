package com.slavikjunior.runtime

import com.slavikjunior.liba.db_manager.DbConnectionManager
import com.slavikjunior.liba.orm.CRUDer
import com.slavikjunior.models.Person

fun main() {
    val manager = DbConnectionManager("testdb", "postgres", "7913")
    val cruder = CRUDer(dbConnectionManager = manager)
    val isCreated = cruder.create(Person(firstName = "Chill", lastName = "Guy", ipAddress = "127.0.0.1"))
    println(isCreated)
}