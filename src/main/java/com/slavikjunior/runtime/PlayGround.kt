package com.slavikjunior.runtime

import com.slavikjunior.models.Person
import com.slavikjunior.orm.Manager

fun main() {
    val person = Manager.getById(Person::class.java, 1002)
    println(person)
    val personIdDeleted = Manager.deleteByEntity(person as Person)
    println("$person is deleted?: $personIdDeleted")
}