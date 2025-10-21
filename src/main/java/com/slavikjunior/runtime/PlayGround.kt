package com.slavikjunior.runtime

import com.slavikjunior.models.Person
import com.slavikjunior.orm.CRUDer

fun main() {
    val CRUDer = CRUDer()
    val person = CRUDer.updateAndGet(Person::class.java, 1012, mapOf("country" to "usa"))
    println(person)
}