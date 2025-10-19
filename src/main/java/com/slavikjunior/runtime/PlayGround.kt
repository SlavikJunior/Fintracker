package com.slavikjunior.runtime

import com.slavikjunior.annotations.Id
import com.slavikjunior.models.Person
import com.slavikjunior.orm.Manager
import com.slavikjunior.util.toAnnotatedFieldsValuesListWithoutAnnotation

fun main() {

    val person = Person(2, "John", "Doe", "", "male", "", "usa")
    Manager.create(person, person.toAnnotatedFieldsValuesListWithoutAnnotation(Id::class))
}