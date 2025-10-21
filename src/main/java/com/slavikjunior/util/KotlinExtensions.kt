package com.slavikjunior.util

import com.slavikjunior.annotations.Column

fun Any.toFieldMapByColumnNames(): Map<String, Any?> {
    return this::class.java.declaredFields.map { field ->
        field.isAccessible = true
        val name = field.annotations.find { it is Column }.let { it as Column }.name
        name to field.get(this)
    }.toMap()
}