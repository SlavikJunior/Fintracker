package com.slavikjunior.runtime

import com.slavikjunior.models.Person
import com.slavikjunior.orm.Manager

fun main() {
    val person = Person(
        id = 1011,
        firstName = "Dima",
        lastName = "Toporov",
        email = "dimooon@gmail.com",
        gender = "male",
        ipAddress = "192.168.0.11",
        country = "russia"
    )
    val isCreated = Manager.createEntity(person)
    println("$person создан?: $isCreated")
    val personByID = Manager.getEntityById(Person::class.java, 1011)
    println("Получили из бд по id: $personByID")
    val personByWhere = Manager.getEntityByPairsOfColumnsAndValues(
        Person::class.java,
        mapOf("id" to 1011, "firstname" to "Dima")
        )
    println("Получили из бд по where: $personByID")
    println("Все три сущности равны?: ${person == personByID && person == personByWhere && personByID == personByWhere}")
}