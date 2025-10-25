package com.slavikjunior.runtime

import com.slavikjunior.liba.orm.EntityManager
import com.slavikjunior.models.Person

fun main() {
    val person = Person(
        1019, "Monya", "Sonya",
        "mon@ya.com", "female", null, null
    )
    val isCreated = EntityManager.create(person)
    println("isCreated: $isCreated")
    val foundedPerson = EntityManager.get(Person::class.java, 1019)
    println("foundedPerson: $foundedPerson")
    val updatedPerson = EntityManager.update(
        Person::class.java, 1019, mapOf(
            "firstName" to "Tonya", "lastName" to "Monya", "ipaddress" to "192.168.31.121",
        ), true
    )
    println("updatedPerson: $updatedPerson")

    val isDeleted = EntityManager.delete(Person::class.java, 1019)
    println("isDeleted: $isDeleted")
}