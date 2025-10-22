@file:JvmName("KotlinExtensionsFunctions")

package com.slavikjunior.liba.utils

import com.slavikjunior.liba.annotations.Column
import kotlin.jvm.java
import kotlin.reflect.KClass

fun Any.toFieldMapByColumnNames(): Map<String, Any?> {
    return this::class.java.declaredFields.map { field ->
        field.isAccessible = true
        val name = field.annotations.find { it is Column }.let { it as Column }.name
        name to field.get(this)
    }.toMap()
}

//fun Class<*>.isNullableColumn(columnName: String): Boolean {
//    val annottion = this::class.java.declaredFields.find { it.name == columnName }?.declaredAnnotations?.find { it is Column } as Column?
//    return annottion?.nullable ?: false
//}

fun Class<*>.isNullableColumn(columnName: String): Boolean {
    val fields = this::class.java.declaredFields
    val annotatedField = fields.find { it.name == columnName }
    val annotation = annotatedField?.declaredAnnotations?.find { annotation -> annotation::class.java == Column::class.java }
    return (annotation as Column).nullable
}

fun KClass<*>.isNullableColumn(columnName: String): Boolean {
    return this.java.isNullableColumn(columnName)
}