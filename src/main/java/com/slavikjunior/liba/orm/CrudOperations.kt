package com.slavikjunior.liba.orm

import com.slavikjunior.liba.annotations.WrappedClass

interface CrudOperations {

    fun <T : Entity> create(entity: T, idIsAutoGenerate: Boolean = true): Boolean
    fun <T : Entity> getById(entityClass: Class<T>, id: Int): T?
    fun <T : Entity> getByValues(entityClass: Class<T>, columnsToValues: Map<String, Any?>?): T?
    fun <T : Entity> update(entityClass: Class<T>, id: Int, columnsToValues: Map<String, Any?>?): Boolean
    fun <T : Entity> updateAndGet(entityClass: Class<T>, id: Int, columnsToValues: Map<String, Any?>?): T?
    fun <T : Entity> deleteByValues(entityClass: Class<T>, columnsToValues: Map<String, Any?>?): Boolean
    fun <T : Entity> deleteById(entityClass: Class<T>, id: Int): Boolean
    fun <T : Entity> deleteByEntity(entity: T): Boolean
}