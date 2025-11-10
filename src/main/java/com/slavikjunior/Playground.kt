package com.slavikjunior

import com.slavikjunior.deorm.Person
import com.slavikjunior.deorm.db_manager.DbConnectionManager
import com.slavikjunior.deorm.orm.EntityManager
import com.slavikjunior.deorm.orm.QueryBuilder

fun main() {
    DbConnectionManager.initialize()

    val query = QueryBuilder()
        .select("*")
        .from("persons")
        .where("id = 1038")
        .execute()

    val res = EntityManager.executeQuery(query)
    val person = Person(
        id = res.getInt(1),
        firstName = res.getString(2),
        lastName = res.getString(3),
        email = res.getString(4),
        gender = res.getString(5),
        ipAddress = res.getString(6),
        country = res.getString(7)
    )

    println(person)
}