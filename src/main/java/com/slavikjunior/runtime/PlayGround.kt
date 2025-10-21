package com.slavikjunior.runtime

import com.slavikjunior.models.Person
import com.slavikjunior.orm.Manager

fun main() {
    val isCreated = Manager.create(Person(
        id = 1002,
        firstName = "Sam",
        lastName = "Altman",
        email = "open@ai.com",
        gender = "male",
        ipAddress = "localhost",
        country = "russia"
    ))
    println(isCreated)
}