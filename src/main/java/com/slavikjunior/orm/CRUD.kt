package com.slavikjunior.orm

import com.slavikjunior.annotations.WrappedClass

interface CRUD {

    fun <T : CRUDable> createEntity(entity: T, idIsAutoGenerate: Boolean = true): Boolean?
    fun <T : CRUDable> getEntityById(entityClass: Class<T>, id: Int): T?
    fun <T : CRUDable, E> getEntityByPairsOfColumnsAndValues(entityClass: Class<T>, columnsToValues: Map<String, @WrappedClass E>): T?
    fun <T : CRUDable, E> update(entityClass: Class<T>, id: Int, columnsToValues: Map<String, @WrappedClass E>): Boolean
    fun <T : CRUDable, E> updateAndGet(entityClass: Class<T>, id: Int, columnsToValues: Map<String, @WrappedClass E>): T?
    fun <T : CRUDable> delete(entity: T?)
}