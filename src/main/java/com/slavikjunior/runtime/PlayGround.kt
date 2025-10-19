package com.slavikjunior.runtime

import com.slavikjunior.annotations.Id
import com.slavikjunior.models.Person
import com.slavikjunior.orm.CRUDable
import com.slavikjunior.orm.Manager
import com.slavikjunior.orm.Wrapper
import com.slavikjunior.util.toAnnotatedFieldsValuesListWithoutAnnotation
import java.lang.management.ManagementFactory

fun main() {

//    val person = Person(4, "Slava", "Romanov", "rvr280206@gmail.com", "male", "", "russia")
//    Manager.create(person, person.toAnnotatedFieldsValuesListWithoutAnnotation(Id::class))
//    val personDb = Manager.read(Person::class.java.newInstance(), 1004)

    val  person = Manager.readWithColumnAndValue(Person::class.java.newInstance(), Wrapper(
        columnName = "firstname",
        stringValue = "Slava",
        isContainsStringValue = true
    ))
    println(person)
}