package com.slavikjunior.runtime

import com.slavikjunior.annotations.Id
import com.slavikjunior.models.Person
import com.slavikjunior.orm.CRUDable
import com.slavikjunior.orm.Manager
import com.slavikjunior.orm.Wrapper
import com.slavikjunior.util.toAnnotatedFieldsValuesListWithoutAnnotation
import java.lang.management.ManagementFactory

fun main() {

    val newPerson = Person(1010, "Sasha", "Bublic", "sbublic@mail.com", "female", "", "")
    Manager.create(newPerson)
    val newPersonFromDB = Manager.readWithColumnAndValue(Person::class.java.newInstance(), Wrapper(
        columnName = "firstname",
        stringValue = "Sasha",
        isContainsStringValue = true
    ))
    println("Созданная сущность: $newPerson")
    println("Полученная сущность: $newPersonFromDB")
    println("Созданная и полученная сущности равны?: ${newPerson == newPersonFromDB}")
}