package com.slavikjunior.runtime

import com.slavikjunior.liba.db_manager.DbConnectionManager
import com.slavikjunior.liba.orm.EntityManager
import com.slavikjunior.models.Person

fun main() {
    val azazin = EntityManager.get(
        entityClass = Person::class.java,
        columnsToValues = mapOf(
            "id" to "1018", "gender" to null,
            "ipaddress" to "localhost",
            "firstname" to "Azazin"
        )
    )
    println(azazin)
}