package com.slavikjunior.runtime

import com.slavikjunior.liba.orm.EntityManager
import com.slavikjunior.models.Person

fun main() {
    val person = Person(
        1019, "Maxim",
        "Crugo", "azaazaaz@za.com",
        null, "",
        "katalina"
    )
    val isCreated = EntityManager.create(person)
    println("Maxim is created: $isCreated")
    val max = EntityManager.get(Person::class.java, mapOf(
        "firstName" to "Maxim", "gender" to null,
    ))
    println("Maxim is finded: $max")
    val azazin = EntityManager.update(Person::class.java, 1019, mapOf(
        "firstName" to "Azazin", "gender" to "jaba",
    ), true)
    println("Maxim is updated to Azazin: $azazin")
    val maxIsDeleted = EntityManager.delete(azazin as Person)
    println("Azazin is deleted: $maxIsDeleted")
}