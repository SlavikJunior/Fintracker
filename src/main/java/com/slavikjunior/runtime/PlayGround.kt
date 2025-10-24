package com.slavikjunior.runtime

import com.slavikjunior.liba.orm.EntityManager
import com.slavikjunior.models.Person

fun main() {
    val azazin = EntityManager.update(Person::class.java, 1018, mapOf(
        "firstName" to "Azazin", "lastName" to "Jaba", "ipaddress" to "localhost", "country" to "russia"
    ), true)
    println("Azazin: $azazin")
}