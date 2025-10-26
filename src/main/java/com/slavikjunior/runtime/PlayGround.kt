package com.slavikjunior.runtime

import com.slavikjunior.liba.orm.EntityManager
import com.slavikjunior.models.Person

fun main() {
    val person = Person(1039, "Sherlock", "Holms", "XXX", "male", null, "uk")
    val _person = Person(1040, "Moriarti", null, null, null, null, null)
    val isCreatedTwo = EntityManager.create(person) && EntityManager.create(_person)
    println("createdTwo: $isCreatedTwo")
    val foundedPerson = EntityManager.get(Person::class.java, 1039)
    val _foundedPerson = EntityManager.get(Person::class.java, 1040)
    println("founded1: $foundedPerson")
    println("founded2: $_foundedPerson")
    val isUpdatedTwo = EntityManager.update(Person::class.java, 1039, mapOf("email" to null)) as Boolean &&
            EntityManager.update(Person::class.java, 1040, mapOf("gender" to "male")) as Boolean
    println("updatedTwo: $isUpdatedTwo")
    val idDeletedTwo = EntityManager.delete(Person::class.java, 1039) && EntityManager.delete(Person::class.java, 1040)
    println("idDeletedTwo: $idDeletedTwo")
}