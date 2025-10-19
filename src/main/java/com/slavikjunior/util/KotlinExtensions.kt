package com.slavikjunior.util

import java.lang.reflect.Field
import kotlin.reflect.KClass

// получить список значений всех параметров
fun Any.toFieldValuesList(): List<Any?> {
    return this::class.java.declaredFields.map { field ->
        field.isAccessible = true
        field.get(this)
    }
}

// получить мапу значений всех параметров
fun Any.toFieldMap(): Map<String, Any?> {
    return this::class.java.declaredFields.map { field ->
        field.isAccessible = true
        field.name to field.get(this)
    }.toMap()
}

// получить список значений всех параметров, аннотированных аннотацией
fun Any.toAnnotatedFieldsValuesList(annotation: Annotation): List<Any?> {
    return this::class.java.declaredFields.map { field ->
        field.isAccessible = true
        field
    }.filter { it.annotations.contains(annotation) }
}

// получить мапу имён и значений всех параметров, аннотированных аннотацией
fun Any.toAnnotatedFieldsValuesMap(annotation: Annotation): Map<String, Any?> {
    return this::class.java.declaredFields.map { field ->
        field.isAccessible = true
        field
    }.filter { it.annotations.contains(annotation) }
        .map { field -> field.name to field.get(this) }.toMap()
}

// получить список значений всех параметров, удовлетворяющих предикату
fun Any.toFieldValuesWithPredicate(predicate: (field: Field) -> Boolean): List<Any?> {
    return this::class.java.declaredFields.map {
        it.isAccessible = true
        predicate(it)
        it
    }
}

// получить список значений всех параметров, не аннотированных аннотацией
fun Any.toAnnotatedFieldsValuesListWithoutAnnotation(annotationClass: KClass<out Annotation>): List<Any?> {
    return this::class.java.declaredFields
        .filter { field -> field.getAnnotation(annotationClass.java) == null }
        .onEach { it.isAccessible = true }
        .map { it.get(this) }
}