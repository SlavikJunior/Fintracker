package com.slavikjunior.runtime

import com.slavikjunior.liba.db_manager.DbConnectionManager
import com.slavikjunior.models.Person
import com.slavikjunior.liba.orm.CRUDer
import com.slavikjunior.liba.utils.isNullableColumn
import kotlin.reflect.KClass

fun main() {
    val cruder = CRUDer(dbConnectionManager = DbConnectionManager("testdb", "postgres", "7913"))
    val isCreated = cruder.create(Person(firstName = "John", lastName = "Doe"))
    println(isCreated)
}