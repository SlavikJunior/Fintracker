package com.slavikjunior.runtime

import com.slavikjunior.models.Person
import com.slavikjunior.orm.Manager

fun main() {
//    val person = Person(
//        id = 1014,
//        firstName = "Tom",
//        lastName = "Soller",
//        email = "tom@gmail.com",
//        gender = "male",
//        ipAddress = "192.168.0.111",
//        country = "usa"
//    )
//    val isCreated = Manager.createEntity(person)
//    println("$person создан?: $isCreated")
//    val personByID = Manager.getEntityById(Person::class.java, 1014)
//    println("Получили из бд по id: $personByID")
//    val personByWhere = Manager.getEntityByPairsOfColumnsAndValues(
//        Person::class.java,
//        mapOf("id" to 1014, "firstname" to "Tom")
//        )
//    println("Получили из бд по where: $personByID")
//    println("Все три сущности равны?: ${person == personByID && person == personByWhere && personByID == personByWhere}")
//    val isUpdated = Manager.update(Person::class.java, 1012, mapOf(
//        "email" to "test@fincher.com",
//    ))
//    println("1012 обновлён?: $isUpdated")

    val updatedPerson = Manager.updateEntityAndGet(Person::class.java, 1002, mapOf(
        "firstName" to "Sam", "lastname" to "Altman", "email" to "open@ai.com", "ipaddress" to "127.0.0.1", "country" to "usa"
    ))
    println(updatedPerson)
}